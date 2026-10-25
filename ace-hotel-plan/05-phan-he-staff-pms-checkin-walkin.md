# 05. Phân hệ Lễ tân PMS: Sơ đồ Phòng, Check-in Gán Phòng Sạch & Walk-in 0đ Cọc (`WF-05`, `WF-06` — `UC04`)

> **Mục tiêu:** Đặc tả chi tiết cách cài đặt **Bàn làm việc Nhân viên Lễ tân (`WF-05` – `/staff/dashboard`)** và **Màn hình Thủ tục Check-in (`WF-06` – `UC04` ⭐ Trọng tâm 2)** gồm: Sơ đồ lưới 12 phòng 4 trạng thái, thao tác 1-Click xác nhận phòng đã dọn sạch (`DIRTY` $\rightarrow$ `AVAILABLE`), quét/nhập mã QR đơn đặt trước để **gán phòng vật lý đang trống sạch (`AVAILABLE`)** và tiếp nhận **Khách vãng lai (`Walk-in`) nhận phòng ngay với 0đ tiền cọc**[cite: 53].

---

## 1. Màn hình `WF-05` — Dashboard Nhân viên Lễ tân (`GET /staff/dashboard`)

### 1.1. Bốn Thẻ Chỉ số Vận hành Realtime (KPI Cards)
Truy vấn trực tiếp từ bảng `rooms` và `bookings` để hiển thị ở đầu màn hình `/staff/dashboard`[cite: 7, 53]:
1. **🟢 Phòng trống sạch (`AVAILABLE`):** Số phòng vật lý sẵn sàng gán cho khách (`COUNT(rooms WHERE status = 'AVAILABLE')` — mẫu ban đầu: `6` phòng)[cite: 21, 53].
2. **🔵 Phòng đang có khách (`OCCUPIED`):** Số phòng đang lưu trú (`COUNT(rooms WHERE status = 'OCCUPIED')` — mẫu ban đầu: `3` phòng, kèm số phòng đang quá hạn `OVERDUE`)[cite: 21, 53].
3. **🟠 Phòng chờ dọn dẹp (`DIRTY`):** Số phòng vừa Check-out chưa vệ sinh (`COUNT(rooms WHERE status = 'DIRTY')` — mẫu ban đầu: `2` phòng là `P.102`, `P.401`)[cite: 21, 53].
4. **⚪ Phòng đang bảo trì (`MAINTENANCE`):** Số phòng đóng cửa sửa chữa (`COUNT(rooms WHERE status = 'MAINTENANCE')` — mẫu ban đầu: `1` phòng là `P.103`)[cite: 21, 53].

---

### 1.2. Thanh Quét Mã QR / Tra cứu Nhanh & Bảng Lịch Hôm nay
- **Ô Quét QR / Nhập mã Booking (`Quick QR Check-in Bar`):**
  - Input text có `autofocus` nhận chuỗi `booking_code` (ví dụ `BK-2402` hoặc `BK-8K4P9X2M`) từ súng bắn mã QR cầm tay hoặc gõ phím $\rightarrow$ submit tới `GET /staff/check-in?code=...` (`WF-06`)[cite: 53].
  - Cạnh bên có nút hành động nhanh: **`[+ Tiếp nhận Khách Walk-in (0đ cọc)]`** dẫn tới `GET /staff/walk-in`[cite: 53].
- **Bảng Đơn Đặt Cần Xử Lý Hôm Nay:**
  - Liệt kê các đơn `RESERVED` có `checkInDate <= TODAY` (nút **[Quét QR & Gán phòng Check-in]**) và các đơn `CHECKED_IN` đang ở hoặc quá hạn (nút **[Room Folio]** và **[Check-out Quyết toán]**)[cite: 7, 53].

---

### 1.3. Sơ đồ Lưới 12 Phòng Vật lý (`Room Status Grid`) & Nút Dọn Sạch 1-Click
Hiển thị toàn bộ 12 phòng sắp xếp theo `floor ASC, roomNumber ASC`[cite: 24, 30]. Mỗi thẻ phòng hiển thị giao diện và nút bấm động theo `room.status`[cite: 7, 53]:

