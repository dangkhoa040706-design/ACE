# ĐỀ XUẤT CHỐT: PHẠM VI, ACTOR, USE CASE, BẢNG CSDL

Hệ thống quản lý khách sạn (Đề tài 1, môn 502050). Đây là bản **đề xuất** để cả nhóm chốt ngay đầu Tuần 1. Chốt xong thì mọi người dùng đúng tên này ở mọi phần (doc, sơ đồ, wireframe, code) để không bị lệch.

---

## 1. Phạm vi hệ thống

Hệ thống phục vụ **một khách sạn vừa và nhỏ**, lễ tân thao tác trực tiếp tại quầy.

**Hệ thống LÀM:**
- Quản lý loại phòng và phòng (giá, sức chứa, trạng thái)
- Tìm phòng trống theo khoảng ngày và đặt phòng
- Check-in và check-out
- Dịch vụ đi kèm (ăn uống, giặt ủi, thuê xe...) tính vào hóa đơn
- Thanh toán và xuất hóa đơn
- Đăng nhập, phân quyền lễ tân / quản lý
- Thống kê doanh thu và công suất phòng

**Hệ thống KHÔNG làm:**
- Khách tự đặt phòng qua website hoặc app, thanh toán trực tuyến
- Kết nối các trang đặt phòng bên ngoài (Booking, Agoda...)
- Quản lý nhân sự, lương, kế toán đầy đủ, thuế
- Quản lý kho nhà hàng, bếp
- Nhiều chi nhánh
- Ứng dụng di động

---

## 2. Actor

| Actor | Vai trò | Quyền |
|---|---|---|
| **Lễ tân** | Tác nhân chính, thao tác hằng ngày | Tìm phòng trống, đặt phòng, check-in, check-out, thanh toán, thêm dịch vụ cho phòng đang ở; chỉ xem danh sách phòng |
| **Quản lý** | Tác nhân chính | Mọi quyền của lễ tân + quản lý loại phòng / phòng, xem thống kê |
| **Khách hàng** | Tác nhân ngoài (gián tiếp) | Không thao tác trực tiếp trên hệ thống; cung cấp thông tin, yêu cầu đặt phòng và dịch vụ, thanh toán qua lễ tân |

Buồng phòng và kế toán xuất hiện ở phần khảo sát hiện trạng (mục 2.3) như đối tượng liên quan, **không** là actor của hệ thống.

---

## 3. Bảy use case

| Mã | Tên use case | Actor | Mô tả ngắn | Người phụ trách | Cài đặt Java |
|---|---|---|---|---|---|
| UC01 | Đăng nhập và phân quyền | Lễ tân, Quản lý | Đăng nhập, hệ thống hiển thị chức năng theo vai trò | Kiệt | Có |
| UC02 | Quản lý phòng và loại phòng | Quản lý (Lễ tân chỉ xem) | Thêm / sửa / xóa / tìm kiếm phòng và loại phòng | Hoàng | Có |
| UC03 | Tìm phòng trống và đặt phòng | Lễ tân (Khách hàng) | Chọn khoảng ngày, xem phòng trống, tìm hoặc tạo khách hàng, tạo đặt phòng | Hoàng | Có |
| UC04 | Check-in | Lễ tân (Khách hàng) | Nhận khách theo đặt phòng, ghi giờ nhận phòng, phòng chuyển sang đang ở | Phong | Có |
| UC05 | Check-out và thanh toán hóa đơn | Lễ tân (Khách hàng) | Trả phòng, tính tiền phòng + dịch vụ, thanh toán, in / xuất hóa đơn | Phong | Có |
| UC06 | Đăng ký dịch vụ đi kèm | Lễ tân (Khách hàng) | Thêm dịch vụ cho phòng đang ở, cộng vào hóa đơn | Kiệt | Có |
| UC07 | Thống kê doanh thu và công suất phòng | Quản lý | Báo cáo theo tháng, theo loại phòng, tỷ lệ lấp đầy | Khoa | Chỉ đặc tả (làm thêm nếu còn thời gian) |

**5 chức năng sẽ cài đặt bằng Java:** UC01, UC02, UC03, UC04 + UC05 (gộp thành một chức năng check-in / check-out + thanh toán), UC06. Đề yêu cầu tối thiểu 3 đến 5 chức năng.

**Sequence hoặc Activity Diagram (đề yêu cầu ít nhất 3):** UC03 (Hoàng), UC05 (Phong), UC06 (Kiệt).

