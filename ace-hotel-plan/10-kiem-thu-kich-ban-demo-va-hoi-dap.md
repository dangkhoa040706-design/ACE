# 10. Checklist Kiểm thử, Kịch bản Bấm máy Demo 6 Bước & 8 Câu hỏi Kỹ thuật của Khoa

> **Mục tiêu:** Tài liệu bỏ túi dành riêng cho **Khoa** trong Tuần 3 và Tuần 4 để: (1) Tự kiểm thử ứng dụng trước khi bàn giao GitHub, (2) Thao tác bấm máy mượt mà khớp từng câu thuyết trình của Phong trong 6 phút Demo, và (3) Trả lời xuất sắc các câu hỏi xoáy sâu vào Code Java & CSDL của hội đồng[cite: 13, 21, 22, 35].

---

## 1. Lộ trình Bấm máy Demo Trực tiếp 6 Bước (Khoa thao tác – Phong dẫn lời)

> **Chuẩn bị trước khi lên bục:** Chạy ứng dụng với `--ace.seed=true` để nạp lại bộ dữ liệu mẫu sạch (6 phòng `AVAILABLE`, 3 phòng `OCCUPIED`, 2 phòng `DIRTY`, 1 phòng `MAINTENANCE`; đơn `BK-2401` ở `P.301`, đơn `BK-2402` chờ Check-in, đơn `BK-2405` Walk-in ở `P.202`)[cite: 13, 21].

