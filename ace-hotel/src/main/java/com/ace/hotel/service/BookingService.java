package com.ace.hotel.service;

import com.ace.hotel.dto.OnlineBookingRequestDto;
import com.ace.hotel.dto.RoomTypeQuoteDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.entity.RoomType;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.Role;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.CustomerRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final CustomerRepository customerRepository;
    private final PricingService pricingService;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository,
                          RoomTypeRepository roomTypeRepository,
                          CustomerRepository customerRepository,
                          PricingService pricingService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.customerRepository = customerRepository;
        this.pricingService = pricingService;
    }

    /**
     * BR1: Tính tồn kho khả dụng cho 1 hạng phòng trong khoảng ngày [checkIn, checkOut)
     */
    public int getAvailableQuota(Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        if (roomTypeId == null || checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return 0;
        }
        int sellableRooms = roomRepository.countSellableRoomsByType(roomTypeId);
        int activeOverlapping = bookingRepository.countOverlappingActiveBookings(roomTypeId, checkIn, checkOut);
        return Math.max(0, sellableRooms - activeOverlapping);
    }

    /**
     * UC03 / WF-04: Tìm các hạng phòng còn tồn kho quota >= 1 và tính báo giá chi tiết
     */
    public List<RoomTypeQuoteDto> searchAvailableRoomTypes(LocalDate checkIn, LocalDate checkOut, int guests) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new BusinessException(MessageConstants.MSG_20);
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new BusinessException("Ngày nhận phòng không thể ở trong quá khứ.");
        }
        int guestCount = Math.max(1, guests);
        long nights = Math.max(1, ChronoUnit.DAYS.between(checkIn, checkOut));

        List<RoomType> allTypes = roomTypeRepository.findAllByOrderByPricePerNightAsc();
        List<RoomTypeQuoteDto> result = new ArrayList<>();

        for (RoomType rt : allTypes) {
            if (rt.getCapacity() >= guestCount) {
                int quota = getAvailableQuota(rt.getRoomTypeId(), checkIn, checkOut);
                if (quota >= 1) {
                    long roomCharge = pricingService.calculateRoomCharge(rt, checkIn, checkOut);
                    long estSvc = pricingService.calculateSvc5Percent(roomCharge);
                    long estVat = pricingService.calculateVat10Percent(roomCharge, estSvc);
                    long estGrandTotal = roomCharge + estSvc + estVat;
                    long depositAmount = pricingService.calculateOnlineDeposit30Percent(estGrandTotal);
                    long remainingAtCounter = estGrandTotal - depositAmount;

                    result.add(new RoomTypeQuoteDto(
                            rt.getRoomTypeId(),
                            rt.getName(),
                            rt.getCapacity(),
                            rt.getBedType(),
                            rt.getDescription(),
                            rt.getImageUrl(),
                            rt.getPricePerNight(),
                            rt.getWeekendPricePerNight(),
                            quota,
                            nights,
                            roomCharge,
                            estSvc,
                            estVat,
                            estGrandTotal,
                            depositAmount,
                            remainingAtCounter
                    ));
                }
            }
        }
        return result;
    }

    /**
     * UC03 / WF-04: Lưu đơn đặt phòng trực tuyến có thanh toán cọc 30%
     */
    @Transactional
    public Booking createOnlineBookingWithDeposit(OnlineBookingRequestDto request, User currentUser) {
        if (request.getCheckInDate() == null || request.getCheckOutDate() == null ||
            !request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new BusinessException(MessageConstants.MSG_20);
        }
        if (request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Ngày nhận phòng không thể ở trong quá khứ.");
        }

        // Kiểm tra lại Quota trong giao dịch để chống Race Condition
        int currentQuota = getAvailableQuota(request.getRoomTypeId(), request.getCheckInDate(), request.getCheckOutDate());
        if (currentQuota < 1) {
            throw new BusinessException(MessageConstants.MSG_22);
        }

        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new BusinessException("Không tìm thấy hạng phòng được chọn."));

        // Kiểm tra sức chứa tối đa của hạng phòng
        if (request.getGuestCount() != null && request.getGuestCount() > roomType.getCapacity()) {
            throw new BusinessException(String.format(
                "Hạng phòng %s (%s) chỉ phục vụ tối đa %d khách. Quý khách đăng ký %d khách vượt quá sức chứa quy định. Vui lòng chọn hạng phòng lớn hơn (Family/Suite) hoặc đặt nhiều phòng!",
                roomType.getName(), roomType.getBedType(), roomType.getCapacity(), request.getGuestCount()
            ));
        }

        // Tìm hoặc gắn Customer
        Customer customer = customerRepository.findByUser_UserId(currentUser.getUserId())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setUser(currentUser);
                    c.setFullName(currentUser.getFullName());
                    c.setPhone("0900000000");
                    return customerRepository.save(c);
                });

        long roomCharge = pricingService.calculateRoomCharge(roomType, request.getCheckInDate(), request.getCheckOutDate());
        long estSvc = pricingService.calculateSvc5Percent(roomCharge);
        long estVat = pricingService.calculateVat10Percent(roomCharge, estSvc);
        long estGrandTotal = roomCharge + estSvc + estVat;
        long depositAmount = pricingService.calculateOnlineDeposit30Percent(estGrandTotal);

        Booking booking = new Booking();
        booking.setBookingCode(generateUniqueBookingCode());
        booking.setCustomer(customer);
        booking.setRoomType(roomType);
        booking.setRoom(null); // Trực tuyến: chưa gán phòng vật lý
        booking.setGuestCount(request.getGuestCount() != null ? Math.max(1, request.getGuestCount()) : 1);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setRoomCharge(roomCharge);
        booking.setDepositAmount(depositAmount);
        booking.setEarlyCheckinFee(0L);
        booking.setLateCheckoutFee(0L);
        booking.setStatus(BookingStatus.RESERVED);
        booking.setCreatedBy(currentUser);

        return bookingRepository.save(booking);
    }

    /**
     * Hủy đơn đặt phòng trực tuyến (chỉ cho phép khi RESERVED)
     */
    @Transactional
    public Booking cancelBooking(Long bookingId, User currentUser) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng: " + bookingId));

        // Kiểm tra quyền: nếu là CUSTOMER thì phải chính là người đặt
        if (currentUser.getRole() == Role.CUSTOMER) {
            if (booking.getCustomer().getUser() == null ||
                !booking.getCustomer().getUser().getUserId().equals(currentUser.getUserId())) {
                throw new BusinessException(MessageConstants.MSG_05);
            }
        }

        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new BusinessException(MessageConstants.MSG_23);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForCustomer(Long userId) {
        return bookingRepository.findByCustomer_User_UserIdOrderByCreatedAtDesc(userId);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng: " + id));
    }

    public Booking getBookingByCode(String code) {
        return bookingRepository.findByBookingCodeIgnoreCase(code.trim())
                .orElseThrow(() -> new BusinessException(String.format(MessageConstants.MSG_26, code)));
    }

    private String generateUniqueBookingCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random random = new Random();
        String code;
        do {
            StringBuilder sb = new StringBuilder("BK-");
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (bookingRepository.findByBookingCodeIgnoreCase(code).isPresent());
        return code;
    }
}
