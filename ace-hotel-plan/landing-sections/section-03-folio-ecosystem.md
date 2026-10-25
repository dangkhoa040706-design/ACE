# SECTION 03: HỆ SINH THÁI ROOM FOLIO & CẦU NỐI CHUYỂN CẢNH XÉO (DIAGONAL KINETIC RIBBON)
*(Folio Ecosystem Bento Grid & Luxury Kinetic Typography Architecture)*

---

## I. TỔNG QUAN & VỊ TRÍ KIẾN TRÚC
- **Vị trí hiển thị trên Landing Page (`WF-01` — `templates/public/landing.html`):**
  - Đặt ngay sau **Section 2 (`#hang-phong` - Chữ "A" Accommodation)**.
  - Mở đầu bằng **Cầu nối Chuyển cảnh Xéo (Diagonal Kinetic Ribbon)** phá vỡ kết cấu lưới ngang truyền thống.
  - Tiếp nối bằng **Section 3: Hệ Sinh Thái Room Folio Tiện Ích (`#dich-vu` - Chữ "E" Extras)**.
  - Đẩy phần Brand Story (Triết lý vận hành A - C - E) xuống Section 4 và Quy trình 3 bước ở Section 5.

---

## II. ĐẶC TẢ CẦU NỐI CHUYỂN CẢNH XÉO (DIAGONAL KINETIC RIBBON)

### 1. Khung Bọc & Góc Nghiêng Chuẩn (True Diagonal Cut -6deg)
- Khung ngoài: `.diagonal-bridge-wrapper`
  - `position: relative; width: 100%; overflow: visible; padding: 0; margin-top: -3.5rem; margin-bottom: -3.5rem; background: transparent; border: none; z-index: 10; pointer-events: auto;`
  - Xóa bỏ hoàn toàn mọi hộp chữ nhật đen và đường cắt ngang 0 độ.
  - Ngăn ngừa thanh cuộn ngang bằng `overflow-x: clip;` ở container cấp cao (`landing-content-wrapper`).
- Dải xéo: `.diagonal-ribbon`
  - Góc xoay đồng bộ góc cắt: `transform: rotate(-6deg);`
  - Kích thước mở rộng: `width: 116%; margin-left: -8%; padding: 1.25rem 0;`
  - Nền Obsidian & Viền Vàng Kim 2px (nổi bật trên cả Dark/Light Mode):
    - `background: linear-gradient(90deg, #06080D 0%, #141A29 50%, #06080D 100%);`
    - `border-top: 2px solid #FDBF2D;`
    - `border-bottom: 2px solid #FDBF2D;`
    - `box-shadow: 0 20px 50px rgba(0, 0, 0, 0.75), 0 0 35px rgba(253, 191, 45, 0.22);`
- Phân cách nền & Vát chéo đáy Section 2 / đỉnh Section 3:
  - Section 2 (`.room-showcase-section`): `background: #080A0F` (Dark) / `#F7F6F2` (Light), `border-bottom: none`, `padding-bottom: 10.5rem` (cách chân thanh phòng 05 ít nhất 48px).
  - Section 3 (`.folio-ecosystem-section`): `background: linear-gradient(180deg, #101522 0%, #080A0F 100%)` (Dark) / `linear-gradient(180deg, #EFECE3 0%, #F7F6F2 100%)` (Light), `border-top: none`, vát chéo đỉnh bằng `clip-path: polygon(0 calc(100vw * 0.1051), 100% 0, 100% 100%, 0 100%); margin-top: calc(-100vw * 0.0525); padding-top: calc(100vw * 0.0525 + 6.5rem);`.

### 2. Chuyển Động Chữ Marquee Liên Tục (60fps Infinite Track)
- `.diagonal-track`:
  - `display: flex; width: max-content; will-change: transform; animation: aceDiagonalMarquee 32s linear infinite; gap: 2rem;`
  - Khi hover (`.diagonal-ribbon:hover .diagonal-track`): `animation-play-state: paused;`
- Keyframe `@keyframes aceDiagonalMarquee`:
  - `from { transform: translate3d(0, 0, 0); } to { transform: translate3d(-50%, 0, 0); }`
- 8 Cụm từ nghệ thuật đan xen Solid Gold/White & Hollow Outline Stroke, phân tách bởi sao 4 cánh phát sáng `✦`:
  1. `ACCOMMODATION` *(Solid Gold `#FDBF2D`, font-weight: 800)* `✦`
  2. `15S SMART QR CHECK-IN` *(Hollow White: `-webkit-text-stroke: 1px rgba(255, 255, 255, 0.78); color: transparent;`)* `✦`
  3. `30% DEPOSIT GUARANTEE` *(Solid White `#FFFFFF`, font-weight: 800)* `✦`
  4. `IN-ROOM DINING 24/7` *(Hollow Gold: `-webkit-text-stroke: 1px rgba(253, 191, 45, 0.85); color: transparent;`)* `✦`
  5. `EXPRESS LAUNDRY 4H` *(Solid Gold `#FDBF2D`, font-weight: 800)* `✦`
  6. `FROZEN FOLIO PRICING` *(Hollow White)* `✦`
  7. `100% CLEAN ROOM GUARD` *(Solid White `#FFFFFF`, font-weight: 800)* `✦`
  8. `TRANSPARENT 5% SVC & 10% VAT` *(Hollow Gold)* `✦`

---

## III. ĐẶC TẢ SECTION 3: THE GRAND CINEMATIC SPLIT STAGE (`#dich-vu` — CHỮ "E" EXTRAS)
*(Kiến trúc 100vh Fullscreen Master-Detail Split chuẩn mực Rosewood & Aman Resorts)*

