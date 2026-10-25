# 03. YÊU CẦU CHI TIẾT: QUY TẮC NGHIỆP VỤ, USER STORY & TIÊU CHÍ NGHIỆM THU

> **Mục đích:** Bản đặc tả tóm tắt chuẩn xác để **Khoa** lập trình tầng `Service`/`Controller` và để **Hoàng, Phong, Kiệt** đối chiếu khi viết đặc tả chi tiết 7 Use Case ở **Chương 4 (Mục 4.2.1 $\rightarrow$ 4.2.7)** của quyển báo cáo.

---

## 1. Bảy Quy tắc Nghiệp vụ Cốt lõi Dùng Chung (`BR1` – `BR7`)

- **BR1 – Đặt phòng theo Tồn kho Hạng phòng (Room Type Quota) & Gán phòng khi Check-in:**
  - Mỗi đơn đặt phòng tương ứng với **1 suất phòng** thuộc một Hạng phòng (`room_type_id`) trong khoảng ngày `[check_in_date, check_out_date)`.
  - Khi khách đặt trực tuyến (`RESERVED`), hệ thống chưa gán phòng vật lý (`room_id = NULL`) mà kiểm tra tồn kho khả dụng của hạng phòng:
    $$N_{\text{available}} = N_{\text{total}} (\text{status} \neq \text{'MAINTENANCE'}) - N_{\text{booked}} (\text{status} \in \{\text{'RESERVED'}, \text{'CHECKED\_IN'}\} \text{ giao khoảng ngày}) \ge 1$$
  - Khoảng ngày tính theo nửa mở `[nhận, trả)`: khách mới được phép nhận phòng đúng vào ngày khách cũ trả phòng (`existing.check_in_date < new.check_out_date AND existing.check_out_date > new.check_in_date`).
- **BR2 – Khóa an toàn khi Gán phòng vật lý (Clean Physical Room Guard):**
  - Khi làm thủ tục Check-in (cho cả khách đặt trước lẫn khách Walk-in), Lễ tân **chỉ được phép gán phòng vật lý đang ở trạng thái `AVAILABLE` (Trống & Đã dọn sạch)**.
  - Các phòng đang ở trạng thái `DIRTY` (chờ dọn), `OCCUPIED` (đang ở) hoặc `MAINTENANCE` (bảo trì) bị khóa hoàn toàn.
- **BR3 – Khoảng ngày lưu trú & Biểu giá linh hoạt Ngày thường / Cuối tuần (Dynamic Pricing):**
  - Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 đêm (`check_out_date > check_in_date`).
  - Tiền phòng gốc (`room_charge`) được tính bằng tổng giá của từng đêm lưu trú $d \in [\text{check\_in\_date}, \text{check\_out\_date} - 1]$: nếu $d$ là Thứ 2 $\rightarrow$ Thứ 5 áp dụng `price_per_night`; nếu $d$ là Thứ 6, Thứ 7, Chủ Nhật áp dụng `weekend_price_per_night`.
- **BR4 – Quy tắc Đặt cọc & Thanh toán thống nhất (Online 30/70 vs Walk-in 0/100):**
  - **Khách Đặt Online (`UC03`):** Bắt buộc thanh toán **cọc 30%** tổng tiền phòng ước tính (`++`) để giữ chỗ (`status = 'RESERVED'`). Khi Check-out sẽ thanh toán **70% tiền phòng còn lại + 100% dịch vụ phát sinh & phụ thu**.
  - **Khách Vãng lai Walk-in (`UC04`):** Nhận phòng trực tiếp tại quầy với **tiền cọc `0đ` (`deposit_amount = 0`)**, trạng thái chuyển ngay sang `CHECKED_IN`. Khi Check-out sẽ thanh toán **100% toàn bộ hóa đơn**.
- **BR5 – Quản lý Room Folio & Đóng băng Đơn giá Lịch sử (Price Snapshot):**
  - Chỉ được thêm hoặc xóa dòng dịch vụ (`service_orders`, thuộc 4 nhóm `MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`) khi đơn đặt phòng đang ở trạng thái `CHECKED_IN`.
  - Khi thêm dịch vụ, đơn giá `services.unit_price` tại thời điểm gọi phải được sao chép cứng vào `service_orders.unit_price` để việc Admin đổi giá sau này không làm lệch hóa đơn cũ.
