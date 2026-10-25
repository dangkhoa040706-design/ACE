# ACE Hotel — PMS & Cổng Đặt Phòng Trực Tuyến

**ACE Hotel** (*Accommodation, Check-in & Extras*) là hệ thống phần mềm quản lý khách sạn (PMS) và Cổng đặt phòng trực tuyến toàn diện, được xây dựng theo chuẩn công nghệ **Spring Boot 3.5.0**, **Java 21 LTS**, **Thymeleaf**, **Spring Security**, **Spring Data JPA** và cơ sở dữ liệu **H2 Database**.

---

## 1. Tổng Quan Kiến Trúc & Công Nghệ

- **Ngôn ngữ & Nền tảng:** Java 21 LTS, Spring Boot 3.5.0.
- **View Template:** Thymeleaf kết hợp layout động (`public-layout.html`, `pms-layout.html`).
- **Design System:** Tông màu Đen - Vàng Gold (#FDBF2D) sang trọng, hỗ trợ chuyển đổi Dark / Light mode tức thời, Anti-FOUC, Pure SVG QR Code Generator offline.
- **Cơ sở dữ liệu:** H2 file-based `./data/ace_hotel_db` (bảo toàn dữ liệu qua các lần khởi động, tự động seed dữ liệu mẫu qua `DemoDataSeeder.java`).
- **Bảo mật:** Phân quyền 3 Role (`ADMIN`, `RECEPTIONIST`, `CUSTOMER`) băm BCrypt, chặn 403 Forbidden theo vai trò và trang lỗi tùy chỉnh.

---

## 2. Danh Sách 7 Use Case Nghiệp Vụ Hoàn Thành (100%)

| Mã UC | Tên Use Case | Phân Hệ | Wireframe | Trạng Thái |
|---|---|---|---|:---:|
| **UC01** | Đăng nhập, Đăng ký 5 trường & Phân quyền 3 Role | Xác thực & Bảo mật | `WF-02` | ✅ Hoàn thành |
| **UC02** | Quản trị Danh mục Hạng phòng (Giá T2–T5 / T6–CN), Phòng & Dịch vụ | Quản trị Backoffice | `WF-09` | ✅ Hoàn thành |
| **UC03** | ⭐ **Trọng tâm 1:** Landing Storytelling, Tìm Quota, Cọc 30% & Phiếu QR | Cổng Khách Hàng | `WF-01`, `WF-03`, `WF-04` | ✅ Hoàn thành |
| **UC04** | ⭐ **Trọng tâm 2:** Sơ đồ 12 Phòng, Quét QR Gán Phòng Sạch & Walk-in 0đ | Bàn Lễ Tân PMS | `WF-05`, `WF-06` | ✅ Hoàn thành |
| **UC05** | ⭐ **Trọng tâm 3:** Check-out Quyết toán `++`, Idempotency & Đổi sang `DIRTY` | Bàn Lễ Tân PMS | `WF-08` | ✅ Hoàn thành |
| **UC06** | Quản lý Sổ cái Phòng (Room Folio) 4 Nhóm Dịch Vụ & Price Snapshot | Bàn Lễ Tân PMS | `WF-07` | ✅ Hoàn thành |
| **UC07** | ⭐ **Trọng tâm 4:** Báo cáo Doanh thu `++`, Công suất $R_{\text{sellable}}=11$, OR%, ADR, RevPAR | Quản Trị Backoffice | `WF-10` | ✅ Hoàn thành |

---

## 3. Các Quy Tắc Nghiệp Vụ Chốt (Core Business Rules)

1. **BR1 — Quota Trừ Gián Tiếp:** Tính tồn kho khả dụng cho từng hạng phòng trong khoảng `[checkInDate, checkOutDate)` bằng công thức:
   $$\text{AvailableQuota} = \text{SellableRooms} - \text{ActiveOverlappingBookings}$$
   *(Đơn online mới chỉ chọn hạng phòng, chưa gán số phòng vật lý `room_id = NULL`)*.
2. **BR2 — Gán Phòng Sạch (Clean Physical Room Guard):** Khi lễ tân thực hiện Check-in, chỉ lọc các phòng vật lý thuộc đúng hạng phòng đang ở trạng thái `AVAILABLE` (Trống sạch). Khóa dòng phòng (`PESSIMISTIC_WRITE`) chống 2 lễ tân gán trùng.
3. **BR3 — Giá Động Ngày Thường vs Cuối Tuần:**
   - Thứ 2 đến Thứ 5: Áp dụng đơn giá ngày thường `pricePerNight`.
   - Thứ 6, Thứ 7, Chủ Nhật: Áp dụng đơn giá cuối tuần `weekendPricePerNight`.
4. **BR4 — Chính Sách Tiền Cọc:**
   - **Khách đặt trực tuyến (Online):** Bắt buộc đặt cọc **30%** tổng gói phòng dự toán `++`.
   - **Khách vãng lai (Walk-in):** Nhận phòng sạch ngay với **0đ tiền cọc** (`depositAmount = 0L`), thanh toán **100% toàn bộ hóa đơn** khi trả phòng.
5. **BR5 — Room Folio & Đóng Băng Đơn Giá (Price Snapshot):** Chỉ cho phép thêm/xóa dịch vụ khi phòng đang `CHECKED_IN` và `quantity >= 1`. Đơn giá dịch vụ được sao chép cứng vào `service_orders.unit_price` tại thời điểm gọi.
6. **BR6 — Quyết Toán Biểu Giá `++` Bằng Số Nguyên Tuần Tự (`BIGINT`):**
   ```java
   subtotal = roomCharge + serviceChargeTotal + earlyCheckinFee + lateCheckoutFee;
   serviceChargeAmount = Math.round(subtotal * 0.05); // SVC 5%
   vatAmount = Math.round((subtotal + serviceChargeAmount) * 0.10); // VAT 10%
   grandTotal = subtotal + serviceChargeAmount + vatAmount;
   depositDeducted = booking.getDepositAmount() != null ? booking.getDepositAmount() : 0L;
   balanceDue = Math.max(0L, grandTotal - depositDeducted);
   ```
7. **BR7 — Vòng Đời Trạng Thái Phòng & Bảo Toàn Lịch Sử:**
   $$\text{AVAILABLE} \xrightarrow{\text{Check-in}} \text{OCCUPIED} \xrightarrow{\text{Check-out}} \text{DIRTY} \xrightarrow{\text{1-Click Dọn sạch}} \text{AVAILABLE}$$
   - Chặn xóa hạng phòng đang có phòng hoặc đơn đặt tham chiếu (`MSG-14`).
   - Chặn xóa phòng đã có lịch sử đặt phòng (`MSG-15`).
   - Chặn bật bảo trì cho phòng đang `OCCUPIED` (`MSG-16`).
   - Chặn xóa dịch vụ đã có trong Room Folio (`MSG-17`).

---

## 4. Danh Sách 3 Tài Khoản Đăng Nhập Chuẩn

Hệ thống được cấu hình chuẩn với đúng **3 tài khoản duy nhất** tương ứng với 3 phân hệ/vai trò:

| Tên Đăng Nhập | Mật Khẩu | Vai Trò (Role) | Phân Hệ & Chức Năng Chính |
|---|---|---|---|
| `admin` | `admin` | `ROLE_ADMIN` | Quản trị danh mục phòng/dịch vụ, cài đặt biểu giá, xem báo cáo doanh thu `++` & công suất phòng |
| `nhanvien` | `nhanvien` | `ROLE_RECEPTIONIST` | Bàn làm việc lễ tân PMS, sơ đồ 12 phòng 4 trạng thái, quét QR Check-in, tiếp nhận Walk-in 0đ cọc, Room Folio, Check-out |
| `khach` | `khach` | `ROLE_CUSTOMER` | Cổng khách hàng thành viên, tìm kiếm phòng theo Quota, đặt phòng cọc 30%, nhận Phiếu xác nhận QR, xem lịch sử và hủy đơn |

---

## 5. Hướng Dẫn Chạy & Kiểm Thử Ứng Dụng

### Yêu cầu môi trường
- **Java:** JDK 21 LTS (Oracle OpenJDK 21 hoặc tương đương).
- **Maven:** Đã tích hợp sẵn Maven Wrapper (`mvnw.cmd`).

### Chạy kiểm thử tự động
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\openjdk\jdk-21.0.8"
.\mvnw.cmd test
```

### Chạy ứng dụng máy chủ
```powershell
$env:JAVA_HOME = "C:\Program Files\Android\openjdk\jdk-21.0.8"
.\mvnw.cmd spring-boot:run
```

Truy cập trình duyệt:
- Cổng khách hàng & Landing Page: `http://localhost:8080/`
- Trang Đăng nhập & Đăng ký: `http://localhost:8080/login`
- Bàn Lễ Tân PMS: `http://localhost:8080/staff/dashboard`
- Dashboard Báo Cáo Admin: `http://localhost:8080/admin/dashboard`
- Cơ sở dữ liệu H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/ace_hotel_db`, User: `sa`, Password: *(trống)*).
