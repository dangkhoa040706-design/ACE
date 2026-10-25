package com.ace.hotel;

import com.ace.hotel.dto.*;
import com.ace.hotel.entity.*;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.entity.enums.PaymentMethod;
import com.ace.hotel.entity.enums.Role;
import com.ace.hotel.entity.enums.RoomStatus;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.*;
import com.ace.hotel.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AceHotelApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private FolioService folioService;

    @Autowired
    private CheckOutService checkOutService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void resetStateForTestIsolation() {
        // Xóa các đơn phát sinh trong quá trình chạy test để bảo toàn Quota (booking_id >= 2500)
        List<Booking> testBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getBookingId() != null && b.getBookingId() >= 2500L)
                .toList();
        for (Booking tb : testBookings) {
            invoiceRepository.findByBooking_BookingId(tb.getBookingId()).ifPresent(invoiceRepository::delete);
            serviceOrderRepository.deleteByBooking_BookingId(tb.getBookingId());
            bookingRepository.delete(tb);
        }

        // Đảm bảo trạng thái phòng 101 là AVAILABLE để test Walk-in
        roomRepository.findByRoomNumberIgnoreCase("101").ifPresent(r -> {
            r.setStatus(RoomStatus.AVAILABLE);
            roomRepository.save(r);
        });
    }

    @Test
    @DisplayName("Cụm 1: Kiểm tra ứng dụng khởi động và nạp đủ 8 bảng CSDL chuẩn")
    void contextLoadsAndSeedDataPopulated() {
        // 1. Users (Ít nhất 3 tài khoản chuẩn: admin, nhanvien, khach)
        assertTrue(userRepository.count() >= 3, "Bảng users phải có ít nhất 3 tài khoản mẫu chuẩn: admin, nhanvien, khach");
        assertTrue(userRepository.findByUsername("admin").isPresent(), "Tài khoản admin phải tồn tại");
        assertTrue(userRepository.findByUsername("nhanvien").isPresent(), "Tài khoản nhanvien phải tồn tại");
        assertTrue(userRepository.findByUsername("khach").isPresent(), "Tài khoản khach phải tồn tại");

        // 2. Room Types (5 Hạng phòng)
        assertTrue(roomTypeRepository.count() >= 5, "Bảng room_types phải có đủ 5 hạng phòng");

        // 3. Rooms (12 phòng: 6 AVAILABLE, 3 OCCUPIED, 2 DIRTY, 1 MAINTENANCE)
        assertTrue(roomRepository.count() >= 12, "Bảng rooms phải có ít nhất 12 phòng vật lý");

        // 4. Customers
        assertTrue(customerRepository.count() >= 6, "Bảng customers phải có ít nhất 6 khách");

        // 5. Bookings
        assertTrue(bookingRepository.count() >= 6, "Bảng bookings phải có ít nhất 6 đơn");

        // 6. Services
        assertTrue(serviceItemRepository.count() >= 7, "Bảng services phải có ít nhất 7 dịch vụ");

        // 7. Service Orders
        assertTrue(serviceOrderRepository.count() >= 6, "Bảng service_orders phải có ít nhất 6 dòng");

        // 8. Invoices
        assertTrue(invoiceRepository.count() >= 1, "Bảng invoices phải có ít nhất 1 hóa đơn mẫu");
    }

    @Test
    @DisplayName("Cụm 1: Kiểm tra công thức tính giá PricingService (T2-T5 vs T6-CN và biểu giá ++)")
    void testPricingServiceFormulas() {
        RoomType standard = roomTypeRepository.findByNameIgnoreCase("Standard").orElseThrow();
        assertEquals(780000L, standard.getPricePerNight());
        assertEquals(950000L, standard.getWeekendPricePerNight());

        // Ngày thường: Thứ 2 (2026-10-19) đến Thứ 4 (2026-10-21) = 2 đêm ngày thường
        LocalDate mon = LocalDate.of(2026, 10, 19);
        LocalDate wed = LocalDate.of(2026, 10, 21);
        long weekdayCharge = pricingService.calculateRoomCharge(standard, mon, wed);
        assertEquals(780000L * 2, weekdayCharge, "2 đêm ngày thường phải là 1.560.000 VNĐ");

        // Cuối tuần: Thứ 6 (2026-10-23) đến Chủ Nhật (2026-10-25) = 2 đêm cuối tuần
        LocalDate fri = LocalDate.of(2026, 10, 23);
        LocalDate sun = LocalDate.of(2026, 10, 25);
        long weekendCharge = pricingService.calculateRoomCharge(standard, fri, sun);
        assertEquals(950000L * 2, weekendCharge, "2 đêm cuối tuần phải là 1.900.000 VNĐ");

        // Công thức Biểu giá ++:
        long subtotal = 2300000L;
        long svc = pricingService.calculateSvc5Percent(subtotal);
        assertEquals(115000L, svc, "SVC 5% của 2.300.000đ phải là 115.000đ");

        long vat = pricingService.calculateVat10Percent(subtotal, svc);
        assertEquals(241500L, vat, "VAT 10% của (2.300.000 + 115.000) phải là 241.500đ");

        long grandTotal = subtotal + svc + vat;
        assertEquals(2656500L, grandTotal, "Grand Total phải là 2.656.500đ");

        long deposit = pricingService.calculateOnlineDeposit30Percent(grandTotal);
        assertEquals(796950L, deposit, "Cọc 30% của 2.656.500đ phải là 796.950đ");
    }

    @Test
    @DisplayName("Cụm 2 — UC01: Kiểm tra đăng ký khách hàng 5 trường bắt buộc và mã hóa mật khẩu")
    void testUC01_CustomerRegistrationAndAuth() {
        long nowTs = System.currentTimeMillis();
        String testUsername = "khach_test_" + nowTs;
        RegisterFormDto form = new RegisterFormDto();
        form.setFullName("Lê Hoàng Nam");
        form.setDateOfBirth(LocalDate.of(1996, 8, 15));
        form.setPhone("09" + String.format("%08d", (nowTs % 100000000L)));
        form.setUsername(testUsername);
        form.setPassword("AceSecret@123");
        form.setConfirmPassword("AceSecret@123");
        form.setIdNumber(String.format("%012d", nowTs));
        form.setEmail("nam." + nowTs + "@example.com");

        // 1. Kiểm tra chặn mật khẩu không khớp
        form.setConfirmPassword("WrongConfirmPassword");
        BusinessException exMismatch = assertThrows(BusinessException.class, () -> authService.registerCustomer(form));
        assertEquals("Mật khẩu và xác nhận mật khẩu không trùng khớp.", exMismatch.getMessage());

        // 2. Kiểm tra chặn khách hàng chưa đủ 18 tuổi
        form.setConfirmPassword("AceSecret@123");
        form.setDateOfBirth(LocalDate.now().minusYears(17));
        BusinessException exUnderage = assertThrows(BusinessException.class, () -> authService.registerCustomer(form));
        assertEquals("Quý khách phải từ đủ 18 tuổi trở lên để đăng ký tài khoản thành viên.", exUnderage.getMessage());

        // Phục hồi ngày sinh hợp lệ (28 tuổi)
        form.setDateOfBirth(LocalDate.of(1996, 8, 15));

        Customer registered = authService.registerCustomer(form);
        assertNotNull(registered.getCustomerId());
        assertNotNull(registered.getUser());
        assertEquals(Role.CUSTOMER, registered.getUser().getRole());
        assertTrue(passwordEncoder.matches("AceSecret@123", registered.getUser().getPasswordHash()));

        // 3. Thử đăng ký lại với cùng username -> Phải báo MSG_04
        BusinessException ex = assertThrows(BusinessException.class, () -> authService.registerCustomer(form));
        assertEquals(MessageConstants.MSG_04, ex.getMessage());

        // 4. Thử đăng ký với username mới nhưng trùng số điện thoại -> Phải báo lỗi trùng SĐT
        RegisterFormDto formDuplicatePhone = new RegisterFormDto();
        formDuplicatePhone.setFullName("Nguyễn Văn Trùng SĐT");
        formDuplicatePhone.setDateOfBirth(LocalDate.of(1995, 1, 1));
        formDuplicatePhone.setPhone(form.getPhone());
        formDuplicatePhone.setUsername("username_unique_" + nowTs);
        formDuplicatePhone.setPassword("AceSecret@123");
        formDuplicatePhone.setConfirmPassword("AceSecret@123");
        BusinessException exPhone = assertThrows(BusinessException.class, () -> authService.registerCustomer(formDuplicatePhone));
        assertEquals("Số điện thoại này đã được đăng ký tài khoản thành viên.", exPhone.getMessage());

        // 5. Kiểm tra 4 tiêu chí bảo mật mật khẩu
        RegisterFormDto pwdTestForm = new RegisterFormDto();
        pwdTestForm.setFullName("Test Mật Khẩu");
        pwdTestForm.setDateOfBirth(LocalDate.of(1995, 1, 1));
        pwdTestForm.setPhone("0988776655");
        pwdTestForm.setUsername("user_pwd_test_" + nowTs);

        // a. Thiếu độ dài (< 8 ký tự)
        pwdTestForm.setPassword("Ab1@");
        pwdTestForm.setConfirmPassword("Ab1@");
        BusinessException exShort = assertThrows(BusinessException.class, () -> authService.registerCustomer(pwdTestForm));
        assertTrue(exShort.getMessage().contains("Mật khẩu phải có ít nhất 8 ký tự"));

        // b. Thiếu chữ in hoa
        pwdTestForm.setPassword("nouppercase@123");
        pwdTestForm.setConfirmPassword("nouppercase@123");
        BusinessException exNoUpper = assertThrows(BusinessException.class, () -> authService.registerCustomer(pwdTestForm));
        assertTrue(exNoUpper.getMessage().contains("1 chữ in hoa"));

        // c. Thiếu chữ số
        pwdTestForm.setPassword("NoDigitPassword@");
        pwdTestForm.setConfirmPassword("NoDigitPassword@");
        BusinessException exNoDigit = assertThrows(BusinessException.class, () -> authService.registerCustomer(pwdTestForm));
        assertTrue(exNoDigit.getMessage().contains("1 chữ số"));

        // d. Thiếu ký tự đặc biệt
        pwdTestForm.setPassword("NoSpecialChar123");
        pwdTestForm.setConfirmPassword("NoSpecialChar123");
        BusinessException exNoSpecial = assertThrows(BusinessException.class, () -> authService.registerCustomer(pwdTestForm));
        assertTrue(exNoSpecial.getMessage().contains("1 ký tự đặc biệt"));

        // e. Chứa dấu cách (dấu cách không được làm mật khẩu, không được tính là ký tự đặc biệt)
        pwdTestForm.setPassword("Khoa0407 ");
        pwdTestForm.setConfirmPassword("Khoa0407 ");
        BusinessException exSpace = assertThrows(BusinessException.class, () -> authService.registerCustomer(pwdTestForm));
        assertTrue(exSpace.getMessage().contains("dấu cách"));
    }

    @Test
    @DisplayName("Cụm 3 — UC02: Quản trị danh mục và ràng buộc lịch sử BR7")
    void testUC02_CatalogManagementAndBR7() {
        // 1. Chặn xóa hạng phòng đang có phòng/booking tham chiếu (Standard id=1) -> MSG_14
        BusinessException exRt = assertThrows(BusinessException.class, () -> catalogService.deleteRoomType(1L));
        assertEquals(MessageConstants.MSG_14, exRt.getMessage());

        // 2. Chặn xóa phòng đã có lịch sử đặt phòng (P.301 có BK-2401) -> MSG_15
        Room room301 = roomRepository.findByRoomNumberIgnoreCase("301").orElseThrow();
        BusinessException exRoom = assertThrows(BusinessException.class, () -> catalogService.deleteRoom(room301.getRoomId()));
        assertEquals(MessageConstants.MSG_15, exRoom.getMessage());

        // 3. Chặn bật bảo trì cho phòng đang OCCUPIED -> MSG_16
        BusinessException exMaint = assertThrows(BusinessException.class, () -> catalogService.toggleMaintenance(room301.getRoomId()));
        assertEquals(MessageConstants.MSG_16, exMaint.getMessage());

        // 4. Cho phép bật bảo trì cho phòng trống (P.205 đang AVAILABLE)
        Room room205 = roomRepository.findByRoomNumberIgnoreCase("205").orElseThrow();
        assertEquals(RoomStatus.AVAILABLE, room205.getStatus());
        catalogService.toggleMaintenance(room205.getRoomId());
        Room updated205 = roomRepository.findById(room205.getRoomId()).orElseThrow();
        assertEquals(RoomStatus.MAINTENANCE, updated205.getStatus());
        // Chuyển lại về AVAILABLE
        catalogService.toggleMaintenance(room205.getRoomId());
        assertEquals(RoomStatus.AVAILABLE, roomRepository.findById(room205.getRoomId()).orElseThrow().getStatus());

        // 5. Chặn xóa dịch vụ đã có trong Room Folio (id = 5 Bữa sáng Buffet) -> MSG_17
        BusinessException exSrv = assertThrows(BusinessException.class, () -> catalogService.deleteService(5L));
        assertEquals(MessageConstants.MSG_17, exSrv.getMessage());

        // 6. Xóa dịch vụ chưa từng được gọi (id = 8 Đền bù mất thẻ) -> Xóa thành công
        if (serviceItemRepository.existsById(8L)) {
            catalogService.deleteService(8L);
            assertFalse(serviceItemRepository.existsById(8L), "Dịch vụ số 8 chưa có đơn gọi phải xóa thành công");
        }
    }

    @Test
    @DisplayName("Cụm 4 — UC03: Tìm kiếm Quota, Đặt phòng cọc 30% và Hủy đơn RESERVED")
    void testUC03_QuotaSearchDepositAndCancel() {
        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = checkIn.plusDays(3); // 3 đêm

        // 1. Tìm phòng: kiểm tra kết quả tính Quota và chi phí ++
        List<RoomTypeQuoteDto> quotes = bookingService.searchAvailableRoomTypes(checkIn, checkOut, 2);
        assertFalse(quotes.isEmpty(), "Khoảng ngày tương lai phải còn phòng trống");
        RoomTypeQuoteDto deluxeQuote = quotes.stream()
                .filter(q -> "Deluxe".equalsIgnoreCase(q.name()))
                .findFirst().orElseThrow();
        assertTrue(deluxeQuote.availableQuota() >= 1);
        assertEquals(3, deluxeQuote.nights());
        assertTrue(deluxeQuote.estGrandTotal() > 0);
        assertEquals(Math.round(deluxeQuote.estGrandTotal() * 0.30), deluxeQuote.depositAmount());

        // 2. Kiểm tra chặn ngày sai checkOut <= checkIn -> MSG_20
        BusinessException exDate = assertThrows(BusinessException.class,
                () -> bookingService.searchAvailableRoomTypes(checkOut, checkIn, 2));
        assertEquals(MessageConstants.MSG_20, exDate.getMessage());

        // 3. Tạo đơn đặt trực tuyến có cọc 30% cho user khach
        User khach = userRepository.findByUsername("khach").orElseThrow();
        int initialQuota = bookingService.getAvailableQuota(deluxeQuote.roomTypeId(), checkIn, checkOut);

        OnlineBookingRequestDto req = new OnlineBookingRequestDto();
        req.setRoomTypeId(deluxeQuote.roomTypeId());
        req.setCheckInDate(checkIn);
        req.setCheckOutDate(checkOut);
        req.setGuestCount(2);

        Booking newBooking = bookingService.createOnlineBookingWithDeposit(req, khach);
        assertNotNull(newBooking.getBookingId());
        assertTrue(newBooking.getBookingCode().startsWith("BK-"));
        assertEquals(BookingStatus.RESERVED, newBooking.getStatus());
        assertNull(newBooking.getRoom(), "Đơn mới đặt trực tuyến chưa được gán phòng vật lý (room_id = NULL)");
        assertEquals(deluxeQuote.depositAmount(), newBooking.getDepositAmount());

        // Sau khi đặt, Quota phải giảm đi đúng 1 suất
        int quotaAfter = bookingService.getAvailableQuota(deluxeQuote.roomTypeId(), checkIn, checkOut);
        assertEquals(initialQuota - 1, quotaAfter, "Quota phải giảm 1 sau khi khách đặt giữ chỗ");

        // 4. Khách hủy đơn đặt phòng khi còn ở trạng thái RESERVED -> MSG_19
        Booking cancelled = bookingService.cancelBooking(newBooking.getBookingId(), khach);
        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());

        // Quota phải được hồi lại 1 suất
        int quotaAfterCancel = bookingService.getAvailableQuota(deluxeQuote.roomTypeId(), checkIn, checkOut);
        assertEquals(initialQuota, quotaAfterCancel, "Quota phải được hoàn trả sau khi đơn bị hủy");
    }

    @Test
    @DisplayName("Cụm 5 — UC04: Dashboard Lễ tân, Quét QR Gán phòng sạch & Tiếp nhận Walk-in 0đ cọc")
    void testUC04_StaffCheckInCleanRoomAndWalkIn() {
        User nhanvien = userRepository.findByUsername("nhanvien").orElseThrow();

        // 1. Kiểm tra 4 trạng thái phòng ban đầu
        assertTrue(roomRepository.countByStatus(RoomStatus.AVAILABLE) >= 4);
        assertTrue(roomRepository.countByStatus(RoomStatus.OCCUPIED) >= 2);
        assertEquals(1, roomRepository.countByStatus(RoomStatus.MAINTENANCE), "Chỉ có phòng 103 là MAINTENANCE");

        // 2. Kiểm tra cờ Quá hạn cho BK-2403 (P.418)
        Booking bk2403 = bookingRepository.findByBookingCodeIgnoreCase("BK-2403").orElseThrow();
        assertTrue(bk2403.isOverdue(), "BK-2403 phải được đánh dấu Quá hạn lưu trú");

        // 3. Thao tác 1-Click dọn sạch phòng DIRTY (P.102) -> chuyển về AVAILABLE (MSG-30)
        Room room102 = roomRepository.findByRoomNumberIgnoreCase("102").orElseThrow();
        room102.setStatus(RoomStatus.DIRTY);
        roomRepository.save(room102);

        checkInService.markRoomAsClean(room102.getRoomId());
        assertEquals(RoomStatus.AVAILABLE, roomRepository.findById(room102.getRoomId()).orElseThrow().getStatus(),
                "Phòng 102 sau khi dọn sạch phải chuyển về AVAILABLE");

        // 4. Chế độ 1: Check-in đơn đặt trước BK-2402 (Deluxe)
        Booking bk2402 = bookingRepository.findByBookingCodeIgnoreCase("BK-2402").orElseThrow();
        bk2402.setStatus(BookingStatus.RESERVED);
        bk2402.setRoom(null);
        bookingRepository.save(bk2402);

        // Thử gán phòng đang OCCUPIED (P.301) -> Phải ném ngoại lệ MSG_28
        Room room301 = roomRepository.findByRoomNumberIgnoreCase("301").orElseThrow();
        room301.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room301);

        BusinessException exBusy = assertThrows(BusinessException.class,
                () -> checkInService.assignAndCheckIn(bk2402.getBookingId(), room301.getRoomId(), 0L, "079198002233", nhanvien));
        assertTrue(exBusy.getMessage().contains("301"));

        // Gán đúng phòng Deluxe trống sạch (P.201)
        Room room201 = roomRepository.findByRoomNumberIgnoreCase("201").orElseThrow();
        room201.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room201);

        Booking checkedIn = checkInService.assignAndCheckIn(bk2402.getBookingId(), room201.getRoomId(), 0L, "079198002233", nhanvien);
        assertEquals(BookingStatus.CHECKED_IN, checkedIn.getStatus());
        assertEquals("201", checkedIn.getRoom().getRoomNumber());
        assertEquals(RoomStatus.OCCUPIED, roomRepository.findById(room201.getRoomId()).orElseThrow().getStatus());
        assertEquals(693000L, checkedIn.getDepositAmount(), "Tiền cọc Online 30% phải được giữ nguyên");

        // 5. Chế độ 2: Tiếp nhận khách Walk-in với 0đ tiền cọc (BR4)
        Room room101 = roomRepository.findByRoomNumberIgnoreCase("101").orElseThrow();
        room101.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room101);

        long nowTs = System.currentTimeMillis();
        WalkInCheckInDto walkInDto = new WalkInCheckInDto();
        walkInDto.setRoomId(room101.getRoomId());
        walkInDto.setCheckOutDate(LocalDate.now().plusDays(2));
        walkInDto.setGuestCount(1);
        walkInDto.setFullName("Phạm Quang Khải");
        walkInDto.setPhone("09" + String.format("%08d", (nowTs % 100000000L)));
        walkInDto.setIdNumber(String.format("%012d", nowTs));

        Booking walkInBooking = checkInService.processWalkInCheckIn(walkInDto, nhanvien);
        assertNotNull(walkInBooking.getBookingId());
        assertEquals(BookingStatus.CHECKED_IN, walkInBooking.getStatus());
        assertEquals(0L, walkInBooking.getDepositAmount(), "Khách Walk-in không thu cọc lúc vào (0đ cọc - BR4)");
        assertNull(walkInBooking.getCustomer().getUser(), "Khách Walk-in không có tài khoản user_id = null");
        assertEquals(RoomStatus.OCCUPIED, roomRepository.findById(room101.getRoomId()).orElseThrow().getStatus());
    }

    @Test
    @DisplayName("Cụm 6 — UC06: Quản lý Room Folio 4 nhóm dịch vụ & Đóng băng đơn giá BR5")
    void testUC06_RoomFolioServicesAndPriceSnapshot() {
        User nhanvien = userRepository.findByUsername("nhanvien").orElseThrow();
        Booking bk2405 = bookingRepository.findByBookingCodeIgnoreCase("BK-2405").orElseThrow();
        bk2405.setStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(bk2405);

        // 1. Chặn thêm dịch vụ với số lượng < 1 -> MSG_35
        BusinessException exQty = assertThrows(BusinessException.class,
                () -> folioService.addServiceOrder(bk2405.getBookingId(), 1L, 0, nhanvien));
        assertEquals(MessageConstants.MSG_35, exQty.getMessage());

        // 2. Thêm mặt hàng Dịch vụ ID = 1
        ServiceItem item1 = serviceItemRepository.findById(1L).orElseThrow();
        ServiceOrder order = folioService.addServiceOrder(bk2405.getBookingId(), item1.getServiceId(), 2, nhanvien);
        try {
            assertNotNull(order.getOrderId());
            assertEquals(item1.getUnitPrice(), order.getUnitPrice(), "Đơn giá phải được đóng băng tại thời điểm gọi dịch vụ (BR5)");
            assertEquals(item1.getUnitPrice() * 2, order.getLineTotal());
        } finally {
            // 3. Xóa dòng dịch vụ vừa thêm khỏi Room Folio
            folioService.deleteServiceOrder(bk2405.getBookingId(), order.getOrderId());
        }
        assertFalse(serviceOrderRepository.existsById(order.getOrderId()), "Dòng dịch vụ phải bị xóa khỏi CSDL");
    }

    @Test
    @DisplayName("Cụm 6 — UC05: Check-out Quyết toán biểu giá ++, Idempotency & Tự động chuyển phòng DIRTY (BR7)")
    void testUC05_CheckOutSettlementAndDIRTYTransition() {
        User nhanvien = userRepository.findByUsername("nhanvien").orElseThrow();
        Booking bk2401 = bookingRepository.findByBookingCodeIgnoreCase("BK-2401").orElseThrow();

        // Đảm bảo trạng thái CHECKED_IN
        bk2401.setStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(bk2401);

        // Dọn dẹp bất kỳ order phụ ngoài 3 order ban đầu (1, 2, 3) nếu có
        List<ServiceOrder> orders = serviceOrderRepository.findByBooking_BookingIdOrderByOrderedAtDesc(bk2401.getBookingId());
        for (ServiceOrder so : orders) {
            if (so.getOrderId() > 3) {
                serviceOrderRepository.delete(so);
            }
        }

        // Xóa invoice cũ nếu có để kiểm tra tạo mới
        invoiceRepository.findByBooking_BookingId(bk2401.getBookingId()).ifPresent(invoiceRepository::delete);

        // 1. Tính toán trước hóa đơn quyết toán (prepareCheckOutSummary)
        CheckOutSummaryDto summary = checkOutService.prepareCheckOutSummary(bk2401.getBookingId(), 0L);
        assertEquals(2000000L, summary.getRoomCharge());
        assertEquals(300000L, summary.getServiceChargeTotal());
        assertEquals(2300000L, summary.getSubtotal());
        assertEquals(115000L, summary.getServiceChargeAmount(), "SVC 5% phải là 115.000đ");
        assertEquals(241500L, summary.getVatAmount(), "VAT 10% phải là 241.500đ");
        assertEquals(2656500L, summary.getGrandTotal(), "Grand Total ++ phải là 2.656.500đ");
        assertEquals(693000L, summary.getDepositDeducted(), "Khấu trừ cọc 30% đã đóng");
        assertEquals(1963500L, summary.getBalanceDue(), "Thực thu tại quầy phải là 1.963.500đ");

        // 2. Xác nhận Thanh toán Check-out
        Invoice invoice = checkOutService.confirmCheckOutAndPay(bk2401.getBookingId(), 0L, PaymentMethod.CASH, nhanvien);
        assertNotNull(invoice.getInvoiceId());
        assertEquals(2656500L, invoice.getGrandTotal());
        assertEquals(1963500L, invoice.getBalanceDue());

        // Đơn đổi sang CHECKED_OUT
        Booking updatedBk = bookingRepository.findById(bk2401.getBookingId()).orElseThrow();
        assertEquals(BookingStatus.CHECKED_OUT, updatedBk.getStatus());

        // 3. BR7: Phòng vật lý (P.301) tự động chuyển sang DIRTY
        Room room301 = roomRepository.findByRoomNumberIgnoreCase("301").orElseThrow();
        assertEquals(RoomStatus.DIRTY, room301.getStatus(), "Phòng sau khi Check-out phải tự động chuyển sang DIRTY");

        // 4. Kiểm tra Idempotency: Gọi lại lần 2 không được chèn thêm hóa đơn
        Invoice secondCall = checkOutService.confirmCheckOutAndPay(bk2401.getBookingId(), 0L, PaymentMethod.CASH, nhanvien);
        assertEquals(invoice.getInvoiceId(), secondCall.getInvoiceId(), "Idempotency: Trả về cùng hóa đơn đã lập");
    }

    @Test
    @DisplayName("Cụm 7 — UC07: Báo cáo Đa chiều Doanh thu ++ & Công suất phòng quốc tế R_sellable = 11")
    void testUC07_AdminMultiDimensionalReportAndKPIs() {
        AdminReportDto report = reportService.generateReport(LocalDate.now().minusMonths(1), LocalDate.now().plusDays(1));

        // 1. Kiểm tra R_sellable = 11 (loại trừ phòng 103 MAINTENANCE)
        assertEquals(11L, report.getSellableRooms(), "R_sellable bắt buộc phải loại trừ phòng MAINTENANCE P.103");
        assertEquals(1L, report.getMaintenanceRooms());
        assertTrue(report.getOccupancyRate() >= 0.0);

        // 2. Kiểm tra số liệu doanh thu ++
        assertTrue(report.getTotalGrandRevenue() > 0L, "Tổng doanh thu grand total ++ phải lớn hơn 0");
        assertTrue(report.getTotalRoomRevenue() > 0L, "Tổng doanh thu tiền phòng phải lớn hơn 0");
        assertTrue(report.getTotalBalanceDue() > 0L, "Tổng thực thu tại quầy phải lớn hơn 0");
        assertTrue(report.getPaymentMethodTotals().containsKey("CASH"));
        assertFalse(report.getInvoices().isEmpty(), "Danh sách hóa đơn trong kỳ không được rỗng");

        // 3. Kiểm tra Trang Chủ Admin Dashboard Dto
        AdminHomeDashboardDto dashboard = reportService.getAdminHomeDashboardData();
        assertNotNull(dashboard.getGreetingTitle());
        assertNotNull(dashboard.getCurrentDateText());
        assertNotNull(dashboard.getGreetingQuote());
        assertEquals(11L, dashboard.getSellableRooms());
        assertNotNull(dashboard.getRecentBookings());
    }

    @Test
    @DisplayName("Kiểm thử Render Thymeleaf cho toàn bộ 10 màn hình (WF-01 đến WF-10)")
    void testViewRenderingAndCsrfFor10Screens() throws Exception {
        // 1. Công khai / Anonymous (WF-01 Landing, WF-02 Login/Register, WF-04 Search)
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/booking/search"))
                .andExpect(status().isOk());

        // 2. Role CUSTOMER: khach (WF-03 Customer Portal & Phiếu QR)
        mockMvc.perform(get("/customer/dashboard").with(user("khach").roles("CUSTOMER")))
                .andExpect(status().isOk());

        // 3. Role RECEPTIONIST: nhanvien (WF-05 Dashboard, WF-06 Check-in & Walk-in, WF-07 Folio, WF-08 Check-out, WF-09 Catalog Read-only)
        mockMvc.perform(get("/staff/dashboard").with(user("nhanvien").roles("RECEPTIONIST")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/staff/check-in").param("code", "BK-2402").with(user("nhanvien").roles("RECEPTIONIST")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/staff/walk-in").with(user("nhanvien").roles("RECEPTIONIST")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/staff/bookings/2401/folio").with(user("nhanvien").roles("RECEPTIONIST")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/staff/bookings/2401/check-out").with(user("nhanvien").roles("RECEPTIONIST")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/staff/invoices/1").with(user("nhanvien").roles("RECEPTIONIST")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/catalog").with(user("nhanvien").roles("RECEPTIONIST")))
                .andExpect(status().isOk());

        // 4. Role ADMIN: admin (WF-09 Catalog Full CRUD, WF-10 Executive Report)
        mockMvc.perform(get("/admin/catalog").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/dashboard").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/reports").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Kiểm thử hồi quy 5 lỗi đã khắc phục (Bugs 1-5)")
    void testBugRemediationsAndDataIntegrity() {
        // 1. Bug 3: Chặn đặt phòng ngày quá khứ
        LocalDate pastDate = LocalDate.now().minusDays(5);
        BusinessException exPastSearch = assertThrows(BusinessException.class,
                () -> bookingService.searchAvailableRoomTypes(pastDate, pastDate.plusDays(2), 2));
        assertTrue(exPastSearch.getMessage().contains("quá khứ"), "Phải chặn tìm phòng ngày quá khứ");

        OnlineBookingRequestDto pastReq = new OnlineBookingRequestDto();
        pastReq.setRoomTypeId(1L);
        pastReq.setCheckInDate(pastDate);
        pastReq.setCheckOutDate(pastDate.plusDays(1));
        pastReq.setGuestCount(2);
        User khach = userRepository.findByUsername("khach").orElseThrow();
        BusinessException exPastBook = assertThrows(BusinessException.class,
                () -> bookingService.createOnlineBookingWithDeposit(pastReq, khach));
        assertTrue(exPastBook.getMessage().contains("quá khứ"), "Phải chặn tạo đơn cọc ngày quá khứ");

        // 2. Bug 4: Mã đặt phòng Walk-in sinh ra phải duy nhất và bắt đầu bằng BK-
        User nhanvien = userRepository.findByUsername("nhanvien").orElseThrow();
        Room room101 = roomRepository.findByRoomNumberIgnoreCase("101").orElseThrow();
        room101.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room101);

        WalkInCheckInDto walkInDto = new WalkInCheckInDto();
        walkInDto.setRoomId(room101.getRoomId());
        walkInDto.setCheckOutDate(LocalDate.now().plusDays(1));
        walkInDto.setGuestCount(1);
        walkInDto.setFullName("Khách Walk-in Test Unique");
        walkInDto.setPhone("0998877665");

        Booking walkInBooking = checkInService.processWalkInCheckIn(walkInDto, nhanvien);
        assertNotNull(walkInBooking.getBookingCode());
        assertTrue(walkInBooking.getBookingCode().startsWith("BK-"));
        assertEquals(nhanvien.getUserId(), walkInBooking.getCreatedBy().getUserId());

        // 3. Bug 5: Giữ nguyên createdBy gốc khi lễ tân check-in cho khách đặt online
        // Tạo đơn online do khach tạo
        OnlineBookingRequestDto onlineReq = new OnlineBookingRequestDto();
        onlineReq.setRoomTypeId(1L); // Standard
        onlineReq.setCheckInDate(LocalDate.now().plusDays(20));
        onlineReq.setCheckOutDate(LocalDate.now().plusDays(22));
        onlineReq.setGuestCount(1);
        Booking onlineBooking = bookingService.createOnlineBookingWithDeposit(onlineReq, khach);
        assertEquals(khach.getUserId(), onlineBooking.getCreatedBy().getUserId(), "Đơn ban đầu do khach tạo");

        // Dọn phòng 101 về AVAILABLE để gán check-in
        room101.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room101);

        Booking checkedInBooking = checkInService.assignAndCheckIn(onlineBooking.getBookingId(), room101.getRoomId(), 0L, "123456789012", nhanvien);
        assertEquals(khach.getUserId(), checkedInBooking.getCreatedBy().getUserId(), "createdBy không bị ghi đè thành lễ tân");
    }

    @Test
    @DisplayName("Kiểm tra cấu hình loại giường (bedType) và sức chứa (capacity) của 5 hạng phòng")
    void testRoomTypeBedTypeAndCapacity() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        assertTrue(roomTypes.size() >= 5);

        for (RoomType rt : roomTypes) {
            assertNotNull(rt.getBedType(), "bedType không được null cho hạng " + rt.getName());
            assertFalse(rt.getBedType().isBlank(), "bedType không được để trống cho hạng " + rt.getName());
            assertTrue(rt.getCapacity() > 0, "capacity phải lớn hơn 0");
        }

        RoomType standard = roomTypeRepository.findByNameIgnoreCase("Standard").orElseThrow();
        assertEquals(2, standard.getCapacity());
        assertTrue(standard.getBedType().contains("Giường"));

        RoomType family = roomTypeRepository.findByNameIgnoreCase("Family").orElseThrow();
        assertEquals(4, family.getCapacity());
        assertTrue(family.getBedType().contains("Giường"));

        RoomType suite = roomTypeRepository.findByNameIgnoreCase("Suite").orElseThrow();
        assertEquals(4, suite.getCapacity());
        assertTrue(suite.getBedType().contains("Giường") || suite.getBedType().contains("Sofa"));
    }

    @Test
    @DisplayName("Kiểm tra chặn logic số lượng khách vượt quá sức chứa khi Walk-in Check-in")
    void testWalkInCapacityValidation() {
        User nhanvien = userRepository.findByUsername("nhanvien").orElseThrow();
        Room room101 = roomRepository.findByRoomNumberIgnoreCase("101").orElseThrow(); // Standard, capacity = 2
        room101.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room101);

        // 1. Trường hợp nhập 0 khách
        WalkInCheckInDto zeroGuestsDto = new WalkInCheckInDto();
        zeroGuestsDto.setRoomId(room101.getRoomId());
        zeroGuestsDto.setCheckOutDate(LocalDate.now().plusDays(1));
        zeroGuestsDto.setGuestCount(0);
        zeroGuestsDto.setFullName("Khách Test Zero");
        zeroGuestsDto.setPhone("0911223344");
        BusinessException exZero = assertThrows(BusinessException.class,
                () -> checkInService.processWalkInCheckIn(zeroGuestsDto, nhanvien));
        assertTrue(exZero.getMessage().contains("lớn hơn 0") || exZero.getMessage().contains("1 người trở lên"));

        // 2. Trường hợp nhập quá sức chứa phòng (6 khách vào phòng Standard 2 người)
        WalkInCheckInDto overloadDto = new WalkInCheckInDto();
        overloadDto.setRoomId(room101.getRoomId());
        overloadDto.setCheckOutDate(LocalDate.now().plusDays(1));
        overloadDto.setGuestCount(6);
        overloadDto.setFullName("Khách Test Quá Sức Chứa");
        overloadDto.setPhone("0911223344");

        BusinessException exOverload = assertThrows(BusinessException.class,
                () -> checkInService.processWalkInCheckIn(overloadDto, nhanvien));
        assertTrue(exOverload.getMessage().contains("sức chứa") || exOverload.getMessage().contains("tối đa"));

        // 3. Trường hợp hợp lệ (2 khách vào phòng Standard 2 người)
        WalkInCheckInDto validDto = new WalkInCheckInDto();
        validDto.setRoomId(room101.getRoomId());
        validDto.setCheckOutDate(LocalDate.now().plusDays(1));
        validDto.setGuestCount(2);
        validDto.setFullName("Khách Test Hợp Lệ");
        validDto.setPhone("0911223344");

        Booking okBooking = checkInService.processWalkInCheckIn(validDto, nhanvien);
        assertNotNull(okBooking);
        assertEquals(2, okBooking.getGuestCount());
    }

    @Test
    @DisplayName("Kiểm tra chặn logic số lượng khách vượt quá sức chứa khi đặt Online cọc 30%")
    void testOnlineBookingCapacityValidation() {
        User khach = userRepository.findByUsername("khach").orElseThrow();
        RoomType standard = roomTypeRepository.findByNameIgnoreCase("Standard").orElseThrow(); // capacity = 2

        OnlineBookingRequestDto overloadReq = new OnlineBookingRequestDto();
        overloadReq.setRoomTypeId(standard.getRoomTypeId());
        overloadReq.setCheckInDate(LocalDate.now().plusDays(10));
        overloadReq.setCheckOutDate(LocalDate.now().plusDays(12));
        overloadReq.setGuestCount(5); // 5 khách > 2

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bookingService.createOnlineBookingWithDeposit(overloadReq, khach));
        assertTrue(ex.getMessage().contains("sức chứa") || ex.getMessage().contains("tối đa"));
    }
}


