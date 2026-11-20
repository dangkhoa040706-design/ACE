package com.ace.hotel.repository;

import com.ace.hotel.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByBooking_BookingId(Long bookingId);

    List<Invoice> findByPaidAtBetweenOrderByPaidAtDesc(LocalDateTime start, LocalDateTime end);
}
