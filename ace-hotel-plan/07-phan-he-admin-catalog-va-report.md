# 07. Phân hệ Admin Backoffice: Quản trị Danh mục, Biểu giá & Báo cáo Doanh thu `++` (`WF-09`, `WF-10` — `UC02` & `UC07`)

> **Mục tiêu:** Đặc tả chi tiết cách cài đặt **Màn hình Quản trị Danh mục & Biểu giá (`WF-09` – `/admin/catalog` – `UC02`)** và **Dashboard Quản trị viên – Báo cáo Doanh thu Đa chiều `++` & Công suất `Occupancy Rate` (`WF-10` – `/admin/dashboard` – `UC07` ⭐ Trọng tâm 4)**[cite: 53].

---

## 1. Màn hình `WF-09` — Quản trị Danh mục Phòng, Biểu giá Động & 4 Nhóm Dịch vụ (`UC02`)

### 1.1. Phân quyền Truy cập (`GET /admin/catalog` vs `POST /admin/**`)
- **Với `ADMIN`:** Hiển thị đầy đủ form Thêm/Sửa và các nút `[Sửa]`, `[Bảo trì / Mở lại]`, `[Xóa]` trên cả 3 tab[cite: 6, 18, 53].
- **Với `RECEPTIONIST`:** Cho phép mở `GET /admin/catalog` ở chế độ **Chỉ xem (`Read-only`)** để tra cứu bảng giá ngày thường/cuối tuần và đơn giá 4 nhóm dịch vụ; ẩn toàn bộ form và nút thao tác bằng `sec:authorize="hasRole('ADMIN')"` và chặn mọi request `POST /admin/**` ở `SecurityConfig` (`403 Forbidden`)[cite: 6, 30, 53].

---

### 1.2. Tab 1: Quản lý Hạng phòng & Biểu giá Ngày thường / Cuối tuần (`room_types`)
- **Các trường dữ liệu (`RoomType`):**
  - `name`: Tên hạng phòng (`Standard`, `Superior`, `Deluxe`, `Family`, `Suite` – bắt buộc, duy nhất)[cite: 8, 9, 21, 53].
  - `capacity`: Sức chứa tối đa (`>= 1` khách)[cite: 8, 9, 53].
  - `pricePerNight`: **Đơn giá 1 đêm Ngày thường (Thứ 2 đến Thứ 5)** (`> 0` VNĐ)[cite: 53].
  - `weekendPricePerNight`: **Đơn giá 1 đêm Cuối tuần & Ngày lễ (Thứ 6, Thứ 7, Chủ Nhật)** (`> 0` VNĐ)[cite: 53].
  - `description`: Mô tả tiện nghi đặc trưng[cite: 8, 9, 53].
- **Xử lý Xóa Hạng phòng (`POST /admin/room-types/{id}/delete` – `BR7`):**
  - Kiểm tra `roomRepository.existsByRoomType_RoomTypeId(id)` hoặc `bookingRepository.existsByRoomType_RoomTypeId(id)`[cite: 8, 30].
  - Nếu đã có phòng vật lý hoặc đơn đặt tham chiếu $\rightarrow$ chặn xóa và báo lỗi `MSG-14`: *"Không thể xóa hạng phòng này vì đang có phòng vật lý hoặc đơn đặt phòng tham chiếu."*[cite: 8, 30].

---

### 1.3. Tab 2: Quản lý Phòng vật lý & Bật/Tắt Bảo trì (`rooms`)
- **Các trường dữ liệu (`Room`):**
  - `roomNumber`: Số phòng (`101`, `201`, `301`... – bắt buộc, duy nhất)[cite: 8, 9, 21, 53].
  - `floor`: Tầng lầu (`>= 1`)[cite: 53].
  - `roomTypeId`: Chọn hạng phòng từ `room_types`[cite: 8, 9, 53].
  - `status` (khi tạo mới): Chỉ cho chọn `AVAILABLE` hoặc `MAINTENANCE` (trạng thái `OCCUPIED` và `DIRTY` do luồng Check-in / Check-out tự động gán)[cite: 30, 53].
