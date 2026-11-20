package com.ace.hotel.service;

import com.ace.hotel.dto.OnlineBookingRequestDto;
import com.ace.hotel.dto.RoomTypeQuoteDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.RoomType;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.Role;
import com.ace.hotel.entity.enums.RoomStatus;
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
     * Lấy danh sách phòng vật lý thực sự còn trống của 1 hạng phòng trong khoảng [checkIn, checkOut)
     */
    public List<Room> getAvailableRoomsForTypeAndDates(Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
        if (roomTypeId == null || checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return List.of();
        }
        List<Long> bookedRoomIds = bookingRepository.findBookedRoomIdsInDateRange(roomTypeId, checkIn, checkOut);
        boolean isSameDayCheckIn = checkIn.equals(LocalDate.now());
        return roomRepository.findAllByOrderByRoomNumberAsc().stream()
                .filter(r -> r.getRoomType() != null && r.getRoomType().getRoomTypeId().equals(roomTypeId))
                .filter(r -> r.getStatus() != RoomStatus.MAINTENANCE)
                .filter(r -> !isSameDayCheckIn || r.getStatus() == RoomStatus.AVAILABLE)
                .filter(r -> !bookedRoomIds.contains(r.getRoomId()))
                .toList();
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
                List<Room> roomsForType = getAvailableRoomsForTypeAndDates(rt.getRoomTypeId(), checkIn, checkOut);
                if (quota >= 1 && !roomsForType.isEmpty()) {
                    long roomCharge = pricingService.calculateRoomCharge(rt, checkIn, checkOut);
                    long estSvc = pricingService.calculateSvc5Percent(roomCharge);
                    long estVat = pricingService.calculateVat10Percent(roomCharge, estSvc);
                    long estGrandTotal = roomCharge + estSvc + estVat;
                    long depositAmount = pricingService.calculateOnlineDeposit30Percent(estGrandTotal);
                    long remainingAtCounter = estGrandTotal - depositAmount;

                    List<String> roomNumbers = roomsForType.stream().map(r -> "P." + r.getRoomNumber() + " (Tầng " + r.getFloor() + ")").toList();
                    String suggestedRoom = roomNumbers.get(0);

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
                            remainingAtCounter,
                            suggestedRoom,
                            roomNumbers
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

        // 1. Khóa bi quan hàng RoomType trong giao dịch để ngăn chặn triệt để Race Condition / Overbooking
        RoomType roomType = roomTypeRepository.findByIdForUpdate(request.getRoomTypeId())
                .orElseThrow(() -> new BusinessException("Không tìm thấy hạng phòng được chọn."));

        // 2. Kiểm tra lại Quota sau khi đã giữ khóa dòng
        int currentQuota = getAvailableQuota(request.getRoomTypeId(), request.getCheckInDate(), request.getCheckOutDate());
        if (currentQuota < 1) {
            throw new BusinessException(MessageConstants.MSG_22);
        }

        // Kiểm tra sức chứa tối đa của hạng phòng
        if (request.getGuestCount() != null && request.getGuestCount() > roomType.getCapacity()) {
            throw new BusinessException(String.format(
                "Hạng phòng %s (%s) chỉ phục vụ tối đa %d khách. Quý khách đăng ký %d khách vượt quá sức chứa quy định. Vui lòng chọn hạng phòng lớn hơn (Family/Suite) hoặc đặt nhiều phòng!",
                roomType.getName(), roomType.getBedType(), roomType.getCapacity(), request.getGuestCount()
            ));
        }

        // Tìm hoặc gắn Customer và cập nhật thông tin nếu khách hàng nhập mới
        Customer customer = customerRepository.findByUser_UserId(currentUser.getUserId())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setUser(currentUser);
                    c.setFullName(request.getFullName() != null && !request.getFullName().isBlank() ? request.getFullName() : currentUser.getFullName());
                    c.setPhone(request.getPhone() != null && !request.getPhone().isBlank() ? request.getPhone() : "0900000000");
                    return customerRepository.save(c);
                });

        boolean customerUpdated = false;
        if (request.getFullName() != null && !request.getFullName().isBlank() && !request.getFullName().equals(customer.getFullName())) {
            customer.setFullName(request.getFullName().trim());
            customerUpdated = true;
        }
        if (request.getPhone() != null && !request.getPhone().isBlank() && !request.getPhone().equals(customer.getPhone())) {
            customer.setPhone(request.getPhone().trim());
            customerUpdated = true;
        }
        if (request.getEmail() != null && !request.getEmail().isBlank() && !request.getEmail().equals(customer.getEmail())) {
            customer.setEmail(request.getEmail().trim());
            customerUpdated = true;
        }
        if (request.getIdNumber() != null && !request.getIdNumber().isBlank() && !request.getIdNumber().equals(customer.getIdNumber())) {
            customer.setIdNumber(request.getIdNumber().trim());
            customerUpdated = true;
        }
        if (customerUpdated) {
            customerRepository.save(customer);
        }

        long roomCharge = pricingService.calculateRoomCharge(roomType, request.getCheckInDate(), request.getCheckOutDate());
        long estSvc = pricingService.calculateSvc5Percent(roomCharge);
        long estVat = pricingService.calculateVat10Percent(roomCharge, estSvc);
        long estGrandTotal = roomCharge + estSvc + estVat;
        long depositAmount = pricingService.calculateOnlineDeposit30Percent(estGrandTotal);

        // Chốt đúng phòng khách đã chọn (và kiểm tra chống trùng phòng theo khoảng ngày)
        Room assignedRoom = null;
        if (request.getRoomId() != null) {
            assignedRoom = roomRepository.findById(request.getRoomId()).orElse(null);
        } else if (request.getRoomNumber() != null && !request.getRoomNumber().isBlank()) {
            String digitsOnly = request.getRoomNumber().replaceAll("[^0-9]", "");
            if (!digitsOnly.isEmpty()) {
                assignedRoom = roomRepository.findByRoomNumberIgnoreCase(digitsOnly).orElse(null);
            }
        }
        if (assignedRoom != null) {
            if (!assignedRoom.getRoomType().getRoomTypeId().equals(roomType.getRoomTypeId())) {
                throw new BusinessException("Phòng P." + assignedRoom.getRoomNumber() + " không thuộc hạng phòng " + roomType.getName() + ".");
            }
            if (assignedRoom.getStatus() == RoomStatus.MAINTENANCE) {
                throw new BusinessException("Phòng P." + assignedRoom.getRoomNumber() + " đang bảo trì. Vui lòng chọn phòng khác.");
            }
            if (bookingRepository.existsOverlappingBookingForRoom(assignedRoom.getRoomId(), request.getCheckInDate(), request.getCheckOutDate())) {
                throw new BusinessException("Phòng P." + assignedRoom.getRoomNumber() + " vừa được khách khác đặt trong khoảng ngày này. Vui lòng chọn phòng khác!");
            }
        } else {
            List<Room> availableRooms = getAvailableRoomsForTypeAndDates(roomType.getRoomTypeId(), request.getCheckInDate(), request.getCheckOutDate());
            if (availableRooms.isEmpty()) {
                throw new BusinessException(MessageConstants.MSG_22);
            }
            assignedRoom = availableRooms.get(0);
        }

        Booking booking = new Booking();
        booking.setBookingCode(generateUniqueBookingCode());
        booking.setCustomer(customer);
        booking.setRoomType(roomType);
        booking.setRoom(assignedRoom);
        booking.setGuestCount(request.getGuestCount() != null ? Math.max(1, request.getGuestCount()) : 1);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        if (request.getEstimatedCheckInTime() != null) {
            booking.setEstimatedCheckInTime(request.getEstimatedCheckInTime());
        }
        if (request.getEstimatedCheckOutTime() != null) {
            booking.setEstimatedCheckOutTime(request.getEstimatedCheckOutTime());
        }
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

    /**
     * Thông điệp kết quả hủy đơn kèm chính sách hoàn cọc / mất cọc 30% (mốc 48 giờ trước 14:00 ngày nhận phòng)
     */
    public String buildCancellationPolicySummary(Booking booking) {
        long deposit = booking.getDepositAmount() != null ? booking.getDepositAmount() : 0L;
        String depositStr = String.format("%,dđ", deposit);
        String checkInStr = booking.getCheckInDate() != null
                ? booking.getCheckInDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        if (deposit <= 0L) {
            return String.format("Đã hủy đơn %s thành công và giải phóng quỹ phòng trống.", booking.getBookingCode());
        }
        if (booking.isEligibleForDepositRefund()) {
            return String.format(
                    "Đã hủy đơn %s thành công (trước mốc 48 giờ). Quỹ phòng trống đã được giải phóng; tiền cọc 30%% (%s) đủ điều kiện HOÀN LẠI 100%% trong 24 giờ.",
                    booking.getBookingCode(), depositStr
            );
        }
        return String.format(
                "Đã hủy đơn %s và giải phóng quỹ phòng trống. Do hủy muộn trong vòng 48 giờ trước giờ nhận phòng (14:00 ngày %s), tiền cọc 30%% (%s) KHÔNG ĐƯỢC HOÀN LẠI (mất cọc theo chính sách khách sạn).",
                booking.getBookingCode(), checkInStr, depositStr
        );
    }

    /**
     * Ghi nhận yêu cầu trả phòng nhanh (Express Check-out) từ Cổng Khách hàng
     */
    @Transactional
    public Booking requestExpressCheckout(Long bookingId, User currentUser) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng: " + bookingId));

        if (currentUser.getRole() == Role.CUSTOMER) {
            if (booking.getCustomer() == null ||
                booking.getCustomer().getUser() == null ||
                !booking.getCustomer().getUser().getUserId().equals(currentUser.getUserId())) {
                throw new BusinessException(MessageConstants.MSG_05);
            }
        } else if (currentUser.getRole() != Role.ADMIN) {
            throw new BusinessException(MessageConstants.MSG_05);
        }

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessException("Chỉ đơn đang lưu trú mới có thể yêu cầu trả phòng.");
        }

        if (booking.getCheckoutRequestedAt() == null) {
            booking.setCheckoutRequestedAt(java.time.LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return booking;
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
