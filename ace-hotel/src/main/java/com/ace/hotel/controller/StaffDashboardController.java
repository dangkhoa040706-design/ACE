package com.ace.hotel.controller;

import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.RoomStatus;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.service.CheckInService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff")
public class StaffDashboardController {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final CheckInService checkInService;

    public StaffDashboardController(RoomRepository roomRepository,
                                    BookingRepository bookingRepository,
                                    CheckInService checkInService) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.checkInService = checkInService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // 1. KPI 4 trạng thái phòng realtime
        long availableCount = roomRepository.countByStatus(RoomStatus.AVAILABLE);
        long occupiedCount = roomRepository.countByStatus(RoomStatus.OCCUPIED);
        long dirtyCount = roomRepository.countByStatus(RoomStatus.DIRTY);
        long maintenanceCount = roomRepository.countByStatus(RoomStatus.MAINTENANCE);

        // 2. Sơ đồ 12 phòng vật lý theo thứ tự Tầng tăng dần, Số phòng tăng dần
        List<Room> rooms = roomRepository.findAllByOrderByFloorAscRoomNumberAsc();

        // 3. Tra cứu nhanh thông tin lưu trú cho các phòng OCCUPIED
        List<Booking> checkedInBookings = bookingRepository.findByStatusOrderByCreatedAtDesc(BookingStatus.CHECKED_IN);
        Map<Long, Booking> roomBookingMap = new HashMap<>();
        long overdueCount = 0;
        for (Booking b : checkedInBookings) {
            if (b.getRoom() != null) {
                roomBookingMap.put(b.getRoom().getRoomId(), b);
                if (b.isOverdue()) {
                    overdueCount++;
                }
            }
        }

        // 4. Danh sách các đơn chờ Check-in (RESERVED)
        List<Booking> reservedBookings = bookingRepository.findByStatusOrderByCreatedAtDesc(BookingStatus.RESERVED);

        model.addAttribute("availableCount", availableCount);
        model.addAttribute("occupiedCount", occupiedCount);
        model.addAttribute("dirtyCount", dirtyCount);
        model.addAttribute("maintenanceCount", maintenanceCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomBookingMap", roomBookingMap);
        model.addAttribute("reservedBookings", reservedBookings);
        model.addAttribute("checkedInBookings", checkedInBookings);

        return "staff/dashboard";
    }

    @PostMapping("/rooms/{id}/clean")
    public String markRoomClean(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Room room = checkInService.markRoomAsClean(id);
        redirectAttributes.addFlashAttribute("successMessage",
                String.format(MessageConstants.MSG_30, room.getRoomNumber()));
        return "redirect:/staff/dashboard";
    }
}
