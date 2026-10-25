# VĂN BẢN CHỐT CHÍNH THỨC: PHẠM VI, 3 ACTOR, 7 USE CASE, 24 SƠ ĐỒ UML & 8 BẢNG CSDL

> **Đề tài 1 (Môn 502050):** Hệ thống Quản lý Khách sạn & Đặt phòng Trực tuyến **ACE Hotel** (*Accommodation, Check-in & Extras*).  
> **Trạng thái:** Đã chốt chính thức toàn nhóm ngày 14/10/2026 (Đồng bộ 100% với `quy-trinh-nghiep-vu-moi.md`). Tất cả thành viên dùng đúng tên Actor, mã Use Case, tên Bảng/Cột và cấu trúc Chương 1 – Chương 8 trong văn bản này.

---

## 1. Phạm vi Hệ thống (2 Phân hệ & 4 Không gian Giao diện)

Hệ thống phục vụ một khách sạn vừa và nhỏ, kết nối khép kín giữa **Phân hệ Khách hàng trực tuyến (Public Booking Portal)** và **Phân hệ Nội bộ tại quầy & Quản trị (Backoffice PMS)** qua **4 không gian giao diện riêng biệt**:
1. **Landing Page Storytelling (`GET /` – `WF-01`):** Giới thiệu thương hiệu, hạng phòng, bảng giá ngày thường/cuối tuần, 4 nhóm dịch vụ và thanh tìm kiếm đặt phòng.
2. **Dashboard Khách hàng (`/customer/dashboard` – `WF-03`, `WF-04`):** Đặt phòng theo Hạng phòng (*Quota*), thanh toán cọc 30%, nhận **Phiếu xác nhận kèm mã QR**, xem lịch sử và hủy đơn giữ chỗ (`RESERVED`).
3. **Dashboard Nhân viên Lễ tân (`/staff/dashboard` – `WF-05`..`WF-08`):** Sơ đồ phòng 4 trạng thái (`AVAILABLE`, `OCCUPIED`, `DIRTY`, `MAINTENANCE`), quét/nhập mã QR Check-in gán phòng sạch, tiếp nhận khách Walk-in (`0đ` cọc lúc vào), ghi nợ dịch vụ vào **Room Folio**, Check-out quyết toán biểu giá `++` và bấm **[✓ Xác nhận đã dọn sạch]**.
4. **Dashboard Quản trị viên (`/admin/dashboard` – `WF-09`, `WF-10`):** Cấu hình hạng phòng, giá ngày thường (T2–T5) / cuối tuần (T6–CN), quản lý phòng vật lý, danh mục 4 nhóm dịch vụ và xem báo cáo doanh thu đa chiều + chỉ số công suất `Occupancy Rate`, `ADR`, `RevPAR`.

---

## 2. Ba Tác nhân Trực tiếp (3 Actors)

| Actor | Mã Role CSDL | Phân hệ sử dụng | Quyền hạn chính |
|---|:---:|---|---|
| **Khách hàng** | `CUSTOMER` | Public & Customer Portal (`/`, `/customer/**`) | Đăng ký tài khoản (5 trường), đăng nhập, tìm hạng phòng trống theo Quota, đặt phòng & cọc 30%, nhận Phiếu xác nhận QR, hủy đơn `RESERVED`. |
| **Nhân viên Lễ tân** | `RECEPTIONIST` | Front-Desk PMS (`/staff/**`) | Xem sơ đồ phòng 4 trạng thái, quét QR Check-in & gán phòng sạch (`AVAILABLE`), tiếp nhận khách Walk-in (`0đ` cọc), ghi nợ/xóa dịch vụ Room Folio, Check-out quyết toán `++` & in hóa đơn VAT, xác nhận phòng đã dọn sạch (`DIRTY` $\rightarrow$ `AVAILABLE`). |
| **Quản trị viên (Admin)** | `ADMIN` | Admin Backoffice (`/admin/**`) | Kế thừa quyền vận hành + Quản lý hạng phòng (giá ngày thường/cuối tuần), quản lý phòng vật lý (Thêm/Sửa/Bảo trì/Xóa), quản lý 4 nhóm dịch vụ, xem báo cáo doanh thu `++` và `Occupancy Rate`, `ADR`, `RevPAR`. |