| `room.status` | Màu Thẻ & Badge | Thông tin hiển thị trên thẻ phòng | Nút thao tác trên thẻ phòng |
|---|---|---|---|
| **`AVAILABLE`** | 🟢 Xanh lá (`--st-free`) | Số phòng, Tầng, Hạng phòng, Giá T2–T5 / T6–CN, *"Trống & Đã dọn sạch"* | **[Nhận khách Walk-in]** (`/staff/walk-in?roomId={id}`)[cite: 53] |
| **`OCCUPIED`** | 🔵 Xanh dương (`--st-occupied`)<br>*(Hoặc 🔴 Đỏ `--st-overdue` nếu `booking.isOverdue()`)* | Số phòng, Hạng phòng, Mã `BK-XXXX`, Tên khách đang ở, Ngày nhận – Ngày trả | **[Room Folio]** (`/staff/bookings/{bId}/folio`)<br>**[Check-out]** (`/staff/bookings/{bId}/check-out`)[cite: 7, 53] |
| **`DIRTY`** | 🟠 Cam (`--st-dirty`) | Số phòng, Hạng phòng, Cảnh báo *"Phòng bẩn – Chờ buồng phòng dọn dẹp"* (Khóa gán khách) | Form `POST /staff/rooms/{id}/clean`:<br>**[✓ Xác nhận đã dọn sạch]**[cite: 53] |
| **`MAINTENANCE`** | ⚪ Xám (`--st-maint`) | Số phòng, Hạng phòng, *"Đang bảo trì thiết bị"* | *(Khóa thao tác nghiệp vụ lễ tân)*[cite: 7, 53] |

#### Xử lý Endpoint `POST /staff/rooms/{id}/clean` (`StaffDashboardController`):
- Khi nhân viên buồng phòng báo đã dọn xong phòng `DIRTY`, Lễ tân bấm **[✓ Xác nhận đã dọn sạch]**[cite: 53].
- `CheckOutService.markRoomAsClean(roomId)` kiểm tra `room.getStatus() == RoomStatus.DIRTY`, cập nhật `room.setStatus(RoomStatus.AVAILABLE)` và redirect về `/staff/dashboard` kèm thông báo `MSG-30` (*"Đã xác nhận dọn vệ sinh hoàn tất! Phòng {roomNumber} đã chuyển về trạng thái Trống sạch (AVAILABLE)."*)[cite: 30, 53].

---

## 2. Màn hình `WF-06` — Chế độ 1: Check-in Khách Đặt Trước & Gán Phòng Sạch (`/staff/check-in`)

### 2.1. Bước 1 — Tra cứu đơn đặt (`GET /staff/check-in?code=...`)
1. Tìm `Booking` theo `bookingCode` (không phân biệt hoa thường) hoặc theo SĐT khách hàng[cite: 30, 53].
2. Kiểm tra `booking.getStatus() == BookingStatus.RESERVED` (nếu không phải `RESERVED` thì báo lỗi `MSG-26`)[cite: 30, 53].
3. Truy vấn danh sách **các phòng vật lý sạch** thuộc đúng hạng phòng khách đã đặt (`BR2` – *Clean Physical Room Guard*)[cite: 53]:
   ~~~java
   List<Room> cleanAvailableRooms = roomRepository.findByRoomType_RoomTypeIdAndStatusOrderByRoomNumberAsc(
       booking.getRoomType().getRoomTypeId(),
       RoomStatus.AVAILABLE
   );
   ~~~
   *(Nếu `cleanAvailableRooms.isEmpty()`, hiển thị cảnh báo `MSG-27` nhắc Lễ tân xác nhận dọn sạch phòng `DIRTY` thuộc hạng phòng đó trước khi gán)[cite: 30, 53].*

### 2.2. Bước 2 — Chọn Phòng Vật lý Sạch, Phụ thu Nhận sớm & Xác nhận (`POST /staff/check-in`)
Trên màn hình `WF-06`, Lễ tân thực hiện:
1. Chọn 1 phòng vật lý từ danh sách `cleanAvailableRooms` (ví dụ chọn **`P.201`** cho đơn **`BK-2402`**)[cite: 21, 53].
2. Kiểm tra và nhập/cập nhật số CCCD / Passport (`idNumber`) của khách vào hồ sơ `Customer`[cite: 18, 53].
3. Chọn chính sách **Phụ thu nhận phòng sớm (`earlyCheckinFee`)** căn cứ giờ nhận phòng chuẩn `14:00`[cite: 53]:
   - Mức `0% (0đ)`: Đúng giờ hoặc miễn phí ưu đãi[cite: 53].
   - Mức `30%`: Nhận phòng từ `09:00 – 14:00` (`ROUND(pricePerNight * 0.30)`)[cite: 53].
   - Mức `50%`: Nhận phòng từ `06:00 – 09:00` (`ROUND(pricePerNight * 0.50)`)[cite: 53].
   - Mức `100%`: Nhận phòng trước `06:00` sáng (`pricePerNight`)[cite: 53].
4. Khi bấm **[Xác nhận Check-in & Giao chìa khóa]**, trong một `@Transactional`[cite: 28, 53]:
   - Khóa dòng phòng `Room` được chọn và kiểm tra lại `room.getStatus() == RoomStatus.AVAILABLE` (nếu không thỏa thì ném `MSG-28`)[cite: 10, 30, 53].
   - Cập nhật `booking.setRoom(room)`, `booking.setStatus(BookingStatus.CHECKED_IN)`, `booking.setActualCheckIn(LocalDateTime.now())`, `booking.setEarlyCheckinFee(earlyCheckinFee)`[cite: 53].
   - Cập nhật `room.setStatus(RoomStatus.OCCUPIED)`[cite: 53].
   - Redirect về `/staff/dashboard` kèm thông báo `MSG-24`[cite: 30, 53].

