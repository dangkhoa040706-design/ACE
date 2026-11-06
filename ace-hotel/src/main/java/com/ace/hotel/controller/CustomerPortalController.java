package com.ace.hotel.controller;

import com.ace.hotel.dto.CustomerDashboardDto;
import com.ace.hotel.dto.CustomerProfileDto;
import com.ace.hotel.dto.OnlineBookingRequestDto;
import com.ace.hotel.dto.RoomTypeQuoteDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Invoice;
import com.ace.hotel.entity.ServiceOrder;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.Role;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.InvoiceRepository;
import com.ace.hotel.repository.ServiceOrderRepository;
import com.ace.hotel.repository.UserRepository;
import com.ace.hotel.service.BookingService;
import com.ace.hotel.service.CustomerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerPortalController {

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    public CustomerPortalController(BookingRepository bookingRepository,
                                    BookingService bookingService,
                                    CustomerService customerService,
                                    UserRepository userRepository,
                                    InvoiceRepository invoiceRepository,
                                    ServiceOrderRepository serviceOrderRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }

    @GetMapping("/booking")
    public String redirectBooking() {
        return "redirect:/booking/search";
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
        User currentUser = getCurrentUser(authentication);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("pageTitle", "Tìm Phòng & Đặt Cọc 30%");

        if (currentUser != null && currentUser.getRole() == Role.CUSTOMER) {
            return "customer/booking-portal";
        }
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
                                    @RequestParam(value = "tab", required = false, defaultValue = "all") String activeTab,
                                    Authentication authentication,
                                    Model model) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        CustomerDashboardDto dashboard = customerService.getCustomerDashboardData(currentUser);
        CustomerProfileDto profileDto = new CustomerProfileDto(
                dashboard.getCustomer().getFullName(),
                dashboard.getCustomer().getPhone(),
                dashboard.getCustomer().getIdNumber(),
                dashboard.getCustomer().getDateOfBirth(),
                dashboard.getCustomer().getEmail()
        );

        model.addAttribute("dashboard", dashboard);
        model.addAttribute("profileDto", profileDto);
        model.addAttribute("bookings", dashboard.getAllBookings());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("newBookingCode", newBookingCode);
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("defaultCheckIn", LocalDate.now());
        model.addAttribute("defaultCheckOut", LocalDate.now().plusDays(1));
        model.addAttribute("pageTitle", "Cổng Hội Viên ACE Elite - Bàn Điều Khiển & Phiếu Đặt Phòng");

        return "customer/dashboard";
    }

    @PostMapping("/customer/profile/update")
    public String handleUpdateProfile(@ModelAttribute("profileDto") CustomerProfileDto profileDto,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            customerService.updateProfile(currentUser.getUserId(), profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin hồ sơ hội viên thành công!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/dashboard?tab=profile";
    }

    @PostMapping("/customer/profile/change-password")
    public String handleChangePassword(@ModelAttribute("profileDto") CustomerProfileDto profileDto,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            customerService.changePassword(currentUser.getUserId(), profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu tài khoản thành công!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/dashboard?tab=password";
    }

    @GetMapping("/customer/invoices/{id}")
    public String viewCustomerInvoice(@PathVariable("id") Long id,
                                      Authentication authentication,
                                      Model model) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy hóa đơn ID: " + id));

        Booking booking = invoice.getBooking();
        // Kiểm tra quyền sở hữu hóa đơn
        if (booking.getCustomer().getUser() == null ||
            !booking.getCustomer().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BusinessException(MessageConstants.MSG_05);
        }

        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByBooking_BookingIdOrderByOrderedAtDesc(booking.getBookingId());

        model.addAttribute("invoice", invoice);
        model.addAttribute("booking", booking);
        model.addAttribute("serviceOrders", serviceOrders);

        return "staff/invoice-print";
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

    @GetMapping(value = "/customer/bookings/{id}/folio-data", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getCustomerBookingFolio(@PathVariable("id") Long id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Vui lòng đăng nhập"));
        }
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Không tìm thấy đơn đặt phòng"));
        }
        if (booking.getCustomer().getUser() == null ||
            !booking.getCustomer().getUser().getUserId().equals(currentUser.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bạn không có quyền xem đơn này"));
        }

        List<ServiceOrder> orders = serviceOrderRepository.findByBooking_BookingIdOrderByOrderedAtDesc(id);
        long serviceTotal = orders.stream().mapToLong(ServiceOrder::getLineTotal).sum();
        long roomCharge = booking.getRoomCharge() != null ? booking.getRoomCharge() : 0L;
        long deposit = booking.getDepositAmount() != null ? booking.getDepositAmount() : 0L;
        long balanceDue = Math.max(0, (roomCharge + serviceTotal) - deposit);

        List<Map<String, Object>> items = orders.stream().map(o -> Map.<String, Object>of(
            "serviceName", o.getService().getName(),
            "category", o.getService().getCategory().name(),
            "quantity", o.getQuantity(),
            "unitPrice", o.getUnitPrice(),
            "lineTotal", o.getLineTotal(),
            "orderedAt", o.getOrderedAt().toString()
        )).toList();

        return ResponseEntity.ok(Map.of(
            "bookingCode", booking.getBookingCode(),
            "roomNumber", booking.getRoom() != null ? booking.getRoom().getRoomNumber() : "Đang chuẩn bị",
            "roomType", booking.getRoomType().getName(),
            "nights", booking.getNights(),
            "roomCharge", roomCharge,
            "depositPaid", deposit,
            "serviceTotal", serviceTotal,
            "balanceDue", balanceDue,
            "items", items
        ));
    }

    @PostMapping("/customer/bookings/{id}/request-checkout")
    public String handleExpressCheckoutRequest(@PathVariable("id") Long id,
                                               Authentication authentication,
                                               RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login";
        }
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + id));
        if (booking.getCustomer().getUser() == null ||
            !booking.getCustomer().getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BusinessException(MessageConstants.MSG_05);
        }
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessException("Chỉ đơn đang lưu trú mới có thể yêu cầu trả phòng nhanh.");
        }

        String roomName = booking.getRoom() != null ? booking.getRoom().getRoomNumber() : booking.getBookingCode();
        redirectAttributes.addFlashAttribute("successMessage",
            "Đã gửi yêu cầu trả phòng nhanh cho phòng " + roomName + " đến Quầy Lễ Tân! Lễ tân đang hoàn tất hóa đơn sẵn sàng tiếp đón quý khách trong 30 giây.");
        return "redirect:/customer/dashboard";
    }
}
