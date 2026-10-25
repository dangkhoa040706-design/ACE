package com.ace.hotel.repository;

import com.ace.hotel.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    Optional<RoomType> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<RoomType> findAllByOrderByPricePerNightAsc();
}
