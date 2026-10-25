# 06. Phân hệ Lễ tân PMS: Room Folio 4 Nhóm & Check-out Quyết toán `++` (`WF-07`, `WF-08` — `UC06` & `UC05`)

> **Mục tiêu:** Đặc tả chi tiết cách cài đặt **Màn hình Quản lý Tài khoản Phòng – Room Folio (`WF-07` – `UC06`)** và **Màn hình Quyết toán Check-out & Xuất Hóa đơn VAT (`WF-08` – `UC05` ⭐ Trọng tâm 3)**: ghi nợ dịch vụ 4 nhóm với cơ chế đóng băng đơn giá lịch sử, tự động tính biểu giá `++` (`SVC 5%` + `VAT 10%`), khấu trừ tiền cọc (**Khách Online thu 70% còn lại + phát sinh; Khách Walk-in thu 100%**), chống tạo trùng hóa đơn (Idempotency) và tự động chuyển phòng sang trạng thái **`DIRTY` (Chờ dọn dẹp)**[cite: 28, 29, 53].

---

## 1. Màn hình `WF-07` — Quản lý Room Folio 4 Nhóm Dịch vụ (`UC06`)

### 1.1. Route & Giao diện (`StaffFolioController.java`)
- **Route xem Room Folio:** `GET /staff/bookings/{id}/folio` (`templates/staff/folio.html`)[cite: 53].
- **Điều kiện tiên quyết (`BR5`):** Chỉ mở form ghi nhận/xóa dịch vụ khi `booking.getStatus() == BookingStatus.CHECKED_IN`[cite: 8, 29, 53].
- **Bố cục màn hình `WF-07`:**
  1. **Thanh thông tin phòng đang lưu trú:** Số phòng (`P.301`), Hạng phòng (`Deluxe`), Mã đơn (`BK-2401`), Tên khách hàng (`Nguyễn Minh Anh`), Giờ nhận thực tế và nút chuyển nhanh **[Chuyển sang Check-out Quyết toán (`WF-08`)]**[cite: 18, 21, 53].
  2. **Form Ghi nợ Dịch vụ theo 4 Nhóm (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`):**
     - Bộ lọc/Tab chọn Nhóm dịch vụ $\rightarrow$ Dropdown chọn mặt hàng từ bảng `services` (hiển thị kèm `unitPrice` và `unit`, ví dụ: *Bữa sáng Buffet – 100.000đ / Suất*) $\rightarrow$ Ô nhập `quantity` (mặc định `1`, `min="1"`) $\rightarrow$ Nút **`[+ Ghi vào Room Folio]`**[cite: 18, 29, 53].
  3. **Bảng Sổ cái Dịch vụ Đã Sử dụng (`service_orders`):**
     - Các cột: STT, Nhóm (`category`), Tên dịch vụ, Thời gian gọi (`dd/MM/yyyy HH:mm`), Nhân viên ghi sổ (`createdBy.fullName`), Số lượng (`quantity`), Đơn giá lúc gọi (`unitPrice`), Thành tiền (`lineTotal`) và nút **`[Xóa]`**[cite: 18, 53].
     - Dòng cuối: **Tổng tiền dịch vụ phát sinh gốc (`serviceChargeTotal` – chưa thuế phí `++`)**[cite: 18, 53].

---

### 1.2. Xử lý Ghi nợ & Đóng băng Đơn giá (`POST /staff/bookings/{id}/folio`)
Trong `FolioService.addServiceOrder(Long bookingId, Long serviceId, int quantity, User currentStaff)` (`@Transactional`):
1. Kiểm tra `booking.getStatus() == BookingStatus.CHECKED_IN` (nếu không phải thì ném `BusinessException(MSG_34)`)[cite: 29, 30, 53].
2. Kiểm tra `quantity >= 1` (nếu `< 1` thì ném `BusinessException(MSG_35)`)[cite: 29, 30].
3. Lấy `ServiceItem` từ `serviceItemRepository.findById(serviceId)`.
4. Tạo mới `ServiceOrder`:
   - `booking = booking`, `service = serviceItem`, `quantity = quantity`.
   - **`unitPrice = serviceItem.getUnitPrice()`** *(Sao chép cứng đơn giá tại thời điểm gọi để nếu sau này Admin đổi giá trong bảng `services` thì Room Folio cũ không bị nhảy số tiền – `BR5`)*[cite: 8, 22, 29, 53].
   - `orderedAt = LocalDateTime.now()`, `createdBy = currentStaff`[cite: 53].
5. Lưu vào `serviceOrderRepository` và redirect về `/staff/bookings/{id}/folio` kèm thông báo `MSG-32`[cite: 29, 30].

### 1.3. Xử lý Xóa Dòng Dịch vụ Ghi Nhầm (`POST /staff/bookings/{id}/folio/{orderId}/delete`)
- Kiểm tra `booking.getStatus() == BookingStatus.CHECKED_IN` và `order.getBooking().getBookingId().equals(bookingId)`[cite: 29, 30].
- Xóa bản ghi `service_orders` và redirect về `/staff/bookings/{id}/folio` kèm thông báo `MSG-33`[cite: 29, 30].

---

## 2. Màn hình `WF-08` — Check-out, Quyết toán Biểu giá `++` & Chuyển Phòng `DIRTY` (`UC05` – ⭐ Trọng tâm 3)

### 2.1. Bước 1 — Tự động Gợi ý Phụ thu Trả phòng Trễ (`Late Check-out Policy`)
Khi Lễ tân mở `GET /staff/bookings/{id}/check-out`[cite: 53]:
- Giờ trả phòng tiêu chuẩn là **`12:00` trưa** của ngày `checkOutDate`[cite: 53].
- Nếu thời điểm hiện tại quá mốc `12:00` trưa (hoặc Lễ tân chọn mức phụ thu trên giao diện), hệ thống gợi ý `lateCheckoutFee` dựa trên đơn giá 1 đêm (`pricePerNight`) của hạng phòng đang ở[cite: 53]:
  - **Trước 12:00:** `0%` (`0đ` – Miễn phí)[cite: 53].
  - **Từ 12:00 đến 15:00:** `30%` (`Math.round(pricePerNight * 0.30)`)[cite: 53].
  - **Từ 15:00 đến 18:00:** `50%` (`Math.round(pricePerNight * 0.50)`)[cite: 53].
  - **Sau 18:00 (hoặc quá hạn ngày):** `100%` (`pricePerNight` – tương đương 1 đêm phòng)[cite: 53].
  *(Trên màn hình `WF-08`, Lễ tân có thể giữ mức gợi ý này hoặc chọn `0đ` nếu khách sạn miễn phí hỗ trợ khách)[cite: 53].*

---

### 2.2. Bước 2 — Công thức Tổng hợp Hóa đơn Quyết toán `++` (`CheckOutService.prepareSummary`)
Để đảm bảo không bao giờ lệch `1đ` với các ràng buộc `CHECK` trong bảng `invoices`, phương thức tính toán thực hiện tuần tự bằng kiểu số nguyên `long` (`BIGINT` – `BR6`)[cite: 53]:

~~~java
long roomCharge = booking.getRoomCharge();
long serviceChargeTotal = serviceOrders.stream()
        .mapToLong(ServiceOrder::getLineTotal)
        .sum();
long earlyCheckinFee = booking.getEarlyCheckinFee() != null ? booking.getEarlyCheckinFee() : 0L;
long lateCheckoutFee = selectedLateFee != null ? selectedLateFee : suggestedLateFee;

// 1. Tổng chi phí gốc (Subtotal Base)
long subtotal = roomCharge + serviceChargeTotal + earlyCheckinFee + lateCheckoutFee;

// 2. Phí phục vụ 5% (Service Charge - SVC 5%)
long serviceChargeAmount = Math.round(subtotal * 0.05);

// 3. Thuế GTGT 10% (VAT 10% tính trên Subtotal + SVC)
long vatAmount = Math.round((subtotal + serviceChargeAmount) * 0.10);

// 4. Tổng cộng hóa đơn (Grand Total ++)
long grandTotal = subtotal + serviceChargeAmount + vatAmount;

// 5. Khấu trừ tiền đã cọc (Online = 30% đã đóng; Walk-in = 0đ)
long depositDeducted = booking.getDepositAmount() != null ? booking.getDepositAmount() : 0L;

// 6. Số tiền thực thu tại quầy khi Check-out (Balance Due)
long balanceDue = Math.max(0L, grandTotal - depositDeducted);
~~~

#### Đối chiếu Kết quả Hiển thị trên `WF-08` với Bộ Dữ liệu Mẫu:
- **Trường hợp 1 — Khách Đặt Online (`BK-2401` tại Phòng `P.301` Deluxe):**
  - `roomCharge` (2 đêm) = `2.000.000đ` | `serviceChargeTotal` (Room Folio) = `300.000đ` | Phụ thu = `0đ`[cite: 21, 53].
  - `subtotal` = **`2.300.000đ`** $\rightarrow$ `SVC 5%` = **`115.000đ`** $\rightarrow$ `VAT 10%` = **`241.500đ`** $\rightarrow$ `grandTotal` = **`2.656.500đ`**[cite: 53].
  - Trừ `depositDeducted` (30% đã cọc lúc đặt Online) = **`- 693.000đ`**[cite: 53].
  - **`balanceDue` (Thực thu tại quầy)** = **`1.963.500đ`** *(Khách Online chỉ trả đúng 70% tiền phòng còn lại `1.617.000đ` + 100% dịch vụ sau thuế `346.500đ`)*[cite: 53].
- **Trường hợp 2 — Khách Vãng Lai Walk-in (`BK-2405` tại Phòng `P.202` Superior):**
  - `roomCharge` (1 đêm) = `950.000đ` | Dịch vụ = `0đ` | Phụ thu = `0đ`[cite: 21].
  - `subtotal` = **`950.000đ`** $\rightarrow$ `SVC 5%` = **`47.500đ`** $\rightarrow$ `VAT 10%` = **`99.750đ`** $\rightarrow$ `grandTotal` = **`1.097.250đ`**.
  - Trừ `depositDeducted` (Walk-in không cọc lúc vào) = **`0đ`**.
  - **`balanceDue` (Thực thu tại quầy)** = **`1.097.250đ`** *(Khách Walk-in thanh toán đủ 100% toàn bộ hóa đơn)*.

---

### 2.3. Bước 3 — Xác nhận Thanh toán, Chống Trùng Hóa đơn & Chuyển Phòng sang `DIRTY` (`POST /staff/bookings/{id}/check-out`)
Trong `CheckOutService.confirmCheckOutAndPay(...)` (`@Transactional`):
1. **Kiểm tra Idempotency (Chống bấm đúp tạo 2 hóa đơn):**
   - Kiểm tra `Optional<Invoice> existing = invoiceRepository.findByBooking_BookingId(bookingId)`[cite: 22, 28].
   - Nếu `existing.isPresent()`, không `INSERT` lần hai mà trả về ngay `existing.get().getInvoiceId()` để redirect sang trang xem hóa đơn[cite: 22, 28].
2. Kiểm tra `booking.getStatus() == BookingStatus.CHECKED_IN` (nếu khác thì ném `MSG-31`)[cite: 28, 30].
3. Lưu bản ghi `Invoice` mới với đầy đủ các trường `roomCharge`, `serviceChargeTotal`, `earlyCheckinFee`, `lateCheckoutFee`, `subtotal`, `serviceChargeAmount`, `vatAmount`, `grandTotal`, `depositDeducted`, `balanceDue`, `paymentMethod` (`CASH`/`CARD`/`TRANSFER`), `paidAt = LocalDateTime.now()`, `createdBy = currentStaff`[cite: 28, 53].
4. Cập nhật `Booking`:
   - `booking.setLateCheckoutFee(lateCheckoutFee)`
   - `booking.setStatus(BookingStatus.CHECKED_OUT)`
   - `booking.setActualCheckOut(LocalDateTime.now())`[cite: 28, 53].
5. **Cập nhật Phòng vật lý sang `DIRTY` (`BR7`):**
   - `Room room = booking.getRoom();`
   - **`room.setStatus(RoomStatus.DIRTY);`** *(Phòng tự động chuyển sang màu cam "Chờ dọn dẹp", khóa cứng không cho gán khách mới cho đến khi Lễ tân bấm xác nhận đã dọn sạch)*[cite: 53].
6. Redirect tới trang Hóa đơn VAT `/staff/invoices/{invoiceId}` kèm thông báo `MSG-29`[cite: 28, 30, 53].

---

## 3. Trang Xem & In Hóa đơn VAT (`GET /staff/invoices/{id}`)

- Hiển thị hóa đơn chuẩn khách sạn ACE Hotel: Logo ACE, Mã hóa đơn `#INV-{id}`, Mã đặt phòng `BK-XXXX`, Tên khách, Phòng vật lý, Giờ nhận – Giờ trả thực tế, bảng kê từng dòng tiền phòng + từng dịch vụ Room Folio, khối tổng hợp `Subtotal`, `SVC 5%`, `VAT 10%`, `Grand Total (++)`, `Khấu trừ tiền đã cọc` và **`SỐ TIỀN THỰC THU TẠI QUẦY (Balance Due)`**[cite: 18, 53].
- Nút **`[🖨 In Hóa đơn VAT]`** gọi `window.print()` kết hợp `print.css` để xuất bản in hoặc lưu PDF sạch đẹp[cite: 8, 28, 53].
- Nút **`[← Quay về Sơ đồ phòng (/staff/dashboard)]`** để Lễ tân thấy ngay phòng vừa trả đã đổi sang màu cam **`🟠 DIRTY`** và thực hiện bấm **`[✓ Xác nhận đã dọn sạch]`** trong buổi Demo[cite: 53].

---

## 4. Tiêu chí Hoàn thành `WF-07` & `WF-08` (`UC06` & `UC05`)

- [ ] Chỉ phòng đang `CHECKED_IN` mới thêm/xóa được dịch vụ trong Room Folio (`WF-07`)[cite: 8, 29, 53].
- [ ] Đơn giá `unit_price` trong `service_orders` được sao chép từ `services.unit_price` lúc gọi; khi Admin đổi giá danh mục thì đơn cũ không bị đổi tiền[cite: 8, 22, 29, 53].
- [ ] Màn hình Check-out (`WF-08`) của `BK-2401` tính ra đúng `Subtotal = 2.300.000đ`, `SVC 5% = 115.000đ`, `VAT 10% = 241.500đ`, `Grand Total = 2.656.500đ`, trừ cọc `693.000đ`, **`Balance Due = 1.963.500đ`**[cite: 53].
- [ ] Màn hình Check-out của khách Walk-in (`BK-2405`) có `deposit_deducted = 0đ` và thu đúng 100% `Grand Total`[cite: 21].
- [ ] Xác nhận thanh toán xong: tạo đúng 1 hóa đơn (bấm 2 lần không tạo trùng), đơn sang `CHECKED_OUT`, phòng vật lý chuyển sang **`🟠 DIRTY`**[cite: 28, 53].