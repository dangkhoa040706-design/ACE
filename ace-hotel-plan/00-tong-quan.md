# 00. Tổng quan

## 1. Mục tiêu của demo

Một ứng dụng web Java **chạy thật** trên máy khi báo cáo (đề: "không dùng mockup thay thế"), gồm:

1. Landing page công khai giới thiệu khách sạn, đặt phòng và dịch vụ.
2. Đăng nhập, phân quyền lễ tân / quản lý.
3. Dashboard và 5 chức năng nghiệp vụ đã chốt (UC01, UC02, UC03, UC04 + UC05, UC06).
4. Dữ liệu mẫu hợp lý, nạp lại sạch được trước buổi demo.

Giao diện và luồng nghiệp vụ phải **đúng với thiết kế trong quyển báo cáo** (đề mục 3.1) và truy vết được từ yêu cầu đến thiết kế đến demo.

## 2. Phạm vi [CHỐT]

**Hệ thống LÀM:** quản lý loại phòng và phòng; tìm phòng trống theo khoảng ngày và đặt phòng; check-in, check-out; dịch vụ đi kèm tính vào hóa đơn; thanh toán và xuất hóa đơn; đăng nhập và phân quyền; thống kê doanh thu và công suất phòng.

**Hệ thống KHÔNG làm:** khách tự đặt phòng hoặc thanh toán online; kết nối Booking, Agoda; nhân sự, lương, kế toán đầy đủ, thuế; kho nhà hàng và bếp; nhiều chi nhánh; ứng dụng di động. Cũng không có giảm giá, phụ thu, thuế, và một đặt phòng chỉ có một phòng.

**Landing page** nằm trong phạm vi demo nhưng **chỉ để giới thiệu**; nó không phải một use case và không thêm actor mới. [CHỐT theo quyết định ở README]

## 3. Actor [CHỐT]

| Actor | Vai trò | Ghi chú |
|---|---|---|
| Lễ tân | Chính, dùng hằng ngày | Tìm phòng trống, đặt phòng, check-in, check-out, thanh toán, thêm dịch vụ; chỉ xem danh sách phòng |
| Quản lý | Chính | Mọi quyền của lễ tân + quản lý loại phòng / phòng, xem thống kê |
| Khách hàng | Ngoài, gián tiếp | Không thao tác trực tiếp; làm việc qua lễ tân. Người xem landing page không đăng nhập |

## 4. Bảy use case [CHỐT]

| Mã | Tên | Người phụ trách đặc tả | Cài đặt Java |
|---|---|---|---|
| UC01 | Đăng nhập và phân quyền | Kiệt | Có |
| UC02 | Quản lý phòng và loại phòng | Hoàng | Có |
| UC03 | Tìm phòng trống và đặt phòng | Hoàng | Có |
| UC04 | Check-in | Phong | Có (gộp chức năng với UC05) |
| UC05 | Check-out và thanh toán hóa đơn | Phong | Có |
| UC06 | Đăng ký dịch vụ đi kèm | Kiệt | Có |
| UC07 | Thống kê doanh thu và công suất phòng | Khoa | Chỉ đặc tả; làm thêm nếu còn thời gian |

Sequence hoặc Activity Diagram: UC03 (Hoàng), UC05 (Phong), UC06 (Kiệt).

## 5. Stack và công cụ

| Hạng mục | Lựa chọn | Nguồn |
|---|---|---|
| Ngôn ngữ, JDK | Java, **JDK 21** | [CHỐT] (NFR-07) |
| Framework | **Spring Boot** (bản ổn định hỗ trợ JDK 21) + **Thymeleaf** | [CHỐT] theo kế hoạch nhóm |
| CSDL | **H2** (đổi sang MySQL chỉ cần đổi cấu hình, NFR-06) | [CHỐT] |
| IDE | IntelliJ | [CHỐT] theo kế hoạch nhóm |
| Truy cập dữ liệu | Spring Data JPA, `schema.sql` là nguồn sự thật, `ddl-auto=none` | [ĐỀ XUẤT] |
| Bảo mật | Spring Security (form login), BCrypt | [ĐỀ XUẤT] cho NFR-02, NFR-03 |
| Build | Maven | [ĐỀ XUẤT] |
| Giao diện | HTML Thymeleaf + CSS thuần dùng biến màu + ít JS; font Inter đặt sẵn trong dự án (không gọi CDN, vì máy demo có thể không có mạng) | [ĐỀ XUẤT] |
| Chạy | `mvn spring-boot:run` hoặc nút Run trong IntelliJ; H2 ở chế độ file để tắt bật vẫn còn dữ liệu (NFR-08) | [ĐỀ XUẤT] |

## 6. Ràng buộc từ đề cần luôn nhớ

- Cài đặt và demo **trực tiếp bằng Java**, tối thiểu 3 đến 5 chức năng chính đã đặc tả; chạy được thực tế.
- Có **demo script** theo đúng use case đã chọn, có dữ liệu mẫu hợp lý cho cả giao diện và phần chạy Java.
- Nộp mã nguồn (zip/rar hoặc link GitHub/GitLab) cùng quyển báo cáo; nộp kèm slide và (nếu có) link/file prototype giao diện trước hoặc ngay sau buổi báo cáo.
- **Khai báo nguồn tham khảo** nếu dùng tài liệu, mẫu thiết kế, hình ảnh, thư viện hoặc framework bên ngoài: gồm Spring, Thymeleaf, font Inter, ảnh trên landing page.
- Buổi báo cáo: 12 đến 15 phút trình bày và demo, 5 đến 8 phút hỏi đáp; mọi thành viên đều trình bày một phần và có mặt trả lời.
- Rubric liên quan đến demo: giao diện / prototype 10%, demo Java 10% (Xuất sắc khi chạy được từ 80% chức năng chính, không lỗi nghiêm trọng, code rõ ràng).

## 7. Bản đồ truy vết (rút gọn)

| UC | Màn hình (file 04) | Route chính (file 06) | Bảng | FR (Must trước) |
|---|---|---|---|---|
| UC01 | S01 Đăng nhập | `/login`, `/logout` | users | FR-01, FR-02 |
| UC02 | S02 đến S04 | `/rooms`, `/room-types` | rooms, room_types | FR-03, 04, 05 |
| UC03 | S05 đến S08 | `/bookings/search`, `/bookings` | bookings, rooms, customers | FR-06, 07, 08, (09) |
| UC04 | S09 | `/bookings/{id}/check-in` | bookings, rooms | FR-10 |
| UC05 | S11 | `/bookings/{id}/check-out` | invoices, bookings, rooms, service_orders | FR-13, 14, (15) |
| UC06 | S10, (S12) | `/bookings/{id}/services` | service_orders, services | FR-11, (12) |
| UC07 | S13 | `/reports` | invoices, bookings, room_types | (FR-16, 17) |
| (phụ) | Landing, Dashboard | `/`, `/dashboard` | room_types, services (đọc) | Không thuộc UC |

Ma trận truy vết đầy đủ (yêu cầu, use case, bảng, màn hình, chức năng Java, bước demo) do Hoàng làm trong báo cáo; bản này là nền để đối chiếu.
