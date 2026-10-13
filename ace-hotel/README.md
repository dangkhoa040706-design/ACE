# ACE Hotel — Hệ thống quản lý khách sạn (demo)

ACE = **A**ccommodation, **C**heck-in & **E**xtras. Môn 502050, Đề tài 1.
Spring Boot + Thymeleaf + H2, chạy bằng **JDK 21**. Kế hoạch chi tiết nằm trong thư mục `ace-hotel-plan`.

## Chạy ứng dụng

Cần JDK 21. Cách dễ nhất là mở thư mục này bằng **IntelliJ IDEA** (đã kèm Maven), mở `AceHotelApplication` và bấm Run.
Hoặc dùng dòng lệnh nếu máy đã cài Maven:

```
mvn spring-boot:run
```

Mở `http://localhost:8080`. Lần chạy đầu, ứng dụng tự tạo bảng (`schema.sql`) và nạp dữ liệu mẫu (`sample-data.sql`).
Dữ liệu lưu ở thư mục `./data` nên tắt bật lại vẫn còn.

**Nạp lại dữ liệu mẫu sạch** (trước buổi demo): chạy với tham số `--ace.seed=true`
(`mvn spring-boot:run -Dspring-boot.run.arguments=--ace.seed=true`, hoặc thêm vào Program arguments trong IntelliJ). Lệnh này xóa hết rồi nạp lại.
Ngày trong dữ liệu mẫu là ngày tương đối so với hôm nay nên luôn khớp ngày demo.

## Tài khoản demo

| Tên đăng nhập | Mật khẩu | Vai trò |
|---|---|---|
| `letan` | `Ace@123` | Lễ tân |
| `quanly` | `Ace@123` | Quản lý |

## Đã làm đến đâu

| Phần | Trạng thái |
|---|---|
| Landing page `/` (giới thiệu, hạng phòng và dịch vụ đọc từ CSDL, không có form đặt phòng) | Xong khung |
| Đăng nhập, đăng xuất, phân quyền lễ tân / quản lý, trang 403 và 404 (UC01) | Xong khung |
| Dashboard: 4 ô KPI, thẻ phòng với trạng thái suy ra, bảng check-in / check-out hôm nay | Xong khung |
| Dark / Light có nút chuyển (bảng màu ACE) | Xong |
| 8 entity + repository khớp 8 bảng | Xong |
| UC02 Quản lý phòng và loại phòng | **Chưa** (đang là trang giữ chỗ) |
| UC03 Tìm phòng trống và đặt phòng | **Chưa** |
| UC04 Check-in, UC05 Check-out và thanh toán | **Chưa** |
| UC06 Dịch vụ đi kèm | **Chưa** |
| UC07 Thống kê (P2) | **Chưa** |

Các màn hình chưa làm vẫn có đường dẫn và phân quyền thật, nên menu và nút trên dashboard bấm được.
Khi viết xong một màn hình, xóa đường dẫn tương ứng trong `PlaceholderController` rồi viết controller thật.

## Cấu trúc

```
src/main/java/com/ace/hotel/
  config/       SecurityConfig, DemoDataLoader, GlobalModelAdvice
  controller/   Landing, Auth, Dashboard, Placeholder (giữ chỗ)
  entity/       User, RoomType, Room, Customer, Booking, ServiceItem, ServiceOrder, Invoice + enum
  repository/   Spring Data JPA, mỗi bảng một interface
  service/      DashboardService
  security/     UserDetailsServiceImpl, AppUserDetails
  dto/, util/
src/main/resources/
  schema.sql, sample-data.sql, application.properties
  templates/    layout (fragments), landing, login, dashboard, placeholder, error/
  static/       css (tokens.css, app.css), js, images (logo ACE), fonts (Inter)
src/test/java/  SecurityAndDashboardTest
```

## Kiểm thử

`mvn test` chạy `SecurityAndDashboardTest` trên H2 trong bộ nhớ: trang công khai, chuyển hướng khi chưa đăng nhập, đăng nhập đúng / sai,
lễ tân bị chặn khỏi trang quản lý, và bốn con số dashboard trên dữ liệu mẫu (phòng trống 6, check-in 3, check-out 1, dịch vụ 2).

## Lưu ý quan trọng về mức độ đã kiểm tra

Khung này được viết và rà soát trong môi trường **không tải được thư viện Maven**, nên **chưa biên dịch và chưa chạy thật trên Spring / H2**.
Đã kiểm tra được: cú pháp Java (`javac` không báo lỗi cú pháp), `schema.sql` và `sample-data.sql` (trên bản chuyển sang SQLite), thẻ HTML cân bằng,
mọi tài nguyên tĩnh có đủ, và giao diện Dark / Light trên máy tính và điện thoại (bằng bản xem trước tĩnh dựng từ template).
**Việc đầu tiên khi mở trên máy bạn:** chạy `mvn test` hoặc Run `AceHotelApplication`, và nếu có lỗi thì gửi lại nguyên văn.
Các chỗ dễ lệch nhất khi chạy lần đầu: cú pháp `DATEADD` và `ALTER TABLE ... RESTART WITH` trong `sample-data.sql`, và biểu thức Thymeleaf ở `dashboard.html`.

## Nguồn tham khảo (khai báo theo yêu cầu của đề)

- Spring Boot 3.5, Spring Security, Spring Data JPA, Thymeleaf, H2 Database (đều mã nguồn mở, lấy qua Maven).
- Font **Inter** (SIL Open Font License 1.1), đặt sẵn trong `static/fonts`.
- Logo ACE và bảng màu ACE Color System do nhóm tự thiết kế. Thông tin liên hệ trên landing page là nội dung mẫu.
