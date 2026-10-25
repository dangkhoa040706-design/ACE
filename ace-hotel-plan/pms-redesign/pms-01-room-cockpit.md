# ACE HOTEL PMS - THIẾT KẾ ĐIỀU HÀNH SƠ ĐỒ PHÒNG COCKPIT
**Phiên bản:** 2.0 (Mews PMS & Linear Luxury Architecture)  
**Tài liệu:** `pms-01-room-cockpit.md`  
**Mục tiêu:** Xóa bỏ hoàn toàn cảm giác "AI tự sinh", tinh giản tối đa thao tác của Lễ tân & Quản trị viên trên màn hình Laptop (Zero-Scroll Experience).

---

## 1. Bối Cảnh & Các Vấn Đề Đã Nhận Diện Ở Bản Cũ

1. **Lạm dụng Emoji hệ điều hành:** Các biểu tượng màu (`🏨`, `📱`, `🚶`, `📋`, `🛏️`, `🔑`, `🧹`, `🔧`...) tạo cảm giác nghiệp dư, không đồng nhất giữa Windows/macOS/iOS.
2. **Chiếm dụng chiều dọc:** 4 khối KPI dày đặc đẩy phần sơ đồ phòng và đơn chờ xuống quá sâu.
3. **Lưới 4 cột phẳng phá vỡ trực quan không gian thực tế:** Phòng tầng 2 (`P.201`) bị xếp chung hàng đầu với các phòng tầng 1 (`P.101, P.102, P.103`).
4. **Văn phong dài dòng trong thẻ phòng:** Các câu giải thích nghiệp vụ lặp đi lặp lại làm rối mắt Lễ tân khi cần thao tác nhanh.
5. **Hàng đợi đơn đặt trước (RESERVED) bị đẩy xuống đáy:** Lễ tân có khách tới nhận phòng bằng mã QR phải cuộn chuột qua 3 màn hình mới thấy bảng đơn chờ.

---

## 2. Kiến Trúc Mới: Architectural Floor Matrix & Split Command Cockpit

### 2.1. Quy Chuẩn Không Gian Zero-Scroll Cho Màn Hình Laptop (1366x768 & 1920x1080)
- **Sidebar:** Chiều rộng cố định `225px` (giảm từ `260px`), nền kính sẫm mờ `rgba(13, 18, 28, 0.98)` (Dark) / `rgba(255, 255, 255, 0.96)` (Light), viền phải mảnh `1px solid rgba(253, 191, 45, 0.18)`.
- **Topbar:** Chiều cao `54px` (giảm từ `64px`), cố định trên cùng, tích hợp tiêu đề phân hệ, đồng hồ ngày realtime tiếng Việt và bộ chuyển chế độ Sáng/Tối.
- **Thanh Điều Hành Kép (Command & Telemetry Bar):** Chiều cao tối ưu `56px - 62px`.
- **Bố cục chính chia 2 cột bất đối xứng (Split-Screen Cockpit):**
  - **Cột Trái (~70% chiều ngang):** Ma trận 4 Tầng Vật lý x 3 phòng/tầng.
  - **Cột Phải (~30% chiều ngang):** Dock Hàng đợi Đơn đặt trước (`RESERVED`) & Widget Chuẩn công suất khai thác (`BR8`).

---

## 3. Hệ Thống Icon Vector SVG Thay Thế Toàn Diện 100% Emoji

Toàn bộ icon chuyển sang chuẩn **Feather / Linear Vector SVG** mảnh, đồng nhất phong cách:
- Kích thước: `17px x 17px`
- Nét vẽ: `stroke-width="1.75"`
- Màu sắc: Kế thừa `currentColor`, kích hoạt trạng thái active với sắc vàng Champagne `#FDBF2D` (Dark) / Vàng Hổ Phách `#B45309` (Light).

