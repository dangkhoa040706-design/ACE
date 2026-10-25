package com.ace.hotel.repository;

import com.ace.hotel.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUser_UserId(Long userId);

    Optional<Customer> findByIdNumber(String idNumber);

    Optional<Customer> findByPhone(String phone);

    boolean existsByIdNumber(String idNumber);

    boolean existsByPhone(String phone);
}