| Bước | Thời lượng | Thao tác bấm máy của Khoa trên trình duyệt | Điểm nhấn Phong thuyết minh với Hội đồng |
|:---:|:---:|---|---|
| **Bước 1**<br>`UC01` | `45 giây` | 1. Mở `/` (`WF-01`), cuộn qua 5 section Storytelling, bấm nút đổi **Dark / Light Mode**.<br>2. Bấm **[Đăng nhập]** (`WF-02`), thử nhập sai pass (`MSG-01`), sau đó đăng nhập **`khach` / `khach`**. | Giới thiệu 4 phân hệ giao diện chuẩn `ACE Color System` và cơ chế tự động nhận diện Role đưa khách về `/customer/dashboard` (`WF-03`)[cite: 21, 53]. |
| **Bước 2**<br>`UC03`<br>*(⭐ TT1)* | `1p 15s` | 1. Chọn tìm phòng từ `Hôm nay` đến `2 ngày tới`, `2 khách` $\rightarrow$ Bấm **[Tìm hạng phòng]** (`WF-04`).<br>2. Chọn **Deluxe Room** $\rightarrow$ Mở **Pop-up xác nhận cọc 30%**.<br>3. Bấm **[Xác nhận & Thanh toán cọc 30%]** $\rightarrow$ Xem **Phiếu xác nhận kèm Mã QR** vừa sinh ra trên `WF-03`. | Giải thích cơ chế đặt theo **Tồn kho Hạng phòng (`Quota`)**, chưa gán cứng phòng vật lý (`room_id = NULL`), tính biểu giá `++` và thu **cọc 30% (`693.000đ`)** để cấp Phiếu QR[cite: 21, 53]. |
| **Bước 3**<br>`UC04`<br>*(⭐ TT2)* | `1p 15s` | 1. Đăng nhập Lễ tân **`nhanvien` / `nhanvien`** $\rightarrow$ Vào `/staff/dashboard` (`WF-05`).<br>2. Nhập mã **`BK-2402`** vào ô Check-in (`WF-06`) $\rightarrow$ Chọn gán phòng sạch **`P.201`** $\rightarrow$ Bấm **[Xác nhận Check-in]**.<br>3. Quay về sơ đồ, bấm vào phòng sạch **`P.101`**, chọn **Khách Walk-in**, nhập tên + CCCD $\rightarrow$ Nhận phòng ngay. | Chỉ ra **Khóa an toàn (`BR2`)**: chỉ phòng `AVAILABLE` (Xanh lá) mới hiện ra để gán; và đối chiếu **Khách Walk-in nhận phòng ngay với `0đ` tiền cọc**[cite: 21, 53]. |
| **Bước 4**<br>`UC06` | `45 giây` | 1. Trên sơ đồ phòng `WF-05`, bấm vào **Phòng `P.301` (`BK-2401`)** $\rightarrow$ Mở **[Room Folio]** (`WF-07`).<br>2. Chọn nhóm `MINIBAR`, thêm `1 Combo Snack (50.000đ)` (tổng lên `350.000đ`), sau đó bấm **[Xóa]** dòng vừa thêm nhầm để về lại `300.000đ`. | Giải thích sổ cái **Room Folio 4 nhóm dịch vụ** và cơ chế **đóng băng đơn giá (`Price Snapshot`)** vào `service_orders.unit_price`[cite: 21, 22, 53]. |
| **Bước 5**<br>`UC05`<br>*(⭐ TT3)* | `1p 30s` | 1. Từ `P.301` (`BK-2401`), bấm chuyển sang **[Check-out Quyết toán]** (`WF-08`).<br>2. Chỉ chuột vào dòng `Grand Total (2.656.500đ) - Cọc 30% (693.000đ) = Thực thu 1.963.500đ`.<br>3. Bấm **[Xác nhận Hoàn tất]** $\rightarrow$ Bấm **[🖨 In Hóa đơn VAT]**.<br>4. Về `WF-05`, chỉ vào thẻ **`P.301` màu Cam (`DIRTY`)**, sau đó bấm **[✓ Xác nhận đã dọn sạch]**. | Điểm ăn điểm lớn nhất: Hệ thống tự tính `SVC 5% + VAT 10%`, **trừ cọc 30% cho khách Online (chỉ thu 70% còn lại + DV)**, tự động đổi phòng sang **`🟠 DIRTY`** và mở khóa về **`🟢 AVAILABLE`** khi dọn xong[cite: 21, 53]. |
| **Bước 6**<br>`UC02`, `UC07`<br>*(⭐ TT4)* | `1 phút` | 1. Đăng nhập Admin **`admin` / `admin`** $\rightarrow$ Vào `/admin/dashboard` (`WF-10`).<br>2. Chỉ vào 3 thẻ **`Occupancy Rate`**, **`ADR`**, **`RevPAR`** và bảng Doanh thu đa chiều `++`.<br>3. Mở `/admin/catalog` (`WF-09`), thử bấm **[Xóa]** phòng `301` để hiện thông báo chặn xóa `MSG-15`. | Giải thích công thức **`Occupancy Rate` loại trừ phòng `MAINTENANCE` (`P.103`) khỏi mẫu số** và cơ chế bảo toàn lịch sử dữ liệu (`BR7`)[cite: 21, 53]. |

---

## 2. Checklist Tự Kiểm thử của Khoa Trước Khi Bàn giao Code (Giữa Tuần 3)

- [ ] **Bảo mật & Điều hướng (`UC01`):** Mật khẩu trong `sample-data.sql` là chuỗi băm BCrypt; 3 Role tự động về đúng 3 Dashboard; gõ URL trái quyền trả về trang `403` tiếng Việt (`MSG-05`)[cite: 6, 30, 53].
- [ ] **Quản trị Danh mục & Giá (`UC02`):** Lưu đúng giá ngày thường (T2–T5) và cuối tuần (T6–CN); chặn xóa hạng phòng/phòng/dịch vụ đã có dữ liệu liên kết (`MSG-14`, `MSG-15`, `MSG-17`)[cite: 30, 53].
- [ ] **Đặt phòng Online theo Quota (`UC03`):** Tính đúng `availableQuota`; Pop-up hiển thị đúng `SVC 5%`, `VAT 10%`, `Cọc 30%`; lưu đơn `RESERVED` với `room_id = NULL`; vẽ được mã QR trên `/customer/dashboard` khi tắt Wi-Fi[cite: 13, 53].
- [ ] **Check-in Gán phòng sạch & Walk-in (`UC04`):** Danh sách gán phòng chỉ hiện phòng `AVAILABLE` (ẩn `DIRTY`, `OCCUPIED`, `MAINTENANCE`); khách Walk-in lưu `user_id = NULL` và `deposit_amount = 0`[cite: 53].
- [ ] **Room Folio (`UC06`):** Chỉ phòng `CHECKED_IN` mới thêm/xóa được dịch vụ; sao chép đúng `unit_price` lúc gọi[cite: 8, 29, 53].
- [ ] **Check-out Quyết toán `++` & Dọn phòng (`UC05`):**
  - Khách Online (`BK-2401`): `2.000k + 300k = 2.300k` $\rightarrow$ `+ SVC 115k + VAT 241.5k = 2.656.500đ` $\rightarrow$ `- Cọc 693k` = **`1.963.500đ`**[cite: 53].
  - Khách Walk-in (`BK-2405`): `deposit_deducted = 0đ` $\rightarrow$ thu đủ **100% `Grand Total`**[cite: 21].
  - Thanh toán xong phòng chuyển sang **`DIRTY`**; bấm `[✓ Xác nhận đã dọn sạch]` chuyển về **`AVAILABLE`**[cite: 53].