- **Xử lý Bật / Tắt Bảo trì (`POST /admin/rooms/{id}/toggle-maintenance`):**
  - Nếu `room.getStatus() == RoomStatus.OCCUPIED` $\rightarrow$ chặn thao tác và báo lỗi `MSG-16` (*"Không thể chuyển sang trạng thái Bảo trì vì phòng đang có khách lưu trú."*)[cite: 27, 30].
  - Nếu `room.getStatus() == RoomStatus.AVAILABLE` (hoặc `DIRTY`) $\rightarrow$ chuyển sang `RoomStatus.MAINTENANCE`[cite: 27, 53].
  - Nếu `room.getStatus() == RoomStatus.MAINTENANCE` $\rightarrow$ chuyển về `RoomStatus.AVAILABLE`[cite: 27, 53].
- **Xử lý Xóa Phòng vật lý (`POST /admin/rooms/{id}/delete` – `BR7`):**
  - Kiểm tra `bookingRepository.existsByRoom_RoomId(id)`[cite: 22, 30].
  - Nếu phòng đã từng có lịch sử đặt phòng (ví dụ `P.301`, `P.401`) $\rightarrow$ chặn xóa và báo lỗi `MSG-15` (*"Không thể xóa phòng đã có lịch sử đặt phòng. Vui lòng chuyển phòng sang trạng thái Bảo trì nếu ngưng khai thác."*)[cite: 8, 21, 30].

---

### 1.4. Tab 3: Quản lý Danh mục 4 Nhóm Dịch vụ (`services`)
- **Các trường dữ liệu (`ServiceItem`):**
  - `name`: Tên dịch vụ / mặt hàng (bắt buộc, duy nhất)[cite: 8, 9, 53].
  - `category`: Chọn 1 trong 4 nhóm chuẩn: `MINIBAR` (Đồ uống/Snack), `LAUNDRY` (Giặt ủi), `FB` (Ẩm thực), `DAMAGES` (Đền bù tài sản)[cite: 53].
  - `unitPrice`: Đơn giá niêm yết (`>= 0` VNĐ)[cite: 8, 9, 53].
  - `unit`: Đơn vị tính (`Lon`, `Gói`, `Bộ`, `Chiếc`, `Suất`, `Phần`, `Cái`, `Thẻ`)[cite: 8, 9, 21, 53].
- **Xử lý Xóa Dịch vụ (`POST /admin/services/{id}/delete` – `BR7`):**
  - Kiểm tra `serviceOrderRepository.existsByService_ServiceId(id)`[cite: 8, 29, 30].
  - Nếu đã từng được gọi trong Room Folio $\rightarrow$ chặn xóa và báo lỗi `MSG-17`[cite: 29, 30]. (Trong dữ liệu mẫu, dịch vụ số 8 *"Đền bù mất thẻ từ chìa khóa phòng"* chưa có đơn gọi nào, dùng để Demo xóa thành công)[cite: 21].

---

## 2. Màn hình `WF-10` — Dashboard Admin: Báo cáo Doanh thu `++` & Công suất `Occupancy Rate` (`UC07` – ⭐ Trọng tâm 4)

### 2.1. Công thức Toán học Đo lường Công suất Phòng Chuẩn Quốc tế (`ReportService.java`)
1. **Tổng số phòng có thể kinh doanh (`sellableRooms` — Loại trừ phòng `MAINTENANCE`):**
   - Theo quy tắc chuẩn khách sạn quốc tế, phòng đang đóng cửa sửa chữa bảo trì (`MAINTENANCE` — ví dụ `P.103`) không đủ điều kiện kỹ thuật để bán nên **bắt buộc loại trừ khỏi mẫu số**[cite: 21, 53]:
     $$R_{\text{sellable}} = \text{COUNT}(\text{rooms WHERE status} \in \{\text{'AVAILABLE'}, \text{'OCCUPIED'}, \text{'DIRTY'}\})$$
