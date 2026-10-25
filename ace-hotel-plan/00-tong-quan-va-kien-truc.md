# 00. Tổng quan Hệ thống, Kiến trúc Kỹ thuật & Cấu trúc Dự án (`ace-hotel/`)

> **Mục tiêu:** Định nghĩa toàn bộ khung kiến trúc, cấu hình Maven (`pom.xml`), `application.properties`, cấu trúc package Java và bản đồ truy vết từ 7 Use Case sang các class trong `ace-hotel/src/` để Khoa dựng lại project sạch sẽ, chuẩn xác 100%[cite: 10, 24, 52].

---

## 1. Bảng Truy vết 7 Use Case $\leftrightarrow$ Phân hệ Giao diện $\leftrightarrow$ Tầng Code Java

| Mã UC | Tên Use Case | Actor | Màn hình (`WF`) | Controller chính | Service chính | Bảng CSDL | Ưu tiên |
|:---:|---|---|:---:|---|---|---|:---:|
| **UC01** | Đăng ký, Đăng nhập & Điều hướng 3 Role | Khách, Lễ tân, Admin | `WF-01`, `WF-02` | `AuthController` | `AuthService`, `CustomUserDetailsService` | `users`, `customers` | **P0** |
| **UC02** | Quản lý Danh mục Phòng, Giá & Dịch vụ | Admin *(Lễ tân xem)* | `WF-09` | `AdminCatalogController` | `CatalogService` | `room_types`, `rooms`, `services` | **P0** |
| **UC03** | Tìm kiếm & Đặt phòng trực tuyến (Quota, Cọc 30%, QR) | Khách hàng | `WF-01`, `WF-03`, `WF-04` | `PublicController`, `CustomerBookingController` | `BookingService`, `PricingService` | `room_types`, `rooms`, `bookings`, `customers` | **P0 (⭐ TT1)** |
| **UC04** | Quản lý Check-in, Gán phòng sạch & Walk-in 0đ cọc | Lễ tân, Admin | `WF-05`, `WF-06` | `StaffDashboardController`, `StaffCheckInController` | `CheckInService` | `bookings`, `rooms`, `customers` | **P0 (⭐ TT2)** |
| **UC05** | Check-out, Quyết toán `++` & Dọn phòng (`DIRTY`) | Lễ tân, Admin | `WF-05`, `WF-08` | `StaffCheckOutController` | `CheckOutService` | `bookings`, `service_orders`, `invoices`, `rooms` | **P0 (⭐ TT3)** |
| **UC06** | Ghi nhận Dịch vụ Lưu trú vào Room Folio (4 nhóm) | Lễ tân, Admin | `WF-07` | `StaffFolioController` | `FolioService` | `service_orders`, `services`, `bookings` | **P0** |
| **UC07** | Báo cáo Doanh thu đa chiều `++` & Công suất `OR%` | Admin | `WF-10` | `AdminReportController` | `ReportService` | `invoices`, `rooms`, `bookings`, `service_orders` | **P1 (⭐ TT4)** |

---

## 2. Bảy Quy tắc Nghiệp vụ Bất biến trong Tầng `Service` (`BR1` – `BR7`)

1. **`BR1` (Đặt phòng theo Tồn kho Hạng phòng – Quota Check):**
   - Đơn đặt Online (`RESERVED`) lưu `room_type_id` và để **`room_id = NULL`**[cite: 53].
   - Số suất còn trống của hạng phòng $X$ trong khoảng ngày `[checkIn, checkOut)`:
     $$N_{\text{available}} = \text{COUNT}(\text{rooms } r \text{ WHERE } r.\text{room\_type\_id} = X \text{ AND } r.\text{status} \neq \text{'MAINTENANCE'}) - \text{COUNT}(\text{bookings } b \text{ WHERE } b.\text{room\_type\_id} = X \text{ AND } b.\text{status} \in \{\text{'RESERVED'}, \text{'CHECKED\_IN'}\} \text{ AND } b.\text{check\_in\_date} < \text{checkOut} \text{ AND } b.\text{check\_out\_date} > \text{checkIn})$$
   - Chỉ cho phép đặt khi $N_{\text{available}} \ge 1$ và `roomType.capacity >= guestCount`.
2. **`BR2` (Khóa an toàn khi Gán phòng vật lý – Clean Room Guard):**
   - Khi Check-in (khách Online hoặc khách Walk-in), **chỉ những phòng vật lý có `status == 'AVAILABLE'` mới được phép gán**[cite: 53]. Các phòng `OCCUPIED`, `DIRTY`, `MAINTENANCE` bị khóa hoàn toàn[cite: 53].
3. **`BR3` (Khoảng ngày lưu trú & Biểu giá động Ngày thường / Cuối tuần):**
   - Bắt buộc `checkOutDate.isAfter(checkInDate)` (tối thiểu 1 đêm)[cite: 8, 53].
   - Duyệt từng đêm $d$ từ `checkInDate` đến `checkOutDate.minusDays(1)`: nếu `d.getDayOfWeek()` là `FRIDAY`, `SATURDAY`, `SUNDAY` thì cộng `weekendPricePerNight`, ngược lại cộng `pricePerNight`[cite: 53].