### 1. Kích Thước & Trải Nghiệm Fullscreen Laptop (100vh)
- Thiết lập `min-height: calc(100vh - 76px); display: flex; flex-direction: column; justify-content: center;`
- Căn giữa trọn vẹn màn hình laptop, loại bỏ hoàn toàn tình trạng "lửng lơ, cắt cụt đáy màn hình".
- Đường đỉnh Section 3 vát chéo `-6deg` bằng `clip-path: polygon(0 calc(100vw * 0.1051), 100% 0, 100% 100%, 0 100%);` kết hợp `margin-top: calc(-100vw * 0.0525);` và `padding-top: calc(100vw * 0.0525 + 5.5rem);`.

### 2. Header Section 3 (Thanh thoát & Tinh tế)
- Badge định vị: `● 03 / 05 • EXTRAS & ROOM FOLIO`
- Tiêu đề chính: `Hệ Sinh Thái Room Folio & Tiện Nghi Thượng Hạng`
- Mô tả phụ: `Chữ "E" (Extras) trong ACE Hotel — Trải nghiệm ẩm thực 24/7, minibar ướp lạnh và giặt ủi hoàng gia với sổ cái điện tử Room Folio đóng băng đơn giá lịch sử (Quy tắc BR5).`

### 3. Bố Cục Sân Khấu Kép (Master-Detail Split Stage: 38% - 62%)
Gồm 2 cột tương tác tỷ lệ vàng:

#### A. Cột Trái (38% — Interactive Service Navigator):
4 Danh mục dịch vụ cao cấp xếp dọc với hiệu ứng hover/click chuyển cảnh tức thì:
1. **`01 / CULINARY` — In-Suite Gastronomy (Ẩm thực tại phòng 24/7):**
   - Tag: `24/7 NÓNG SỐT`
   - Mô tả ngắn: Bữa sáng buffet gọi thêm & món nóng sốt giao tận phòng bởi đầu bếp chuyên nghiệp.
2. **`02 / MINIBAR` — Curated In-Room Minibar (Tủ rượu & Đồ uống hảo hạng):**
   - Tag: `ƯỚP LẠNH SẴN`
   - Mô tả ngắn: Nước ngọt, bia lon Tiger ướp lạnh & snack hạt điều cao cấp tại phòng.
3. **`03 / VALET` — Royal Garment Care (Giặt ủi tốc hành 4H):**
   - Tag: `HỎA TỐC 4 GIỜ`
   - Mô tả ngắn: Giặt ủi bộ comple, áo vest chỉn chu sẵn sàng cho mọi cuộc hẹn quan trọng.
4. **`04 / FOLIO` — Smart Room Folio & Safety (Sổ cái điện tử & Minh bạch):**
   - Tag: `ĐÓNG BĂNG GIÁ BR5`
   - Mô tả ngắn: Biểu phí đền bù niêm yết rõ ràng, không kê giá, quyết toán chuẩn 5% SVC + 10% VAT.

#### B. Cột Phải (62% — Cinematic Visual Stage):
- Khung ảnh nhiếp ảnh khổ lớn bo góc 20px, viền vàng kim hairline, bóng đổ chiều sâu 3D sang trọng.
- 4 Lớp ảnh Cinematic chất lượng cao tương ứng 4 dịch vụ, chuyển đổi mượt mà bằng hiệu ứng cross-fade và zoom nhẹ (Ken Burns effect).
- **Thẻ Kính Nổi Cao Cấp (Floating Glassmorphism Detail Card):**
  - Đặt nổi trên ảnh ở góc dưới, phủ mờ `backdrop-filter: blur(16px)`, viền ánh vàng.
  - Hiển thị danh sách dịch vụ thực tế từ CSDL & đơn giá đóng băng:
    - *In-Suite Gastronomy:* Bữa sáng Buffet `100.000 đ / suất` • Món nóng phục vụ 24/7 `220.000 đ / phần`.
    - *Curated Minibar:* Nước ngọt & Bia Tiger lon `35.000 đ / lon` • Snack hạt điều cao cấp `50.000 đ / gói`.
    - *Royal Garment Care:* Giặt ủi đồ bộ `80.000 đ / bộ` • Giặt hấp comple / áo vest `150.000 đ / chiếc` (4H).
    - *Smart Room Folio:* Niêm yết đền bù ly cốc `100.000 đ`, thẻ từ `200.000 đ` • Chuẩn 5% SVC + 10% VAT • Khấu trừ 30% tiền cọc.
  - Nhãn cam kết: `✓ Đóng băng đơn giá lịch sử tại thời điểm gọi (Quy tắc BR5)`.
  - Nút hành động chính: `Khám Phá Menu & Đặt Phòng →` (`/booking/search`).

### 4. Tương Tác 60fps & Khả Năng Thích Ứng (Responsive)
- **Desktop / Laptop:** Toàn bộ Section 3 vừa khít 1 màn hình laptop, tương tác chuột rê đến đâu hình ảnh và chi tiết đổi mượt mà đến đó.
- **Tablet / Mobile (< 992px):** Tự động chuyển đổi thành dạng danh mục dạng accordion hoặc thẻ cuộn ngang tiện lợi, bảo toàn 100% hình ảnh và dữ liệu.

  - `[Sổ cái Room Folio 4 nhóm]` + `[Tiền phòng lưu trú]` ➔ `Biểu giá chuẩn quốc tế (5% Phí phục vụ SVC & 10% VAT)` ➔ `Khấu trừ 30% tiền cọc Online`.
  - Nút CTA chuyển hướng: `Trải Nghiệm Đặt Phòng Ngay →` (`/booking/search`).
