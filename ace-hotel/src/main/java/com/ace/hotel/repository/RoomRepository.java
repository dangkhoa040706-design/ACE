package com.ace.hotel.repository;

import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.enums.RoomStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // BR1: Đếm số phòng kinh doanh của 1 hạng phòng (loại trừ phòng đang MAINTENANCE)
    @Query("SELECT COUNT(r) FROM Room r WHERE r.roomType.roomTypeId = :typeId AND r.status <> 'MAINTENANCE'")
    int countSellableRoomsByType(@Param("typeId") Long typeId);

    // BR2: Lọc danh sách phòng vật lý đang trống sạch (AVAILABLE) để gán khi Check-in
    List<Room> findByRoomType_RoomTypeIdAndStatusOrderByRoomNumberAsc(Long roomTypeId, RoomStatus status);

    // Khóa dòng phòng khi thực hiện gán phòng Check-in để chống 2 lễ tân gán trùng
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.roomId = :roomId")
    Optional<Room> findByIdForUpdate(@Param("roomId") Long roomId);

    // UC07: Đếm tổng số phòng có thể kinh doanh toàn khách sạn (AVAILABLE + OCCUPIED + DIRTY)
    @Query("SELECT COUNT(r) FROM Room r WHERE r.status <> 'MAINTENANCE'")
    long countAllSellableRooms();

    long countByStatus(RoomStatus status);

    boolean existsByRoomType_RoomTypeId(Long roomTypeId);

    boolean existsByRoomNumberIgnoreCase(String roomNumber);

    Optional<Room> findByRoomNumberIgnoreCase(String roomNumber);

    List<Room> findAllByOrderByRoomNumberAsc();

    List<Room> findAllByOrderByFloorAscRoomNumberAsc();

    List<Room> findByStatusOrderByRoomNumberAsc(RoomStatus status);

    List<Room> findByFloorOrderByRoomNumberAsc(Integer floor);
}
