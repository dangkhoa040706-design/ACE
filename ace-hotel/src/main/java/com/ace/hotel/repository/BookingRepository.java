package com.ace.hotel.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    long countByCheckInDateAndStatusIn(LocalDate date, Collection<BookingStatus> statuses);

    long countByCheckOutDateAndStatusIn(LocalDate date, Collection<BookingStatus> statuses);

    /** Các đặt phòng theo trạng thái, kèm khách và phòng (dùng cho thẻ phòng trên dashboard). */
    @Query("select b from Booking b join fetch b.customer join fetch b.room where b.status = :status")
    List<Booking> findByStatusWithDetails(@Param("status") BookingStatus status);

    @Query("select b from Booking b join fetch b.customer join fetch b.room "
            + "where b.status = :status and b.checkInDate = :date")
    List<Booking> findByStatusAndCheckInDateWithDetails(@Param("status") BookingStatus status,
                                                        @Param("date") LocalDate date);

    /**
     * Bảng "Check-in / Check-out hôm nay": đặt phòng nhận hôm nay, trả hôm nay,
     * hoặc đang ở nhưng đã quá hạn trả. Bỏ qua đặt phòng đã hủy.
     */
    @Query("select b from Booking b join fetch b.customer c join fetch b.room r join fetch r.roomType "
            + "where b.status <> :cancelled "
            + "and (b.checkInDate = :today or b.checkOutDate = :today "
            + "or (b.status = :checkedIn and b.checkOutDate < :today)) "
            + "order by b.checkInDate, b.id")
    List<Booking> findTodayRows(@Param("today") LocalDate today,
                                @Param("cancelled") BookingStatus cancelled,
                                @Param("checkedIn") BookingStatus checkedIn);
}
