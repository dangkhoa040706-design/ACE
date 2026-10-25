# KẾ HOẠCH TRIỂN KHAI & ĐẶC TẢ KỸ THUẬT: SECTION 1 — FLAGSHIP HERO SHOWCASE (`WF-01`)

> **Thuộc chiến dịch:** Nâng cấp toàn diện Landing Page (`WF-01` — `templates/public/landing.html`) theo tiêu chuẩn Flagship Luxury (lấy cảm hứng từ các trang ra mắt sản phẩm Apple / Xiaomi).  
> **Phần hệ:** Public Booking Portal (`GET /`).  
> **Nguyên tắc:** Cuốn chiếu từng Section. Section 1 được hoàn thiện độc lập, giữ nguyên các Section 2, 3, 4, 5 và Header/Footer.

---

## 1. Mục tiêu Thiết kế
1. **Trải nghiệm Thị giác Đỉnh cao (Cinematic & Full-bleed):** Chiều cao `min-height: calc(100vh - 76px)`, tràn viền sắc nét, hiệu ứng thu phóng chậm Ken Burns cho ảnh và tỷ lệ 16:9 điện ảnh cho video.
2. **Kể chuyện Động (Dynamic Storytelling):** Chữ nổi và huy hiệu badge trượt nhẹ theo đường cong `cubic-bezier(0.16, 1, 0.3, 1)` đồng bộ theo từng slide media.
3. **Thanh tiến trình 5 Vạch Thời Gian Thực:** Lấy cảm hứng từ thanh tiến trình Story cao cấp, chạy tự động theo thời lượng thực của video hoặc 5 giây của ảnh, kèm nút Pause/Play và cho phép click chọn slide trực tiếp.
4. **Loại bỏ Quick Search Bar ở Section 1:** Phô diễn trọn vẹn vẻ đẹp của kiến trúc và không gian nghỉ dưỡng, dẫn dắt người dùng bằng các nút CTA chuyên biệt.

---

## 2. Danh mục 5 Slide Media Tuần tự (Playlist)

| Slide # | Loại Thẻ | Đường dẫn Web | File Vật lý Local | Nội dung Khung hình | Cơ chế Thời gian |
|:---:|:---:|---|---|---|:---:|
| **01** | `<video>` | `/videos/vid1.mp4` | `src/main/resources/static/videos/vid1.mp4` | Đại sảnh ACE Hotel về đêm, đèn chùm sang trọng | Theo thời lượng video (`ended` event, fallback 6s) |
| **02** | `<video>` | `/videos/vid2.mp4` | `src/main/resources/static/videos/vid2.mp4` | Không gian phòng Presidential Suite với ban công | Theo thời lượng video (`ended` event, fallback 6s) |
| **03** | `<img>` | `/images/hero/anh1.png` | `src/main/resources/static/images/hero/anh1.png` | Quầy lễ tân công nghệ Check-in QR | 5.000 ms (5 giây) + Hiệu ứng Ken Burns |
| **04** | `<img>` | `/images/hero/anh2.png` | `src/main/resources/static/images/hero/anh2.png` | Ẩm thực In-Room Dining & Minibar hảo hạng | 5.000 ms (5 giây) + Hiệu ứng Ken Burns |
| **05** | `<img>` | `/images/hero/anh3.png` | `src/main/resources/static/images/hero/anh3.png` | Phòng tắm Spa đá đen & Bồn tắm thư giãn | 5.000 ms (5 giây) + Hiệu ứng Ken Burns |

---

## 3. Lớp Phủ Tương Phản & Nội dung Chữ Động 5 Slide

### 3.1. Lớp phủ Kính Gradient Kép
Đảm bảo độ tương phản hoàn hảo ở cả **Dark Mode** và **Light Mode**:
```css
background: linear-gradient(90deg, rgba(6, 8, 12, 0.90) 0%, rgba(6, 8, 12, 0.58) 50%, rgba(6, 8, 12, 0.18) 100%),
            linear-gradient(180deg, rgba(0,0,0,0.30) 0%, rgba(0,0,0,0) 45%, rgba(6, 8, 12, 0.85) 100%);
```

