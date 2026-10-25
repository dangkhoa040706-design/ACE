package com.ace.hotel.controller;

import com.ace.hotel.dto.AdminReportDto;
import com.ace.hotel.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public String showAdminHome(Model model) {
        model.addAttribute("dashboard", reportService.getAdminHomeDashboardData());
        model.addAttribute("pageTitle", "Trang Chủ Quản Trị Viên - Executive Cockpit");
        return "admin/home";
    }

    @GetMapping("/reports")
    public String showAdminReports(@RequestParam(value = "fromDate", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                   @RequestParam(value = "toDate", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                   Model model) {
        if (fromDate == null) {
            fromDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }

        AdminReportDto report = reportService.generateReport(fromDate, toDate);
        model.addAttribute("report", report);
        model.addAttribute("pageTitle", "Báo Cáo Doanh Thu & Công Suất Phòng");

        return "admin/dashboard-report";
    }
}