*(Bộ phận Buồng phòng và Kế toán được mô tả ở Chương 2 như đối tượng liên quan ngoài hệ thống).*

---

## 3. Bảy Use Case Chính & Phân công 24 Sơ đồ UML (Chương 4)

Mỗi Use Case từ `UC01` đến `UC07` đều có đầy đủ **4 thành phần** trong Chương 4: **(1) Sơ đồ UC chi tiết (`uc-0x.png`) + (2) Bảng đặc tả 9 dòng + (3) Activity Diagram (`act-uc0x.png`) + (4) Sequence Diagram (`seq-uc0x.png`)**:

| Mã UC | Tên Use Case | Actor chính | Phụ trách Đặc tả & 3 Sơ đồ (`UC`, `Act`, `Seq`) | Trọng tâm phản biện | Cài đặt Java |
|:---:|---|---|:---:|:---:|:---:|
| **UC01** | Đăng ký, Đăng nhập và Điều hướng vai trò | Khách hàng, Lễ tân, Admin | **Kiệt** (`uc-01`, `act-uc01`, `seq-uc01`) | Nền tảng | Có |
| **UC02** | Quản lý Danh mục Phòng, Biểu giá & Dịch vụ | Admin *(Lễ tân chỉ xem)* | **Hoàng** (`uc-02`, `act-uc02`, `seq-uc02`) | Nền tảng | Có |
| **UC03** | Tìm kiếm & Đặt phòng trực tuyến (Theo Hạng phòng, Cọc 30%, Mã QR) | Khách hàng | **Hoàng** (`uc-03`, `act-uc03`, `seq-uc03`) | ⭐ **Trọng tâm 1** | Có |
| **UC04** | Quản lý Check-in, Gán phòng sạch & Tiếp nhận Walk-in (0đ cọc) | Lễ tân *(Khách hàng)* | **Phong** (`uc-04`, `act-uc04`, `seq-uc04`) | ⭐ **Trọng tâm 2** | Có |
| **UC05** | Check-out, Quyết toán biểu giá `++` & Chuyển trạng thái dọn phòng | Lễ tân *(Khách hàng)* | **Phong** (`uc-05`, `act-uc05`, `seq-uc05`) | ⭐ **Trọng tâm 3** | Có |
| **UC06** | Ghi nhận sử dụng dịch vụ lưu trú vào Room Folio (4 nhóm) | Lễ tân *(Khách hàng)* | **Kiệt** (`uc-06`, `act-uc06`, `seq-uc06`) | Bổ trợ | Có |
| **UC07** | Báo cáo thống kê doanh thu đa chiều & Công suất phòng | Admin | **Khoa** (`uc-07`, `act-uc07`, `seq-uc07`) | ⭐ **Trọng tâm 4** | Có |

* **3 Sơ đồ cấp toàn hệ thống trong Chương 4:**
  1. **Mục 4.1:** Biểu đồ Use Case tổng quát (`usecase-tong-quat.png`) — **Hoàng** vẽ.
  2. **Mục 4.3:** Biểu đồ Lớp phân tích & thiết kế (`class-diagram.png` – 8 lớp) — **Khoa** vẽ.
  3. **Mục 4.4:** Mô hình Thực thể – Kết hợp (`erd.png` – 8 bảng) — **Khoa** vẽ.
  $\Rightarrow$ **Tổng cộng toàn Chương 4 = 24 sơ đồ UML.**

---

## 4. Tám Bảng Cơ sở Dữ liệu Chuẩn (Chương 5)

