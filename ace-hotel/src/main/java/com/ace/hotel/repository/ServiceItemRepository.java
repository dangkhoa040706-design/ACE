package com.ace.hotel.repository;

import com.ace.hotel.entity.ServiceItem;
import com.ace.hotel.entity.enums.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {

    Optional<ServiceItem> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<ServiceItem> findByCategoryOrderByNameAsc(ServiceCategory category);

    List<ServiceItem> findAllByOrderByCategoryAscNameAsc();
}
