package com.ace.hotel.service;

import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.ServiceItem;
import com.ace.hotel.entity.ServiceOrder;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.ServiceItemRepository;
import com.ace.hotel.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FolioService {

    private final BookingRepository bookingRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    public FolioService(BookingRepository bookingRepository,
                        ServiceItemRepository serviceItemRepository,
                        ServiceOrderRepository serviceOrderRepository) {
        this.bookingRepository = bookingRepository;
        this.serviceItemRepository = serviceItemRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    /**
     * BR5: Ghi nhận dịch vụ vào Room Folio và đóng băng đơn giá (Price Snapshot)
     */
    @Transactional
    public ServiceOrder addServiceOrder(Long bookingId, Long serviceId, int quantity, User staff) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + bookingId));

        // Chỉ được thêm dịch vụ khi phòng đang có khách lưu trú (CHECKED_IN)
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessException(MessageConstants.MSG_34);
        }

        // Số lượng dịch vụ phải >= 1
        if (quantity < 1) {
            throw new BusinessException(MessageConstants.MSG_35);
        }

        ServiceItem serviceItem = serviceItemRepository.findById(serviceId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy dịch vụ ID: " + serviceId));

        ServiceOrder order = new ServiceOrder();
        order.setBooking(booking);
        order.setService(serviceItem);
        order.setQuantity(quantity);
        // BR5: Sao chép cứng đơn giá tại thời điểm gọi dịch vụ (Price Snapshot)
        order.setUnitPrice(serviceItem.getUnitPrice());
        order.setOrderedAt(LocalDateTime.now());
        order.setCreatedBy(staff);

        return serviceOrderRepository.save(order);
    }

    /**
     * Xóa dòng dịch vụ ghi nhầm khỏi Room Folio
     */
    @Transactional
    public void deleteServiceOrder(Long bookingId, Long orderId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessException(MessageConstants.MSG_34);
        }

        ServiceOrder order = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy dòng dịch vụ ID: " + orderId));

        if (!order.getBooking().getBookingId().equals(bookingId)) {
            throw new BusinessException("Dòng dịch vụ không thuộc đơn đặt phòng này.");
        }

        serviceOrderRepository.delete(order);
    }
}
