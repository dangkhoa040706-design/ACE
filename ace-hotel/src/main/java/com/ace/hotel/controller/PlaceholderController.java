package com.ace.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ace.hotel.dto.ModuleInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Giữ chỗ cho các màn hình nghiệp vụ chưa làm (UC02 đến UC07), để menu và nút trên dashboard
 * bấm được và phân quyền kiểm thử được ngay. Khi làm xong màn hình nào thì xóa đường dẫn
 * tương ứng khỏi danh sách dưới đây và viết controller thật.
 * Xem plan 04-man-hinh-nghiep-vu.md và 06-kien-truc-route.md.
 */
@Controller
public class PlaceholderController {

    @GetMapping({
            "/rooms", "/rooms/**",
            "/room-types", "/room-types/**",
            "/bookings", "/bookings/**",
            "/services", "/services/**",
            "/reports", "/reports/**",
            "/invoices/**",
            "/customers/**"})
    public String placeholder(HttpServletRequest request, Model model) {
        ModuleInfo info = resolve(request.getRequestURI());
        model.addAttribute("info", info);
        model.addAttribute("active", info.active());
        return "placeholder";
    }

    /**
     * Trang 403 (Spring Security chuyển về đây khi sai vai trò). Dùng @RequestMapping để nhận cả POST,
     * vì khi lễ tân gửi form tới chức năng của quản lý, yêu cầu được forward giữ nguyên phương thức.
     */
    @RequestMapping("/403")
    public String forbidden(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return "error/403";
    }

    private ModuleInfo resolve(String path) {
        if (path.equals("/bookings/search")) {
            return new ModuleInfo("Tìm phòng trống và đặt phòng", "UC03 (FR-06, FR-07, FR-08)", "S05, S06, S07", "Tuần 2", "search");
        }
        if (path.matches("/bookings/\\d+/check-in")) {
            return new ModuleInfo("Check-in", "UC04 (FR-10)", "S09", "Tuần 2", "checkin");
        }
        if (path.matches("/bookings/\\d+/check-out")) {
            return new ModuleInfo("Check-out và thanh toán", "UC05 (FR-13, FR-14, FR-15)", "S11", "Tuần 2", "checkout");
        }
        if (path.matches("/bookings/\\d+/services")) {
            return new ModuleInfo("Dịch vụ đi kèm", "UC06 (FR-11)", "S10", "Tuần 2", "services-in-house");
        }
        if (path.startsWith("/bookings")) {
            return new ModuleInfo("Danh sách và chi tiết đặt phòng", "UC03 (FR-09)", "S08", "Tuần 2", "bookings");
        }
        if (path.startsWith("/room-types")) {
            return new ModuleInfo("Loại phòng", "UC02 (FR-03)", "S04", "Tuần 2", "room-types");
        }
        if (path.startsWith("/rooms")) {
            return new ModuleInfo("Quản lý phòng", "UC02 (FR-04, FR-05)", "S02, S03", "Tuần 2", "rooms");
        }
        if (path.startsWith("/services")) {
            return new ModuleInfo("Danh mục dịch vụ", "UC06 (FR-12)", "S12", "Tuần 2 (P1)", "services");
        }
        if (path.startsWith("/reports")) {
            return new ModuleInfo("Thống kê doanh thu và công suất phòng", "UC07 (FR-16, FR-17)", "S13", "Nếu còn thời gian (P2)", "reports");
        }
        if (path.startsWith("/invoices")) {
            return new ModuleInfo("Hóa đơn", "UC05 (FR-14, FR-15)", "S11", "Tuần 2", "bookings");
        }
        if (path.startsWith("/customers")) {
            return new ModuleInfo("Khách hàng", "UC03 (FR-07)", "S06", "Tuần 2", "search");
        }
        return new ModuleInfo("Màn hình nghiệp vụ", "-", "-", "-", "");
    }
}
