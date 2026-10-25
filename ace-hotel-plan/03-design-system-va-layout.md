# 03. Giao diện, ACE Design System (`tokens.css`) & 2 Khung Layout Thymeleaf

> **Mục tiêu:** Chuẩn hóa toàn bộ hệ thống biến màu CSS (`tokens.css`) theo bộ **ACE Color System** (Dark Mode mặc định, có nút chuyển sang Light Mode)[cite: 11, 37, 46, 53], bổ sung màu trạng thái **`DIRTY` (Chờ dọn dẹp)**[cite: 53], cơ chế `theme.js` chống nháy sáng[cite: 11, 53], quy tắc gắn 4 file Logo ACE[cite: 1, 2, 3, 4, 11] và 2 khung Layout Thymeleaf dùng chung cho 4 phân hệ[cite: 10, 53].

---

## 1. Bảng Token Màu Chuẩn & File `static/css/tokens.css`

Giữ nguyên màu vàng thương hiệu **`#FDBF2D`** ở cả hai chế độ[cite: 11, 37, 46, 53] và bổ sung đầy đủ 6 màu trạng thái phòng/đơn đặt (`AVAILABLE`, `RESERVED`, `OCCUPIED`, `DIRTY`, `OVERDUE`, `MAINTENANCE`)[cite: 53]:

~~~css
/* =========================================================================
   ACE HOTEL DESIGN SYSTEM - CSS CUSTOM PROPERTIES (tokens.css)
   ========================================================================= */

:root,
:root[data-theme="dark"] {
  /* Nền & Bề mặt (Dark Mode - Mặc định) */
  --bg: #000000;
  --surface: #121212;
  --elevated: #1C1C1C;
  --border: #2A2A2A;

  /* Màu thương hiệu ACE Gold */
  --primary: #FDBF2D;
  --primary-hover: #FFD056;
  --primary-pressed: #E5A90F;
  --primary-soft: #2A2208;
  --on-primary: #0A0A0A;

  /* Chữ */
  --text: #FFFFFF;
  --text-secondary: #A1A1AA;
  --text-muted: #71717A;
  --primary-text: #FDBF2D;

  /* 6 Trạng thái Phòng & Booking (Dark Mode) */
  --st-free: #22C55E;           /* AVAILABLE - Trống sạch */
  --st-free-bg: rgba(34, 197, 94, 0.14);
  --st-reserved: #A78BFA;       /* RESERVED - Đã đặt & Cọc 30% */
  --st-reserved-bg: rgba(167, 139, 250, 0.14);
  --st-occupied: #60A5FA;       /* OCCUPIED - Đang ở */
  --st-occupied-bg: rgba(96, 165, 250, 0.14);
  --st-dirty: #F59E0B;          /* DIRTY - Chờ dọn dẹp (Mới) */
  --st-dirty-bg: rgba(245, 158, 11, 0.16);
  --st-overdue: #F87171;        /* OVERDUE - Quá hạn */
  --st-overdue-bg: rgba(248, 113, 113, 0.16);
  --st-maint: #9CA3AF;          /* MAINTENANCE - Bảo trì */
  --st-maint-bg: rgba(156, 163, 175, 0.14);
}

:root[data-theme="light"] {
  /* Nền & Bề mặt (Light Mode) */
  --bg: #F7F6F2;
  --surface: #FFFFFF;
  --elevated: #FFFFFF;
  --border: #E6E3DA;

  /* Màu thương hiệu ACE Gold */
  --primary: #FDBF2D;
  --primary-hover: #E5A90F;
  --primary-pressed: #C98F00;
  --primary-soft: #FFF4D1;
  --on-primary: #0A0A0A;

  /* Chữ */
  --text: #0A0A0A;
  --text-secondary: #52525B;
  --text-muted: #71717A;
  --primary-text: #B45309;      /* Vàng nâu đậm đủ độ tương phản trên nền sáng */

  /* 6 Trạng thái Phòng & Booking (Light Mode) */
  --st-free: #15803D;
  --st-free-bg: #DCFCE7;
  --st-reserved: #7C3AED;
  --st-reserved-bg: #F3E8FF;
  --st-occupied: #2563EB;
  --st-occupied-bg: #DBEAFE;
  --st-dirty: #D97706;
  --st-dirty-bg: #FEF3C7;
  --st-overdue: #DC2626;
  --st-overdue-bg: #FEE2E2;
  --st-maint: #6B7280;
  --st-maint-bg: #F3F4F6;
}
~~~