- [ ] **Thống kê (`UC07`):** Mẫu số $R_{\text{sellable}}$ không tính phòng `MAINTENANCE`; không bị lỗi chia cho 0 khi lọc khoảng ngày trống[cite: 29, 53].

---

## 3. Tám Câu Hỏi & Đáp Án Kỹ Thuật / CSDL Dành Riêng Cho Khoa Khi Bảo Vệ

1. **Khi khách đặt phòng Online (`RESERVED`), cột `room_id` đang là `NULL`, vậy câu lệnh SQL kiểm tra còn phòng trống (`Quota`) viết như thế nào?**
   - *Đáp án của Khoa:* Em lấy tổng số phòng vật lý thuộc `room_type_id` đó có `status <> 'MAINTENANCE'`, trừ đi số bản ghi trong bảng `bookings` cùng `room_type_id` có `status IN ('RESERVED', 'CHECKED_IN')` mà giao khoảng ngày (`check_in_date < :newOut AND check_out_date > :newIn`). Nếu hiệu số $\ge 1$ thì hạng phòng còn suất trống[cite: 9, 53].
2. **Làm sao hệ thống phân biệt và tính tiền đúng cho Khách đặt Online (đã cọc 30%) và Khách Walk-in (không cọc lúc vào) mà không cần tách thành 2 bảng hóa đơn?**
   - *Đáp án của Khoa:* Trong bảng `bookings`, cả hai luồng đều dùng chung cột `deposit_amount`. Khách Online lúc đặt cọc 30% thì `deposit_amount = ROUND(estGrandTotal * 0.30)`; còn khách Walk-in lúc nhận phòng tại quầy thì `deposit_amount = 0`. Khi Check-out ở `CheckOutService`, hệ thống luôn tính `balance_due = grand_total - booking.getDepositAmount()`, nhờ vậy khách Online tự động chỉ trả 70% tiền phòng còn lại + dịch vụ/phụ thu, còn khách Walk-in tự động trả 100% hóa đơn[cite: 53].
3. **Trong bảng `invoices` có ràng buộc `CHECK (grand_total = subtotal + service_charge_amount + vat_amount)`. Nếu nhân `subtotal * 1.155` rồi làm tròn thì có nguy cơ bị lỗi lệch 1 đồng không?**
   - *Đáp án của Khoa:* Có ạ! Vì vậy trong `PricingService` và `CheckOutService`, em tuyệt đối không nhân tắt `1.155`, mà tính tuần tự từng biến kiểu `long`: tính `svc = Math.round(subtotal * 0.05)`, sau đó tính `vat = Math.round((subtotal + svc) * 0.10)`, và lấy `grandTotal = subtotal + svc + vat`. Nhờ cộng từ hai số nguyên đã làm tròn nên ràng buộc `CHECK` trong H2 Database luôn khớp 100% không bao giờ lệch 1 đồng[cite: 53].
