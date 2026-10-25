# 02. DANH SÁCH CHỨC NĂNG & PRODUCT BACKLOG (MOSCOW)

> **Nguồn tham chiếu:** `quy-trinh-nghiep-vu-moi.md` và `yeu-cau-fr-nfr-moscow.md`.  
> **Ánh xạ Báo cáo:** Phục vụ trực tiếp **Chương 1 (Mục 1.3)** và **Chương 3 (Mục 3.2, 3.4)** của quyển báo cáo.

---

## 1. Danh sách Chức năng theo Module (7 Use Case Chính)

| Module / Phân hệ | Mã UC | Tên Use Case | Actor chính | Phụ trách Đặc tả & 3 Sơ đồ UML (`UC`, `Act`, `Seq`) | Vai trò phản biện | Cài đặt Java |
|---|:---:|---|---|:---:|:---:|:---:|
| **Xác thực & Điều hướng** | `UC01` | Đăng ký, Đăng nhập và Điều hướng vai trò | Khách hàng, Lễ tân, Admin | **Kiệt** | Nền tảng | Có (P0) |
| **Quản trị Danh mục & Giá** | `UC02` | Quản lý Danh mục Phòng, Biểu giá & Dịch vụ | Admin *(Lễ tân chỉ xem)* | **Hoàng** | Nền tảng | Có (P0) |
| **Cổng Đặt phòng Online** | `UC03` | Tìm kiếm & Đặt phòng trực tuyến (Theo Hạng phòng, Cọc 30%, Mã QR) | Khách hàng | **Hoàng** | ⭐ **Trọng tâm 1** | Có (P0) |
| **Nghiệp vụ Nhận phòng** | `UC04` | Quản lý Check-in, Gán phòng sạch & Tiếp nhận Walk-in (0đ cọc) | Lễ tân *(Khách hàng)* | **Phong** | ⭐ **Trọng tâm 2** | Có (P0) |
| **Nghiệp vụ Quyết toán** | `UC05` | Check-out, Quyết toán biểu giá `++` & Chuyển trạng thái dọn phòng | Lễ tân *(Khách hàng)* | **Phong** | ⭐ **Trọng tâm 3** | Có (P0) |
| **Tài khoản Phòng (Folio)** | `UC06` | Ghi nhận sử dụng dịch vụ lưu trú vào Room Folio (4 nhóm) | Lễ tân *(Khách hàng)* | **Kiệt** | Bổ trợ | Có (P0) |
| **Quản trị Chiến lược** | `UC07` | Báo cáo thống kê doanh thu đa chiều & Công suất phòng | Admin | **Khoa** | ⭐ **Trọng tâm 4** | Có (P1) |

---

## 2. Product Backlog Ưu tiên theo MoSCoW (`FR-01` đến `FR-18`)

