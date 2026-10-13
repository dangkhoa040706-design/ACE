package com.ace.hotel.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ace.hotel.entity.ServiceOrder;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    long countByOrderedAtGreaterThanEqualAndOrderedAtLessThan(LocalDateTime from, LocalDateTime to);

    @Query("select o from ServiceOrder o join fetch o.service join fetch o.booking b "
            + "where b.id in :bookingIds order by o.orderedAt")
    List<ServiceOrder> findByBookingIds(@Param("bookingIds") Collection<Long> bookingIds);
}