4. **Tại sao trong bảng `customers`, cột `user_id` lại vừa là `UNIQUE` vừa cho phép `NULL`?**
   - *Đáp án của Khoa:* Trong chuẩn SQL và H2 Database, ràng buộc `UNIQUE` trên cột nullable cho phép có một bản ghi khách hàng liên kết 1–1 với 1 tài khoản `users` (đối với khách tự đăng ký Online), đồng thời cho phép nhiều bản ghi khách hàng vãng lai (*Walk-in*) có `user_id = NULL` vì khách Walk-in chỉ đưa CCCD tại quầy lễ tân chứ không tạo tài khoản đăng nhập[cite: 53].
5. **Nếu 2 nhân viên lễ tân ở 2 máy tính cùng bấm gán chung một phòng `P.201` đang `AVAILABLE` cho 2 khách khác nhau cùng lúc thì hệ thống xử lý thế nào?**
   - *Đáp án của Khoa:* Phương thức `confirmPreBookedCheckIn()` và `processWalkInCheckIn()` được đặt trong `@Transactional` và gọi `roomRepository.findByIdForUpdate(roomId)` sử dụng khóa bi quan `@Lock(LockModeType.PESSIMISTIC_WRITE)` (`SELECT ... FOR UPDATE`). Giao dịch thứ hai sẽ phải đợi giao dịch thứ nhất commit xong, lúc đó trạng thái phòng đã đổi sang `OCCUPIED` nên giao dịch thứ hai sẽ bị từ chối với thông báo `MSG-28`[cite: 10, 30, 53].
6. **Vì sao khi tính Tỷ lệ lấp đầy (`Occupancy Rate`) trong `ReportService`, em lại dùng `countAllSellableRooms()` (`status <> 'MAINTENANCE'`) làm mẫu số mà không dùng `roomRepository.count()`?**
   - *Đáp án của Khoa:* Theo chuẩn quản trị khách sạn quốc tế, các phòng đang đóng cửa bảo trì (`MAINTENANCE`, ví dụ phòng `103`) không thể bán cho khách nên không được tính vào tổng quỹ phòng khai thác ($R_{\text{sellable}} = \text{AVAILABLE} + \text{OCCUPIED} + \text{DIRTY}$)[cite: 21, 53].
7. **Tại sao bảng `service_orders` vừa có khóa ngoại `service_id` nối sang bảng `services`, lại vừa lưu thêm cột `unit_price`?**
   - *Đáp án của Khoa:* Để thực hiện kỹ thuật **Price Snapshot (Đóng băng giá lịch sử – `BR5`)**. Khi lễ tân bấm ghi nợ dịch vụ vào Room Folio, hệ thống chép `services.unit_price` tại thời điểm đó vào `service_orders.unit_price`. Nếu hôm sau Admin tăng giá *Bữa sáng* hoặc *Bia Tiger* ở màn hình danh mục, các hóa đơn và Room Folio đã gọi trước đó vẫn giữ nguyên mức giá cũ lúc khách dùng[cite: 8, 22, 29, 53].
8. **Dữ liệu của H2 Database được lưu ở đâu? Khi tắt IntelliJ bật lại có mất dữ liệu vừa tạo lúc Demo không? Và muốn reset lại dữ liệu mẫu sạch trước khi thi thì làm sao?**
   - *Đáp án của Khoa:* Em cấu hình H2 ở chế độ lưu file vật lý `jdbc:h2:file:./data/ace_hotel_db` với `ddl-auto=none` và `CREATE TABLE IF NOT EXISTS`, nên tắt ứng dụng bật lại dữ liệu vẫn còn nguyên vẹn (`NFR-08`). Khi cần nạp lại bộ dữ liệu mẫu sạch trước giờ báo cáo, chỉ cần chạy kèm tham số `--ace.seed=true`, class `DemoDataSeeder` sẽ chạy lại `sample-data.sql` với hàm ngày tương đối `CURRENT_DATE` để mọi đơn đặt phòng đều khớp đúng ngày chấm thi[cite: 9, 10, 13, 24].