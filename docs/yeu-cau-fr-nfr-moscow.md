# YÊU CẦU CHỨC NĂNG, PHI CHỨC NĂNG VÀ MOSCOW (BẢN NHÁP)

Hệ thống quản lý khách sạn (Đề tài 1, môn 502050). Bản nháp cho **mục 2.4** của quyển báo cáo, bám đúng 7 use case, 8 bảng CSDL và các quy tắc nghiệp vụ trong file `chot-pham-vi-actor-usecase-bang.md`. Hoàng chỉnh lại câu chữ theo văn phong báo cáo và đối chiếu với đặc tả use case của từng bạn.

Đề yêu cầu tối thiểu 10 đến 15 yêu cầu chức năng, có mã số, và bảng ưu tiên MoSCoW. Bản nháp có 17 yêu cầu, Hoàng có thể gộp bớt nếu thấy dài.

---

## 1. Yêu cầu chức năng (FR)

| Mã | Yêu cầu | Use case | Bảng liên quan | MoSCoW |
|---|---|---|---|---|
| FR-01 | Hệ thống cho phép người dùng đăng nhập bằng tên đăng nhập và mật khẩu, và đăng xuất | UC01 | users | Must |
| FR-02 | Hệ thống phân quyền theo vai trò: lễ tân chỉ dùng chức năng nghiệp vụ hằng ngày, quản lý có thêm quản lý phòng và thống kê | UC01 | users | Must |
| FR-03 | Hệ thống cho phép quản lý thêm, sửa, xóa, xem loại phòng (tên, giá một đêm, sức chứa, mô tả) | UC02 | room_types | Must |
| FR-04 | Hệ thống cho phép quản lý thêm, sửa, xóa, xem phòng (số phòng, tầng, loại phòng, trạng thái) | UC02 | rooms, room_types | Must |
| FR-05 | Hệ thống cho phép tìm kiếm, lọc phòng theo số phòng, loại phòng, trạng thái | UC02 | rooms, room_types | Must |
| FR-06 | Hệ thống cho phép tìm phòng trống theo khoảng ngày nhận, trả phòng và loại phòng | UC03 | rooms, bookings | Must |
| FR-07 | Hệ thống cho phép tìm khách hàng theo tên, số điện thoại, số giấy tờ; tạo mới nếu chưa có | UC03 | customers | Must |
| FR-08 | Hệ thống cho phép tạo đặt phòng cho khách và từ chối nếu phòng trùng khoảng ngày với đặt phòng khác | UC03 | bookings, rooms, customers, users | Must |
| FR-09 | Hệ thống cho phép hủy đặt phòng đang ở trạng thái đã đặt | UC03 | bookings | Should |
| FR-10 | Hệ thống cho phép check-in theo đặt phòng, ghi giờ nhận phòng thực tế, chuyển phòng sang đang ở | UC04 | bookings, rooms | Must |
| FR-11 | Hệ thống cho phép thêm dịch vụ (số lượng, đơn giá) cho đặt phòng đang ở trạng thái đã check-in | UC06 | service_orders, services, bookings | Must |
| FR-12 | Hệ thống cho phép quản lý danh mục dịch vụ: thêm, sửa, xóa, xem | UC06 | services | Should |
| FR-13 | Hệ thống tự tính tiền khi check-out: tiền phòng theo số đêm × giá loại phòng, cộng tổng tiền dịch vụ | UC05 | bookings, room_types, service_orders | Must |
| FR-14 | Hệ thống cho phép xác nhận thanh toán (chọn hình thức), lập hóa đơn, ghi giờ trả phòng, chuyển phòng về trống | UC05 | invoices, bookings, rooms, users | Must |
| FR-15 | Hệ thống cho phép in hoặc xuất hóa đơn để đưa cho khách | UC05 | invoices | Could |
| FR-16 | Hệ thống cho phép quản lý xem doanh thu theo tháng và theo loại phòng | UC07 | invoices, bookings, room_types | Should |
| FR-17 | Hệ thống cho phép quản lý xem công suất phòng (tỷ lệ phòng được lấp đầy) theo khoảng ngày | UC07 | rooms, bookings | Could |
| FR-18 | Hệ thống hiển thị trang tổng quan (Dashboard) gồm 4 chỉ số (Phòng trống, Check-in hôm nay, Check-out hôm nay, Dịch vụ đi kèm) và danh sách đặt phòng cần xử lý trong ngày | UC01, UC04, UC05 | rooms, bookings, service_orders | Should |

