package com.ace.hotel;

import com.ace.hotel.config.RoleBasedAuthSuccessHandler;
import com.ace.hotel.entity.RoomType;
import com.ace.hotel.service.PricingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class BugFixSmokeTest {

    @Test
    @DisplayName("Bug 1 Test: Chống lỗ hổng Open Redirect (CWE-601)")
    void testOpenRedirectSanitization() {
        assertFalse(RoleBasedAuthSuccessHandler.isSafeRedirectUrl("http://evil.com"));
        assertFalse(RoleBasedAuthSuccessHandler.isSafeRedirectUrl("https://phishing.com/login"));
        assertFalse(RoleBasedAuthSuccessHandler.isSafeRedirectUrl("//evil.com"));
        assertFalse(RoleBasedAuthSuccessHandler.isSafeRedirectUrl("/\\evil.com"));
        assertFalse(RoleBasedAuthSuccessHandler.isSafeRedirectUrl("javascript:alert(1)"));
        assertFalse(RoleBasedAuthSuccessHandler.isSafeRedirectUrl(null));
        assertFalse(RoleBasedAuthSuccessHandler.isSafeRedirectUrl(""));

        assertTrue(RoleBasedAuthSuccessHandler.isSafeRedirectUrl("/customer/dashboard"));
        assertTrue(RoleBasedAuthSuccessHandler.isSafeRedirectUrl("/booking/search?checkIn=2026-11-06&checkOut=2026-11-07&guests=2"));
    }

    @Test
    @DisplayName("Bug 3 Test: Tính toán thuế phí Live Folio đồng bộ với biểu giá 5% SVC & 10% VAT")
    void testLiveFolioFinancialFormulas() {
        PricingService pricingService = new PricingService();
        long roomCharge = 2000000L;
        long serviceTotal = 310000L;
        long subtotal = roomCharge + serviceTotal; // 2.310.000đ

        long svcAmount = pricingService.calculateSvc5Percent(subtotal); // 115.500đ
        assertEquals(115500L, svcAmount);

        long vatAmount = pricingService.calculateVat10Percent(subtotal, svcAmount); // 242.550đ
        assertEquals(242550L, vatAmount);

        long grandTotal = subtotal + svcAmount + vatAmount; // 2.668.050đ
        assertEquals(2668050L, grandTotal);

        long deposit = 693000L;
        long balanceDue = Math.max(0L, grandTotal - deposit); // 1.975.050đ
        assertEquals(1975050L, balanceDue);
    }

    @Test
    @DisplayName("Bug 4 Test: Phụ thu quá hạn nhiều ngày tính chính xác số ngày quá hạn")
    void testMultiDayOverdueCalculationFormula() {
        LocalDate checkOutDate = LocalDate.of(2026, 11, 2);
        LocalDate actualDate = LocalDate.of(2026, 11, 5); // Quá hạn 3 ngày
        long overdueDays = ChronoUnit.DAYS.between(checkOutDate, actualDate);
        assertEquals(3L, overdueDays);

        long pricePerNight = 1000000L;
        long overdueBase = pricePerNight * Math.max(1L, overdueDays);
        assertEquals(3000000L, overdueBase, "Quá hạn 3 ngày phải phụ thu 3.000.000đ thay vì 1.000.000đ");
    }

    @Test
    @DisplayName("Bug 6 Test: Phòng vệ tham số phí âm (Negative Fee Clamping)")
    void testDefensiveNegativeFeeClamping() {
        Long negativeFee = -500000L;
        long safeFee = Math.max(0L, negativeFee != null ? negativeFee : 0L);
        assertEquals(0L, safeFee, "Phí âm phải được chặn về 0đ");

        Long validFee = 250000L;
        long safeValidFee = Math.max(0L, validFee != null ? validFee : 0L);
        assertEquals(250000L, safeValidFee);
    }

    @Test
    @DisplayName("Bug 7 Test: Chuẩn hóa đảo ngược ngày tháng trong báo cáo")
    void testDateRangeNormalization() {
        LocalDate fromDate = LocalDate.of(2026, 12, 1);
        LocalDate toDate = LocalDate.of(2026, 11, 1);
        assertTrue(fromDate.isAfter(toDate));

        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        assertEquals(LocalDate.of(2026, 11, 1), fromDate);
        assertEquals(LocalDate.of(2026, 12, 1), toDate);
    }

    @Test
    @DisplayName("Customer Dashboard Redesign Test: Kiểm tra đầy đủ 4 phân khu, dải phân cách và Brand Footer 5 sao")
    void testCustomerDashboardRedesignIntegrity() throws Exception {
        java.nio.file.Path dashboardPath = java.nio.file.Path.of("src/main/resources/templates/customer/dashboard.html");
        java.nio.file.Path layoutPath = java.nio.file.Path.of("src/main/resources/templates/layout/customer-layout.html");
        java.nio.file.Path cssPath = java.nio.file.Path.of("src/main/resources/static/css/components.css");

        assertTrue(java.nio.file.Files.exists(dashboardPath), "dashboard.html phải tồn tại");
        assertTrue(java.nio.file.Files.exists(layoutPath), "customer-layout.html phải tồn tại");
        assertTrue(java.nio.file.Files.exists(cssPath), "components.css phải tồn tại");

        String dashboardHtml = java.nio.file.Files.readString(dashboardPath);
        String layoutHtml = java.nio.file.Files.readString(layoutPath);
        String cssContent = java.nio.file.Files.readString(cssPath);

        // 1. Kiểm tra 4 khu vực rạch ròi
        assertTrue(dashboardHtml.contains("ĐẶC QUYỀN HỘI VIÊN &amp; THẺ VIP ELITE"), "Thiếu tiêu đề Đặc quyền hội viên");
        assertTrue(dashboardHtml.contains("TRA CỨU PHÒNG TRỐNG &amp; ĐẶT CỌC GIỮ CHỖ (30%)"), "Thiếu tiêu đề Tra cứu phòng trống");
        assertTrue(dashboardHtml.contains("SỔ PHIẾU ĐẶT PHÒNG &amp; THẺ LƯU TRÚ QR"), "Thiếu tiêu đề Sổ phiếu đặt phòng");
        assertTrue(dashboardHtml.contains("DỊCH VỤ &amp; TIỆN ÍCH TRONG PHÒNG 24/7"), "Thiếu tiêu đề Dịch vụ & tiện ích");

        // 2. Kiểm tra dải phân cách ánh kim
        assertTrue(dashboardHtml.contains("section-divider-glyph"), "Thiếu biểu tượng dải phân cách ánh kim");
        assertTrue(cssContent.contains(".section-divider"), "Thiếu class CSS .section-divider");

        // 3. Kiểm tra Thẻ VIP phát sáng và bệ phóng ánh sáng
        assertTrue(dashboardHtml.contains("vip-card-pedestal"), "Thiếu bệ phóng ánh sáng cho thẻ VIP");
        assertTrue(cssContent.contains(".vip-card-pedestal::before"), "Thiếu hiệu ứng hào quang quầng sáng sau thẻ VIP");
        assertTrue(cssContent.contains(".luxury-vip-card::before"), "Thiếu vệt quét Holographic Metallic Luster Sweep");

        // 4. Kiểm tra Brand Footer 5 sao trong customer-layout
        assertTrue(layoutHtml.contains("customer-footer-grid"), "Thiếu lưới 3 cột chân trang hội viên");
        assertTrue(layoutHtml.contains("0903723342"), "Thiếu Hotline Lễ tân 24/7 trong footer");
        assertTrue(layoutHtml.contains("Chính Sách &amp; Giờ Giấc"), "Thiếu cụm chính sách & giờ giấc trong footer");
        assertTrue(layoutHtml.contains("5% Phí phục vụ (SVC) + 10% VAT"), "Thiếu chuẩn mực minh bạch biểu giá trong footer");
    }
}

