package com.ace.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace.hotel.entity.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    List<RoomType> findAllByOrderByPricePerNightAsc();
}
