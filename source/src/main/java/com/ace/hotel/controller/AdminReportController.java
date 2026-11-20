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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    private LocalDate[] normalizeDateRange(LocalDate fromDate, LocalDate toDate) {
        LocalDate from = fromDate != null ? fromDate : LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate to = toDate != null ? toDate : LocalDate.now();
        if (from.isAfter(to)) {
            LocalDate temp = from;
            from = to;
            to = temp;
        }
        return new LocalDate[]{from, to};
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
        LocalDate[] range = normalizeDateRange(fromDate, toDate);
        AdminReportDto report = reportService.generateReport(range[0], range[1]);
        model.addAttribute("report", report);
        model.addAttribute("pageTitle", "Báo Cáo Doanh Thu & Công Suất Phòng");

        return "admin/dashboard-report";
    }

    @GetMapping("/reports/export")
    public String exportAdminReportPdf(@RequestParam(value = "fromDate", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                       @RequestParam(value = "toDate", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                       Model model) {
        LocalDate[] range = normalizeDateRange(fromDate, toDate);
        AdminReportDto report = reportService.generateReport(range[0], range[1]);
        LocalDateTime now = LocalDateTime.now();

        model.addAttribute("report", report);
        model.addAttribute("generatedAt", now);
        model.addAttribute("reportCode", "RPT-" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")));
        model.addAttribute("pageTitle", "Báo Cáo Doanh Thu A4 | ACE Hotel");

        return "admin/report-pdf";
    }
}
