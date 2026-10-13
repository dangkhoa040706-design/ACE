package com.ace.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.hotel.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
