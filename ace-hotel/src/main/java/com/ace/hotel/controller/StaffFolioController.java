package com.ace.hotel.controller;

import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.ServiceItem;
import com.ace.hotel.entity.ServiceOrder;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.ServiceCategory;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.ServiceItemRepository;
import com.ace.hotel.repository.ServiceOrderRepository;
import com.ace.hotel.repository.UserRepository;
import com.ace.hotel.service.FolioService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff")
public class StaffFolioController {

    private final BookingRepository bookingRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final UserRepository userRepository;
    private final FolioService folioService;

    public StaffFolioController(BookingRepository bookingRepository,
                                ServiceOrderRepository serviceOrderRepository,
                                ServiceItemRepository serviceItemRepository,
                                UserRepository userRepository,
                                FolioService folioService) {
        this.bookingRepository = bookingRepository;
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceItemRepository = serviceItemRepository;
        this.userRepository = userRepository;
        this.folioService = folioService;
    }

    private User getStaff(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByUsername(userDetails.getUsername()).orElse(null);
    }

    @GetMapping("/bookings/{id}/folio")
    public String showFolio(@PathVariable("id") Long id, Model model) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + id));

        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByBooking_BookingIdOrderByOrderedAtDesc(id);
        long serviceChargeTotal = serviceOrders.stream().mapToLong(ServiceOrder::getLineTotal).sum();
        List<ServiceItem> services = serviceItemRepository.findAllByOrderByCategoryAscNameAsc();

        model.addAttribute("booking", booking);
        model.addAttribute("serviceOrders", serviceOrders);
        model.addAttribute("serviceChargeTotal", serviceChargeTotal);
        model.addAttribute("services", services);
        model.addAttribute("categories", ServiceCategory.values());

        return "staff/folio";
    }

    @GetMapping("/bookings/{id}/folio-modal")
    public String showFolioModal(@PathVariable("id") Long id, Model model) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy đơn đặt phòng ID: " + id));

        List<ServiceOrder> serviceOrders = serviceOrderRepository.findByBooking_BookingIdOrderByOrderedAtDesc(id);
        long serviceChargeTotal = serviceOrders.stream().mapToLong(ServiceOrder::getLineTotal).sum();
        List<ServiceItem> services = serviceItemRepository.findAllByOrderByCategoryAscNameAsc();

        model.addAttribute("booking", booking);
        model.addAttribute("serviceOrders", serviceOrders);
        model.addAttribute("serviceChargeTotal", serviceChargeTotal);
        model.addAttribute("services", services);
        model.addAttribute("categories", ServiceCategory.values());

        return "staff/folio-modal :: folioModal";
    }

    @PostMapping("/bookings/{id}/folio")
    public String addServiceOrder(@PathVariable("id") Long id,
                                  @RequestParam("serviceId") Long serviceId,
                                  @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            User staff = getStaff(userDetails);
            ServiceOrder order = folioService.addServiceOrder(id, serviceId, quantity, staff);
            String roomNum = order.getBooking().getRoom() != null ? order.getBooking().getRoom().getRoomNumber() : "N/A";
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format(MessageConstants.MSG_32, roomNum));
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/staff/bookings/" + id + "/folio";
    }

    @PostMapping(value = "/bookings/{id}/folio/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addServiceOrderAjax(@PathVariable("id") Long id,
                                                   @RequestParam("serviceId") Long serviceId,
                                                   @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User staff = getStaff(userDetails);
            ServiceOrder order = folioService.addServiceOrder(id, serviceId, quantity, staff);
            String roomNum = order.getBooking().getRoom() != null ? order.getBooking().getRoom().getRoomNumber() : "N/A";
            return Map.of("success", true, "message", String.format(MessageConstants.MSG_32, roomNum));
        } catch (BusinessException ex) {
            return Map.of("success", false, "message", ex.getMessage());
        }
    }

    @PostMapping("/bookings/{id}/folio/{orderId}/delete")
    public String deleteServiceOrder(@PathVariable("id") Long id,
                                     @PathVariable("orderId") Long orderId,
                                     RedirectAttributes redirectAttributes) {
        try {
            folioService.deleteServiceOrder(id, orderId);
            redirectAttributes.addFlashAttribute("successMessage", MessageConstants.MSG_33);
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/staff/bookings/" + id + "/folio";
    }

    @PostMapping(value = "/bookings/{id}/folio/{orderId}/delete/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> deleteServiceOrderAjax(@PathVariable("id") Long id,
                                                      @PathVariable("orderId") Long orderId) {
        try {
            folioService.deleteServiceOrder(id, orderId);
            return Map.of("success", true, "message", MessageConstants.MSG_33);
        } catch (BusinessException ex) {
            return Map.of("success", false, "message", ex.getMessage());
        }
    }
}