4. **`BR4` (Chính sách Đặt cọc & Quyết toán: Online 30/70 vs Walk-in 0/100):**
   - **Khách Online (`UC03`):** Đóng cọc trước **30%** tổng gói phòng ước tính (`++`) $\rightarrow$ lưu vào `bookings.deposit_amount` (`status = 'RESERVED'`)[cite: 53]. Khi Check-out chỉ thu **70% tiền phòng còn lại + 100% dịch vụ & phụ thu**[cite: 53].
   - **Khách Walk-in (`UC04`):** Nhận phòng trực tiếp tại quầy với **`deposit_amount = 0`** (`status = 'CHECKED_IN'`). Khi Check-out thu **100% toàn bộ hóa đơn**.
5. **`BR5` (Room Folio & Đóng băng đơn giá lịch sử – Price Snapshot):**
   - Chỉ thêm/xóa `service_orders` khi `booking.status == 'CHECKED_IN'`[cite: 8, 53].
   - Sao chép `services.unit_price` tại thời điểm gọi vào `service_orders.unit_price` và lưu `created_by` là nhân viên thực hiện[cite: 8, 53].
6. **`BR6` (Công thức Quyết toán Biểu giá `++` trên kiểu số nguyên `BIGINT`):**
   - Tính tuần tự:
     - `subtotal = roomCharge + serviceChargeTotal + earlyCheckinFee + lateCheckoutFee`[cite: 53]
     - `serviceChargeAmount = Math.round(subtotal * 0.05)` *(Phí phục vụ SVC 5%)*[cite: 53]
     - `vatAmount = Math.round((subtotal + serviceChargeAmount) * 0.10)` *(Thuế GTGT VAT 10%)*[cite: 53]
     - `grandTotal = subtotal + serviceChargeAmount + vatAmount`[cite: 53]
     - `depositDeducted = booking.getDepositAmount()`[cite: 53]
     - `balanceDue = grandTotal - depositDeducted`[cite: 53]
7. **`BR7` (Vòng đời Dọn phòng `DIRTY` & Bảo toàn Lịch sử):**
   - Ngay khi Check-out thành công, cập nhật `room.status = RoomStatus.DIRTY`[cite: 53]. Khi Lễ tân bấm `[✓ Xác nhận đã dọn sạch]`, cập nhật `room.status = RoomStatus.AVAILABLE`[cite: 53].
   - Chỉ hủy được đơn đang ở trạng thái `RESERVED`[cite: 53]. Chặn xóa `room_types`, `rooms`, `services` nếu đã có bản ghi con tham chiếu[cite: 8, 30].

---

## 3. Cấu trúc Cây Thư mục Dự án `ace-hotel/`

