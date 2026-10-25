# 05. CONTENT BRIEF – LANDING PAGE STORYTELLING & CỔNG ĐẶT PHÒNG (`WF-01`)

> **Trạng thái:** Đã chốt theo Quy trình Nghiệp vụ Mới (`quy-trinh-nghiep-vu-moi.md`).  
> **Ánh xạ Báo cáo:** Phục vụ **Chương 6 – Thiết kế giao diện người dùng (Mục 6.1, 6.2, 6.3 – Màn hình `WF-01`)**.  
> **Route:** `GET /` (Công khai, hỗ trợ chuyển đổi Dark Mode / Light Mode, tự động nhận diện trạng thái đăng nhập 3 Role).

---

## 1. Mục tiêu & Định hướng Thiết kế (`Storytelling Scroll`)
1. **Kể câu chuyện thương hiệu ACE Hotel (*Accommodation, Check-in & Extras*):** Thiết kế trải nghiệm cuộn dọc qua 4 phân đoạn (sections) mang phong cách sang trọng, hiện đại với tông màu Đen – Vàng Gold (`#FDBF2D`) chủ đạo ở chế độ Dark Mode (và Trắng kem `#F7F6F2` ở chế độ Light Mode).
2. **Mở cổng tìm kiếm & đặt phòng trực tuyến cho Khách hàng (`UC03`):** Tích hợp thanh công cụ tìm kiếm hạng phòng trống theo ngày nhận, ngày trả và số lượng khách ngay tại Hero Section, dẫn thẳng vào luồng kiểm tra tồn kho Hạng phòng (*Room Type Quota*) và đặt cọc 30%.
3. **Phân luồng thông minh cho cả 3 nhóm người dùng (`UC01`):**
   - Chưa đăng nhập: Hiển thị nút **[Đăng nhập / Đăng ký]** dẫn tới `/login` (`WF-02`).
   - Đã đăng nhập: Tự động hiển thị nút **[Vào Bảng điều khiển]** dẫn về đúng Dashboard của vai trò đang đăng nhập (`/customer/dashboard`, `/staff/dashboard` hoặc `/admin/dashboard`).

---

## 2. Cấu trúc Chi tiết Các Khối trên Trang (`WF-01`)

