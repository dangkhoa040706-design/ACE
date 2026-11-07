<div align="center">

  <img src="../ace-hotel-plan/assets/1_logo-day-du_khong-nen.png" alt="ACE Hotel Flagship Logo" width="300" style="filter: drop-shadow(0 6px 20px rgba(253, 191, 45, 0.45));" />

  <br />

  # ✦ ACE HOTEL PMS & ONLINE BOOKING ENGINE ✦
  ### **Source Code & Developer Guide • Spring Boot 3.5.0 + Java 21 LTS**

  <p align="center">
    <img src="https://img.shields.io/badge/Java-21_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21 LTS" />
    <img src="https://img.shields.io/badge/Spring_Boot-3.5.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.5.0" />
    <img src="https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
    <img src="https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf 3" />
    <img src="https://img.shields.io/badge/Database-H2_File--based-003545?style=for-the-badge&logo=h2&logoColor=white" alt="H2 Database" />
  </p>

  <hr style="border: 0; height: 1px; background: linear-gradient(90deg, transparent, #FDBF2D, transparent); margin: 1.5rem 0;" />
</div>

<br />

## 1. Tổng Quan Kiến Trúc Kỹ Thuật

- **Ngôn ngữ & Nền tảng:** Java 21 LTS, Spring Boot 3.5.0.
- **Tầng Hiển Thị (View Layer):** Thymeleaf kết hợp layout động (`public-layout.html`, `customer-layout.html`, `pms-layout.html`).
- **Design System:** Tông màu Đen Obsidian & Vàng Champagne (#FDBF2D), hỗ trợ chuyển đổi Dark / Light mode tức thời, Anti-FOUC, Pure SVG QR Code Generator offline, Custom Scrollbars 8px/5px.
- **Cơ Sở Dữ Liệu:** H2 file-based `./data/ace_hotel_db` (bảo toàn dữ liệu qua các lần khởi động, tự động seed dữ liệu mẫu qua `DemoDataSeeder.java`).
- **Bảo Mật:** Phân quyền 3 Role (`ADMIN`, `RECEPTIONIST`, `CUSTOMER`) băm BCrypt, chặn 403 Forbidden theo vai trò và trang lỗi tùy chỉnh.

---

## 2. Danh Sách 7 Use Case Nghiệp Vụ Hoàn Thành (100%)

| Mã UC | Tên Use Case | Phân Hệ | Wireframe | Trạng Thái |
|:---:|---|---|:---:|:---:|
| **UC01** | Đăng nhập, Đăng ký 5 trường & Phân quyền 3 Role | Xác thực & Bảo mật | `WF-02` | ✅ Hoàn thành |
| **UC02** | Quản trị Danh mục Hạng phòng (Giá T2–T5 / T6–CN), Phòng & Dịch vụ | Quản trị Backoffice | `WF-09` | ✅ Hoàn thành |
| **UC03** | ⭐ **Trọng tâm 1:** Landing Storytelling, Tìm Quota, Cọc 30% & Phiếu QR | Cổng Khách Hàng | `WF-01`, `WF-03`, `WF-04` | ✅ Hoàn thành |
| **UC04** | ⭐ **Trọng tâm 2:** Sơ đồ 12 Phòng, Quét QR Gán Phòng Sạch & Walk-in 0đ | Bàn Lễ Tân PMS | `WF-05`, `WF-06` | ✅ Hoàn thành |
| **UC05** | ⭐ **Trọng tâm 3:** Check-out Quyết toán `++`, Idempotency & Đổi sang `DIRTY` | Bàn Lễ Tân PMS | `WF-08` | ✅ Hoàn thành |
| **UC06** | Quản lý Sổ cái Phòng (Room Folio) 4 Nhóm Dịch Vụ & Price Snapshot | Bàn Lễ Tân PMS | `WF-07` | ✅ Hoàn thành |
| **UC07** | ⭐ **Trọng tâm 4:** Báo cáo Doanh thu `++`, Công suất $R_{\text{sellable}}=11$, OR%, ADR, RevPAR | Quản Trị Backoffice | `WF-10` | ✅ Hoàn thành |

---

## 3. Các Quy Tắc Nghiệp Vụ Chốt (Core Business Rules)

1. **BR1 — Quota Trừ Gián Tiếp (0% Overbooking):**
   $$\text{AvailableQuota} = \text{SellableRooms} - \text{ActiveOverlappingBookings}$$
2. **BR2 — Gán Phòng Sạch (Clean Physical Room Guard):** Khóa dòng phòng (`PESSIMISTIC_WRITE`) chống 2 lễ tân gán trùng phòng.
3. **BR3 — Giá Động Ngày Thường vs Cuối Tuần:**
   - Thứ 2 đến Thứ 5: Áp dụng đơn giá ngày thường `pricePerNight`.
   - Thứ 6, Thứ 7, Chủ Nhật: Áp dụng đơn giá cuối tuần `weekendPricePerNight`.
4. **BR4 — Chính Sách Tiền Cọc:**
   - **Khách đặt trực tuyến (Online):** Bắt buộc đặt cọc **30%** tổng tiền phòng dự toán.
   - **Khách vãng lai (Walk-in):** Nhận phòng sạch ngay với **0đ tiền cọc**, thanh toán 100% khi trả phòng.
5. **BR5 — Room Folio & Đóng Băng Đơn Giá (Price Snapshot):** Đơn giá dịch vụ được sao chép cứng vào `service_orders.unit_price` tại thời điểm gọi.
6. **BR6 — Quyết Toán Biểu Giá `++` Bằng Số Nguyên Tuần Tự (`BIGINT`):**
   ```java
   subtotal = roomCharge + serviceChargeTotal + earlyCheckinFee + lateCheckoutFee;
   serviceChargeAmount = Math.round(subtotal * 0.05);               // SVC 5%
   vatAmount = Math.round((subtotal + serviceChargeAmount) * 0.10);  // VAT 10%
   grandTotal = subtotal + serviceChargeAmount + vatAmount;
   depositDeducted = booking.getDepositAmount() != null ? booking.getDepositAmount() : 0L;
   balanceDue = Math.max(0L, grandTotal - depositDeducted);
   ```
7. **BR7 — Vòng Đời Trạng Thái Phòng & Bảo Toàn Lịch Sử:**
   $$\text{AVAILABLE} \xrightarrow{\text{Check-in}} \text{OCCUPIED} \xrightarrow{\text{Check-out}} \text{DIRTY} \xrightarrow{\text{1-Click Dọn sạch}} \text{AVAILABLE}$$

---

## 4. Danh Sách 3 Tài Khoản Đăng Nhập Chuẩn

| Tên Đăng Nhập | Mật Khẩu | Vai Trò (Role) | Phân Hệ & Chức Năng Chính |
|---|:---:|---|---|
| `admin` | `Admin@2026` | `ROLE_ADMIN` | Quản trị danh mục phòng/dịch vụ, cài đặt biểu giá, xem báo cáo doanh thu `++` & công suất phòng |
| `letan` | `Letan@2026` | `ROLE_RECEPTIONIST` | Bàn làm việc lễ tân PMS, sơ đồ 12 phòng 4 trạng thái, quét QR Check-in, tiếp nhận Walk-in 0đ cọc, Room Folio, Check-out |
| `khach` | `Khach@2026` | `ROLE_CUSTOMER` | Cổng khách hàng thành viên, tìm kiếm phòng theo Quota, đặt phòng cọc 30%, nhận Phiếu xác nhận QR, xem lịch sử và hủy đơn |

---

## 5. Hướng Dẫn Chạy & Kiểm Thử Ứng Dụng

### Khởi chạy server
```bash
mvnw.cmd spring-boot:run
```

### Chạy kiểm thử tích hợp (Smoke Tests)
```bash
mvnw.cmd test -Dtest=BugFixSmokeTest
```
