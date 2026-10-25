# SECTION 04: TRIẾT LÝ THƯƠNG HIỆU & HỆ ĐIỀU HÀNH LƯU TRÚ (ARCHITECTURAL BENTO GRID)
*(The ACE Brand Philosophy & 12-Column Architectural Bento Grid — Compact Laptop Viewport Edition)*

---

## I. TỔNG QUAN & BỐI CẢNH NÂNG CẤP
- **Vị trí hiển thị trên Landing Page (`WF-01` — `templates/public/landing.html`):**
  - Đặt tại anchor `#cau-chuyen`, ngay sau Section 3 (`#dich-vu` — Chữ "E" Extras & Hệ sinh thái Room Folio).
  - Đóng vai trò là linh hồn câu chuyện thương hiệu, đúc kết toàn bộ giá trị cốt lõi của **ACE** (*Accommodation, Check-in & Extras*) trước khi dẫn dắt người dùng vào Quy trình 3 bước (Section 5).
- **Mục tiêu tối ưu hiển thị trên màn hình Laptop (15.6 inch, scale 125%):**
  - Khung nhìn khả dụng thực tế của trình duyệt trên laptop là khoảng `700px - 720px`.
  - Toàn bộ khối Section 4 (Header + Lưới Bento Hàng 1 + Hàng 2) được quy chuẩn chiều cao trong khoảng **`540px - 570px`** để nằm gọn 100% trong một màn hình duy nhất mà không bị tràn mép dưới.
  - Sửa lỗi lặp 2 dấu chấm ở badge: chỉ giữ lại 1 dấu chấm tròn vàng phát sáng.
  - Sửa lỗi watermark `ACE` bị thò ra ngoài viền dưới: thu gọn kích thước và khóa chặt với `overflow: hidden;`.

---

## II. THÔNG SỐ KIẾN TRÚC & PHÂN BỔ CHIỀU CAO

### 1. Section Container & Header (~140px)
- **Section Padding:** `padding: 2.2rem 0 2.5rem;`
- **Badge:**
  - HTML text: `04 / 05 • OUR STORY & PHILOSOPHY` (xóa ký tự `●` trong text, chỉ dùng CSS `.story-badge::before` để tạo đúng 1 chấm tròn vàng phát sáng).
  - Khoảng cách: `margin-bottom: 0.35rem; font-size: 0.7rem; letter-spacing: 2px;`
- **Tiêu đề chính:**
  - `font-size: 1.85rem; line-height: 1.25; margin: 0 0 0.4rem 0; letter-spacing: -0.3px;`
- **Mô tả phụ:**
  - `font-size: 0.84rem; line-height: 1.45; max-width: 720px; margin: 0 auto 1.15rem; white-space: normal;`

### 2. Cấu Trúc Lưới Bento 12 Cột (`.ace-bento-grid`)
- `display: grid; grid-template-columns: repeat(12, 1fr); gap: 0.85rem; max-width: 1240px; margin: 0 auto; box-sizing: border-box;`
- Thu gọn `padding` của tất cả 5 thẻ `.bento-card` xuống `1.15rem 1.35rem;`, `border-radius: 16px;`, bắt buộc `overflow: hidden;`.

---

## III. CHI TIẾT 5 Ô THẺ KÍNH THEO 2 HÀNG

### HÀNG 1: Chiều cao tối ưu ~215px - 225px

#### ✦ Ô 1 — Tuyên Ngôn Thương Hiệu ACE (`grid-column: span 7;`)
- **Watermark:** `ACE` chìm mờ vi tế (`font-size: 5.5rem; opacity: 0.035; position: absolute; right: 1rem; bottom: 0.25rem; pointer-events: none; line-height: 1; user-select: none;`). Nằm gọn 100% bên trong thẻ.
- **Nhãn trên:** `✦ THE ACE MANIFESTO • TUYÊN NGÔN THƯƠNG HIỆU` (`font-size: 0.68rem; margin-bottom: 0.35rem; font-weight: 800; color: #FDBF2D;`).
- **Câu tuyên ngôn chính:**
  > *"Sự sang trọng đích thực bắt đầu từ sự minh bạch tuyệt đối và không gian riêng tư không rào cản."* (`font-size: 1.18rem; line-height: 1.35; margin: 0.35rem 0 0.45rem; font-weight: 800;`).