---

## 2. Cơ chế Chuyển đổi Dark / Light Mode (`static/js/theme.js`)

- Đặt đoạn script nhỏ đọc `localStorage.getItem('ace-theme')` ngay trong thẻ `<head>` của cả 2 layout để áp dụng `data-theme="dark"` (mặc định) hoặc `data-theme="light"` **trước khi trình duyệt vẽ trang**, tránh hiện tượng nháy trắng[cite: 11, 53]:

~~~javascript
// Đặt inline trong <head> để chống nháy sáng (FOUC)
(function () {
  try {
    var saved = localStorage.getItem('ace-theme');
    var theme = (saved === 'light' || saved === 'dark') ? saved : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
  } catch (e) {
    document.documentElement.setAttribute('data-theme', 'dark');
  }
})();

// Hàm gắn vào nút bấm [☀/🌙] trên Header / Topbar
function toggleAceTheme() {
  try {
    var current = document.documentElement.getAttribute('data-theme') || 'dark';
    var next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('ace-theme', next);
  } catch (e) {}
}
~~~

---

## 3. Quy tắc Sử dụng 4 File Logo ACE (`static/images/`)

Copy 4 file logo từ `ace-hotel-plan/assets/` sang `ace-hotel/src/main/resources/static/images/`[cite: 1, 2, 3, 4, 10, 11]:

| Tên file ảnh | Vị trí sử dụng trong 4 Phân hệ Giao diện | Ghi chú hiển thị |
|---|---|---|
| `1_logo-day-du_khong-nen.png` | Khu vực Hero của Landing Page (`WF-01`) & Đầu phiếu Hóa đơn VAT (`WF-08`)[cite: 4, 11] | Có biểu tượng 5 ngôi sao dưới chữ HOTEL[cite: 4] |
| `2_logo-ACE_khong-nen.png` | Thanh Header của Public/Customer Layout, đầu Sidebar của PMS/Admin Layout & Favicon[cite: 1, 11] | Gọn gàng, rõ nét ở kích thước chiều cao `32px–40px`[cite: 1, 11] |
| `3_logo-ACE-co-ten_khong-nen.png` | Đầu khung Đăng nhập / Đăng ký (`WF-02`), đầu Phiếu xác nhận QR (`WF-03`) & Footer[cite: 2, 11] | Có dòng chữ `Accommodation, Check-in & Extras`[cite: 2] |
| `4_logo-giong-hien-tai_them-ten.png` | Ảnh OpenGraph / Banner giới thiệu trên nền đen[cite: 3, 11] | Nền đen đặc trưng[cite: 3] |

> **Lưu ý trên Light Mode:** Do logo mang tông vàng champagne (`#D7B68D`), thanh Header của `public-layout.html` và Sidebar của `pms-layout.html` luôn giữ nền tối sang trọng (`#0D0D0D` / `#121212`) ở cả hai chế độ màu để logo luôn nổi bật tuyệt đối[cite: 5, 11].

---

## 4. Hai Khung Layout Thymeleaf (`templates/layout/`)

Để phân tách rõ trải nghiệm giữa **Khách hàng trực tuyến** và **Nhân viên vận hành nội bộ**, hệ thống sử dụng 2 file layout chuẩn[cite: 53]:

### 4.1. Layout 1: `templates/layout/public-layout.html` (Dùng cho `WF-01`, `WF-02`, `WF-03`, `WF-04`)
- **Bố cục:** Thanh Header ngang cố định phía trên (`Top Navbar`) + Vùng nội dung toàn chiều rộng (`Full-width Container`) + Chân trang (`Footer`)[cite: 5, 53].
- **Menu trên Header:**
  - Nếu ở Landing Page (`/`): `Câu chuyện` · `Hạng phòng` · `Dịch vụ` · `Quy trình đặt`[cite: 53].
  - Nếu đã đăng nhập `CUSTOMER`: Thêm liên kết nổi bật **`Đơn đặt của tôi (/customer/dashboard)`** và **`Đặt phòng mới (/booking/search)`**[cite: 53].
  - Góc phải: Nút chuyển đổi `Dark / Light Mode` + Nút **[Đăng nhập / Đăng ký]** (nếu chưa login) hoặc **[Tên người dùng + Đăng xuất]** (nếu đã login)[cite: 5, 53].

