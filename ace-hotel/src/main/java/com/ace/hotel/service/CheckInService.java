package com.ace.hotel.service;

import com.ace.hotel.dto.WalkInCheckInDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.RoomStatus;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.CustomerRepository;
import com.ace.hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CheckInService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final PricingService pricingService;
    private final BookingService bookingService;

    public CheckInService(BookingRepository bookingRepository,
                           RoomRepository roomRepository,
                           CustomerRepository customerRepository,
                           PricingService pricingService,
                           BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.pricingService = pricingService;
        this.bookingService = bookingService;
    }

    /**
     * Chế độ 1: Check-in Khách đặt trước (RESERVED) & Gán phòng vật lý sạch (AVAILABLE)
     */
    @Transactional
    public Booking assignAndCheckIn(Long bookingId, Long roomId, Long earlyCheckinFee, String idNumber, User staff) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException(String.format(MessageConstants.MSG_26, bookingId)));

        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new BusinessException(String.format(MessageConstants.MSG_26, booking.getBookingCode()));
        }

        // Khóa dòng phòng chống 2 lễ tân gán trùng đồng thời (BR2)
        Room room = roomRepository.findByIdForUpdate(roomId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy phòng vật lý ID: " + roomId));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BusinessException(String.format(MessageConstants.MSG_28, room.getRoomNumber()));
        }

        if (!room.getRoomType().getRoomTypeId().equals(booking.getRoomType().getRoomTypeId())) {
            throw new BusinessException("Phòng vật lý " + room.getRoomNumber() + " không thuộc hạng phòng " + booking.getRoomType().getName());
        }

        // Kiểm tra sức chứa tối đa của phòng khi gán
        if (booking.getGuestCount() != null && booking.getGuestCount() > room.getRoomType().getCapacity()) {
            throw new BusinessException(String.format(
                "Phòng %s (%s - %s) chỉ phục vụ tối đa %d khách. Đơn đặt %s có %d khách vượt quá sức chứa quy định!",
                room.getRoomNumber(), room.getRoomType().getName(), room.getRoomType().getBedType(),
                room.getRoomType().getCapacity(), booking.getBookingCode(), booking.getGuestCount()
            ));
        }

        // Cập nhật CCCD nếu có
        if (idNumber != null && !idNumber.isBlank()) {
            Customer customer = booking.getCustomer();
            customer.setIdNumber(idNumber.trim());
            customerRepository.save(customer);
        }

        // Cập nhật đơn đặt phòng sang CHECKED_IN
        booking.setRoom(room);
        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setActualCheckIn(LocalDateTime.now());
        booking.setEarlyCheckinFee(earlyCheckinFee != null ? earlyCheckinFee : 0L);
        if (staff != null && booking.getCreatedBy() == null) {
            booking.setCreatedBy(staff);
        }

        // Chuyển phòng sang OCCUPIED
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        return bookingRepository.save(booking);
    }

    /**
     * Chế độ 2: Tiếp nhận Khách vãng lai (Walk-in) - 0đ cọc lúc vào, trả 100% khi Check-out (BR4)
     */
    @Transactional
    public Booking processWalkInCheckIn(WalkInCheckInDto dto, User staff) {
        if (dto.getCheckOutDate() == null || !dto.getCheckOutDate().isAfter(LocalDate.now())) {
            throw new BusinessException(MessageConstants.MSG_20);
        }

        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty() ||
            dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
            throw new BusinessException("Vui lòng nhập đầy đủ Họ tên và Số điện thoại của khách hàng.");
        }

        // Khóa dòng phòng vật lý được chọn (BR2)
        Room room = roomRepository.findByIdForUpdate(dto.getRoomId())
                .orElseThrow(() -> new BusinessException("Vui lòng chọn một phòng vật lý hợp lệ."));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BusinessException(String.format(MessageConstants.MSG_28, room.getRoomNumber()));
        }

        // Kiểm tra số lượng khách lưu trú và sức chứa tối đa của phòng
        if (dto.getGuestCount() <= 0) {
            throw new BusinessException("Số lượng khách lưu trú phải lớn hơn 0 (từ 1 người trở lên).");
        }
        if (dto.getGuestCount() > room.getRoomType().getCapacity()) {
            throw new BusinessException(String.format(
                "Phòng %s (%s - %s) chỉ phục vụ tối đa %d khách. Quý khách nhập %d khách vượt quá sức chứa quy định. Vui lòng chọn phòng có sức chứa lớn hơn (Family/Suite) hoặc tiếp nhận thêm phòng!",
                room.getRoomNumber(), room.getRoomType().getName(), room.getRoomType().getBedType(),
                room.getRoomType().getCapacity(), dto.getGuestCount()
            ));
        }

        // Kiểm tra tồn kho Quota hạng phòng trong khoảng lưu trú (BR1)
        int quota = bookingService.getAvailableQuota(room.getRoomType().getRoomTypeId(), LocalDate.now(), dto.getCheckOutDate());
        if (quota < 1) {
            throw new BusinessException(MessageConstants.MSG_22);
        }

        // Tìm hoặc tạo khách hàng Walk-in (user_id = null)
        Customer customer = null;
        if (dto.getIdNumber() != null && !dto.getIdNumber().isBlank()) {
            customer = customerRepository.findByIdNumber(dto.getIdNumber().trim()).orElse(null);
        }
        if (customer == null && dto.getPhone() != null && !dto.getPhone().isBlank()) {
            customer = customerRepository.findByPhone(dto.getPhone().trim()).orElse(null);
        }
        if (customer == null) {
            customer = new Customer();
            customer.setFullName(dto.getFullName().trim());
            customer.setPhone(dto.getPhone().trim());
            customer.setIdNumber(dto.getIdNumber() != null ? dto.getIdNumber().trim() : null);
            customer.setUser(null);
            customer = customerRepository.save(customer);
        } else {
            // Cập nhật thông tin nếu có bổ sung
            customer.setFullName(dto.getFullName().trim());
            customer.setPhone(dto.getPhone().trim());
            if (dto.getIdNumber() != null && !dto.getIdNumber().isBlank()) {
                customer.setIdNumber(dto.getIdNumber().trim());
            }
            customer = customerRepository.save(customer);
        }

        // Tính tiền phòng gốc theo ngày thường / cuối tuần (BR3)
        long roomCharge = pricingService.calculateRoomCharge(room.getRoomType(), LocalDate.now(), dto.getCheckOutDate());

        // Sinh mã booking ngẫu nhiên duy nhất (chống trùng lặp CSDL)
        String bookingCode;
        do {
            String randomSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            bookingCode = "BK-" + randomSuffix;
        } while (bookingRepository.existsByBookingCode(bookingCode));

        // Tạo đơn đặt phòng Walk-in
        Booking booking = new Booking();
        booking.setBookingCode(bookingCode);
        booking.setCustomer(customer);
        booking.setRoomType(room.getRoomType());
        booking.setRoom(room);
        booking.setCheckInDate(LocalDate.now());
        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setActualCheckIn(LocalDateTime.now());
        booking.setGuestCount(dto.getGuestCount() > 0 ? dto.getGuestCount() : 1);
        booking.setRoomCharge(roomCharge);
        booking.setDepositAmount(0L); // Walk-in 0đ cọc lúc vào (BR4)
        booking.setEarlyCheckinFee(0L);
        booking.setLateCheckoutFee(0L);
        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setCreatedBy(staff);
        booking.setCreatedAt(LocalDateTime.now());

        // Chuyển phòng sang OCCUPIED
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        return bookingRepository.save(booking);
    }

    /**
     * 1-Click: Xác nhận phòng đã dọn sạch (DIRTY -> AVAILABLE)
     */
    @Transactional
    public Room markRoomAsClean(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy phòng vật lý ID: " + roomId));

        if (room.getStatus() == RoomStatus.DIRTY) {
            room.setStatus(RoomStatus.AVAILABLE);
            return roomRepository.save(room);
        }
        return room;
    }
}
