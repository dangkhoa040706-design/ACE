# 09. Demo và kiểm thử

Demo script chính thức do **Phong** viết (Tuần 2 bản nháp, Tuần 3 chốt). Dưới đây là phần Khoa chuẩn bị để script của Phong có chỗ dựa và để phần chạy Java không vấp.

## 1. Đường demo gợi ý [ĐỀ XUẤT]

Tổng thời gian trình bày + demo của cả nhóm là 12 đến 15 phút, nên phần demo nên gọn, khoảng 5 phút. Thứ tự đi theo vòng đời của một lượt lưu trú:

| # | Bước | Use case | Ghi chú |
|---|---|---|---|
| 1 | Mở landing page, chỉ ba trụ cột và luồng đặt phòng qua lễ tân | (phụ) | 30 giây |
| 2 | Đăng nhập `letan`, vào dashboard | UC01 | Chỉ KPI và bảng hôm nay |
| 3 | Tìm phòng trống theo khoảng ngày, chọn khách mới, tạo đặt phòng | UC03 | Thử đặt trùng lịch để thấy bị từ chối |
| 4 | Check-in cho một đặt phòng có sẵn của hôm nay | UC04 | Phòng chuyển "Đang ở" trên dashboard |
| 5 | Thêm dịch vụ (ăn sáng, giặt ủi) | UC06 | Dùng đặt phòng đang ở |
| 6 | Check-out, chọn hình thức thanh toán, xem hóa đơn | UC05 | Đối chiếu tiền phòng + dịch vụ |
| 7 | Đăng xuất, đăng nhập `quanly`, xem / sửa phòng; (thống kê nếu có) | UC02, (UC07) | Cho thấy phân quyền |

Mỗi bước phải đi **được bằng dữ liệu mẫu có sẵn**, không phải gõ nhiều. Các đặt phòng chuẩn bị trước nằm ở `05-csdl.md`, mục 5.

## 2. Kiểm thử theo use case

### UC01
- [ ] Đăng nhập đúng và sai theo hai vai trò; lễ tân vào trang quản lý bị 403 (`02`, mục 6).

### UC02
- [ ] Thêm, sửa, xóa phòng và loại phòng; trùng số phòng bị chặn.
- [ ] Xóa phòng đã có đặt phòng bị chặn, báo tiếng Việt.
- [ ] Lọc phòng theo số phòng, loại, trạng thái.

### UC03
- [ ] Ngày trả không sau ngày nhận ít nhất 1 đêm: báo lỗi.
- [ ] Phòng đã đặt chồng lịch không xuất hiện trong kết quả; phòng bảo trì không xuất hiện.
- [ ] Hai đặt phòng chồng lịch cùng phòng: cái sau bị từ chối.
- [ ] Đặt liền kề (ngày trả = ngày nhận kế tiếp) được chấp nhận (nếu nhóm chốt nửa mở).
- [ ] Tạo khách mới và dùng khách có sẵn đều đặt được.
- [ ] Hủy đặt phòng BOOKED thành công; hủy đặt phòng đang ở bị chặn.

### UC04
- [ ] Check-in đặt phòng BOOKED: trạng thái và phòng đổi đúng, có giờ nhận thực tế.
- [ ] Check-in lần hai, hoặc đặt phòng đã hủy / đã trả: bị chặn.

### UC06
- [ ] Chỉ thêm được cho đặt phòng CHECKED_IN; đặt phòng khác bị chặn.
- [ ] Số lượng 0 hoặc âm bị chặn; đơn giá lấy từ danh mục tại thời điểm thêm.
- [ ] Đổi giá danh mục sau đó không làm đổi dòng dịch vụ cũ.