- **BR6 – Công thức Quyết toán Chuẩn `++` (SVC 5% + VAT 10%) & Khấu trừ Cọc:**
  - Mọi phép tính tiền tệ dùng kiểu số nguyên `BIGINT` (VNĐ) theo thứ tự tuần tự để không lệch ràng buộc `CHECK` trong CSDL:
    1. `subtotal = room_charge + service_charge_total + early_checkin_fee + late_checkout_fee`
    2. `service_charge_amount = Math.round(subtotal * 0.05)` *(Phí phục vụ 5%)*
    3. `vat_amount = Math.round((subtotal + service_charge_amount) * 0.10)` *(Thuế GTGT 10%)*
    4. `grand_total = subtotal + service_charge_amount + vat_amount`
    5. `deposit_deducted = booking.deposit_amount`
    6. `balance_due = grand_total - deposit_deducted` *(Số tiền thực thu tại quầy khi Check-out)*
- **BR7 – Vòng đời Dọn phòng (`DIRTY`) & Quy tắc Hủy đơn / Bảo toàn Lịch sử:**
  - Ngay sau khi Check-out hoàn tất, phòng vật lý tự động chuyển từ `OCCUPIED` sang **`DIRTY` (Chờ dọn dẹp)**; chỉ khi Lễ tân bấm **[Xác nhận đã dọn sạch]** phòng mới về lại `AVAILABLE`.
  - Chỉ cho phép hủy đơn đặt phòng khi đang ở trạng thái `RESERVED` (chuyển sang `CANCELLED`).
  - Tuyệt đối không xóa phòng đã từng có booking, không xóa hạng phòng đang có phòng/booking, không xóa dịch vụ đã có trong `service_orders`.

---

## 2. User Stories & Tiêu chí Nghiệm thu theo 7 Use Case

### UC01 – Đăng ký, Đăng nhập và Điều hướng vai trò (Phụ trách: Kiệt) · `FR-01`, `FR-02`
**Story:** Là Khách hàng, Lễ tân hoặc Admin, tôi muốn đăng ký/đăng nhập để hệ thống tự động đưa tôi về đúng không gian làm việc theo vai trò của mình.
- **Tiêu chí nghiệm thu:**
  - Khách hàng mới truy cập `/register`, nhập đủ 5 trường (`full_name`, `date_of_birth`, `phone`, `username`, `password`) $\rightarrow$ tạo thành công bản ghi `users` (`role = 'CUSTOMER'`, mật khẩu băm `BCrypt`) và `customers` (liên kết `user_id`), tự động chuyển vào `/customer/dashboard`.
  - Đăng nhập đúng tài khoản:
    - `ADMIN` (`admin` / `admin`) $\rightarrow$ tự động chuyển tới `/admin/dashboard`.
    - `RECEPTIONIST` (`nhanvien` / `nhanvien`) $\rightarrow$ tự động chuyển tới `/staff/dashboard`.
    - `CUSTOMER` (`khach` / `khach`) $\rightarrow$ tự động chuyển tới `/customer/dashboard`.
  - Đăng nhập sai tên hoặc mật khẩu $\rightarrow$ báo lỗi chung *"Sai tên đăng nhập hoặc mật khẩu."*, không tiết lộ sai trường nào.
  - Gõ trực tiếp URL của phân hệ khác (ví dụ `CUSTOMER` hoặc `RECEPTIONIST` gõ `/admin/**`) $\rightarrow$ bị chặn phía máy chủ (`403 Forbidden`).

---

### UC02 – Quản lý Danh mục Phòng, Biểu giá và Dịch vụ (Phụ trách: Hoàng) · `FR-03`, `FR-04`, `FR-05`
**Story:** Là Quản trị viên (`ADMIN`), tôi muốn cấu hình hạng phòng (kèm giá ngày thường/cuối tuần), danh sách phòng vật lý và 4 nhóm dịch vụ để vận hành kinh doanh.
- **Tiêu chí nghiệm thu:**
  - Thêm/sửa Hạng phòng với `price_per_night > 0` (T2–T5), `weekend_price_per_night > 0` (T6–CN), `capacity >= 1`.
  - Thêm/sửa Phòng vật lý với `room_number` không trùng lặp, `floor >= 1`, chọn hạng phòng; cho phép chuyển phòng đang trống sang `MAINTENANCE` và mở lại.
  - Thêm/sửa Dịch vụ với phân nhóm `category IN ('MINIBAR', 'LAUNDRY', 'FB', 'DAMAGES')`, `unit_price >= 0`, `unit`.
  - Thử xóa hạng phòng đang có phòng, xóa phòng đã có booking hoặc xóa dịch vụ đã từng gọi $\rightarrow$ hệ thống chặn xóa và hiển thị thông báo tiếng Việt rõ ràng.

