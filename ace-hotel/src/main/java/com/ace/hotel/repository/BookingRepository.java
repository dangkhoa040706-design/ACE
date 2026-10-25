package com.ace.hotel.repository;

import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingCodeIgnoreCase(String bookingCode);

    boolean existsByBookingCode(String bookingCode);

    boolean existsByBookingCodeIgnoreCase(String bookingCode);

    List<Booking> findByCustomer_CustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Booking> findByCustomer_User_UserIdOrderByCreatedAtDesc(Long userId);

    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);

    List<Booking> findAllByOrderByCreatedAtDesc();

    List<Booking> findTop5ByOrderByCreatedAtDesc();

    long countByCheckInDateAndStatus(LocalDate checkInDate, BookingStatus status);

    long countByCheckOutDateAndStatus(LocalDate checkOutDate, BookingStatus status);

    long countByStatus(BookingStatus status);

    // BR1: Đếm số đơn đang giữ chỗ (RESERVED) hoặc đang ở (CHECKED_IN) giao khoảng ngày [checkIn, checkOut)
    @Query("""
        SELECT COUNT(b) FROM Booking b
        WHERE b.roomType.roomTypeId = :typeId
          AND b.status IN ('RESERVED', 'CHECKED_IN')
          AND b.checkInDate < :checkOut
          AND b.checkOutDate > :checkIn
    """)
    int countOverlappingActiveBookings(@Param("typeId") Long typeId,
                                       @Param("checkIn") LocalDate checkIn,
                                       @Param("checkOut") LocalDate checkOut);

    boolean existsByRoom_RoomId(Long roomId);

    boolean existsByRoomType_RoomTypeId(Long roomTypeId);

    Optional<Booking> findFirstByRoom_RoomIdAndStatus(Long roomId, BookingStatus status);
}
