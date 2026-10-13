package com.ace.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.hotel.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
