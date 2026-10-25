package com.ace.hotel.service;

import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.RoomType;
import com.ace.hotel.entity.ServiceItem;
import com.ace.hotel.entity.enums.RoomStatus;
import com.ace.hotel.entity.enums.ServiceCategory;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.repository.RoomTypeRepository;
import com.ace.hotel.repository.ServiceItemRepository;
import com.ace.hotel.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final BookingRepository bookingRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    public CatalogService(RoomTypeRepository roomTypeRepository,
                          RoomRepository roomRepository,
                          ServiceItemRepository serviceItemRepository,
                          BookingRepository bookingRepository,
                          ServiceOrderRepository serviceOrderRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.serviceItemRepository = serviceItemRepository;
        this.bookingRepository = bookingRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    // ==========================================
    // 1. Quản lý Hạng phòng (RoomType)
    // ==========================================
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAllByOrderByPricePerNightAsc();
    }

    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy hạng phòng có mã: " + id));
    }

    @Transactional
    public RoomType saveRoomType(RoomType roomType) {
        if (roomType.getName() == null || roomType.getName().isBlank()) {
            throw new BusinessException("Tên hạng phòng không được để trống.");
        }
        if (roomType.getPricePerNight() == null || roomType.getPricePerNight() <= 0
                || roomType.getWeekendPricePerNight() == null || roomType.getWeekendPricePerNight() <= 0
                || roomType.getCapacity() == null || roomType.getCapacity() < 1) {
            throw new BusinessException(MessageConstants.MSG_13);
        }

        // Kiểm tra trùng tên
        roomTypeRepository.findByNameIgnoreCase(roomType.getName().trim())
                .ifPresent(existing -> {
                    if (roomType.getRoomTypeId() == null || !existing.getRoomTypeId().equals(roomType.getRoomTypeId())) {
                        throw new BusinessException(MessageConstants.MSG_12);
                    }
                });

        roomType.setName(roomType.getName().trim());
        if (roomType.getImageUrl() == null || roomType.getImageUrl().isBlank()) {
            roomType.setImageUrl("/images/room-standard.png");
        }
        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public void deleteRoomType(Long id) {
        if (roomRepository.existsByRoomType_RoomTypeId(id) || bookingRepository.existsByRoomType_RoomTypeId(id)) {
            throw new BusinessException(MessageConstants.MSG_14);
        }
        roomTypeRepository.deleteById(id);
    }

    // ==========================================
    // 2. Quản lý Phòng vật lý (Room)
    // ==========================================
    public List<Room> getAllRooms() {
        return roomRepository.findAllByOrderByRoomNumberAsc();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy phòng vật lý có mã: " + id));
    }

    @Transactional
    public Room saveRoom(Room room, Long roomTypeId) {
        if (room.getRoomNumber() == null || room.getRoomNumber().isBlank()) {
            throw new BusinessException("Số phòng không được để trống.");
        }
        if (room.getFloor() == null || room.getFloor() < 1) {
            throw new BusinessException("Tầng phòng phải lớn hơn hoặc bằng 1.");
        }

        String roomNum = room.getRoomNumber().trim();
        roomRepository.findByRoomNumberIgnoreCase(roomNum)
                .ifPresent(existing -> {
                    if (room.getRoomId() == null || !existing.getRoomId().equals(room.getRoomId())) {
                        throw new BusinessException(MessageConstants.MSG_12);
                    }
                });

        RoomType roomType = getRoomTypeById(roomTypeId);
        room.setRoomNumber(roomNum);
        room.setRoomType(roomType);
        if (room.getStatus() == null) {
            room.setStatus(RoomStatus.AVAILABLE);
        }
        return roomRepository.save(room);
    }

    @Transactional
    public void toggleMaintenance(Long roomId) {
        Room room = getRoomById(roomId);
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            throw new BusinessException(MessageConstants.MSG_16);
        }
        if (room.getStatus() == RoomStatus.MAINTENANCE) {
            room.setStatus(RoomStatus.AVAILABLE);
        } else {
            room.setStatus(RoomStatus.MAINTENANCE);
        }
        roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        if (bookingRepository.existsByRoom_RoomId(roomId)) {
            throw new BusinessException(MessageConstants.MSG_15);
        }
        roomRepository.deleteById(roomId);
    }

    // ==========================================
    // 3. Quản lý Danh mục Dịch vụ (ServiceItem)
    // ==========================================
    public List<ServiceItem> getAllServices() {
        return serviceItemRepository.findAllByOrderByCategoryAscNameAsc();
    }

    public ServiceItem getServiceById(Long id) {
        return serviceItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy dịch vụ có mã: " + id));
    }

    @Transactional
    public ServiceItem saveService(ServiceItem service) {
        if (service.getName() == null || service.getName().isBlank()) {
            throw new BusinessException("Tên dịch vụ không được để trống.");
        }
        if (service.getUnitPrice() == null || service.getUnitPrice() < 0) {
            throw new BusinessException("Đơn giá dịch vụ phải lớn hơn hoặc bằng 0 VNĐ.");
        }
        if (service.getUnit() == null || service.getUnit().isBlank()) {
            throw new BusinessException("Đơn vị tính không được để trống.");
        }

        String name = service.getName().trim();
        serviceItemRepository.findByNameIgnoreCase(name)
                .ifPresent(existing -> {
                    if (service.getServiceId() == null || !existing.getServiceId().equals(service.getServiceId())) {
                        throw new BusinessException(MessageConstants.MSG_12);
                    }
                });

        service.setName(name);
        return serviceItemRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id) {
        if (serviceOrderRepository.existsByService_ServiceId(id)) {
            throw new BusinessException(MessageConstants.MSG_17);
        }
        serviceItemRepository.deleteById(id);
    }
}
