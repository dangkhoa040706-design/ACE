package com.ace.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.RoomStatus;

public interface RoomRepository extends JpaRepository<Room, Long> {

    long countByStatus(RoomStatus status);

    @Query("select r from Room r join fetch r.roomType order by r.roomNumber")
    List<Room> findAllWithType();
}
