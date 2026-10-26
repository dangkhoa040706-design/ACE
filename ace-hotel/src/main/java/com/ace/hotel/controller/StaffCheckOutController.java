package com.ace.hotel.controller;

import com.ace.hotel.dto.CheckOutSummaryDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Invoice;
import com.ace.hotel.entity.ServiceOrder;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.PaymentMethod;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.InvoiceRepository;
import com.ace.hotel.repository.ServiceOrderRepository;
import com.ace.hotel.repository.UserRepository;
import com.ace.hotel.service.CheckOutService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffCheckOutController {

    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;
    private final CheckOutService checkOutService;

    public StaffCheckOutController(BookingRepository bookingRepository,
                                  InvoiceRepository invoiceRepository,
                                  ServiceOrderRepository serviceOrderRepository,
                                  UserRepository userRepository,
                                  CheckOutService checkOutService) {
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
        this.serviceOrderRepository = serviceOrderRepository;
        this.userRepository = userRepository;
        this.checkOutService = checkOutService;
    }

    private User getStaff(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByUsername(userDetails.getUsername()).orElse(null);
    }

    /**
     * Màn hình Quyết toán Check-out (WF-08)
     */
    @GetMapping("/bookings/{id}/check-out")
    public String showCheckOutPage(@PathVariable("id") Long id,
                                   @RequestParam(value = "lateFee", required = false) Long lateFee,
                                   Model model) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + id));

        boolean isAlreadyCheckedOut = (booking.getStatus() == BookingStatus.CHECKED_OUT);
        boolean isNotCheckedIn = (booking.getStatus() != BookingStatus.CHECKED_IN && !isAlreadyCheckedOut);
        if (isAlreadyCheckedOut) {
            invoiceRepository.findByBooking_BookingId(id)
                    .ifPresent(inv -> model.addAttribute("existingInvoiceId", inv.getInvoiceId()));
        }
        model.addAttribute("isAlreadyCheckedOut", isAlreadyCheckedOut);
        model.addAttribute("isNotCheckedIn", isNotCheckedIn);

        CheckOutSummaryDto summary = checkOutService.prepareCheckOutSummary(id, lateFee);

        model.addAttribute("summary", summary);
        model.addAttribute("booking", booking);
        model.addAttribute("paymentMethods", PaymentMethod.values());

        return "staff/check-out";
    }

    /**
     * Xác nhận Thanh toán Check-out & Lập Hóa đơn VAT
     */
    @PostMapping("/bookings/{id}/check-out")
    public String processCheckOut(@PathVariable("id") Long id,
                                  @RequestParam(value = "lateCheckoutFee", defaultValue = "0") Long lateCheckoutFee,
                                  @RequestParam(value = "paymentMethod", defaultValue = "CASH") PaymentMethod paymentMethod,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            User staff = getStaff(userDetails);
            Invoice invoice = checkOutService.confirmCheckOutAndPay(id, lateCheckoutFee, paymentMethod, staff);
            String roomNum = invoice.getBooking().getRoom() != null ? invoice.getBooking().getRoom().getRoomNumber() : "N/A";
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format(MessageConstants.MSG_29, invoice.getInvoiceId(), roomNum));
            return "redirect:/staff/invoices/" + invoice.getInvoiceId();
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/staff/bookings/" + id + "/check-out";
        }
    }

    /**
     * Xem & In Hóa đơn VAT
     */
    @GetMapping("/invoices/{id}")
    public String viewInvoice(@PathVariable("id") Long id, Model model) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy hóa đơn ID: " + id));

        Booking booking = invoice.getBooking();
        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByBooking_BookingIdOrderByOrderedAtDesc(booking.getBookingId());

        model.addAttribute("invoice", invoice);
        model.addAttribute("booking", booking);
        model.addAttribute("serviceOrders", serviceOrders);

        return "staff/invoice-print";
    }
}