### UC05
- [ ] Tiền phòng = số đêm × giá; tổng = tiền phòng + dịch vụ; ba con số trùng nhau giữa màn hình, hóa đơn và CSDL.
- [ ] Sau thanh toán: đặt phòng CHECKED_OUT, phòng AVAILABLE, có đúng một hóa đơn.
- [ ] Check-out lần hai bị chặn.
- [ ] Đặt phòng không dịch vụ vẫn ra hóa đơn đúng.

### Dashboard và chung
- [ ] KPI khớp truy vấn tay; thẻ phòng đổi sau mỗi thao tác.
- [ ] Tắt ứng dụng rồi bật lại, dữ liệu còn (NFR-08).
- [ ] Dark và Light đều dùng được; đổi theme không mất dữ liệu đang nhập.
- [ ] Chrome và Edge bản mới đều chạy đúng (NFR-07).
- [ ] Thao tác thông thường phản hồi dưới 2 giây (NFR-01).

## 3. Chuẩn bị máy demo (Tuần 4)

- [ ] JDK 21 cài sẵn; chạy thử từ máy sạch theo README.
- [ ] Cổng 8080 trống; trình duyệt Chrome hoặc Edge.
- [ ] Không phụ thuộc mạng (font, thư viện đã đặt sẵn trong dự án).
- [ ] Nạp lại dữ liệu mẫu sạch ngay trước buổi báo cáo (`--ace.seed=true`).
- [ ] Đăng nhập sẵn các tab cần thiết; tắt thông báo hệ thống; phóng to giao diện cho dễ nhìn.
- [ ] Bản dự phòng: file zip source + video demo trên USB và trên đám mây.
- [ ] Máy phụ (nếu có) cũng đã chạy thử.

## 4. Câu hỏi hỏi đáp phần code và CSDL (gợi ý để tập)

- Vì sao chọn Spring Boot + Thymeleaf + H2? Đổi sang MySQL thì sửa gì?
- Chống đặt trùng lịch ở đâu và bằng cách nào? Hai lễ tân cùng đặt một phòng thì sao?
- Vì sao `service_orders` có cột `unit_price` riêng?
- Vì sao hóa đơn 1 – 1 với đặt phòng? Muốn một đơn nhiều phòng thì sửa bảng nào?
- Phân quyền kiểm tra ở đâu? Ẩn nút có đủ không?
- Mật khẩu lưu thế nào?
- Thêm giảm giá, phụ thu, thuế thì đặt ở đâu trong tính tiền?
- Phần việc của bạn trong nhóm là gì?

## 5. Rủi ro và cách giảm

| Rủi ro | Cách giảm |
|---|---|
| Code trễ so với Tuần 2 | Mốc cắt giảm ở `08`; làm landing và theme Light sau cùng |
| Lỗi khi chạy trên máy khác (sai JDK, cổng bận) | README rõ, chạy thử trên máy sạch trước bàn giao |
| Dữ liệu mẫu lỗi thời ngày | Dùng ngày tương đối; nạp lại trước buổi demo |
| Lệch giữa demo và wireframe trong báo cáo | Đối chiếu từng màn hình đầu Tuần 3; nhờ Kiệt cập nhật 2.7 nếu đổi |
| Ứng dụng lỗi lúc báo cáo | Video demo dự phòng, bản zip dự phòng |
| Mất mạng tại phòng thi | Không phụ thuộc mạng |
| Lỗi tiền hoặc trạng thái sau check-out | Giao dịch trọn gói, kiểm thử ở mục 2 |

## 6. Định nghĩa "xong" cho demo

- [ ] Cả 5 chức năng (UC01, UC02, UC03, UC04 + UC05, UC06) chạy đủ luồng chính trên dữ liệu mẫu, không lỗi nghiêm trọng.
- [ ] Giao diện đúng wireframe và đúng bảng màu.
- [ ] README cho phép người khác chạy được; tài khoản demo hoạt động.
- [ ] Đã quay video dự phòng; đã có bản zip và link repo.
- [ ] Nguồn tham khảo (thư viện, font, ảnh) đã khai báo trong báo cáo.
