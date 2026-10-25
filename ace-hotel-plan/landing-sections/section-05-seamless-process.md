# Đặc Tả Kiến Trúc & Thiết Kế: Section 5 — Sticky Stacking Cards (Quy Trình 3 Bước Tinh Gọn)

## 1. Bối Cảnh & Mục Tiêu (Context & Objectives)
Section 5 (`#quy-trinh`) là điểm chốt chặn quan trọng nhất của trang chủ, dẫn dắt khách hàng từ sự thấu hiểu thương hiệu (Section 4) bước vào hành động đặt phòng thực tế (Conversion CTA).

Thay vì hiển thị 3 cột ngang truyền thống bằng phẳng và rời rạc, Section 5 được tái thiết kế theo phong cách **Sticky Stacking Cards (Lưới Thẻ Xếp Chồng 3 Tầng Chiều Sâu 3D)**:
- Từng thẻ xuất hiện và neo đứng yên (`position: sticky`) khi người dùng cuộn chuột.
- Thẻ sau trượt lên đè chồng lên thẻ trước, chừa lại chính xác **64px mép trên** (Header chứa Số bước + Tên bước + Tag).
- Khi hoàn tất 3 bước, khối tạo thành một xấp 3 ngăn thẻ bài VIP mạ vàng kim sắc sảo, tích hợp nút bấm Call-to-Action chuyển thẳng sang hệ thống đặt phòng.

---

## 2. Quy Chuẩn Kỹ Thuật "4 Điểm Vàng"

### 2.1. Vùng Header 64px Cố Định & Tọa Độ Neo Sticky Lệch Nhau
Mỗi thẻ được quy hoạch thanh Header cao cố định `64px`:
- Trái: Số thứ tự `01` / `02` / `03` màu Vàng Gold (`#FDBF2D`) + Dấu chấm phân cách `•` + Tên bước in đậm.
- Phải: Tag định danh `[ BƯỚC 01/03 ]`, `[ BƯỚC 02/03 ]`, `[ BƯỚC 03/03 ]`.

Tọa độ neo `position: sticky` (cách top viewport):
- **Thẻ 1:** `top: 110px;` (cách thanh navbar ~50px).
- **Thẻ 2:** `top: 174px;` (110px + 64px).
- **Thẻ 3:** `top: 238px;` (174px + 64px).

> **Kết quả:** Khi 3 thẻ xếp chồng, 2 khúc mép trên chừa lại có chiều cao chính xác 64px, không bao giờ bị cắt phạm vào nội dung mô tả bên dưới.

### 2.2. Hiệu Ứng Chiều Sâu 3D (Depth Scale & Dimming)
Để các lớp thẻ có sự tách khối 3 chiều thay vì bằng phẳng:
- Khi Thẻ 2 trượt lên đè Thẻ 1: Thẻ 1 tự động thu nhỏ `transform: scale(0.96)` và giảm độ sáng `filter: brightness(0.72)`.
- Khi Thẻ 3 trượt lên đè Thẻ 2: Thẻ 2 tự động thu nhỏ `transform: scale(0.98)` và giảm độ sáng `filter: brightness(0.85)`.
- Thẻ 3 giữ `scale(1.0)` với bóng đổ khối hướng lên trên `box-shadow: 0 -16px 40px rgba(0, 0, 0, 0.75)` cùng đường viền đỉnh Vàng Gold `#FDBF2D`.

### 2.3. Tận Dụng Ảnh Sẵn Có & Lớp Phủ Kính Obsidian
- **Thẻ 1:** `/images/room-suite.jpg` (Buồng phòng sang trọng).
- **Thẻ 2:** `/images/hero/anh1.png` (Quầy lễ tân với màn hình mã QR phát sáng vàng kim).
- **Thẻ 3:** `/images/hero/anh2.png` (Trải nghiệm nhận phòng thượng hạng).
- Phủ lớp kính Obsidian Gradient tối từ trên xuống và từ phải sang trái (`linear-gradient(90deg, rgba(8, 10, 15, 0.94) 0%, rgba(8, 10, 15, 0.78) 55%, rgba(8, 10, 15, 0.45) 100%)`) đảm bảo chữ Trắng & Vàng Gold nổi bật hoàn hảo ở cả Dark và Light mode.

### 2.4. Nút Kêu Gọi Hành Động (CTA Conversion)
Chân Thẻ 3 trang bị nút bấm lớn:
`[ ★ Bắt Đầu Đặt Phòng Ngay Hôm Nay ➔ ]`
Liên kết trực tiếp tới `/booking/search`.

---

## 3. Cấu Trúc Nội Dung 3 Bước

### Bước 01: Chọn Hạng Phòng & Kiểm Tra Quota Thực Tế
- **Tiêu đề phụ:** Minh Bạch Quota Khả Dụng & Biểu Giá Động T2–CN
- **Mô tả:** Tra cứu thời gian thực quỹ phòng kinh doanh thực tế (loại trừ 100% phòng bảo trì MAINTENANCE). Biểu giá tự động điều chỉnh linh hoạt theo ngày thường và cuối tuần với cam kết không phụ phí ẩn.
- **Trụ cột:**
  - `5 Hạng Phòng Độc Bản:` Standard, Superior, Deluxe, Suite, Family.
  - `Quota Engine:` Cập nhật theo thời gian thực, loại trừ phòng bảo trì.
  - `Biểu Giá Rõ Ràng:` Niêm yết công khai và tự động theo ngày lưu trú.

### Bước 02: Đặt Cọc 30% Online & Nhận Phiếu Mã QR Offline
- **Tiêu đề phụ:** Ký Quỹ 30% Khóa Suất & Nhận Phiếu Xác Nhận Bảo Mật
- **Mô tả:** Thanh toán ký quỹ 30% trực tuyến an toàn. Hệ thống lập tức khóa suất phòng trên Quota Engine và tự động phát hành Phiếu xác nhận kèm Mã QR Offline độc bản.
- **Trụ cột:**
  - `0% Overbooking:` Suất phòng được khóa chặt ngay khi nhận cọc.
  - `Mã QR Offline:` Hoạt động không cần kết nối mạng tại quầy sảnh.
  - `Minh Bạch Tài Chính:` Hóa đơn thể hiện rõ 5% SVC và 10% VAT chuẩn quốc tế.

### Bước 03: Quét Mã QR Tại Sảnh & Nhận Phòng Sạch Trong 15 Giây
- **Tiêu đề phụ:** Nhận Phòng Không Chạm — Khởi Tạo Room Folio Tức Thì
- **Mô tả:** Lễ tân quét mã QR tại sảnh trong 15 giây, đối chiếu CCCD. Hệ thống tự động lọc phòng sạch kiểm định (AVAILABLE), đóng băng đơn giá Room Folio và trao chìa khóa phòng ngay lập tức.
- **Trụ cột:**
  - `15 Giây Thần Tốc:` Quy trình không chạm, loại bỏ thủ tục giấy tờ rườm rà.
  - `Clean Guard 5 Sao:` Khóa cứng phòng DIRTY, chỉ bàn giao phòng sạch tinh tươm.
  - `Room Folio Điện Tử:` Đóng băng giá dịch vụ (Price Snapshot) suốt kỳ lưu trú.
