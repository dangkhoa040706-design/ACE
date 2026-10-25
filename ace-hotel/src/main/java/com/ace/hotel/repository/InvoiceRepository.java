package com.ace.hotel.repository;

import com.ace.hotel.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByBooking_BookingId(Long bookingId);

    List<Invoice> findAllByOrderByPaidAtDesc();

    List<Invoice> findByPaidAtBetweenOrderByPaidAtDesc(java.time.LocalDateTime start, java.time.LocalDateTime end);

    @Query("SELECT COALESCE(SUM(i.grandTotal), 0) FROM Invoice i")
    long sumGrandTotal();

    @Query("SELECT COALESCE(SUM(i.roomCharge), 0) FROM Invoice i")
    long sumRoomCharge();

    @Query("SELECT COALESCE(SUM(i.serviceChargeTotal), 0) FROM Invoice i")
    long sumServiceChargeTotal();

    @Query("SELECT COALESCE(SUM(i.earlyCheckinFee), 0) FROM Invoice i")
    long sumEarlyCheckinFee();

    @Query("SELECT COALESCE(SUM(i.lateCheckoutFee), 0) FROM Invoice i")
    long sumLateCheckoutFee();

    @Query("SELECT COALESCE(SUM(i.serviceChargeAmount), 0) FROM Invoice i")
    long sumServiceChargeAmount();

    @Query("SELECT COALESCE(SUM(i.vatAmount), 0) FROM Invoice i")
    long sumVatAmount();
}