---

### UC03 – Tìm kiếm và Đặt phòng trực tuyến (Phụ trách: Hoàng – ⭐ Trọng tâm 1) · `FR-06`..`FR-09`
**Story:** Là Khách hàng (`CUSTOMER`), tôi muốn tìm hạng phòng còn trống theo ngày, thanh toán cọc 30% và nhận Phiếu xác nhận có mã QR để giữ chỗ trước khi đến khách sạn.
- **Tiêu chí nghiệm thu:**
  - Chọn `check_in_date`, `check_out_date` (`> check_in_date`), `guest_count` $\rightarrow$ hệ thống chỉ hiển thị các hạng phòng còn suất trống ($N_{\text{available}} \ge 1$) và đủ sức chứa (`capacity >= guest_count`).
  - Tự động bóc tách số đêm ngày thường (T2–T5) và cuối tuần (T6–CN) để tính `room_charge`, `est_svc (5%)`, `est_vat (10%)`, `est_grand_total`, **Tiền cọc 30% (`deposit_amount`)** và **70% còn lại thanh toán tại quầy**.
  - Hiển thị Pop-up Modal xác nhận thông tin trước khi thanh toán cọc.
  - Sau khi xác nhận thanh toán cọc 30%: tạo bản ghi `bookings` với mã `BK-XXXX`, `status = 'RESERVED'`, `room_type_id` đã chọn, **`room_id = NULL`**, `deposit_amount = 30%`; hiển thị ngay **Phiếu xác nhận đặt phòng kèm mã QR** và lưu trong `/customer/dashboard`.
  - Cho phép hủy đơn khi trạng thái đang là `RESERVED` $\rightarrow$ chuyển sang `CANCELLED`, trả lại 1 suất trống cho hạng phòng.

---

### UC04 – Quản lý Check-in, Gán phòng sạch & Tiếp nhận Walk-in (Phụ trách: Phong – ⭐ Trọng tâm 2) · `FR-10`, `FR-11`
**Story:** Là Lễ tân (`RECEPTIONIST`), tôi muốn quét mã QR của khách đặt trước để gán phòng vật lý đang sạch hoặc tiếp nhận nhanh khách vãng lai (Walk-in) không cần thu cọc trước.
- **Tiêu chí nghiệm thu:**
  - **Với khách đặt trước (`RESERVED`):** Quét hoặc nhập mã `BK-XXXX` / SĐT $\rightarrow$ hiển thị thông tin đơn đặt, tiền cọc 30% đã đóng và lọc danh sách các phòng vật lý thuộc đúng hạng phòng đang ở trạng thái **`AVAILABLE` (Trống sạch)** (không hiển thị phòng `DIRTY`, `OCCUPIED`, `MAINTENANCE`).
  - Khi Lễ tân chọn phòng vật lý (ví dụ `P.201`), nhập/đối chiếu CCCD, chọn mức phụ thu nhận sớm (nếu đến trước 14:00: 0%, 30%, 50%, 100%) và bấm **[Xác nhận Check-in]** $\rightarrow$ cập nhật `bookings.room_id = 201`, `bookings.status = 'CHECKED_IN'`, `actual_check_in = NOW()`, `rooms.status = 'OCCUPIED'`.
  - **Với khách vãng lai (`Walk-in`):** Lễ tân chọn phòng đang `AVAILABLE`, nhập Họ tên, SĐT, CCCD, ngày trả phòng $\rightarrow$ bấm **[Hoàn tất Check-in Walk-in]** $\rightarrow$ tạo hồ sơ `customers` (`user_id = NULL`), tạo `bookings` với **`deposit_amount = 0`**, `status = 'CHECKED_IN'`, đổi phòng sang `OCCUPIED` ngay lập tức.

---