- **Đoạn dẫn giải:**
  > ACE Hotel được kiến tạo để loại bỏ hoàn toàn ba nỗi ám ảnh của ngành khách sạn truyền thống: tình trạng quá tải phòng ảo (Overbooking), sự chờ đợi mệt mỏi tại quầy lễ tân và những khoản phụ phí mập mờ khi trả phòng. (`font-size: 0.79rem; line-height: 1.45; margin-bottom: 0.75rem;`).
- **Dải 3 Trụ Cột Đáy Thẻ (`display: grid; grid-template-columns: repeat(3, 1fr); gap: 0.65rem;`):**
  - Hộp nhỏ: `padding: 0.5rem 0.65rem; border-radius: 10px;`
  - **A • ACCOMMODATION:** 5 hạng phòng độc bản & giá động T2–CN (Key: `0.68rem;`, Val: `0.72rem; line-height: 1.3;`).
  - **C • CHECK-IN:** Quét QR nhận phòng sạch trong 15 giây.
  - **E • EXTRAS:** Sổ cái Room Folio đóng băng đơn giá.

#### ✦ Ô 2 — Điểm Nhấn Công Nghệ Check-in QR (`grid-column: span 5;`)
- **Header:** Nhãn `● TOUCHLESS CONCIERGE` (`font-size: 0.68rem;`) + Icon SVG QR `34px x 34px`.
- **Con số Monumental Typography:** `15 Giây` (`font-size: 2.35rem; line-height: 1.05; margin-bottom: 0.25rem;` gradient vàng kim).
- **Tiêu đề:** `Nhận Phòng Không Chạm Qua Mã QR Offline` (`font-size: 0.96rem; line-height: 1.3; margin: 0 0 0.35rem 0; font-weight: 800;`).
- **Mô tả:** `Ngay sau khi đặt cọc, hệ thống phát hành Phiếu xác nhận kèm Mã QR hoạt động 100% offline. Lễ tân quét mã, đối chiếu CCCD và trao chìa khóa tức thì.` (`font-size: 0.79rem; line-height: 1.45; margin-bottom: 0.6rem;`).
- **Badge đáy thẻ:** `⚡ Tích hợp linh hoạt phụ thu nhận sớm (0% • 30% • 50% • 100%)` (`padding-top: 0.5rem; font-size: 0.74rem;`).

---

### HÀNG 2: Chiều cao tối ưu ~175px - 185px (3 Ô span 4 đồng nhất)

#### ✦ Ô 3 — Bảo Chứng Giữ Chỗ Quota (`grid-column: span 4;`)
- **Header:** Nhãn `● QUOTA ENGINE` (`0.68rem`) + Icon SVG Khiên Khóa `32px x 32px`.
- **Cụm Số & Tag (Hàng ngang):** `30% Cọc` (`font-size: 1.65rem; font-weight: 900; line-height: 1.1;`) + Tag `0% Overbooking` (`font-size: 0.65rem; padding: 0.15rem 0.45rem;`).
- **Tiêu đề:** `Khóa Chặt Suất Phòng Thực Tế` (`font-size: 0.92rem; font-weight: 700; margin-bottom: 0.25rem; line-height: 1.3;`).
- **Mô tả:** `Mỗi đơn đặt phòng trực tuyến khóa trực tiếp vào quỹ phòng kinh doanh khả dụng (loại trừ phòng MAINTENANCE), đảm bảo luôn có phòng chờ đón bạn.` (`font-size: 0.76rem; line-height: 1.4; margin-bottom: 0.55rem;`).
- **Badge đáy thẻ:** `✓ Hỗ trợ cả khách Walk-in 0đ cọc tại quầy (BR1 & BR4)` (`padding-top: 0.45rem; font-size: 0.72rem;`).

