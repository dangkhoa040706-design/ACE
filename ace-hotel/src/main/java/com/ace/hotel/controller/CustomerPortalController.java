package com.ace.hotel.controller;

import com.ace.hotel.dto.OnlineBookingRequestDto;
import com.ace.hotel.dto.RoomTypeQuoteDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.User;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.UserRepository;
import com.ace.hotel.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
public class CustomerPortalController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public CustomerPortalController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }

    @GetMapping("/booking/search")
    public String searchRooms(@RequestParam(value = "checkIn", required = false)
                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                              @RequestParam(value = "checkOut", required = false)
                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
                              @RequestParam(value = "guests", required = false, defaultValue = "2") Integer guests,
                              Authentication authentication,
                              Model model) {
        if (checkIn == null) {
            checkIn = LocalDate.now();
        }
        if (checkOut == null) {
            checkOut = checkIn.plusDays(1);
        }

        if (!checkOut.isAfter(checkIn)) {
            model.addAttribute("errorMessage", MessageConstants.MSG_20);
            checkOut = checkIn.plusDays(1);
        }

        int guestCount = Math.max(1, guests != null ? guests : 2);
        List<RoomTypeQuoteDto> quotes = Collections.emptyList();

        if (guestCount > 4) {
            model.addAttribute("capacityAdviceMessage", String.format(
                "Đoàn của quý khách gồm %d khách. Tại ACE Hotel, sức chứa tối đa của 1 phòng là 4 khách (Hạng Family & Suite). Để đảm bảo không gian nghỉ dưỡng thoải mái và đúng quy định an toàn, quý khách vui lòng đặt từ %d phòng trở lên hoặc liên hệ hotline để được hỗ trợ sắp xếp phòng liền kề!",
                guestCount, (int) Math.ceil(guestCount / 4.0)
            ));
        }

        try {
            quotes = bookingService.searchAvailableRoomTypes(checkIn, checkOut, guestCount);
            if (quotes.isEmpty() && !model.containsAttribute("errorMessage")) {
                if (guestCount > 4) {
                    model.addAttribute("infoMessage", "Không có phòng đơn nào chứa đủ " + guestCount + " khách (Tối đa 4 khách/phòng). Quý khách vui lòng chọn số khách ít hơn để đặt từng phòng.");
                } else {
                    model.addAttribute("infoMessage", MessageConstants.MSG_21);
                }
            }
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        OnlineBookingRequestDto requestDto = new OnlineBookingRequestDto();
        requestDto.setCheckInDate(checkIn);
        requestDto.setCheckOutDate(checkOut);
        requestDto.setGuestCount(guestCount);

        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("guests", guestCount);
        model.addAttribute("quotes", quotes);
        model.addAttribute("bookingRequest", requestDto);
        model.addAttribute("currentUser", getCurrentUser(authentication));
        model.addAttribute("pageTitle", "Tìm Phòng & Đặt Cọc 30%");

        return "customer/search-deposit";
    }

    @PostMapping("/booking/deposit")
    public String handleDepositPayment(@ModelAttribute("bookingRequest") OnlineBookingRequestDto request,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.createOnlineBookingWithDeposit(request, currentUser);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format(MessageConstants.MSG_18, booking.getBookingCode()));
            return "redirect:/customer/dashboard?newBooking=" + booking.getBookingCode();
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/booking/search?checkIn=" + request.getCheckInDate() +
                    "&checkOut=" + request.getCheckOutDate() + "&guests=" + request.getGuestCount();
        }
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard(@RequestParam(value = "newBooking", required = false) String newBookingCode,
                                    Authentication authentication,
                                    Model model) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Booking> bookings = bookingService.getBookingsForCustomer(currentUser.getUserId());
        model.addAttribute("bookings", bookings);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("newBookingCode", newBookingCode);
        model.addAttribute("pageTitle", "Cổng Khách Hàng - Phiếu Đặt Phòng QR");

        return "customer/dashboard";
    }

    @PostMapping("/customer/bookings/{id}/cancel")
    public String handleCancelBooking(@PathVariable("id") Long id,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            Booking cancelled = bookingService.cancelBooking(id, currentUser);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format(MessageConstants.MSG_19, cancelled.getBookingCode()));
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/dashboard";
    }
}