| STT | Tên bảng | Khóa chính (PK) | Các cột chính | Khóa ngoại (FK) & Ghi chú quan trọng |
|:---:|---|---|---|---|
| **1** | `users` | `user_id` | `username` (UK), `password_hash`, `full_name`, `role`, `created_at` | `role IN ('ADMIN', 'RECEPTIONIST', 'CUSTOMER')` |
| **2** | `room_types` | `room_type_id` | `name` (UK), `price_per_night`, `weekend_price_per_night`, `capacity`, `description`, `image_url` | Hỗ trợ giá ngày thường (T2–T5) & cuối tuần (T6–CN) |
| **3** | `rooms` | `room_id` | `room_number` (UK), `floor`, `status`, `room_type_id` | `FK -> room_types`; `status IN ('AVAILABLE', 'OCCUPIED', 'DIRTY', 'MAINTENANCE')` |
| **4** | `customers` | `customer_id` | `user_id` (UK, nullable), `full_name`, `date_of_birth` (nullable), `phone`, `id_number` (UK), `email` | `FK -> users` (`NULL` đối với khách Walk-in tại quầy) |
| **5** | `bookings` | `booking_id` | `booking_code` (UK), `customer_id`, `room_type_id`, `room_id` (nullable), `guest_count`, `check_in_date`, `check_out_date`, `actual_check_in`, `actual_check_out`, `room_charge`, `deposit_amount`, `early_checkin_fee`, `late_checkout_fee`, `status`, `created_at`, `created_by` | `FK -> customers, room_types, rooms, users`; `status IN ('RESERVED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED')` |
| **6** | `services` | `service_id` | `name` (UK), `category`, `unit_price`, `unit` | `category IN ('MINIBAR', 'LAUNDRY', 'FB', 'DAMAGES')` |
| **7** | `service_orders` | `order_id` | `booking_id`, `service_id`, `quantity`, `unit_price`, `ordered_at`, `created_by` | `FK -> bookings, services, users` (Đóng băng `unit_price`) |
| **8** | `invoices` | `invoice_id` | `booking_id` (UK), `room_charge`, `service_charge_total`, `early_checkin_fee`, `late_checkout_fee`, `subtotal`, `service_charge_amount`, `vat_amount`, `grand_total`, `deposit_deducted`, `balance_due`, `payment_method`, `paid_at`, `created_by` | Quan hệ 1–1 với `bookings`; chuẩn biểu giá `++` (`SVC 5%`, `VAT 10%`) và khấu trừ cọc |

---

## 5. Bảy Quy tắc Nghiệp vụ Cốt lõi (`BR1` – `BR7`)

1. **`BR1` (Đặt theo Tồn kho Hạng phòng – Quota Check):** Khách đặt Online chọn `room_type_id` (`room_id = NULL`). Hạng phòng còn đặt được khi $N_{\text{available}} = N_{\text{total}}(\neq \text{'MAINTENANCE'}) - N_{\text{booked}}(\in \{\text{'RESERVED'}, \text{'CHECKED\_IN'}\}) \ge 1$.
2. **`BR2` (Khóa an toàn khi Gán phòng vật lý):** Chỉ được gán phòng vật lý có `rooms.status = 'AVAILABLE'` (Trống sạch) khi Check-in. Khóa hoàn toàn phòng `DIRTY`, `OCCUPIED`, `MAINTENANCE`.
3. **`BR3` (Khoảng ngày & Giá động Ngày thường / Cuối tuần):** `check_out_date > check_in_date` (tối thiểu 1 đêm). Đêm Thứ 2–Thứ 5 tính `price_per_night`, đêm Thứ 6–Chủ Nhật tính `weekend_price_per_night`.
4. **`BR4` (Đặt cọc & Quyết toán: Online 30/70 vs Walk-in 0/100):** Khách Online cọc trước **30%** (`deposit_amount > 0`), khi Check-out thu **70% tiền phòng còn lại + 100% dịch vụ/phụ thu**. Khách Walk-in nhận phòng ngay **0đ cọc** (`deposit_amount = 0`), khi Check-out thu **100% toàn bộ hóa đơn**.
5. **`BR5` (Room Folio & Đóng băng giá lịch sử):** Chỉ thêm/xóa dịch vụ khi đơn đang `CHECKED_IN`; sao chép `services.unit_price` lúc gọi vào `service_orders.unit_price`.
6. **`BR6` (Công thức Quyết toán `++`):** Tính tuần tự số nguyên `BIGINT`: `subtotal` $\rightarrow$ `SVC 5%` $\rightarrow$ `VAT 10%` $\rightarrow$ `grand_total = subtotal + SVC + VAT` $\rightarrow$ `balance_due = grand_total - deposit_deducted`.
7. **`BR7` (Vòng đời Dọn phòng `DIRTY` & Bảo toàn lịch sử):** Check-out xong phòng tự động sang `DIRTY`; Lễ tân bấm `[✓ Xác nhận đã dọn sạch]` mới về `AVAILABLE`. Chỉ hủy được đơn `RESERVED`. Chặn xóa danh mục đã phát sinh giao dịch.