| Vị trí / Mục | Emoji Cũ | Chuẩn SVG Thay Thế | Thuộc tính / Ý nghĩa |
| :--- | :---: | :--- | :--- |
| **Sidebar: Sơ đồ phòng** | 🏨 | Grid 4 ô vuông (`rect 18x18`, `path 3h18`, `path 9v21`) | Bản đồ mặt bằng khách sạn |
| **Sidebar: Check-in QR** | 📱 | QR Code Matrix Vector 3 ô định vị | Quét mã phiếu đặt phòng QR |
| **Sidebar: Khách Walk-in** | 🚶 | User Plus Vector (`user` + `plus`) | Tiếp nhận khách vãng lai trực tiếp |
| **Sidebar: Bảng giá & DV** | 📋 | Price Tag / Layers Vector | Danh mục bảng giá niêm yết |
| **Sidebar: Báo cáo Doanh thu** | 📊 | Bar Chart 3 cột tăng trưởng | Báo cáo tài chính & OR% |
| **Sidebar: Cài đặt Hệ thống** | ⚙️ | Sliders / Cog Vector kỹ thuật | Quản trị thiết lập CSDL |
| **Sidebar: Đăng xuất** | ⎋ / 🚫 | Log Out Vector (`box` + `arrow-right`) | Thoát phiên làm việc |
| **Cockpit: Thẻ Trống sạch** | 🟢 🛏️ | Chấm tròn xanh `#10B981` + Tag mỏng | Phòng sẵn sàng đón khách |
| **Cockpit: Thẻ Đang ở** | 🔵 🔑 | Chấm tròn xanh dương `#3B82F6` + Tag mỏng | Phòng đang lưu trú |
| **Cockpit: Thẻ Chờ dọn** | 🟠 🧹 | Chấm tròn cam `#F59E0B` + Stripe Pattern | Phòng bẩn chờ buồng phòng |
| **Cockpit: Thẻ Bảo trì** | ⚪ 🔧 | Chấm tròn xám `#94A3B8` + Wrench Vector | Khóa kỹ thuật |
| **Cockpit: Tìm kiếm QR** | 🔍 📱 | Search Vector + QR Mini Vector | Tra cứu nhanh đơn đặt |

---

## 4. Thanh Điều Hành Kép (Command & Telemetry Bar)

### 4.1. Cụm Trái: 5 Nút Filter Pills Tương Tác Trực Tiếp
- `[Tất cả • 12]` (Active mặc định)
- `[● Trống sạch • 6]` (Chấm xanh ngọc `#10B981`)
- `[● Đang ở • 3]` (Chấm xanh dương `#3B82F6`, kèm badge đỏ `1 Quá hạn` nếu có)
- `[● Chờ dọn • 2]` (Chấm hổ phách `#F59E0B`)
- `[● Bảo trì • 1]` (Chấm xám đá `#94A3B8`)

**Cơ chế JavaScript Live Filter:**
- Bấm vào pill bất kỳ, hệ thống lập tức lọc các thẻ phòng mang `data-status`.
- Thẻ không khớp trạng thái được áp dụng hiệu ứng mờ cao cấp (`opacity: 0.18; filter: grayscale(0.85); transform: scale(0.98);`), giúp Lễ tân định vị tức thì các phòng mục tiêu mà không làm mất tương quan vị trí không gian của toàn bộ khách sạn.

### 4.2. Cụm Phải: Tra Cứu Nhanh & Nút Đón Khách Walk-in
- **Input Tra cứu:** Tích hợp icon SVG mã QR và kính lúp bên trong, hỗ trợ quét mã vạch bằng máy đọc QR cầm tay hoặc gõ mã `BK-2402`, số điện thoại khách.
- **Nút Walk-in:** Đón khách vãng lai nhận phòng ngay với 0đ tiền cọc theo đúng quy chuẩn nghiệp vụ `WF-06`.

---

## 5. Ma Trận 4 Tầng Vật Lý (`Architectural Floor Matrix`)

### 5.1. Cấu Trúc Hàng Ngang Theo Tầng
Mỗi tầng là một hàng `.floor-row` khép kín:
- **Nhãn Tầng (`.floor-badge` - rộng `54px`):** Font Monospace đậm `#FDBF2D` (`FL 01`, `FL 02`, `FL 03`, `FL 04`), kèm chữ nhỏ `Tầng 1`, `Tầng 2`...
- **Lưới 3 Phòng (`.floor-rooms-grid`):** Đúng 3 phòng vật lý của tầng đó.
  - Tầng 1: `P.101` (Standard), `P.102` (Standard), `P.103` (Superior)
  - Tầng 2: `P.201` (Deluxe), `P.202` (Superior), `P.205` (Standard)
  - Tầng 3: `P.301` (Deluxe), `P.302` (Deluxe), `P.305` (Family)
  - Tầng 4: `P.401` (Family), `P.418` (Suite), `P.420` (Suite)