**Quan hệ giữa các use case (cho Use Case tổng quát):**
- UC03 include "Tìm phòng trống" và "Tìm hoặc tạo khách hàng"
- UC04 chỉ thực hiện được khi đã có đặt phòng ở UC03
- UC06 chỉ thực hiện được khi phòng đã check-in (UC04)
- UC05 include "Tổng hợp tiền phòng và dịch vụ"
- Mọi use case (trừ UC01) đều yêu cầu đã đăng nhập

---

## 4. Tên các bảng chính (8 bảng)

Tên bảng đặt tiếng Anh dạng snake_case để code Java và CSDL dễ khớp. Mô tả tiếng Việt dùng trong từ điển dữ liệu.

| Bảng | Mô tả | Cột chính | Khóa ngoại |
|---|---|---|---|
| `users` | Tài khoản lễ tân, quản lý | user_id (PK), username, password_hash, full_name, role | |
| `room_types` | Loại phòng | room_type_id (PK), name, price_per_night, capacity, description | |
| `rooms` | Phòng cụ thể | room_id (PK), room_number, floor, status | room_type_id → room_types |
| `customers` | Khách hàng | customer_id (PK), full_name, phone, id_number, email | |
| `bookings` | Đặt phòng (cũng lưu check-in / check-out thực tế) | booking_id (PK), check_in_date, check_out_date, actual_check_in, actual_check_out, status, created_at | customer_id → customers; room_id → rooms; created_by → users |
| `services` | Danh mục dịch vụ | service_id (PK), name, unit_price, unit | |
| `service_orders` | Dịch vụ đã dùng của một đặt phòng | order_id (PK), quantity, unit_price, ordered_at | booking_id → bookings; service_id → services |
| `invoices` | Hóa đơn thanh toán | invoice_id (PK), room_charge, service_charge, total, payment_method, paid_at | booking_id → bookings (1-1); created_by → users |

**Giá trị trạng thái đề xuất:**
- `rooms.status`: AVAILABLE (trống), OCCUPIED (đang ở), MAINTENANCE (bảo trì)
- `bookings.status`: BOOKED (đã đặt), CHECKED_IN (đang ở), CHECKED_OUT (đã trả), CANCELLED (đã hủy)
- `users.role`: RECEPTIONIST, MANAGER

**Quan hệ:**
- `room_types` 1 – N `rooms`
- `customers` 1 – N `bookings`
- `rooms` 1 – N `bookings`
- `bookings` 1 – N `service_orders`
- `services` 1 – N `service_orders`
- `bookings` 1 – 1 `invoices`
- `users` 1 – N `bookings` và `users` 1 – N `invoices`

---

## 5. Quy tắc nghiệp vụ cần thống nhất

1. Một đặt phòng gồm **một phòng** cho một khoảng ngày (đơn giản hóa; "nhiều phòng một đơn" ghi vào hướng phát triển).
2. Một phòng **không được đặt trùng** khoảng ngày với đặt phòng khác chưa hủy.
3. Ngày check-out phải sau ngày check-in ít nhất 1 đêm.
4. Tiền phòng = số đêm × giá một đêm của loại phòng. Số đêm tối thiểu là 1.
5. Chỉ thêm dịch vụ khi đặt phòng đang ở trạng thái CHECKED_IN.
6. Hóa đơn = tiền phòng + tổng tiền dịch vụ. Không có giảm giá, phụ thu, thuế.
7. Chỉ hủy được đặt phòng ở trạng thái BOOKED.

---

## 6. Bốn điểm cả nhóm đã chốt (ngày đầu Tuần 1)

- [x] Khách có tự đặt phòng online không? **Đã chốt: Không** (khách là tác nhân ngoài)
- [x] Một đặt phòng chỉ một phòng? **Đã chốt: Đúng (`BR1`)**
- [x] Có làm phụ thu check-out trễ, giảm giá, thuế? **Đã chốt: Không (`BR6`, `D7`)**
- [x] Dùng tên bảng tiếng Anh như bảng trên? **Đã chốt: Có (8 bảng chuẩn)**

Toàn bộ ERD, Từ điển dữ liệu và Đặc tả chi tiết 7 Use Case đã được hoàn thiện đồng bộ theo đúng văn bản chốt này tại các file `06`, `09`, `10`, `11`, `12`.
