# 03. Dashboard

Route: `GET /dashboard` (cần đăng nhập). Ưu tiên **P0** cho khung và menu; các khối thống kê nhanh **P1**. Tham chiếu hình: bảng ACE Color System (Dark / Light) đã có phần mẫu.

## 1. Bố cục

- **Thanh bên (sidebar):** logo ACE, menu theo vai trò, vùng người dùng (họ tên, nhãn vai trò, đăng xuất) ở cuối.
- **Thanh trên (topbar):** tiêu đề trang, ngày hôm nay (định dạng `Thứ Hai, 12/10/2026`), nút đổi theme.
- **Nội dung:** theo thứ tự: KPI → Tình trạng phòng → Check-in / Check-out hôm nay.
- Trên điện thoại sidebar thu thành ngăn kéo.

## 2. Menu theo vai trò

| Mục menu | Lễ tân | Quản lý | Ưu tiên |
|---|:-:|:-:|---|
| Dashboard | ✓ | ✓ | P0 |
| Tìm phòng & đặt phòng | ✓ | ✓ | P0 |
| Danh sách đặt phòng | ✓ | ✓ | P0 |
| Check-in | ✓ | ✓ | P0 |
| Check-out & thanh toán | ✓ | ✓ | P0 |
| Dịch vụ phòng đang ở | ✓ | ✓ | P0 |
| Phòng (xem) | ✓ | ✓ | P0 |
| Quản lý phòng và loại phòng | | ✓ | P0 |
| Danh mục dịch vụ | | ✓ | P1 |
| Thống kê | | ✓ | P2 |

Lễ tân không thấy các mục dành cho quản lý, và vẫn bị chặn ở máy chủ nếu gõ tay đường dẫn.

## 3. Khối KPI [ĐỀ XUẤT định nghĩa]

| Ô | Cách tính | Màu nhấn |
|---|---|---|
| Phòng trống | Số phòng có `rooms.status = AVAILABLE` | Xanh lá |
| Check-in hôm nay | Số đặt phòng có `check_in_date = hôm nay`, trạng thái BOOKED hoặc CHECKED_IN | Xanh dương |
| Check-out hôm nay | Số đặt phòng có `check_out_date = hôm nay`, trạng thái CHECKED_IN hoặc CHECKED_OUT | Tím |
| Dịch vụ đi kèm | Số dòng `service_orders` có `ordered_at` trong hôm nay | Vàng |

Số trong hình mẫu (24, 12, 9, 38) chỉ là minh họa. [CẦN XÁC NHẬN] định nghĩa ô "Dịch vụ đi kèm" với nhóm.

## 4. Tình trạng phòng

Thẻ phòng hiển thị: số phòng, loại, sức chứa, giá một đêm, nhãn trạng thái, các dịch vụ đang dùng (nếu có), nút thao tác theo trạng thái.

**Trạng thái hiển thị là trạng thái suy ra**, vì CSDL chỉ có ba giá trị `rooms.status` (AVAILABLE, OCCUPIED, MAINTENANCE). [ĐỀ XUẤT]

| Nhãn hiển thị | Điều kiện suy ra |
|---|---|
| Phòng trống | `rooms.status = AVAILABLE` và không có đặt phòng BOOKED nào bắt đầu hôm nay |
| Đã đặt | `rooms.status = AVAILABLE` và có đặt phòng BOOKED với `check_in_date` là hôm nay |
| Đang ở | Có đặt phòng CHECKED_IN (phòng OCCUPIED) |
| Quá hạn | Có đặt phòng CHECKED_IN mà `check_out_date` đã qua so với hôm nay |
| Bảo trì | `rooms.status = MAINTENANCE` |

Hình mẫu có thêm nhãn "Đang dọn": CSDL hiện chưa có trạng thái này, nên nhãn mẫu đổi thành "Bảo trì" hoặc bổ sung trạng thái nếu nhóm đồng ý (xem `10-cau-hoi-mo.md`, mục 3).

| Nhãn | Nút trên thẻ |
|---|---|
| Phòng trống | Đặt phòng |
| Đã đặt | Xem đặt phòng |
| Đang ở | Thêm dịch vụ / Check-out |
| Quá hạn | Check-out |
| Bảo trì | (không có thao tác nghiệp vụ; quản lý sửa trạng thái trong màn hình phòng) |

## 5. Bảng "Check-in / Check-out hôm nay"

Cột: Mã đặt phòng (hiển thị `BK-` + số), Khách hàng, Phòng (số · loại), Check-in, Check-out, Trạng thái, Thao tác.

| Trạng thái dòng | Nút |
|---|---|
| BOOKED | **Check-in** |
| CHECKED_IN (đến hạn hôm nay) hoặc Quá hạn | **Check-out** |
| CHECKED_IN (chưa đến hạn) | Xem |
| CHECKED_OUT | Xem hóa đơn |

Dòng cần xử lý ngay (đến giờ nhận phòng hoặc quá hạn) được nhấn bằng màu `Primary soft`.

## 6. Tiêu chí xong

- [ ] Menu đúng theo vai trò; mục không thuộc quyền không xuất hiện.
- [ ] Bốn ô KPI tính đúng so với dữ liệu mẫu (đối chiếu bằng truy vấn tay).
- [ ] Thẻ phòng và bảng hôm nay đổi đúng sau khi đặt phòng, check-in, check-out.
- [ ] Mỗi nút dẫn tới đúng màn hình nghiệp vụ và mang sẵn đặt phòng đã chọn.
- [ ] Trạng thái rỗng có thông báo thân thiện (ví dụ "Hôm nay chưa có lượt nhận phòng").
- [ ] Dark và Light đều đúng bảng màu.
