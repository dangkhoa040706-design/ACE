# 05. CONTENT BRIEF – LANDING PAGE / TRANG CHỦ GIỚI THIỆU

> **Đã chốt:** Phạm vi nhóm không có website cho khách tự đặt phòng online. Vì vậy landing page ở đây là **trang giới thiệu hệ thống nội bộ** (route `/`, màn hình `S01`) đặt trước màn đăng nhập, dùng để mở đầu phần demo, chuyển đổi Light/Dark mode và minh họa luồng điều hướng.

## 1. Mục đích và người xem
- Giới thiệu nhanh hệ thống ACE trong 5 giây, dẫn người dùng đến **Đăng nhập (`/login`)**.
- Người xem: nhân viên khách sạn (lễ tân, quản lý), và hội đồng chấm khi demo.

## 2. Cấu trúc các section
| # | Section | Nội dung | Ghi chú |
|---|---|---|---|
| 1 | Header | Logo ACE (ô vàng #FDBF2D, chữ ACE), menu: Tính năng · Quy trình · [Light/Dark] · Đăng nhập | Nút Đăng nhập dùng Primary |
| 2 | Hero | Tên **ACE – Accommodation, Check-in & Extras**; câu mô tả "Hệ thống quản lý khách sạn cho lễ tân và quản lý"; CTA **[Đăng nhập]** | Slogan đã chốt bên dưới |
| 3 | Tính năng | 6 thẻ: Quản lý phòng · Tìm phòng trống và đặt phòng · Check-in/Check-out · Dịch vụ đi kèm · Thanh toán và hóa đơn · Thống kê doanh thu | Mỗi thẻ: icon, tiêu đề, 1 câu |
| 4 | Quy trình | 4 bước: Đặt phòng → Check-in → Dịch vụ → Check-out và thanh toán | Dạng timeline ngang |
| 5 | Vai trò | Lễ tân (nghiệp vụ hằng ngày) và Quản lý (thêm quản lý phòng, thống kê) | 2 cột |
| 6 | CTA cuối | "Sẵn sàng làm việc?" + [Đăng nhập] | |
| 7 | Footer | Tên môn, trường, nhóm, năm 2026 | Không đưa thông tin liên hệ giả |

## 3. Nội dung chính thức (đã chốt)
- **Slogan chính thức:** *"Từ đặt phòng đến trả phòng, gọn trong một màn hình"* (Phụ đề: *"Quầy lễ tân số hóa, chính xác từng đêm phòng"*).
- Mô tả tính năng:
  - Quản lý phòng: "Theo dõi loại phòng, giá và trạng thái từng phòng."
  - Đặt phòng: "Chọn ngày, thấy ngay phòng trống, không lo trùng lịch."
  - Check-in/out: "Nhận và trả phòng chỉ vài thao tác."
  - Dịch vụ: "Ghi dịch vụ đi kèm, tự cộng vào hóa đơn."
  - Thanh toán: "Tính tiền phòng và dịch vụ tự động."
  - Thống kê: "Xem doanh thu và công suất theo tháng." (ghi chú: chỉ đặc tả, nếu không làm thì ẩn thẻ này khi demo)

## 4. Tài sản cần chuẩn bị
| Tài sản | Người làm | Tình trạng |
|---|---|---|
| Logo ACE (SVG/PNG) | Kiệt | Đã có dạng ô vàng trong bảng màu, cần xuất file |
| Icon 6 tính năng | Kiệt/Khoa | Chưa có (dùng bộ icon mã nguồn mở, khai báo nguồn) |
| Ảnh hero | Kiệt | Chưa có (ảnh bản quyền: dùng ảnh tự chụp hoặc nguồn miễn phí, khai báo nguồn) |
| Light/Dark mode | Khoa | Bảng màu đã có (xem tài liệu 04, mục 5) |

## 5. Yêu cầu giao diện
- Tiếng Việt toàn bộ; responsive cơ bản cho màn hình laptop (Chrome/Edge).
- Hỗ trợ light và dark theo bảng màu ACE; chữ Primary text dùng #B45309 trên nền sáng để đủ tương phản.
- CTA chính chỉ một nút Primary mỗi vùng nhìn.