### 5.2. Thẻ Phòng Kỹ Thuật Số (`.room-keycard` - Chiều cao ~108px - 115px)
- **Số phòng & Hạng phòng:** Số phòng font Tabular Numbers lớn (`P.101`), tag hạng phòng mạ vàng đồng thanh mảnh.
- **Badge trạng thái:** Đèn LED chỉ báo trạng thái hình viên thuốc nhỏ gọn ở góc phải.
- **Trạng thái AVAILABLE:**
  - Hiển thị giá kép: `T2-T5: 780,000đ • T6-CN: 950,000đ`.
  - Nút nhanh toàn chiều ngang: `[+ Nhận Walk-in]`.
- **Trạng thái OCCUPIED:**
  - Tên khách hàng in đậm + Mã đơn monospace vàng + Hạn trả phòng.
  - Nếu quá hạn (`P.418`): Viền thẻ đổi sang Đỏ Ruby `#EF4444`, tag `Quá hạn trả` phát sáng cảnh báo.
  - 2 nút hành động tinh gọn: `[Room Folio]` (nạp dịch vụ phát sinh) và `[Check-out]` (quyết toán trả phòng).
- **Trạng thái DIRTY (`P.102, P.401`):**
  - Nền hoa văn sọc chéo kiến trúc cảnh báo.
  - Nhãn ngắn gọn `Khóa gán phòng • Chờ dọn`.
  - Nút 1-click `[✓ Xác nhận đã dọn sạch]` để chuyển tức thì sang `AVAILABLE`.
- **Trạng thái MAINTENANCE (`P.103`):**
  - Nền sọc chéo xám kỹ thuật, nút bị khóa mờ `[Đang bảo trì thiết bị]`.

---

## 6. Hàng Đợi Đơn Đặt Trước (`Live Reserved Queue Dock`)

- **Vị trí:** Cột phải rộng `30%`, hiển thị song song ngay cạnh ma trận phòng.
- **Thẻ Boarding Pass VIP (`.reserved-pass-card`):**
  - Mã đơn `BK-2402` monospace vàng nổi bật, kèm badge hạng phòng và khoảng ngày lưu trú.
  - Tên khách + Số điện thoại.
  - Số tiền cọc đã thanh toán (30% theo quy định `BR4`).
  - Nút chuyển tiếp `[Quét QR & Gán Phòng →]` đưa thẳng tới màn hình gán phòng thực tế `WF-06`.
- **Widget Đo Lường OR% (`BR8`):**
  - Hiển thị tỷ lệ lấp đầy phòng tức thì: `OR% = (Số phòng OCCUPIED / Số phòng khả dụng không bảo trì) * 100%`.
  - Thanh tiến trình trực quan kèm liên kết nhanh tới Báo cáo Doanh thu Admin.

---

## 7. Quy Chuẩn Màu Sắc Luxury (Dark & Light Mode)

| Thành phần | Dark Mode (Obsidian & Gold) | Light Mode (Porcelain & Bronze) |
| :--- | :--- | :--- |
| **Nền Cockpit** | `#080A0F` | `#F8F9FA` |
| **Thẻ Phòng** | Bề mặt than đá `#0F141F`, viền `rgba(253, 191, 45, 0.2)` | Bề mặt sứ ngà `#FFFFFF`, viền `rgba(180, 83, 9, 0.2)` |
| **Thẻ Quá hạn** | Nền `#1A0F12`, viền `#EF4444` | Nền `#FEF2F2`, viền `#DC2626` |
| **Sọc chéo Chờ dọn** | Sọc vàng cam trên nền tối | Sọc hổ phách nhạt trên nền trắng sứ |
| **Sọc chéo Bảo trì** | Sọc xám kỹ thuật `#1E293B` | Sọc xám ngà `#E2E8F0` |
| **Boarding Pass Cột Phải**| Nền nâng cao `#131926`, viền vàng đồng | Nền trắng sứ `#FFFFFF`, bóng nổi dịu mắt |
