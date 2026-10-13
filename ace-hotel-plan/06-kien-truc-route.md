# 06. Kiến trúc và route

Tách lớp controller, service, repository (NFR-06). Toàn bộ mục này là [ĐỀ XUẤT], trừ stack đã chốt ở `00-tong-quan.md`.

## 1. Cấu trúc dự án

```
ace-hotel/
├─ pom.xml
├─ README.md                      (cài JDK 21, cách chạy, tài khoản demo, nạp dữ liệu mẫu, nguồn tham khảo)
├─ src/main/java/com/ace/hotel/
│  ├─ AceHotelApplication.java
│  ├─ config/        SecurityConfig, WebConfig (locale vi-VN), DemoDataLoader
│  ├─ controller/    LandingController, AuthController, DashboardController,
│  │                 RoomController, RoomTypeController, BookingController,
│  │                 CheckInController, CheckOutController, ServiceController, ReportController
│  ├─ service/       UserDetailsServiceImpl, RoomService, RoomTypeService, CustomerService,
│  │                 BookingService, ServiceOrderService, CheckoutService,
│  │                 ServiceCatalogService, DashboardService, ReportService
│  ├─ repository/    một interface cho mỗi bảng (Spring Data JPA)
│  ├─ entity/        User, RoomType, Room, Customer, Booking, ServiceItem, ServiceOrder, Invoice, enum
│  ├─ dto/           form và view model (SearchForm, BookingForm, CheckoutSummary, DashboardView...)
│  └─ exception/     BusinessException, GlobalExceptionHandler
└─ src/main/resources/
   ├─ application.properties
   ├─ schema.sql                  (DDL, xem file 05)
   ├─ sample-data.sql             (nạp lại được nhiều lần)
   ├─ templates/   layout/ (base, sidebar, topbar), landing, auth/login, dashboard,
   │               rooms/, room-types/, bookings/, services/, reports/, error/ (403, 404, 500)
   └─ static/      css/ (tokens.css, base.css, components.css), js/ (theme.js, app.js),
                   fonts/ (Inter), images/ (logo ACE)
```

## 2. Cấu hình chính

- H2 chế độ **file** để dữ liệu còn sau khi tắt bật (NFR-08), `ddl-auto=none`, `schema.sql` có `IF NOT EXISTS`.
- `sample-data.sql` **không** chạy tự động mỗi lần khởi động; `DemoDataLoader` chỉ chạy khi có tham số `--ace.seed=true`, dùng khi cần nạp lại dữ liệu mẫu sạch.
- Thymeleaf: locale `vi-VN`, định dạng ngày `dd/MM/yyyy`, tiền `#,###đ` với dấu chấm ngăn cách nghìn, múi giờ `Asia/Ho_Chi_Minh`.
- Mọi thao tác ghi dùng `@Transactional`; thao tác nhiều bảng (đặt phòng, check-in, check-out) phải thành công hoặc hủy trọn gói.
- Khi tạo đặt phòng, khóa dòng phòng (`SELECT ... FOR UPDATE`) trước khi kiểm tra trùng lịch (P1, để an toàn khi hai lễ tân thao tác cùng lúc).
- `GlobalExceptionHandler`: `BusinessException` → thông báo tiếng Việt qua flash message (thành công / lỗi); trang lỗi 403, 404, 500 thân thiện.

## 3. Bảng route

| Phương thức | Đường dẫn | Quyền | Chức năng | Màn hình |
|---|---|---|---|---|
| GET | `/` | Công khai | Landing page | Landing |
| GET, POST | `/login` | Công khai | Đăng nhập | S01 |
| POST | `/logout` | Đã đăng nhập | Đăng xuất | |
| GET | `/dashboard` | Cả hai | Dashboard | Dashboard |
| GET | `/rooms` | Cả hai | Danh sách phòng (lọc) | S02 |
| GET, POST | `/rooms/new`, `/rooms/{id}/edit` | Quản lý | Thêm, sửa phòng | S03 |
| POST | `/rooms/{id}/delete` | Quản lý | Xóa phòng | S03 |
| GET, POST | `/room-types`, `/room-types/new`, `/room-types/{id}/edit`, `/room-types/{id}/delete` | Quản lý | Loại phòng | S04 |
| GET | `/bookings/search` | Cả hai | Tìm phòng trống | S05 |
| GET | `/customers/search` | Cả hai | Tìm khách (JSON hoặc fragment) | S06 |
| POST | `/customers` | Cả hai | Tạo khách | S06 |
| GET, POST | `/bookings/new`, `/bookings` | Cả hai | Xác nhận, tạo đặt phòng | S07 |
| GET | `/bookings`, `/bookings/{id}` | Cả hai | Danh sách, chi tiết | S08 |
| POST | `/bookings/{id}/cancel` | Cả hai | Hủy (BOOKED) | S08 |
| GET, POST | `/bookings/{id}/check-in` | Cả hai | Check-in | S09 |
| GET, POST | `/bookings/{id}/services` | Cả hai | Thêm, xem dịch vụ | S10 |
| GET, POST | `/bookings/{id}/check-out` | Cả hai | Xem tổng hợp, thanh toán | S11 |
| GET | `/invoices/{id}` | Cả hai | Xem, in hóa đơn | S11 |
| GET, POST | `/services`, `/services/new`, `/services/{id}/edit`, `/services/{id}/delete` | Quản lý | Danh mục dịch vụ | S12 |
| GET | `/reports` | Quản lý | Thống kê | S13 |
| GET | `/public/room-types`, `/public/services` | Công khai | Dữ liệu chỉ đọc cho landing page | Landing |

## 4. Git và README

- Repo GitHub; commit sau mỗi chức năng chạy được (kế hoạch nhóm: commit ngay khi xong từng chức năng). Mời Hoàng, Phong, Kiệt vào repo trước khi bàn giao giữa Tuần 3; khi nộp thì dùng link repo hoặc file nén theo yêu cầu của đề.
- Không đưa mật khẩu thật, khóa hay dữ liệu cá nhân thật vào repo.
- README phải đủ để người khác chạy được trong 5 phút: JDK 21, lệnh chạy, địa chỉ `http://localhost:8080`, tài khoản demo, lệnh nạp dữ liệu mẫu, lỗi thường gặp (cổng bận, sai JDK), danh mục nguồn tham khảo.