---

## 3. Màn hình `WF-06` — Chế độ 2: Tiếp nhận Khách Vãng Lai (`Walk-in` – 0đ Cọc Lúc Vào)

> **Quy tắc chốt (`BR4`):** Khách vãng lai (*Walk-in*) đến trực tiếp quầy được chọn phòng trống sạch (`AVAILABLE`) và nhận phòng ngay lập tức với **tiền cọc `0đ` (`deposit_amount = 0`)**. Khi trả phòng (Check-out), khách Walk-in sẽ thanh toán **100% toàn bộ hóa đơn** (`balance_due = grand_total`).

### 3.1. Form Tiếp nhận Walk-in (`GET /staff/walk-in`)
- Lễ tân chọn **Phòng vật lý đang `AVAILABLE`** (ví dụ `P.101 – Standard`), nhập `Ngày trả phòng dự kiến` (`checkOutDate`, mặc định là ngày mai), `Số lượng khách` (`guestCount`) và thông tin khách hàng:
  - `Họ và tên` (`fullName` – bắt buộc)[cite: 53].
  - `Số điện thoại` (`phone` – bắt buộc)[cite: 53].
  - `Số CCCD / Passport` (`idNumber` – bắt buộc khi nhận phòng trực tiếp)[cite: 53].

### 3.2. Xử lý Lưu Check-in Walk-in (`POST /staff/walk-in`)
Trong `CheckInService.processWalkInCheckIn(WalkInCheckInDto dto, User currentStaff)` (`@Transactional`):
1. Kiểm tra `checkOutDate.isAfter(LocalDate.now())` (`BR3`) và kiểm tra phòng vật lý được chọn đang có `status == RoomStatus.AVAILABLE` (`BR2`)[cite: 53].
2. Kiểm tra hạng phòng của phòng đó còn đủ tồn kho `availableQuota >= 1` trong khoảng `[TODAY, checkOutDate)` để không lẹm vào số phòng mà khách Online đã đặt cọc giữ chỗ (`RESERVED`).
3. Tìm khách hàng theo `idNumber` trong bảng `customers`; nếu chưa có thì tạo mới `Customer` với **`user = null`**, **`dateOfBirth = null`**, `fullName`, `phone`, `idNumber`[cite: 53].
4. Tính `roomCharge` cho khoảng ngày `[TODAY, checkOutDate)` theo giá ngày thường/cuối tuần[cite: 53].
5. Tạo mới bản ghi `Booking`:
   - `bookingCode` = `BK-` + chuỗi định danh mới.
   - `customer` = khách hàng vừa tìm/tạo.
   - `roomType` = `room.getRoomType()`.
   - **`room` = phòng vật lý vừa chọn** (gán trực tiếp)[cite: 53].
   - `checkInDate = LocalDate.now()`, `checkOutDate = dto.getCheckOutDate()`.
   - `actualCheckIn = LocalDateTime.now()`[cite: 53].
   - `roomCharge = calculatedRoomCharge`.
   - **`depositAmount = 0L`** *(Không thu cọc lúc vào)*[cite: 53].
   - **`status = BookingStatus.CHECKED_IN`**[cite: 53].
   - `createdBy = currentStaff`.
6. Cập nhật `room.setStatus(RoomStatus.OCCUPIED)` và redirect về `/staff/dashboard` kèm thông báo `MSG-25`[cite: 30, 53].

---

## 4. Tiêu chí Hoàn thành `WF-05` & `WF-06` (`UC04`)

- [ ] Sơ đồ phòng trên `/staff/dashboard` hiển thị đúng 4 màu trạng thái (`AVAILABLE`, `OCCUPIED`, `DIRTY`, `MAINTENANCE`) và nhãn đỏ `Quá hạn` cho phòng `P.418` (`BK-2403`)[cite: 21, 53].
- [ ] Bấm nút **[✓ Xác nhận đã dọn sạch]** trên phòng `P.102` (`DIRTY`) chuyển phòng về `AVAILABLE` ngay lập tức[cite: 21, 53].
- [ ] Nhập mã `BK-2402` vào ô Check-in chỉ lọc ra các phòng Deluxe đang `AVAILABLE` (`P.201`, `P.302`); hoàn toàn không hiện phòng `DIRTY` hay `OCCUPIED`[cite: 21, 53].
- [ ] Gán phòng `P.201` cho `BK-2402` chuyển đơn sang `CHECKED_IN` (giữ nguyên cọc `693.000đ`) và chuyển `P.201` sang `OCCUPIED`[cite: 21, 53].
- [ ] Tạo khách Walk-in cho phòng `P.101` lưu thành công với `user_id = NULL` trong `customers`, `deposit_amount = 0` trong `bookings` và chuyển `P.101` sang `OCCUPIED`[cite: 21, 53].