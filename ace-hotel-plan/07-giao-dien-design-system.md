# 07. Giao diện và design system

Nguồn: bộ **ACE Color System** (Dark Mode và Light Mode) bạn đã có. Mặc định **Dark**, có nút chuyển Light [CHỐT]. Màu vàng thương hiệu `#FDBF2D` giữ nguyên ở cả hai chế độ.

## 1. Bảng màu (token)

| Token | Dark | Light | Dùng cho |
|---|---|---|---|
| `--bg` | `#000000` | `#F7F6F2` | Nền chính |
| `--surface` | `#121212` | `#FFFFFF` | Thẻ, sidebar |
| `--elevated` | `#1C1C1C` | `#FFFFFF` | Modal, dropdown |
| `--border` | `#2A2A2A` | `#E6E3DA` | Viền, đường kẻ |
| `--primary` | `#FDBF2D` | `#FDBF2D` | Nút, thương hiệu |
| `--primary-hover` | `#FFD056` | `#E5A90F` | Khi rê chuột |
| `--primary-pressed` | `#E5A90F` | `#C98F00` | Khi nhấn |
| `--primary-soft` | `#2A2208` | `#FFF4D1` | Badge, highlight |
| `--text` | `#FFFFFF` | `#0A0A0A` | Chữ chính |
| `--text-secondary` | `#A1A1AA` | `#52525B` | Chữ phụ |
| `--text-muted` | `#71717A` | `#71717A` | Placeholder |
| `--primary-text` | `#FDBF2D` | `#B45309` | Link, icon vàng |

Màu trạng thái phòng:

| Trạng thái | Dark | Light |
|---|---|---|
| Phòng trống | `#22C55E` | `#15803D` |
| Đã đặt | `#A78BFA` | `#7C3AED` |
| Đang ở | `#60A5FA` | `#2563EB` |
| Đang dọn / bảo trì | `#9CA3AF` | `#6B7280` |
| Quá hạn | `#F87171` | `#DC2626` |

Chữ trên nút vàng luôn là màu tối (như hình mẫu).

## 2. Cách cài đặt theme

- Khai báo token dưới `:root[data-theme="dark"]` và `:root[data-theme="light"]` trong `tokens.css`; component chỉ dùng `var(--...)`, không ghi mã màu trực tiếp.
- `theme.js` đọc lựa chọn từ `localStorage` và đặt `data-theme` **trước khi vẽ trang** (script nhỏ ở đầu `<head>`) để không bị nháy sáng. Bọc đọc ghi trong `try/catch`.
- Mặc định Dark khi chưa có lựa chọn. Nút chuyển có nhãn truy cập (`aria-label`), dùng chung cho landing page và dashboard.

## 3. Component cần có (P0 trừ khi ghi khác)

| Component | Ghi chú theo hình mẫu |
|---|---|
| Nút | Primary, Hover, Pressed, Secondary (viền), Soft, Disabled |
| Ô nhập, ô ngày | Viền vàng và vòng sáng khi focus; nhãn nằm trên ô |
| Checkbox, công tắc | Vàng khi bật |
| Badge trạng thái | Chấm màu + chữ, nền là màu trạng thái nhạt |
| Ô KPI | Biểu tượng tròn màu + nhãn + số lớn |
| Thẻ phòng | Số phòng, nhãn, loại · số khách, giá `/ đêm`, các chip dịch vụ, nút thao tác |
| Bảng dữ liệu | Hàng tiêu đề nền nhạt, hàng nhấn bằng `--primary-soft`, nút thao tác ở cột cuối |
| Thông báo (flash) | Thành công, lỗi, cảnh báo |
| Hộp xác nhận | Hủy đặt phòng, xóa |

## 4. Chữ và định dạng

- Font UI: **Inter** (như hình mẫu), đặt sẵn trong `static/fonts/`, không gọi CDN. Ghi vào tài liệu tham khảo.
- Tiếng Việt toàn bộ giao diện (NFR-05). Tiền: `1.250.000đ`. Ngày: `12/10/2026`. Thứ: `Thứ Hai, 12/10/2026`.
- Cỡ chữ nội dung tối thiểu 14 px; vùng bấm tối thiểu khoảng 40 px chiều cao.

## 5. Dùng logo

Các file nằm trong `assets/`:

| File | Dùng khi |
|---|---|
| `1_logo-day-du_khong-nen.png` | Landing page (hero hoặc footer), slide, bìa |
| `2_logo-ACE_khong-nen.png` | Header, sidebar, favicon (cắt vuông phần ACE nếu cần) |
| `3_logo-ACE-co-ten_khong-nen.png` | Trang đăng nhập, phần giới thiệu ý nghĩa tên |
| `4_logo-giong-hien-tai_them-ten.png` | Ảnh chia sẻ, slide mở đầu |

Lưu ý:
- Logo là vàng champagne (khoảng `#D7B68D`), **khác** `#FDBF2D` của nút. Dùng logo cho nhận diện, `--primary` cho thành phần tương tác; không đặt sát nhau trong cùng một khối nếu thấy lệch màu.
- Trên theme **Light**, logo vàng nhạt trên nền sáng sẽ khó đọc. Hướng xử lý: giữ header và sidebar nền tối ở cả hai theme, hoặc tạo bản logo đậm hơn cho Light (việc này chưa làm; [P1]).
- Dòng tên "Accommodation, Check-in & Extras" trong file 3 và 4 đang dùng font Caladea tạm. Nếu nhóm có font thương hiệu thì thay.
- Ô vàng ghi "ACE" ở đầu hai hình ACE Color System là biểu tượng ứng dụng mẫu, không phải logo chính.