#### ✦ Ô 4 — Chốt Chặn Buồng Phòng Sạch (`grid-column: span 4;`)
- **Header:** Nhãn `● CLEAN ROOM GUARD` (`0.68rem`) + Icon SVG Ngôi Sao `32px x 32px`.
- **Cụm Số & Tag (Hàng ngang):** `100% Sạch` (`font-size: 1.65rem; font-weight: 900; line-height: 1.1;`) + Tag `DIRTY ➔ AVAILABLE` (`font-size: 0.65rem; padding: 0.15rem 0.45rem;`).
- **Tiêu đề:** `Kiểm Định Buồng Phòng Khép Kín` (`font-size: 0.92rem; font-weight: 700; margin-bottom: 0.25rem; line-height: 1.3;`).
- **Mô tả:** `Phòng vừa trả lập tức chuyển sang trạng thái DIRTY (Chờ dọn dẹp) và bị khóa gán khách mới cho đến khi hoàn tất quy trình vệ sinh chuẩn 5 sao.` (`font-size: 0.76rem; line-height: 1.4; margin-bottom: 0.55rem;`).
- **Badge đáy thẻ:** `✓ Chốt chặn an toàn trạng thái phòng (BR2 & BR7)` (`padding-top: 0.45rem; font-size: 0.72rem;`).

#### ✦ Ô 5 — Chuẩn Mực Tài Chính Quốc Tế `++` (`grid-column: span 4;`)
- **Header:** Nhãn `● FINANCIAL STANDARD` (`0.68rem`) + Icon SVG Hóa Đơn `32px x 32px`.
- **Cụm Số & Tag (Hàng ngang):** `5% + 10%` (`font-size: 1.65rem; font-weight: 900; line-height: 1.1;`) + Tag `Biểu Giá ++` (`font-size: 0.65rem; padding: 0.15rem 0.45rem;`).
- **Tiêu đề:** `Đóng Băng Giá & Quyết Toán Rõ Ràng` (`font-size: 0.92rem; font-weight: 700; margin-bottom: 0.25rem; line-height: 1.3;`).
- **Mô tả:** `Đơn giá Room Folio được đóng băng ngay lúc gọi (Price Snapshot). Hóa đơn VAT bóc tách minh bạch 5% Phí phục vụ, 10% VAT và tự động trừ tiền cọc.` (`font-size: 0.76rem; line-height: 1.4; margin-bottom: 0.55rem;`).
- **Badge đáy thẻ:** `✓ Làm tròn số nguyên BIGINT chính xác tuyệt đối (BR5 & BR6)` (`padding-top: 0.45rem; font-size: 0.72rem;`).

---

## IV. TƯƠNG THÍCH DARK / LIGHT MODE & RESPONSIVE
- **Dark Mode:** Nền kính Obsidian, viền vàng kim mảnh `rgba(253, 191, 45, 0.18)`, bóng đổ sâu.
- **Light Mode:** Nền kem sứ hoàng gia, viền hổ phách `rgba(180, 83, 9, 0.18)`, typography số đổi sang vàng đồng đậm `#B45309`.
- **Hover:** Nâng nhẹ 3D `transform: translateY(-4px);` và viền bừng sáng `rgba(253, 191, 45, 0.6)`.
- **Responsive:**
  - $\ge 1024\text{px}$: Hàng 1 (7 + 5) + Hàng 2 (4 + 4 + 4) = chuẩn 1 màn hình laptop.
  - $768\text{px} \le W < 1024\text{px}$: Ô 1 và Ô 2 `span 12`, Ô 3 & 4 `span 6`, Ô 5 `span 12`.
  - $< 768\text{px}$: Tất cả 5 ô `span 12`, xếp chồng dọc mượt mà.