2. **Tỷ lệ lấp đầy phòng hiện thời (`currentOccupancyRate`):**
   $$\text{Occupancy Rate (\%)} = \frac{\text{Số phòng đang OCCUPIED}}{R_{\text{sellable}}} \times 100\%$$
   *(Ví dụ trên bộ dữ liệu mẫu: Có 12 phòng tổng cộng, trừ 1 phòng `P.103` đang `MAINTENANCE` còn $R_{\text{sellable}} = 11$ phòng. Khi có 4 phòng `OCCUPIED` sau bước Demo Check-in, `Occupancy Rate = 4 / 11 = 36.4%`)*[cite: 21, 53].
3. **Giá bán bình quân mỗi đêm phòng (`ADR` – Average Daily Rate):**
   $$\text{ADR} = \frac{\text{Tổng doanh thu tiền phòng gốc (`SUM(invoices.room_charge)`)}}{\text{Tổng số đêm phòng đã thanh toán}}$$
4. **Doanh thu trên mỗi phòng sẵn có (`RevPAR` – Revenue Per Available Room):**
   $$\text{RevPAR} = \frac{\text{Tổng doanh thu tiền phòng gốc (`SUM(invoices.room_charge)`)}}{R_{\text{sellable}} \times \text{Số ngày trong kỳ}} \quad (\text{hoặc } \text{ADR} \times \text{Occupancy Rate}) \quad[cite: 53]$$

---

### 2.2. Bảng Báo cáo Cơ cấu Doanh thu Đa chiều (`++`) theo Kỳ Lọc `[fromDate, toDate]`
Truy vấn từ bảng `invoices` (theo `paid_at`) và `service_orders` để bóc tách 6 dòng doanh thu minh bạch trên `WF-10`[cite: 53]:
1. **Doanh thu Tiền phòng gốc (`totalRoomRevenue`):** `SUM(invoices.room_charge)`[cite: 53].
2. **Doanh thu Dịch vụ theo 4 Nhóm (`revenueByCategory`):**
   - Nhóm `MINIBAR` (Đồ uống & Snack)[cite: 53].
   - Nhóm `LAUNDRY` (Giặt ủi)[cite: 53].
   - Nhóm `FB` (Ẩm thực & Bữa sáng)[cite: 53].
   - Nhóm `DAMAGES` (Đền bù tài sản)[cite: 53].
3. **Tổng tiền Phụ thu Nhận sớm & Trả trễ (`totalSurchargeRevenue`):** `SUM(invoices.early_checkin_fee + invoices.late_checkout_fee)`[cite: 53].
4. **Tổng Phí phục vụ (`totalSvcRevenue` – 5%):** `SUM(invoices.service_charge_amount)`[cite: 53].
5. **Tổng Thuế GTGT thu hộ ngân sách (`totalVatAmount` – 10%):** `SUM(invoices.vat_amount)`[cite: 53].
6. **TỔNG CỘNG DOANH THU HÓA ĐƠN (`totalGrandRevenue ++`):** `SUM(invoices.grand_total)` (kèm cột tổng tiền đã thu cọc `SUM(deposit_deducted)` và thực thu tại quầy `SUM(balance_due)`)[cite: 53].

---

## 3. Tiêu chí Hoàn thành `WF-09` & `WF-10` (`UC02` & `UC07`)

- [ ] Admin thêm/sửa hạng phòng có đủ 2 mức giá `pricePerNight` (T2–T5) và `weekendPricePerNight` (T6–CN)[cite: 53].
- [ ] Admin thử xóa phòng `P.301` hoặc hạng phòng `Standard` bị chặn xóa và hiện đúng thông báo `MSG-15` / `MSG-14`[cite: 21, 30].
- [ ] Admin xóa thử dịch vụ số 8 (*"Đền bù mất thẻ từ"*, chưa có đơn gọi) xóa thành công (`MSG-11`); xóa dịch vụ số 5 (*"Bữa sáng Buffet"*, đã có đơn) bị chặn (`MSG-17`)[cite: 21, 30].
- [ ] Dashboard Admin (`/admin/dashboard` – `WF-10`) tính đúng $R_{\text{sellable}} = 11$ (loại trừ phòng `103` đang `MAINTENANCE`), hiển thị đúng `Occupancy Rate (%)`, `ADR`, `RevPAR` và bảng cơ cấu doanh thu `++`[cite: 21, 53].