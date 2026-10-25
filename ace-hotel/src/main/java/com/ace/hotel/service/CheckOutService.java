package com.ace.hotel.service;

import com.ace.hotel.dto.CheckOutSummaryDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Invoice;
import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.ServiceOrder;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.PaymentMethod;
import com.ace.hotel.entity.enums.RoomStatus;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.InvoiceRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CheckOutService {

    private final BookingRepository bookingRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final InvoiceRepository invoiceRepository;
    private final RoomRepository roomRepository;

    public CheckOutService(BookingRepository bookingRepository,
                           ServiceOrderRepository serviceOrderRepository,
                           InvoiceRepository invoiceRepository,
                           RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.serviceOrderRepository = serviceOrderRepository;
        this.invoiceRepository = invoiceRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * BR6: Chuẩn bị bảng tính quyết toán Check-out với số nguyên BIGINT tuần tự
     */
    public CheckOutSummaryDto prepareCheckOutSummary(Long bookingId, Long customLateFee) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + bookingId));

        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByBooking_BookingIdOrderByOrderedAtDesc(bookingId);
        long serviceChargeTotal = serviceOrders.stream().mapToLong(ServiceOrder::getLineTotal).sum();

        // Gợi ý phụ thu trả phòng trễ (Late Check-out Policy)
        long pricePerNight = booking.getRoomType().getPricePerNight();
        LocalDateTime checkoutDeadline = booking.getCheckOutDate().atTime(12, 0);
        LocalDateTime now = LocalDateTime.now();
        long suggestedLateFee = 0L;

        if (now.isAfter(checkoutDeadline)) {
            if (now.toLocalDate().isAfter(booking.getCheckOutDate())) {
                suggestedLateFee = pricePerNight; // Quá hạn ngày: 100%
            } else if (now.getHour() >= 18) {
                suggestedLateFee = pricePerNight; // Sau 18:00: 100%
            } else if (now.getHour() >= 15) {
                suggestedLateFee = Math.round(pricePerNight * 0.50); // 15:00 - 18:00: 50%
            } else {
                suggestedLateFee = Math.round(pricePerNight * 0.30); // 12:00 - 15:00: 30%
            }
        }

        long lateCheckoutFee = customLateFee != null ? customLateFee : suggestedLateFee;
        long roomCharge = booking.getRoomCharge();
        long earlyCheckinFee = booking.getEarlyCheckinFee() != null ? booking.getEarlyCheckinFee() : 0L;

        // BR6: Tuần tự số nguyên để không bao giờ lệch 1đ với CHECK constraint CSDL
        long subtotal = roomCharge + serviceChargeTotal + earlyCheckinFee + lateCheckoutFee;
        long serviceChargeAmount = Math.round(subtotal * 0.05);
        long vatAmount = Math.round((subtotal + serviceChargeAmount) * 0.10);
        long grandTotal = subtotal + serviceChargeAmount + vatAmount;

        // Khấu trừ tiền cọc: Online = 30% đã đóng; Walk-in = 0đ (BR4)
        long depositDeducted = booking.getDepositAmount() != null ? booking.getDepositAmount() : 0L;
        long balanceDue = Math.max(0L, grandTotal - depositDeducted);

        CheckOutSummaryDto dto = new CheckOutSummaryDto();
        dto.setBookingId(booking.getBookingId());
        dto.setBookingCode(booking.getBookingCode());
        dto.setCustomerName(booking.getCustomer().getFullName());
        dto.setCustomerPhone(booking.getCustomer().getPhone());
        dto.setRoomNumber(booking.getRoom() != null ? booking.getRoom().getRoomNumber() : "N/A");
        dto.setRoomTypeName(booking.getRoomType().getName());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setActualCheckIn(booking.getActualCheckIn());
        dto.setActualCheckOut(booking.getActualCheckOut() != null ? booking.getActualCheckOut() : now);
        dto.setNights(booking.getNights());

        dto.setRoomCharge(roomCharge);
        dto.setServiceChargeTotal(serviceChargeTotal);
        dto.setEarlyCheckinFee(earlyCheckinFee);
        dto.setSuggestedLateFee(suggestedLateFee);
        dto.setLateCheckoutFee(lateCheckoutFee);
        dto.setSubtotal(subtotal);

        dto.setServiceChargeAmount(serviceChargeAmount);
        dto.setVatAmount(vatAmount);
        dto.setGrandTotal(grandTotal);

        dto.setDepositDeducted(depositDeducted);
        dto.setBalanceDue(balanceDue);
        dto.setServiceOrders(serviceOrders);

        return dto;
    }

    /**
     * Xác nhận Thanh toán, Chống Trùng Hóa đơn (Idempotency) & Chuyển Phòng sang DIRTY (BR7)
     */
    @Transactional
    public Invoice confirmCheckOutAndPay(Long bookingId, Long lateCheckoutFee, PaymentMethod paymentMethod, User staff) {
        // 1. Kiểm tra Idempotency chống tạo trùng hóa đơn khi bấm nhiều lần
        Optional<Invoice> existingInvoice = invoiceRepository.findByBooking_BookingId(bookingId);
        if (existingInvoice.isPresent()) {
            return existingInvoice.get();
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessException(MessageConstants.MSG_31);
        }

        // Tính toán lại các số tiền chính xác
        CheckOutSummaryDto summary = prepareCheckOutSummary(bookingId, lateCheckoutFee);

        // 2. Tạo bản ghi Hóa đơn VAT (invoices)
        Invoice invoice = new Invoice();
        invoice.setBooking(booking);
        invoice.setRoomCharge(summary.getRoomCharge());
        invoice.setServiceChargeTotal(summary.getServiceChargeTotal());
        invoice.setEarlyCheckinFee(summary.getEarlyCheckinFee());
        invoice.setLateCheckoutFee(summary.getLateCheckoutFee());
        invoice.setSubtotal(summary.getSubtotal());
        invoice.setServiceChargeAmount(summary.getServiceChargeAmount());
        invoice.setVatAmount(summary.getVatAmount());
        invoice.setGrandTotal(summary.getGrandTotal());
        invoice.setDepositDeducted(summary.getDepositDeducted());
        invoice.setBalanceDue(summary.getBalanceDue());
        invoice.setPaymentMethod(paymentMethod != null ? paymentMethod : PaymentMethod.CASH);
        invoice.setPaidAt(LocalDateTime.now());
        invoice.setCreatedBy(staff);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 3. Cập nhật trạng thái đơn đặt phòng sang CHECKED_OUT
        booking.setLateCheckoutFee(summary.getLateCheckoutFee());
        booking.setStatus(BookingStatus.CHECKED_OUT);
        booking.setActualCheckOut(LocalDateTime.now());
        bookingRepository.save(booking);

        // 4. BR7: Tự động chuyển phòng vật lý sang trạng thái DIRTY (Chờ dọn dẹp)
        Room room = booking.getRoom();
        if (room != null) {
            room.setStatus(RoomStatus.DIRTY);
            roomRepository.save(room);
        }

        return savedInvoice;
    }
}