| Mã FR | Tên yêu cầu chức năng (Tóm tắt chuẩn nghiệp vụ mới) | Mã UC | Bảng CSDL liên quan | MoSCoW | Sprint thực hiện |
|:---:|---|:---:|---|:---:|:---:|
| **FR-01** | **Đăng ký tài khoản khách hàng mới:** Nhập 5 trường (`full_name`, `date_of_birth`, `phone`, `username`, `password`), băm mật khẩu `BCrypt`, gán role `CUSTOMER`. | `UC01` | `users`, `customers` | **Must** | Tuần 2 |
| **FR-02** | **Đăng nhập & Tự động điều hướng 3 Role:** Xác thực tài khoản và tự động chuyển hướng về đúng Dashboard (`/customer/dashboard`, `/staff/dashboard`, `/admin/dashboard`); chặn truy cập trái quyền phía Server (`403`). | `UC01` | `users` | **Must** | Tuần 2 |
| **FR-03** | **Quản lý Hạng phòng & Biểu giá linh hoạt (Dynamic Pricing):** Thêm, sửa, xóa, xem hạng phòng kèm **giá ngày thường (T2–T5)** và **giá cuối tuần/lễ (T6–CN)**; chặn xóa khi đã có phòng hoặc đơn đặt. | `UC02` | `room_types`, `rooms`, `bookings` | **Must** | Tuần 2 |
| **FR-04** | **Quản lý Phòng vật lý & Bảo trì:** Thêm, sửa, xóa phòng (`room_number` duy nhất, tầng, hạng phòng), bật/tắt trạng thái bảo trì (`MAINTENANCE`); chặn xóa phòng đã có lịch sử đặt phòng. | `UC02` | `rooms`, `room_types`, `bookings` | **Must** | Tuần 2 |
| **FR-05** | **Quản lý Danh mục 4 Nhóm Dịch vụ:** Thêm, sửa, xóa, xem dịch vụ theo 4 nhóm chuẩn (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`); chặn xóa dịch vụ đã phát sinh trong `service_orders`. | `UC02` | `services`, `service_orders` | **Must** | Tuần 2 |
| **FR-06** | **Tìm kiếm Hạng phòng trống theo Tồn kho (Quota) & Tính giá động:** Kiểm tra số lượng phòng khả dụng của từng hạng phòng trong khoảng ngày `[nhận, trả)` ($N_{\text{available}} = N_{\text{total}} - N_{\text{booked}} \ge 1$) và tự động tính tiền phòng theo đêm ngày thường / cuối tuần. | `UC03` | `room_types`, `rooms`, `bookings` | **Must** | Tuần 2 |
| **FR-07** | **Xác nhận Pop-up, Thanh toán Cọc 30% & Giữ chỗ (`Hold Booking`):** Hiển thị Pop-up minh bạch biểu giá `++` (Tiền phòng, SVC 5%, VAT 10%), thu **cọc 30%** (`deposit_amount`), lưu đơn trạng thái `RESERVED` (`room_id = NULL`). | `UC03` | `bookings`, `customers` | **Must** | Tuần 2 |
| **FR-08** | **Xuất Phiếu xác nhận đặt phòng kèm Mã QR & Quản lý Dashboard Khách hàng:** Sinh mã `BK-XXXX` kèm mã QR tra cứu trên Phiếu xác nhận; hiển thị lịch sử đơn đặt trên `/customer/dashboard`. | `UC03` | `bookings`, `customers` | **Must** | Tuần 2 |
| **FR-09** | **Hủy đơn đặt phòng giữ chỗ (`RESERVED`):** Cho phép khách hàng hoặc lễ tân hủy đơn đang ở trạng thái `RESERVED` (chuyển sang `CANCELLED`, hoàn trả suất Quota cho hạng phòng). | `UC03` | `bookings` | **Should** | Tuần 2 |
| **FR-10** | **Check-in khách đặt trước (Quét mã QR & Gán phòng vật lý sạch):** Tra cứu mã `BK-XXXX` / QR, lọc danh sách phòng vật lý thuộc đúng hạng phòng đang ở trạng thái **`AVAILABLE` (Trống sạch)** để gán `room_id`, tính phụ thu nhận sớm (nếu có), chuyển đơn sang `CHECKED_IN` và phòng sang `OCCUPIED`. | `UC04` | `bookings`, `rooms`, `customers` | **Must** | Tuần 2 |
| **FR-11** | **Tiếp nhận & Check-in Khách vãng lai (`Walk-in` – 0đ cọc lúc vào):** Lễ tân chọn trực tiếp phòng đang `AVAILABLE`, nhập thông tin khách (Họ tên, SĐT, CCCD) và cho nhận phòng ngay với **`deposit_amount = 0`** (thanh toán 100% lúc Check-out). | `UC04` | `customers`, `bookings`, `rooms` | **Must** | Tuần 2 |
| **FR-12** | **Ghi nhận Dịch vụ Lưu trú vào Room Folio (Đóng băng giá):** Thêm dịch vụ thuộc 4 nhóm (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`) cho phòng đang `CHECKED_IN`, sao chép `unit_price` lúc gọi vào `service_orders` và cho phép xóa dòng ghi nhầm khi chưa thanh toán. | `UC06` | `service_orders`, `services`, `bookings` | **Must** | Tuần 2 |
| **FR-13** | **Tự động Quyết toán Hóa đơn theo Biểu giá `++` & Khấu trừ Cọc:** Tính tự động `Subtotal = Tiền phòng + Room Folio + Phụ thu (Nhận sớm / Trả trễ)`, cộng `SVC 5%` và `VAT 10%` ra `Grand Total`, sau đó trừ `deposit_deducted` (**Khách Online trả 70% tiền phòng còn lại + DV/Phụ thu; Khách Walk-in trả 100%**). | `UC05` | `bookings`, `service_orders`, `invoices` | **Must** | Tuần 2 |
| **FR-14** | **Xác nhận Thanh toán, Xuất/In Hóa đơn VAT & Chuyển phòng sang `DIRTY`:** Lưu bản ghi `invoices` (1–1, chống trùng Idempotency), hỗ trợ in hóa đơn `window.print()`, đổi đơn sang `CHECKED_OUT` và tự động chuyển phòng vật lý sang **`DIRTY` (Chờ dọn dẹp)**. | `UC05` | `invoices`, `bookings`, `rooms` | **Must** | Tuần 2 |
| **FR-15** | **Quản lý Vòng đời Dọn phòng (`Housekeeping Clean Confirmation`):** Khóa không cho gán phòng `DIRTY` cho khách mới; tích hợp nút **[Xác nhận đã dọn sạch]** trên Dashboard Lễ tân để chuyển phòng từ `DIRTY` về lại `AVAILABLE`. | `UC05` | `rooms` | **Must** | Tuần 2 |
| **FR-16** | **Dashboard Lễ tân & Sơ đồ Lưới Phòng Trực quan 4 Trạng thái:** Hiển thị các chỉ số vận hành trong ngày, danh sách Check-in/Check-out hôm nay và sơ đồ phòng mã hóa màu (`AVAILABLE`, `OCCUPIED`, `DIRTY`, `MAINTENANCE`). | `UC04`, `UC05` | `rooms`, `bookings`, `service_orders` | **Should** | Tuần 2 |
| **FR-17** | **Báo cáo Doanh thu Đa chiều (`++`):** Thống kê doanh thu theo thời gian, tách bạch Tiền phòng, 4 nhóm Dịch vụ, Phụ thu, Phí phục vụ `SVC 5%` và Thuế `VAT 10%` trên `/admin/dashboard`. | `UC07` | `invoices`, `service_orders`, `bookings` | **Should** | Tuần 2–3 |
| **FR-18** | **Đo lường Công suất Khai thác Phòng (`Occupancy Rate`, `ADR`, `RevPAR`):** Tính tỷ lệ lấp đầy phòng chuẩn quốc tế (loại trừ phòng `MAINTENANCE` khỏi mẫu số), giá bán bình quân đêm (`ADR`) và doanh thu trên mỗi phòng sẵn có (`RevPAR`). | `UC07` | `rooms`, `bookings`, `invoices` | **Could** | Tuần 2–3 |

