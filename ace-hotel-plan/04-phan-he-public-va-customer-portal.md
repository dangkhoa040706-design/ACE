# 04. Phân hệ Public & Customer Portal (`WF-01`, `WF-02`, `WF-03`, `WF-04` — `UC01` & `UC03`)

> **Mục tiêu:** Đặc tả chi tiết cách cài đặt **Phân hệ Công khai & Cổng Khách hàng** gồm: Landing Page Storytelling (`WF-01`), Đăng nhập/Đăng ký (`WF-02`), Tìm kiếm Hạng phòng theo Tồn kho (*Quota*) & Pop-up xác nhận thanh toán **cọc 30%** (`WF-04`), và Dashboard Khách hàng lưu **Phiếu xác nhận đặt phòng kèm Mã QR** (`WF-03` — ⭐ Trọng tâm 1)[cite: 47, 53].

---

## 1. Màn hình `WF-01` — Landing Page Storytelling (`GET /`)

### 1.1. Controller & Dữ liệu truyền xuống View (`PublicController.java`)
- **Route:** `GET /` (Công khai, không cần đăng nhập)[cite: 5, 53].
- **Dữ liệu Model:**
  - `roomTypes`: Danh sách 5 hạng phòng từ `roomTypeRepository.findAll()` (hiển thị tên, sức chứa, mô tả, **giá ngày thường T2–T5** `pricePerNight` và **giá cuối tuần T6–CN** `weekendPricePerNight`)[cite: 5, 53].
  - `servicesByCategory`: Danh sách dịch vụ nhóm theo 4 danh mục (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`)[cite: 53].
  - `defaultCheckIn`: `LocalDate.now()` | `defaultCheckOut`: `LocalDate.now().plusDays(2)`.
- **Thanh tìm kiếm nhanh (`Quick Booking Bar`):** Form `GET /booking/search` gửi 3 tham số `checkIn`, `checkOut`, `guests` sang màn hình `WF-04`[cite: 53].

---

## 2. Màn hình `WF-04` — Tìm Hạng phòng theo Quota & Pop-up Cọc 30% (`UC03` – ⭐ Trọng tâm 1)

### 2.1. Công thức Kiểm tra Tồn kho Hạng phòng (`Quota Check`) & Tính giá Động
Khi khách hàng tìm phòng từ `checkIn` đến `checkOut` với số khách `guests`[cite: 53]:
1. **Kiểm tra hợp lệ ngày (`BR3`):** Nếu `!checkOut.isAfter(checkIn)` $\rightarrow$ báo lỗi `MSG-20` (*"Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 đêm."*)[cite: 8, 30, 53].
2. **Tính số suất trống (`availableQuota`) cho từng hạng phòng có `capacity >= guests` (`BR1`):**
   - `totalSellableRooms` = Số phòng vật lý thuộc hạng phòng đó có `status != 'MAINTENANCE'`[cite: 53].
   - `overlappingBookings` = Số đơn đặt thuộc hạng phòng đó có `status IN ('RESERVED', 'CHECKED_IN')` và thỏa `existing.checkInDate < checkOut AND existing.checkOutDate > checkIn`[cite: 9, 53].
   - `availableQuota = totalSellableRooms - overlappingBookings`.
   - Chỉ hiển thị các hạng phòng có `availableQuota >= 1`.
3. **Tính toán Tài chính cho từng Hạng phòng (Theo chuẩn `BIGINT` không lệch số – `BR3`, `BR6`):**
   - `roomCharge`: Lặp từng ngày `d` từ `checkIn` đến `checkOut.minusDays(1)`. Nếu `d` là Thứ 6, Thứ 7, Chủ Nhật (`FRIDAY`, `SATURDAY`, `SUNDAY`) cộng `weekendPricePerNight`, nếu Thứ 2–Thứ 5 cộng `pricePerNight`[cite: 53].
   - `estSvc = Math.round(roomCharge * 0.05)` *(Phí phục vụ 5%)*[cite: 53].
   - `estVat = Math.round((roomCharge + estSvc) * 0.10)` *(Thuế GTGT 10%)*[cite: 53].
   - `estGrandTotal = roomCharge + estSvc + estVat` *(Tổng gói phòng ước tính `++`)*[cite: 53].
   - **`depositAmount = Math.round(estGrandTotal * 0.30)`** *(Mức cọc 30% bắt buộc thanh toán ngay)*[cite: 53].
   - **`remainingAtCounter = estGrandTotal - depositAmount`** *(70% tiền phòng còn lại sẽ thanh toán khi Check-out)*[cite: 53].

### 2.2. DTO `RoomTypeQuoteDto.java` (Truyền xuống `templates/customer/search-deposit.html`)
~~~java
public record RoomTypeQuoteDto(
    Long roomTypeId,
    String name,
    int capacity,
    String description,
    long pricePerNight,
    long weekendPricePerNight,
    int availableQuota,
    long nights,
    long roomCharge,
    long estSvc,
    long estVat,
    long estGrandTotal,
    long depositAmount,
    long remainingAtCounter
) {}
~~~

### 2.3. Thiết kế Pop-up Modal Xác nhận & Thanh toán Cọc 30% trên `WF-04`
- Khi khách nhấn nút **[Tiếp tục đặt phòng]** trên một thẻ hạng phòng:
  - Nếu khách chưa đăng nhập: chuyển hướng sang `/login` (sau khi đăng nhập xong quay lại đặt phòng)[cite: 6, 53].
  - Nếu đã đăng nhập: Mở **Pop-up Modal *"Xác nhận thông tin đặt phòng & Thanh toán cọc 30%"*** ngay trên màn hình `WF-04`[cite: 53].
- **Các trường hiển thị trên Pop-up Modal:**
  - Họ tên khách, SĐT, Hạng phòng đã chọn, Ngày nhận (`14:00`) – Ngày trả (`12:00`), Số đêm[cite: 53].
  - Bảng kê chi phí minh bạch: `Tiền phòng gốc`, `Phí phục vụ (SVC 5%)`, `Thuế GTGT (VAT 10%)`, `Tổng giá trị gói phòng (++)`, **`Mức cọc 30% cần thanh toán ngay`**, `Số tiền 70% còn lại thanh toán tại quầy`[cite: 53].
  - Radio chọn phương thức thanh toán cọc: `TRANSFER` (Quét mã VietQR) hoặc `CARD` (Thẻ ngân hàng)[cite: 53].
  - Hai nút: `[Quay lại chỉnh sửa]` (đóng Modal) và **`[Xác nhận & Thanh toán cọc 30%]`** (submit form `POST /booking/deposit`)[cite: 53].

---

## 3. Xử lý Lưu Đơn Đặt Phòng Online (`POST /booking/deposit`)

Trong `BookingService.createOnlineBookingWithDeposit(...)` (`@Transactional`):
1. Kiểm tra lại `availableQuota >= 1` ngay trong giao dịch để chống 2 khách đặt trùng suất cuối cùng (nếu hết suất thì ném `BusinessException(MSG_22)`)[cite: 8, 30, 53].
2. Sinh mã `bookingCode` duy nhất theo định dạng `BK-` + 6 ký tự chữ/số in hoa (ví dụ: `BK-8K4P9X` hoặc `BK-` + `bookingId`)[cite: 47, 53].
3. Lưu thực thể `Booking`:
   - `customer` = khách hàng đang đăng nhập.
   - `roomType` = hạng phòng đã chọn.
   - **`room = null`** *(Chưa gán phòng vật lý; Lễ tân sẽ gán phòng sạch khi khách đến Check-in)*[cite: 53].
   - `checkInDate`, `checkOutDate`, `guestCount`.
   - `roomCharge` = tiền phòng gốc đã tính.
   - **`depositAmount` = số tiền cọc 30%**[cite: 53].
   - **`status = BookingStatus.RESERVED`**[cite: 53].
   - `createdBy` = user đang đăng nhập.
4. Redirect về `/customer/dashboard?newBooking={bookingCode}` kèm flash message `MSG-18`[cite: 30, 53].

---

## 4. Màn hình `WF-03` — Dashboard Khách hàng & Phiếu Xác nhận QR (`/customer/dashboard`)

### 4.1. Hiển thị Phiếu Xác nhận Đặt phòng kèm Mã QR (Offline SVG QR Generator)
- Trên `/customer/dashboard` (`templates/customer/dashboard.html`), mỗi đơn đặt phòng của khách được hiển thị kèm một **Thẻ Phiếu Xác Nhận Đặt Phòng (Booking Confirmation Card)** chuẩn thiết kế `phieu-xac-nhan-dat-phong-mau.png`[cite: 47, 53]:
  - Hiển thị nổi bật **`bookingCode` (`BK-2404`, `BK-8K4P9X2M`)** và badge trạng thái (`Đã xác nhận – Đã cọc 30%` màu tím cho đơn `RESERVED`)[cite: 47, 53].
  - **Vẽ Mã QR tự động không cần mạng Internet (`static/js/qr-generator.js`):** Sử dụng hàm vẽ ma trận QR SVG/Canvas nội bộ từ chuỗi `bookingCode` để đảm bảo tại phòng thi dù mất mạng mã QR vẫn hiển thị sắc nét 100%[cite: 13, 47, 53].
  - Hiển thị đầy đủ bảng kê: Tiền phòng gốc, SVC 5%, VAT 10%, Tổng gói phòng `++`, **Tiền cọc 30% (Đã thanh toán)** và **Còn phải thanh toán tại quầy (70%)**[cite: 47, 53].

### 4.2. Hủy Đơn Đặt Phòng Giữ Chỗ (`POST /customer/bookings/{id}/cancel`)
- Nút **[Hủy đơn đặt phòng]** chỉ hiển thị khi `booking.status == BookingStatus.RESERVED` (kèm hộp thoại xác nhận)[cite: 8, 53].
- Khi xác nhận hủy: cập nhật `booking.status = BookingStatus.CANCELLED` và hiển thị thông báo `MSG-19` (tồn kho `availableQuota` của hạng phòng tự động tăng lại 1 suất)[cite: 8, 30, 53].

---

## 5. Tiêu chí Hoàn thành Phân hệ Public & Customer Portal

- [ ] Trang chủ `/` (`WF-01`) cuộn mượt qua 5 section Storytelling, hiển thị đúng giá ngày thường (T2–T5) và cuối tuần (T6–CN) từ CSDL[cite: 5, 53].
- [ ] Tìm kiếm tại `/booking/search` (`WF-04`) tính đúng số suất trống `availableQuota` của từng hạng phòng và tính đúng tiền phòng theo đêm thường/cuối tuần[cite: 53].
- [ ] Pop-up Modal hiển thị đúng `SVC 5%`, `VAT 10%`, `Cọc 30%` và `Còn lại 70%`[cite: 53].
- [ ] Bấm xác nhận cọc 30% tạo đơn `RESERVED` với `room_id = NULL` và sinh ngay **Phiếu xác nhận kèm Mã QR** trên `/customer/dashboard` (`WF-03`)[cite: 47, 53].
- [ ] Hủy đơn `RESERVED` chuyển sang `CANCELLED` thành công; đơn `CHECKED_IN` / `CHECKED_OUT` không có nút hủy[cite: 8, 53].