~~~text
ace-hotel/
├─ pom.xml
├─ README.md
├─ data/                                         (Tự sinh: chứa file H2 ace_hotel_db.mv.db)
└─ src/main/
   ├─ java/com/ace/hotel/
   │  ├─ AceHotelApplication.java
   │  ├─ config/
   │  │  ├─ SecurityConfig.java                  (Cấu hình phân quyền URL & BCryptPasswordEncoder)
   │  │  ├─ RoleBasedAuthSuccessHandler.java     (Tự động redirect ADMIN / RECEPTIONIST / CUSTOMER)
   │  │  ├─ WebMvcConfig.java                    (Locale vi-VN, định dạng ngày/tiền)
   │  │  └─ DemoDataSeeder.java                  (Chạy lại sample-data.sql khi có cờ --ace.seed=true)
   │  ├─ entity/
   │  │  ├─ enums/
   │  │  │  ├─ Role.java                         (ADMIN, RECEPTIONIST, CUSTOMER)
   │  │  │  ├─ RoomStatus.java                   (AVAILABLE, OCCUPIED, DIRTY, MAINTENANCE)
   │  │  │  ├─ BookingStatus.java                (RESERVED, CHECKED_IN, CHECKED_OUT, CANCELLED)
   │  │  │  ├─ ServiceCategory.java              (MINIBAR, LAUNDRY, FB, DAMAGES)
   │  │  │  └─ PaymentMethod.java                (CASH, CARD, TRANSFER)
   │  │  ├─ User.java
   │  │  ├─ RoomType.java
   │  │  ├─ Room.java
   │  │  ├─ Customer.java
   │  │  ├─ Booking.java
   │  │  ├─ ServiceItem.java
   │  │  ├─ ServiceOrder.java
   │  │  └─ Invoice.java
   │  ├─ repository/
   │  │  ├─ UserRepository.java
   │  │  ├─ RoomTypeRepository.java
   │  │  ├─ RoomRepository.java
   │  │  ├─ CustomerRepository.java
   │  │  ├─ BookingRepository.java
   │  │  ├─ ServiceItemRepository.java
   │  │  ├─ ServiceOrderRepository.java
   │  │  └─ InvoiceRepository.java
   │  ├─ dto/
   │  │  ├─ RegisterFormDto.java
   │  │  ├─ RoomTypeQuoteDto.java
   │  │  ├─ OnlineBookingRequestDto.java
   │  │  ├─ WalkInCheckInDto.java
   │  │  ├─ CheckOutSummaryDto.java
   │  │  └─ AdminReportDto.java
   │  ├─ service/
   │  │  ├─ CustomUserDetailsService.java
   │  │  ├─ AuthService.java
   │  │  ├─ PricingService.java                  (Tính giá T2–T5 / T6–CN & công thức làm tròn ++)
   │  │  ├─ CatalogService.java                  (UC02: CRUD hạng phòng, phòng, dịch vụ & bảo trì)
   │  │  ├─ BookingService.java                  (UC03: Kiểm tra Quota, đặt cọc 30%, sinh QR, hủy đơn)
   │  │  ├─ CheckInService.java                  (UC04: Quét QR gán phòng sạch & Walk-in 0đ cọc)
   │  │  ├─ FolioService.java                    (UC06: Ghi nợ 4 nhóm dịch vụ & đóng băng đơn giá)
   │  │  ├─ CheckOutService.java                 (UC05: Quyết toán ++, lập hóa đơn & chuyển DIRTY)
   │  │  └─ ReportService.java                   (UC07: Doanh thu đa chiều ++ & Occupancy Rate, ADR, RevPAR)
   │  ├─ controller/
   │  │  ├─ PublicController.java                (WF-01: Landing Page Storytelling)
   │  │  ├─ AuthController.java                  (WF-02: /login, /register)
   │  │  ├─ CustomerPortalController.java        (WF-03, WF-04: /customer/dashboard, /booking/**)
   │  │  ├─ StaffDashboardController.java        (WF-05: /staff/dashboard, /staff/rooms/{id}/clean)
   │  │  ├─ StaffCheckInController.java          (WF-06: /staff/check-in, /staff/walk-in)
   │  │  ├─ StaffFolioController.java            (WF-07: /staff/bookings/{id}/folio/**)
   │  │  ├─ StaffCheckOutController.java         (WF-08: /staff/bookings/{id}/check-out, /staff/invoices/{id})
   │  │  ├─ AdminCatalogController.java          (WF-09: /admin/catalog, /admin/room-types/**, /admin/rooms/**, /admin/services/**)
   │  │  └─ AdminReportController.java           (WF-10: /admin/dashboard)
   │  └─ exception/
   │     ├─ BusinessException.java
   │     ├─ MessageConstants.java                (Chứa 35 hằng số MSG-01 đến MSG-35)
   │     └─ GlobalExceptionHandler.java
   └─ resources/
      ├─ application.properties
      ├─ schema.sql
      ├─ sample-data.sql
      ├─ static/
      │  ├─ css/        (tokens.css, base.css, components.css, print.css)
      │  ├─ js/         (theme.js, qr-generator.js, app.js)
      │  ├─ fonts/      (Inter-*.woff2)
      │  └─ images/     (4 file logo ACE)
      └─ templates/
         ├─ layout/     (public-layout.html, pms-layout.html, fragments.html)
         ├─ public/     (landing.html, login-register.html)
         ├─ customer/   (dashboard.html, search-deposit.html)
         ├─ staff/      (dashboard.html, check-in.html, folio.html, check-out.html, invoice-print.html)
         ├─ admin/      (dashboard-report.html, catalog.html)
         └─ error/      (403.html, 404.html, 500.html)
~~~

---

## 4. Cấu hình Chuẩn `pom.xml` & `application.properties`

### 4.1. Các Dependencies bắt buộc trong `pom.xml` (Java 21 & Spring Boot 3.5)
~~~xml
<properties>
    <java.version>21</java.version>
</properties>

<dependencies>
    <!-- Web & Thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.thymeleaf.extras</groupId>
        <artifactId>thymeleaf-extras-springsecurity6</artifactId>
    </dependency>

    <!-- Security & Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- JPA & H2 Database -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
~~~

### 4.2. File `src/main/resources/application.properties`
~~~properties
spring.application.name=ace-hotel
server.port=8080

# H2 Database ở chế độ lưu file bền vững (NFR-08)
spring.datasource.url=jdbc:h2:file:./data/ace_hotel_db;AUTO_SERVER=TRUE;MODE=LEGACY
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Khởi tạo bảng từ schema.sql, quản lý dữ liệu mẫu qua DemoDataSeeder
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.jpa.open-in-view=false

# H2 Console (chỉ mở cho ADMIN khi debug)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Cấu hình Thymeleaf & Múi giờ Việt Nam
spring.thymeleaf.cache=false
spring.jackson.time-zone=Asia/Ho_Chi_Minh
ace.seed=false
~~~
*(Mẹo khi chạy lần đầu hoặc trước giờ Demo: truyền tham số `--ace.seed=true` để `DemoDataSeeder` tự động chạy lại `sample-data.sql` làm sạch toàn bộ dữ liệu về trạng thái chuẩn kịch bản Demo)[cite: 10, 13].*