---

## 3. Won't Have (Ngoài phạm vi đồ án)
- Không tích hợp cổng thanh toán ngân hàng thật (sử dụng mã VietQR minh họa / xác nhận giả lập).
- Không đồng bộ kênh OTA bên ngoài (Booking.com, Agoda); không quản lý nhiều chi nhánh; không quản lý kho bếp hay nhân sự/tiền lương; không đặt nhiều phòng trong 1 mã đơn.

---

## 4. Tóm tắt các Quyết định Nghiệp vụ đã Chốt trên Giao diện & CSDL
| Hạng mục | Quyết định chuẩn hóa để Code & Viết Báo cáo |
|---|---|
| **Kiến trúc Giao diện** | **4 phân hệ độc lập:** Landing Storytelling (`/`) + Customer Dashboard (`/customer/dashboard`) + Receptionist PMS (`/staff/dashboard`) + Admin Backoffice (`/admin/dashboard`). |
| **Đặt phòng & Gán phòng** | Đặt Online giữ chỗ theo **Hạng phòng (`room_type_id`, `room_id = NULL`)**; khi khách đến Check-in tại quầy Lễ tân mới chọn **gán phòng vật lý đang trống sạch (`AVAILABLE`)**. |
| **Quy tắc Cọc & Quyết toán** | **Khách Online:** Cọc 30% lúc đặt $\rightarrow$ Check-out trả **70% tiền phòng còn lại + 100% dịch vụ & phụ thu**. **Khách Walk-in:** Lúc vào cọc `0đ` $\rightarrow$ Check-out trả **100% toàn bộ hóa đơn**. |
| **Biểu giá `++` & Phụ thu** | Áp dụng **Phí phục vụ 5% (`SVC`)** và **Thuế GTGT 10% (`VAT`)** tính tuần tự trên `Subtotal`; hỗ trợ phụ thu nhận phòng sớm (`early_checkin_fee`) và trả phòng trễ sau 12:00 (`late_checkout_fee`). |
| **Vòng đời Buồng phòng** | Bổ sung trạng thái **`DIRTY` (Chờ dọn dẹp)** màu cam (`#F59E0B` / `#D97706`) ngay sau khi Check-out; Lễ tân bấm **[Xác nhận đã dọn sạch]** để mở lại phòng về `AVAILABLE`. |