### UC05 – Check-out, Quyết toán `++` & Dọn phòng (Phụ trách: Phong – ⭐ Trọng tâm 3) · `FR-13`, `FR-14`, `FR-15`
**Story:** Là Lễ tân (`RECEPTIONIST`), tôi muốn hệ thống tự động tổng hợp tiền phòng, Room Folio, phụ thu trễ giờ, thuế phí `++` và khấu trừ tiền cọc để thu đúng số tiền còn lại, xuất hóa đơn VAT và chuyển phòng sang chờ dọn dẹp.
- **Tiêu chí nghiệm thu:**
  - Mở màn hình Check-out của phòng đang `CHECKED_IN` $\rightarrow$ tự động gợi ý phụ thu trả trễ `late_checkout_fee` nếu quá 12:00 trưa (12h–15h: 30%, 15h–18h: 50%, sau 18h: 100%).
  - Tính chính xác `subtotal`, `service_charge_amount (5%)`, `vat_amount (10%)`, `grand_total` và `balance_due = grand_total - deposit_deducted`:
    - Nếu là **Khách Online** (đã cọc 30%): `balance_due` thu đúng **70% tiền phòng còn lại + 100% dịch vụ & phụ thu (`++`)**.
    - Nếu là **Khách Walk-in** (cọc `0đ`): `balance_due` thu đúng **100% toàn bộ `grand_total`**.
  - Chọn phương thức thanh toán (`CASH`, `CARD`, `TRANSFER`) và bấm xác nhận $\rightarrow$ tạo đúng 1 bản ghi `invoices` (chống tạo trùng Idempotency), chuyển `bookings.status = 'CHECKED_OUT'`, chuyển **`rooms.status = 'DIRTY'`** (màu cam) và mở trang Hóa đơn VAT hỗ trợ `window.print()`.
  - Trên Dashboard Lễ tân, thẻ phòng `DIRTY` hiển thị nút **[Xác nhận đã dọn sạch]** $\rightarrow$ bấm vào chuyển `rooms.status` về lại **`AVAILABLE`** (màu xanh lá).

---

### UC06 – Ghi nhận Dịch vụ Lưu trú vào Room Folio (Phụ trách: Kiệt) · `FR-12`
**Story:** Là Lễ tân (`RECEPTIONIST`), tôi muốn ghi nhận các dịch vụ phát sinh thuộc 4 nhóm vào tài khoản phòng đang ở (Room Folio) để tự động cộng vào hóa đơn khi khách trả phòng.
- **Tiêu chí nghiệm thu:**
  - Chỉ mở và cho phép ghi nợ đối với đơn đặt phòng đang ở trạng thái `CHECKED_IN`.
  - Chọn dịch vụ theo 4 tab nhóm (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`), nhập `quantity >= 1` $\rightarrow$ lưu vào `service_orders` với `unit_price` sao chép từ `services.unit_price` tại thời điểm gọi và `created_by` là lễ tân đang đăng nhập.
  - Cho phép xóa dòng dịch vụ thêm nhầm khi phòng vẫn đang `CHECKED_IN` và cập nhật lại tổng tiền Room Folio tức thì.

---

### UC07 – Báo cáo Doanh thu đa chiều & Công suất phòng (Phụ trách: Khoa – ⭐ Trọng tâm 4) · `FR-17`, `FR-18`
**Story:** Là Quản trị viên (`ADMIN`), tôi muốn xem báo cáo cơ cấu doanh thu `++` và tỷ lệ lấp đầy phòng (`Occupancy Rate`, `ADR`, `RevPAR`) để đánh giá hiệu quả kinh doanh.
- **Tiêu chí nghiệm thu:**
  - Lọc theo khoảng ngày / tháng / năm $\rightarrow$ hiển thị bảng và biểu đồ doanh thu tách bạch: Tiền phòng, Dịch vụ 4 nhóm, Phụ thu, Phí phục vụ `SVC 5%` và Thuế `VAT 10%`.
  - Tính chính xác tổng số phòng có thể kinh doanh $R_{\text{sellable}} = \text{AVAILABLE} + \text{OCCUPIED} + \text{DIRTY}$ (loại trừ phòng `MAINTENANCE`) và hiển thị 3 chỉ số: **Occupancy Rate (%)**, **ADR** và **RevPAR**.