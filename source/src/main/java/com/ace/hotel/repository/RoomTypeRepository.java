package com.ace.hotel.repository;

import com.ace.hotel.entity.RoomType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    Optional<RoomType> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<RoomType> findAllByOrderByPricePerNightAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rt FROM RoomType rt WHERE rt.roomTypeId = :id")
    Optional<RoomType> findByIdForUpdate(@Param("id") Long id);
}