**Liên hệ với phần cài đặt Java:** 5 chức năng sẽ code (UC01, UC02, UC03, UC04 + UC05, UC06) gồm toàn bộ yêu cầu **Must** (FR-01 đến FR-08, FR-10, FR-11, FR-13, FR-14) và các yêu cầu **Should/Could** hỗ trợ trực tiếp luồng demo (`FR-09`, `FR-12`, `FR-15`, `FR-18`).

---

## 2. Yêu cầu phi chức năng (NFR)

| Mã | Nhóm | Yêu cầu | MoSCoW |
|---|---|---|---|
| NFR-01 | Hiệu năng | Các thao tác thông thường (tìm phòng, đặt phòng, check-in, check-out) phản hồi trong vòng 2 giây với dữ liệu demo vài trăm bản ghi | Should |
| NFR-02 | Bảo mật | Mật khẩu được băm (ví dụ BCrypt), không lưu mật khẩu dạng văn bản thô | Must |
| NFR-03 | Bảo mật | Quyền được kiểm tra ở phía máy chủ; người chưa đăng nhập không truy cập được các trang nghiệp vụ | Must |
| NFR-04 | Toàn vẹn dữ liệu | Có khóa chính, khóa ngoại, ràng buộc; không đặt trùng lịch; không xóa phòng hoặc khách hàng đang có đặt phòng | Must |
| NFR-05 | Tính khả dụng | Giao diện tiếng Việt; các thao tác chính (đặt phòng, check-in, check-out) hoàn thành trong tối đa 3 đến 4 bước; thông báo lỗi dễ hiểu | Should |
| NFR-06 | Khả năng mở rộng | Mã nguồn tách lớp (controller, service, repository); đổi CSDL từ H2 sang MySQL chỉ cần đổi cấu hình; dễ thêm nhiều phòng một đặt phòng, giảm giá, phụ thu sau này | Should |
| NFR-07 | Tương thích | Chạy trên Windows hoặc macOS với JDK 21; truy cập bằng trình duyệt Chrome hoặc Edge bản mới | Must |
| NFR-08 | Độ tin cậy | Dữ liệu được lưu bền, tắt rồi bật lại ứng dụng vẫn còn | Should |
| NFR-09 | Bảo trì | Mã nguồn đặt tên rõ ràng, có chú thích; có README hướng dẫn cài đặt và chạy | Should |

---

## 3. Bảng ưu tiên MoSCoW (tổng hợp)

| Mức | Yêu cầu chức năng | Yêu cầu phi chức năng |
|---|---|---|
| **Must** (bắt buộc có) | FR-01, 02, 03, 04, 05, 06, 07, 08, 10, 11, 13, 14 | NFR-02, 03, 04, 07 |
| **Should** (nên có) | FR-09, 12, 16, 18 | NFR-01, 05, 06, 08, 09 |
| **Could** (có thì tốt) | FR-15, 17 | |
| **Won't** (không làm trong phạm vi này) | Khách tự đặt phòng online; thanh toán trực tuyến; kết nối trang đặt phòng bên ngoài (Booking, Agoda); nhiều chi nhánh; ứng dụng di động; giảm giá, phụ thu, thuế; đặt nhiều phòng trong một đơn | |

---

## 4. Việc Hoàng làm tiếp với file này

1. Đọc và sửa lại câu chữ cho thống nhất văn phong báo cáo.
2. Kiểm tra từng FR có khớp với đặc tả use case của Phong, Kiệt và Hoàng, nhất là FR-08 (chống trùng lịch), FR-13 (cách tính tiền) và FR-14 (thanh toán).
3. Dùng cột Use case và Bảng liên quan làm nền cho **ma trận truy vết** (yêu cầu, use case, bảng CSDL, màn hình, chức năng Java, bước demo).
