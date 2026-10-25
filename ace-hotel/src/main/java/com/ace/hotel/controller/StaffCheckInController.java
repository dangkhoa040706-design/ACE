package com.ace.hotel.controller;

import com.ace.hotel.dto.WalkInCheckInDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.RoomStatus;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.CustomerRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.repository.UserRepository;
import com.ace.hotel.service.CheckInService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffCheckInController {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CheckInService checkInService;

    public StaffCheckInController(BookingRepository bookingRepository,
                                  RoomRepository roomRepository,
                                  CustomerRepository customerRepository,
                                  UserRepository userRepository,
                                  CheckInService checkInService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.checkInService = checkInService;
    }

    private User getStaff(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByUsername(userDetails.getUsername()).orElse(null);
    }

    /**
     * Chế độ 1: Tra cứu đơn đặt trước theo mã QR (BK-XXXX) hoặc SĐT
     */
    @GetMapping("/check-in")
    public String showCheckInPage(@RequestParam(value = "code", required = false) String code,
                                  Model model) {
        model.addAttribute("activeTab", "reserved");

        if (code != null && !code.trim().isEmpty()) {
            String trimmed = code.trim();
            Booking booking = bookingRepository.findByBookingCodeIgnoreCase(trimmed).orElse(null);

            // Nếu không tìm thấy theo mã booking, thử tìm theo SĐT khách
            if (booking == null) {
                Customer cust = customerRepository.findByPhone(trimmed).orElse(null);
                if (cust != null) {
                    List<Booking> list = bookingRepository.findByCustomer_CustomerIdOrderByCreatedAtDesc(cust.getCustomerId());
                    booking = list.stream()
                            .filter(b -> b.getStatus() == BookingStatus.RESERVED)
                            .findFirst().orElse(null);
                }
            }

            if (booking != null && booking.getStatus() == BookingStatus.RESERVED) {
                model.addAttribute("booking", booking);
                // Lọc phòng vật lý sạch (AVAILABLE) thuộc đúng hạng phòng (BR2)
                List<Room> cleanRooms = roomRepository.findByRoomType_RoomTypeIdAndStatusOrderByRoomNumberAsc(
                        booking.getRoomType().getRoomTypeId(),
                        RoomStatus.AVAILABLE
                );
                model.addAttribute("cleanRooms", cleanRooms);
                if (cleanRooms.isEmpty()) {
                    model.addAttribute("warningMessage", MessageConstants.MSG_27);
                }
            } else {
                model.addAttribute("errorMessage", String.format(MessageConstants.MSG_26, trimmed));
            }
        }

        return "staff/check-in";
    }

    /**
     * Xác nhận Check-in gán phòng sạch cho khách đặt trước
     */
    @PostMapping("/check-in")
    public String processCheckIn(@RequestParam("bookingId") Long bookingId,
                                 @RequestParam("roomId") Long roomId,
                                 @RequestParam(value = "earlyCheckinFee", defaultValue = "0") Long earlyCheckinFee,
                                 @RequestParam(value = "idNumber", required = false) String idNumber,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            User staff = getStaff(userDetails);
            Booking booking = checkInService.assignAndCheckIn(bookingId, roomId, earlyCheckinFee, idNumber, staff);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format(MessageConstants.MSG_24, booking.getRoom().getRoomNumber(), booking.getBookingCode()));
            return "redirect:/staff/dashboard";
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            String redirectCode = bookingRepository.findById(bookingId)
                    .map(Booking::getBookingCode)
                    .orElse(String.valueOf(bookingId));
            return "redirect:/staff/check-in?code=" + redirectCode;
        }
    }

    /**
     * Chế độ 2: Tiếp nhận Khách Vãng lai (Walk-in - 0đ cọc)
     */
    @GetMapping("/walk-in")
    public String showWalkInPage(@RequestParam(value = "roomId", required = false) Long roomId,
                                 Model model) {
        model.addAttribute("activeTab", "walkin");
        List<Room> availableRooms = roomRepository.findByStatusOrderByRoomNumberAsc(RoomStatus.AVAILABLE);
        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("selectedRoomId", roomId);
        model.addAttribute("defaultCheckOutDate", LocalDate.now().plusDays(1));
        model.addAttribute("walkInDto", new WalkInCheckInDto());
        return "staff/check-in";
    }

    /**
     * Lưu tiếp nhận Walk-in
     */
    @PostMapping("/walk-in")
    public String processWalkIn(@ModelAttribute WalkInCheckInDto dto,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            User staff = getStaff(userDetails);
            Booking booking = checkInService.processWalkInCheckIn(dto, staff);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format(MessageConstants.MSG_25, booking.getRoom().getRoomNumber()));
            return "redirect:/staff/dashboard";
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/staff/walk-in" + (dto.getRoomId() != null ? "?roomId=" + dto.getRoomId() : "");
        }
    }
}
