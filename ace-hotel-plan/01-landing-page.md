# 01. Landing page

Route: `GET /` (công khai, không cần đăng nhập). Ưu tiên chung: **P1**, timebox nửa ngày đến một ngày, làm **sau** khi 5 chức năng chạy được (xem `08-ke-hoach-theo-tuan.md`).

## 1. Mục tiêu

- Giới thiệu khách sạn ACE và ý nghĩa tên: **Accommodation** (lưu trú), **Check-in** (nhận phòng), **Extras** (dịch vụ đi kèm).
- Giới thiệu cách đặt phòng và các dịch vụ; dẫn nhân viên vào đăng nhập.
- **Không** có form đặt phòng, không thanh toán, không tài khoản khách. Mọi nút "đặt phòng" dẫn tới thông tin liên hệ.

## 2. Các khối của trang

| # | Khối | Nội dung | Nguồn dữ liệu | Ưu tiên |
|---|---|---|---|---|
| 1 | Header cố định | Logo ACE, menu neo (Giới thiệu, Phòng, Đặt phòng, Dịch vụ, Liên hệ), nút đổi theme, nút **Đăng nhập** | Tĩnh | P0 |
| 2 | Hero | Khẩu hiệu ngắn, một câu mô tả, nút chính "Liên hệ đặt phòng" (neo xuống Liên hệ), nút phụ "Đăng nhập nhân viên" | Tĩnh | P0 |
| 3 | Giới thiệu | Ba trụ cột Lưu trú / Check-in / Dịch vụ đi kèm, mỗi trụ một đoạn ngắn | Tĩnh | P0 |
| 4 | Loại phòng | Thẻ cho từng loại: tên, sức chứa, giá một đêm, mô tả | Bảng `room_types` (chỉ đọc); nếu trống thì hiện nội dung mẫu | P0 |
| 5 | Đặt phòng | Ba bước: liên hệ lễ tân; lễ tân kiểm tra phòng trống và tạo đặt phòng; nhận phòng tại quầy | Tĩnh | P0 |
| 6 | Dịch vụ đi kèm | Danh sách dịch vụ (ăn uống, giặt ủi, thuê xe...) kèm đơn giá | Bảng `services` (chỉ đọc) | P1 |
| 7 | Dành cho nhân viên | Bốn ô tóm tắt chức năng: Đặt phòng, Check-in / Check-out, Dịch vụ, Thống kê (dành cho quản lý); nút "Đăng nhập để sử dụng" | Tĩnh | P1 |
| 8 | Liên hệ | Địa chỉ, điện thoại, email, giờ làm việc của lễ tân. **Dùng nội dung mẫu và ghi rõ là mẫu cho demo** | Tĩnh | P0 |
| 9 | Chân trang | `ACE · Accommodation, Check-in & Extras`, môn 502050, năm 2026 | Tĩnh | P1 |

Khối 7 trả lời yêu cầu "giới thiệu các chức năng booking, service" mà không mở chức năng cho khách.

## 3. Hành vi

- Chưa đăng nhập: nút góc phải là **Đăng nhập** → `/login`.
- Đã đăng nhập: nút đổi thành **Vào dashboard** → `/dashboard`.
- Menu neo cuộn mượt; trên điện thoại menu thu gọn thành nút ba gạch.
- Nút đổi theme dùng chung cơ chế với dashboard (xem `07-giao-dien-design-system.md`); mặc định Dark.
- Dữ liệu động (khối 4 và 6) đọc qua endpoint chỉ đọc, **chỉ trả các cột công khai** (tên, giá, sức chứa, mô tả; tên, đơn giá, đơn vị). Không lộ khách hàng, đặt phòng, tài khoản.

## 4. Nội dung và hình ảnh

- Viết bằng tiếng Việt, giọng chuyên nghiệp, câu ngắn. Số liệu liên hệ chỉ là mẫu.
- Hình ảnh: ưu tiên ảnh tự chụp, ảnh tự vẽ hoặc nền gradient vàng-đen theo bảng màu. Nếu dùng ảnh hoặc icon bên ngoài thì **ghi vào danh mục tài liệu tham khảo** (đề bắt buộc khai báo).
- Logo: dùng `assets/1_logo-day-du_khong-nen.png` (hoặc `2_logo-ACE_khong-nen.png` cho header nhỏ). Màu logo là vàng champagne, nên trên theme Light cần nền tối cho header hoặc bản logo đậm hơn (xem file 07).

## 5. Tiêu chí xong

- [ ] Mở `/` không cần đăng nhập, không lỗi khi CSDL trống (hiện nội dung mẫu).
- [ ] Không có form hay nút nào tạo đặt phòng hoặc thanh toán.
- [ ] Nút Đăng nhập / Vào dashboard đúng theo trạng thái.
- [ ] Hiển thị đúng trên màn hình 360 px, 768 px và 1280 px; không cuộn ngang.
- [ ] Dark và Light đều đọc rõ, tương phản chữ đạt mức đọc được.
- [ ] Ảnh và font bên ngoài (nếu có) đã ghi vào tài liệu tham khảo.
- [ ] Tải trang dưới 2 giây trên máy demo.

## 6. Liên quan đến báo cáo

Landing page và dashboard **không có trong danh sách wireframe hiện tại của Kiệt** (đăng nhập, quản lý phòng, tìm phòng và đặt phòng, check-in, check-out và thanh toán, dịch vụ, thống kê). Đề yêu cầu demo phản ánh đúng thiết kế trong báo cáo, nên cần nhờ Kiệt đưa hai màn hình này vào mục 2.7 (xem `10-cau-hoi-mo.md`, mục 8).