### 4.2. Layout 2: `templates/layout/pms-layout.html` (Dùng cho `WF-05` đến `WF-10` của `RECEPTIONIST` & `ADMIN`)
- **Bố cục:** Thanh bên trái cố định (`Sidebar` rộng `260px`) + Thanh trên (`Topbar` hiển thị tiêu đề màn hình, thứ ngày hiện tại định dạng `Thứ Tư, 14/10/2026`, nút đổi theme) + Vùng làm việc chính (`Main Workspace`)[cite: 7, 11, 53].
- **Menu Sidebar tự động hiển thị theo Role (`sec:authorize`):**
  - **Cụm Lễ tân PMS (`hasAnyRole('RECEPTIONIST', 'ADMIN')`):**
    1. `Sơ đồ phòng & Bàn lễ tân` (`/staff/dashboard` – `WF-05`)[cite: 53]
    2. `Check-in QR & Gán phòng sạch` (`/staff/check-in` – `WF-06`)[cite: 53]
    3. `Tiếp nhận Khách Walk-in (0đ cọc)` (`/staff/walk-in` – `WF-06`)[cite: 53]
    4. `Tra cứu Bảng giá & Danh mục` (`/admin/catalog` – Lễ tân chỉ xem)[cite: 23, 53]
  - **Cụm Quản trị viên (`hasRole('ADMIN')`):**
    5. `Báo cáo Doanh thu ++ & Công suất OR%` (`/admin/dashboard` – `WF-10`)[cite: 53]
    6. `Quản trị Hạng phòng, Giá & Dịch vụ` (`/admin/catalog` – `WF-09` toàn quyền CRUD)[cite: 53]

---

## 5. Bộ Component Giao diện Dùng chung (`static/css/components.css`)

| Tên Class CSS | Thành phần giao diện | Quy cách hiển thị chuẩn ACE |
|---|---|---|
| `.btn-primary` | Nút hành động chính | Nền `--primary` (`#FDBF2D`), chữ đen `--on-primary` (`#0A0A0A`), `font-weight: 600`, bo góc `8px`[cite: 11, 37, 46] |
| `.btn-secondary` | Nút phụ / Quay lại | Nền trong suốt, viền `1px solid var(--border)`, chữ `--text`[cite: 11, 37, 46] |
| `.btn-clean` | Nút `[✓ Xác nhận đã dọn sạch]` | Viền và chữ màu cam/xanh nổi bật trên thẻ phòng `DIRTY`[cite: 53] |
| `.badge-status` | Nhãn trạng thái phòng/đơn | Có chấm tròn `8px` phía trước chữ; áp dụng 6 cặp biến `--st-*` và `--st-*-bg`[cite: 11, 37, 46, 53] |
| `.room-card` | Thẻ phòng trên sơ đồ `WF-05` | Hiển thị `P.{roomNumber}`, tầng, hạng phòng, giá T2–T5 / T6–CN, badge trạng thái và cụm nút thao tác theo trạng thái[cite: 7, 11, 53] |
| `.modal-backdrop` | Hộp thoại Pop-up (`WF-04`, `WF-07`) | Nền mờ `rgba(0,0,0,0.75)`, hộp nội dung dùng `--elevated`, viền vàng nhạt, đóng/mở bằng JS nhẹ[cite: 11, 53] |
| `.flash-alert` | Thông báo `MSG-01`..`MSG-35` | Hiển thị `successMessage` (viền xanh lá) hoặc `errorMessage` (viền đỏ) ở đầu vùng nội dung[cite: 10, 11, 30] |

---

## 6. Định dạng Bản in Hóa đơn VAT (`static/css/print.css`)

Khi Lễ tân nhấn nút **[🖨 In Hóa đơn VAT]** trên `WF-08` (gọi `window.print()`)[cite: 8, 28, 53]:
~~~css
@media print {
  .sidebar, .topbar, .no-print, .btn, .flash-alert {
    display: none !important;
  }
  body, .invoice-print-card {
    background: #FFFFFF !important;
    color: #000000 !important;
    border: none !important;
    box-shadow: none !important;
    margin: 0 !important;
    padding: 0 !important;
  }
}
~~~