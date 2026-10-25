package com.ace.hotel.repository;

import com.ace.hotel.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    List<ServiceOrder> findByBooking_BookingIdOrderByOrderedAtDesc(Long bookingId);

    List<ServiceOrder> findByBooking_BookingIdIn(List<Long> bookingIds);

    boolean existsByService_ServiceId(Long serviceId);

    @org.springframework.transaction.annotation.Transactional
    void deleteByBooking_BookingId(Long bookingId);
}
