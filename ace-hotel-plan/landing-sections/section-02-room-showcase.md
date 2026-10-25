# SECTION 02: 5 HẠNG PHÒNG - VERTICAL EXPANDING ACCORDION CARDS
*(Flagship Room Showcase Architecture & Specification - Updated Layout)*

---

## 1. TỔNG QUAN VỊ TRÍ & CHIẾN LƯỢC ĐIỀU CHỈNH
- **Vị trí hiển thị:** Section 2 trên Landing Page (`WF-01` — `templates/public/landing.html`), đặt ngay dưới Section 1 (Hero Showcase).
- **Mục tiêu UX & Tỷ lệ chuyển đổi (Conversion):**
  - Đón trọn nhịp hứng thú mua sắm của khách hàng ngay sau Hero Showcase.
  - Phục vụ trực tiếp hành động khi click nút CTA **"Khám Phá Hạng Phòng"** (`#hang-phong`) từ Hero Slide 1 & Slide 2.
  - Đại diện cho chữ **"A" (Accommodation)** trong triết lý thương hiệu **ACE (Accommodation, Check-in & Extras)**.

---

## 2. BỐ CỤC KHÔNG GIAN (2-COLUMN BALANCED LAYOUT)
Tỷ lệ bố cục tối ưu chống tràn trên Desktop:
- **Cột Trái (Left Column - Cố định 255px, ~22% Width):**
  - Mỏ neo thông tin tĩnh (Fixed Story & CTA Anchor).
  - Thu gọn kích thước để nhường tối đa không gian cho 5 thanh bên phải.
  - Badge nhận diện: `02 / 05 • ACCOMMODATION` (Chữ "A" trong triết lý ACE).
  - Tiêu đề chính: `Tuyệt Tác Không Gian Nghỉ Dưỡng 5 Sao` (`font-size: 1.85rem`).
  - Đoạn văn mô tả: `font-size: 0.86rem; line-height: 1.65;`.
  - Nút CTA chính: `Tìm Phòng & Đặt Cọc` (`padding: 0.75rem 1.45rem; font-size: 0.88rem;`).
- **Cột Phải (Right Column - Phần còn lại, ~78% Width):**
  - Khung chứa **5 Thanh Dọc Co Giãn (Vertical Expanding Accordion Cards)**.
  - Gap giữa 2 cột: `1.5rem` (24px).
  - Gap giữa 5 thanh: `0.65rem` (10px).
  - Chiều cao khung cố định: `560px`.

---

## 3. DANH SÁCH 5 HẠNG PHÒNG & NGUỒN DỮ LIỆU
Được nạp động từ Spring Boot Controller (`List<RoomType> roomTypes` sắp xếp theo giá tăng dần):
1. **01 • Standard:** 
   - Sức chứa: 2 khách | Ảnh: `/images/room-standard.jpg` (Fallback: `/images/room-standard.svg`)
   - Giá T2–T5: 780.000 đ/đêm | Giá T6–CN: 950.000 đ/đêm | Giá tóm tắt: `780k`
   - **Trạng thái khởi tạo: MỞ RỘNG MẶC ĐỊNH (Default Active Slide 01)**
2. **02 • Superior:** 
   - Sức chứa: 2 khách | Ảnh: `/images/room-superior.jpg`
   - Giá T2–T5: 950.000 đ/đêm | Giá T6–CN: 1.150.000 đ/đêm | Giá tóm tắt: `950k`
3. **03 • Deluxe:** 
   - Sức chứa: 2 khách | Ảnh: `/images/room-deluxe.jpg`
   - Giá T2–T5: 1.000.000 đ/đêm | Giá T6–CN: 1.250.000 đ/đêm | Giá tóm tắt: `1.0tr`
4. **04 • Family:** 
   - Sức chứa: 4 khách | Ảnh: `/images/room-family.jpg`
   - Giá T2–T5: 1.850.000 đ/đêm | Giá T6–CN: 2.200.000 đ/đêm | Giá tóm tắt: `1.85tr`
5. **05 • Suite:** 
   - Sức chứa: 4 khách | Ảnh: `/images/room-suite.jpg`
   - Giá T2–T5: 3.200.000 đ/đêm | Giá T6–CN: 3.800.000 đ/đêm | Giá tóm tắt: `3.2tr`

---

## 4. QUY TẮC HIỆU ỨNG TƯƠNG TÁC (CHỐNG TRÀN 100%)
- **Thanh thu gọn (Collapsed State):**
  - `flex: 1; min-width: 0;` (bắt buộc có `min-width: 0` để flexbox không bị kẹt bởi nội dung).
  - Bo góc `18px`, viền `1px solid rgba(255, 255, 255, 0.12)`.
  - Padding nội dung: `1.25rem 0.35rem`.
  - Số thứ tự: `01`, `02`... ở đỉnh thanh.
  - Tên phòng viết dọc: `writing-mode: vertical-rl; transform: rotate(180deg); font-size: 0.92rem;`.
  - Giá tóm tắt: `780k`, `950k`, `1.0tr`, `1.85tr`, `3.2tr` căn giữa thanh.
- **Thanh mở rộng (Active State):**
  - `flex: 3.0;`
  - Padding nội dung: `1.35rem 1.5rem;`
  - Đảm bảo 100% không bị cắt mép chữ, số hay viền ngay cả với thanh 05 ngoài cùng bên phải.
