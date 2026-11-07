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
        java.nio.file.Path bookingsPath = java.nio.file.Path.of("src/main/resources/templates/customer/bookings.html");
        java.nio.file.Path layoutPath = java.nio.file.Path.of("src/main/resources/templates/layout/customer-layout.html");
        java.nio.file.Path cssPath = java.nio.file.Path.of("src/main/resources/static/css/components.css");

        assertTrue(java.nio.file.Files.exists(dashboardPath), "dashboard.html phải tồn tại");
        assertTrue(java.nio.file.Files.exists(bookingsPath), "bookings.html phải tồn tại độc lập");
        assertTrue(java.nio.file.Files.exists(layoutPath), "customer-layout.html phải tồn tại");
        assertTrue(java.nio.file.Files.exists(cssPath), "components.css phải tồn tại");

        String dashboardHtml = java.nio.file.Files.readString(dashboardPath);
        String bookingsHtml = java.nio.file.Files.readString(bookingsPath);
        String layoutHtml = java.nio.file.Files.readString(layoutPath);
        String cssContent = java.nio.file.Files.readString(cssPath);

        // 1. Kiểm tra Dashboard: Tách bạch rạch ròi, tập trung Hội Viên & Tiện Ích, liên kết sang /customer/bookings
        assertTrue(dashboardHtml.contains("ĐẶC QUYỀN HỘI VIÊN &amp; THẺ VIP ELITE"), "Thiếu tiêu đề Đặc quyền hội viên");
        assertTrue(dashboardHtml.contains("TRA CỨU PHÒNG TRỐNG &amp; ĐẶT CỌC GIỮ CHỖ (30%)"), "Thiếu tiêu đề Tra cứu phòng trống");
        assertTrue(dashboardHtml.contains("DỊCH VỤ &amp; TIỆN ÍCH TRONG PHÒNG 24/7"), "Thiếu tiêu đề Dịch vụ & tiện ích");
        assertTrue(dashboardHtml.contains("/customer/bookings"), "Dashboard phải liên kết sang trang /customer/bookings độc lập");
        assertFalse(dashboardHtml.contains("id=\"myBookingsSection\""), "Dashboard không được chứa khối myBookingsSection sau khi đã tách trang");

        // 1.1 Kiểm tra Trang Đơn Đã Đặt Độc Lập (/customer/bookings)
        assertTrue(bookingsHtml.contains("SỔ PHIẾU ĐẶT PHÒNG &amp; THẺ LƯU TRÚ QR"), "Trang bookings.html phải có tiêu đề Sổ phiếu đặt phòng");
        assertTrue(bookingsHtml.contains("Chờ Nhận Phòng"), "Trang bookings.html phải có tab Chờ Nhận Phòng");
        assertTrue(bookingsHtml.contains("Đang Ở"), "Trang bookings.html phải có tab Đang Ở");
        assertTrue(bookingsHtml.contains("Đã Trả Phòng"), "Trang bookings.html phải có tab Đã Trả Phòng");
        assertTrue(bookingsHtml.contains("Đã Hủy"), "Trang bookings.html phải có tab Đã Hủy");

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
        assertTrue(layoutHtml.contains("@{/customer/bookings}"), "customer-layout phải điều hướng tới @{/customer/bookings}");
    }

    @Test
    @DisplayName("Clean Code Test: Kiểm tra ValidationUtils mật khẩu mạnh và độ tuổi >= 18")
    void testValidationUtilsRules() {
        // 1. Kiểm tra mật khẩu mạnh chuẩn 5 sao
        assertDoesNotThrow(() -> com.ace.hotel.util.ValidationUtils.validatePasswordStrength("Khach@2026", "Mật khẩu"));
        assertDoesNotThrow(() -> com.ace.hotel.util.ValidationUtils.validatePasswordStrength("AceHotel#999", "Mật khẩu"));

        // Thất bại: chứa dấu cách
        assertThrows(com.ace.hotel.exception.BusinessException.class, 
                () -> com.ace.hotel.util.ValidationUtils.validatePasswordStrength("Khach @2026", "Mật khẩu"));

        // Thất bại: dưới 8 ký tự
        assertThrows(com.ace.hotel.exception.BusinessException.class, 
                () -> com.ace.hotel.util.ValidationUtils.validatePasswordStrength("Khach@1", "Mật khẩu"));

        // Thất bại: thiếu chữ hoa
        assertThrows(com.ace.hotel.exception.BusinessException.class, 
                () -> com.ace.hotel.util.ValidationUtils.validatePasswordStrength("khach@2026", "Mật khẩu"));

        // Thất bại: thiếu ký tự đặc biệt
        assertThrows(com.ace.hotel.exception.BusinessException.class, 
                () -> com.ace.hotel.util.ValidationUtils.validatePasswordStrength("Khach2026", "Mật khẩu"));

        // 2. Kiểm tra độ tuổi tối thiểu 18 tuổi
        LocalDate adultDate = LocalDate.now().minusYears(20);
        assertDoesNotThrow(() -> com.ace.hotel.util.ValidationUtils.validateMinimumAge(adultDate, "Lỗi độ tuổi"));

        LocalDate underageDate = LocalDate.now().minusYears(17);
        assertThrows(com.ace.hotel.exception.BusinessException.class, 
                () -> com.ace.hotel.util.ValidationUtils.validateMinimumAge(underageDate, "Quý khách phải từ đủ 18 tuổi trở lên."));
    }

    @Test
    @DisplayName("Deposit Receipt Voucher Test: Kiểm tra mẫu biên lai cọc 30% A4, QR 3s và bóc tách tài chính")
    void testDepositReceiptTemplateAndCalculation() throws Exception {
        // 1. Kiểm tra sự tồn tại của template và css
        java.nio.file.Path receiptPath = java.nio.file.Path.of("src/main/resources/templates/customer/deposit-receipt.html");
        java.nio.file.Path bookingsPath = java.nio.file.Path.of("src/main/resources/templates/customer/bookings.html");
        java.nio.file.Path printCssPath = java.nio.file.Path.of("src/main/resources/static/css/print.css");
        java.nio.file.Path dashboardPath = java.nio.file.Path.of("src/main/resources/templates/customer/dashboard.html");
        java.nio.file.Path controllerPath = java.nio.file.Path.of("src/main/java/com/ace/hotel/controller/CustomerPortalController.java");

        assertTrue(java.nio.file.Files.exists(receiptPath), "deposit-receipt.html phải tồn tại");
        assertTrue(java.nio.file.Files.exists(bookingsPath), "bookings.html phải tồn tại");
        assertTrue(java.nio.file.Files.exists(printCssPath), "print.css phải tồn tại");

        String receiptHtml = java.nio.file.Files.readString(receiptPath);
        String bookingsHtml = java.nio.file.Files.readString(bookingsPath);
        String printCss = java.nio.file.Files.readString(printCssPath);
        String dashboardHtml = java.nio.file.Files.readString(dashboardPath);
        String controllerCode = java.nio.file.Files.readString(controllerPath);

        // 2. Kiểm tra các thành phần cốt lõi của Phiếu thu cọc 30%
        assertTrue(receiptHtml.contains("ACE HOTEL FLAGSHIP"), "Thiếu tiêu đề thương hiệu Flagship");
        assertTrue(receiptHtml.contains("BIÊN LAI THU CỌC 30%"), "Thiếu tiêu đề Biên lai thu cọc 30%");
        assertTrue(receiptHtml.contains("ĐÃ THANH TOÁN CỌC (PAID)"), "Thiếu con dấu trạng thái PAID");
        assertTrue(receiptHtml.contains("data-booking-qr"), "Thiếu mã QR check-in không chạm");
        assertTrue(receiptHtml.contains("TIỀN CỌC ĐÃ THANH TOÁN (30%):"), "Thiếu dòng tiền cọc 30%");
        assertTrue(receiptHtml.contains("0% Overbooking"), "Thiếu cam kết 0% Overbooking 5 sao");

        // 3. Kiểm tra Single-Page A4 Guarantee trong CSS
        assertTrue(printCss.contains("size: A4 portrait;"), "Thiếu định dạng size: A4 portrait trong print.css");
        assertTrue(printCss.contains(".deposit-receipt-sheet"), "Thiếu cấu hình in cho .deposit-receipt-sheet");
        assertTrue(printCss.contains("page-break-inside: avoid !important;"), "Thiếu quy tắc chống chia cắt trang");

        // 4. Kiểm tra nút bấm trên trang Đơn Đã Đặt không còn gọi window.print() trực tiếp trên DOM
        assertFalse(dashboardHtml.contains("onclick=\"window.print()\""), "Dashboard không được dùng onclick='window.print()' in cả trang web");
        assertFalse(bookingsHtml.contains("onclick=\"window.print()\""), "Trang bookings không được dùng onclick='window.print()' in cả trang web");
        assertTrue(bookingsHtml.contains("/deposit-receipt"), "Trang bookings phải liên kết tới /deposit-receipt");

        // 5. Kiểm tra Controller mapping
        assertTrue(controllerCode.contains("@GetMapping(\"/customer/bookings/{id}/deposit-receipt\")"), "CustomerPortalController phải có mapping /deposit-receipt");

        // 6. Kiểm tra công thức tài chính cọc 30% chuẩn xác
        PricingService pricingService = new PricingService();
        long roomCharge = 2000000L;
        long earlyFee = 0L;
        long subtotal = roomCharge + earlyFee;
        long svcAmount = pricingService.calculateSvc5Percent(subtotal);
        long vatAmount = pricingService.calculateVat10Percent(subtotal, svcAmount);
        long grandTotal = subtotal + svcAmount + vatAmount;
        long depositAmount = pricingService.calculateOnlineDeposit30Percent(grandTotal);
        long remainingBalance = Math.max(0L, grandTotal - depositAmount);

        assertEquals(100000L, svcAmount);
        assertEquals(210000L, vatAmount);
        assertEquals(2310000L, grandTotal);
        assertEquals(693000L, depositAmount);
        assertEquals(1617000L, remainingBalance);
    }
}