### 3.2. Nội dung 5 Slide
1. **Slide 1 (`vid1.mp4` — Đại sảnh về đêm):**
   - **Badge:** `01 / 05 • ACE HOTEL FLAGSHIP`
   - **Tiêu đề:** `Đẳng Cấp Thượng Lưu, Minh Bạch Tuyệt Đối`
   - **Mô tả:** `Chuẩn mực khách sạn 5 sao thế hệ mới — nơi kiến trúc Đen Obsidian & Vàng Champagne hội tụ cùng trải nghiệm vận hành số hóa hoàn hảo.`
   - **Nút CTA:**
     - Nút chính: `Khám Phá Hạng Phòng` (`href="#hang-phong"`)
     - Nút phụ: `Câu Chuyện Thương Hiệu` (`href="#cau-chuyen"`)

2. **Slide 2 (`vid2.mp4` — Phòng Presidential Suite):**
   - **Badge:** `02 / 05 • ACCOMMODATION`
   - **Tiêu đề:** `Tuyệt Tác Không Gian Nghỉ Dưỡng 5 Sao`
   - **Mô tả:** `Năm hạng phòng từ Standard đến Presidential Suite với tầm nhìn toàn cảnh thành phố và biểu giá ngày thường, cuối tuần niêm yết rõ ràng.`
   - **Nút CTA:**
     - Nút chính: `Xem 5 Hạng Phòng` (`href="#hang-phong"`)
     - Nút phụ: `Tìm Phòng Trống` (`href="/booking/search"`)

3. **Slide 3 (`anh1.png` — Check-in QR không chạm):**
   - **Badge:** `03 / 05 • SMART QR CHECK-IN`
   - **Tiêu đề:** `Giữ Chỗ Chắc Chắn Với 30% Cọc & Mã QR`
   - **Mô tả:** `Khóa suất phòng thực tế theo tồn kho Quota thời gian thực. Nhận ngay Phiếu xác nhận QR và gán phòng sạch tại sảnh chỉ trong 15 giây.`
   - **Nút CTA:**
     - Nút chính: `Đặt Phòng & Nhận QR` (`href="/booking/search"`)
     - Nút phụ: `Quy Trình 3 Bước` (`href="#quy-trinh"`)

4. **Slide 4 (`anh2.png` — In-Room Dining & Minibar):**
   - **Badge:** `04 / 05 • EXTRAS & ROOM FOLIO`
   - **Tiêu đề:** `Hệ Sinh Thái Dịch Vụ Room Folio Tinh Tế`
   - **Mô tả:** `Thưởng thức ẩm thực In-Room Dining, Minibar và giặt ủi tốc hành với đơn giá đóng băng minh bạch ngay tại thời điểm sử dụng.`
   - **Nút CTA:**
     - Nút chính: `Khám Phá Dịch Vụ` (`href="#dich-vu"`)
     - Nút phụ: `Đặt Phòng Ngay` (`href="/booking/search"`)

5. **Slide 5 (`anh3.png` — Phòng tắm Spa & Thư giãn):**
   - **Badge:** `05 / 05 • CLEAN ROOM GUARD`
   - **Tiêu đề:** `Cam Kết 100% Phòng Sạch Tinh Tươm`
   - **Mô tả:** `Quy trình kiểm định buồng phòng khép kín trước mỗi lượt nhận phòng và quyết toán chuẩn biểu giá quốc tế (5% Phí phục vụ & 10% VAT).`
   - **Nút CTA:**
     - Nút chính: `Bắt Đầu Trải Nghiệm` (`href="/booking/search"`)
     - Nút phụ: `Xem Bảng Giá` (`href="#hang-phong"`)

---

## 4. Cụm Điều Khiển & Thanh Tiến Trình 5 Vạch
- **Nút Pause / Resume:** Icon SVG tròn `[⏸ / ▶]`, toggle trạng thái tự động chuyển động.
- **5 Thanh tiến trình nằm ngang:**
  - `01 Đại sảnh`
  - `02 Phòng Suite`
  - `03 Check-in QR`
  - `04 Ẩm thực`
  - `05 Không gian Spa`
- **Vạch chạy vàng `#FDBF2D`:** Chạy từ `0%` đến `100%`.
- **Tương tác click:** Nhấn vào thanh nào, carousel lập tức chuyển đến slide đó.