| # | Tên Section | Nội dung hiển thị chi tiết | Nguồn dữ liệu | Ưu tiên |
|:---:|---|---|---|:---:|
| **1** | **Header Cố định** | Logo ACE (`2_logo-ACE_khong-nen.png`), menu cuộn mượt (*Câu chuyện · Hạng phòng · Dịch vụ · Quy trình đặt*), nút chuyển đổi **Dark / Light Mode**, nút **[Đăng nhập / Đăng ký]** (hoặc **[Vào Dashboard]**) | Tĩnh + Session | P0 |
| **2** | **Section 1: Hero Storytelling & Thanh Tìm Phòng** | • Logo đầy đủ `1_logo-day-du_khong-nen.png` (5 sao)<br>• Tiêu đề lớn: *"Nghệ thuật Lưu trú & Trải nghiệm Đẳng cấp tại ACE Hotel"*<br>• Phụ đề: *"Accommodation, Check-in & Extras — Chuẩn mực hiếu khách hiện đại"*<br>• **Thanh tìm kiếm nhanh (`Quick Booking Bar`):** Ô chọn `Ngày nhận phòng`, `Ngày trả phòng`, `Số lượng khách` và nút Primary **[Tìm hạng phòng trống]** dẫn tới `/booking/search` (`WF-04`) | Form `GET /booking/search` | P0 |
| **3** | **Section 2: Ba Trụ cột Thương hiệu ACE** | Giải nghĩa 3 chữ cái làm nên thương hiệu ACE:<br>1. **A – Accommodation (Không gian lưu trú độc bản):** 5 hạng phòng từ Standard đến Suite.<br>2. **C – Check-in (Nhận phòng QR & Gán phòng sạch tức thì):** Quét mã QR nhận ngay phòng vật lý đã kiểm định vệ sinh (`AVAILABLE / Clean`).<br>3. **E – Extras (Hệ sinh thái dịch vụ Room Folio):** Ẩm thực, Minibar, Giặt ủi, Spa phục vụ tận phòng. | Tĩnh | P0 |
| **4** | **Section 3: Các Hạng Phòng Nổi bật** | Hiển thị danh sách thẻ hạng phòng: Tên hạng (`Standard`, `Superior`, `Deluxe`, `Family`, `Suite`), sức chứa tối đa, mô tả tiện nghi, **Giá ngày thường (T2–T5)**, **Giá cuối tuần (T6–CN)** và nút **[Đặt hạng phòng này]** | Đọc động từ bảng `room_types` | P0 |
| **5** | **Section 4: Trải nghiệm Dịch vụ & Tiện ích (4 Nhóm)** | Giới thiệu 4 nhóm dịch vụ lưu trú chính gắn với Room Folio:<br>• **Ẩm thực (`FB`):** Buffet sáng & Phục vụ tại phòng (*In-room Dining*)<br>• **Minibar (`MINIBAR`):** Đồ uống ướp lạnh & Đồ ăn nhẹ tại phòng<br>• **Giặt ủi (`LAUNDRY`):** Giặt khô, ủi hơi nước lấy nhanh trong ngày<br>• **Chăm sóc & Bảo đảm (`DAMAGES`):** Chính sách minh bạch, niêm yết rõ ràng | Đọc động từ bảng `services` (hoặc thẻ tóm tắt) | P1 |
| **6** | **Section 5: Quy trình Đặt phòng & Nhận phòng 4 Bước** | Sơ đồ trực quan 4 bước dành cho khách hàng:<br>**(1) Chọn Hạng phòng & Ngày** $\rightarrow$ **(2) Xác nhận Pop-up & Cọc 30%** $\rightarrow$ **(3) Nhận Phiếu xác nhận kèm Mã QR** $\rightarrow$ **(4) Xuất trình QR tại quầy để nhận Phòng sạch & Quyết toán 70% còn lại khi trả phòng** | Tĩnh | P0 |
| **7** | **Footer (Chân trang)** | Logo `3_logo-ACE-co-ten_khong-nen.png`, thông tin đồ án học phần `502050 – Phân tích và Thiết kế Yêu cầu`, Khoa CNTT – ĐH Tôn Đức Thắng, năm 2026 | Tĩnh | P0 |

---

## 3. Quy chuẩn Nội dung, Hình ảnh & Tài sản Thương hiệu

1. **Bộ 4 File Logo ACE (`assets/`):**
   - `1_logo-day-du_khong-nen.png`: Đặt tại khu vực Hero mở đầu trang chủ.
   - `2_logo-ACE_khong-nen.png`: Đặt trên thanh Header cố định và dùng làm Favicon.
   - `3_logo-ACE-co-ten_khong-nen.png`: Đặt tại phần giới thiệu ý nghĩa tên gọi ACE và chân trang (Footer).
   - `4_logo-giong-hien-tai_them-ten.png`: Dùng cho trang bìa slide thuyết trình và ảnh chia sẻ.
2. **Định dạng hiển thị:**
   - Ngôn ngữ giao diện: Tiếng Việt 100%, văn phong khách sạn 5 sao sang trọng, rõ ràng.
   - Định dạng tiền tệ: Phân cách hàng nghìn bằng dấu chấm kèm ký hiệu `đ` (ví dụ: `1.250.000đ / đêm`).
   - Ghi chú minh bạch: Ghi rõ biểu giá niêm yết áp dụng công thức chuẩn khách sạn `++` (5% Phí phục vụ SVC & 10% Thuế GTGT VAT).
3. **Khai báo nguồn tham khảo (Đáp ứng Mục 5 Đề thi):**
   - Font chữ **Inter** (nhúng nội bộ trong `static/fonts/`) và bộ biểu tượng SVG mã nguồn mở **Lucide Icons** được khai báo đầy đủ trong mục **Danh mục Tài liệu tham khảo** cuối quyển báo cáo.