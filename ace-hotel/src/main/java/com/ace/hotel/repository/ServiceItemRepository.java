package com.ace.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.hotel.entity.ServiceItem;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    List<ServiceItem> findAllByOrderByNameAsc();
}
