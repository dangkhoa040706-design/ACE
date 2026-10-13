# 02. Đăng nhập và phân quyền (UC01)

Liên quan: FR-01, FR-02, NFR-02, NFR-03. Ưu tiên **P0**. Người đặc tả trong báo cáo: Kiệt. Người code: Khoa.

## 1. Luồng

1. Người dùng bấm **Đăng nhập** ở landing page → `GET /login`.
2. Nhập tên đăng nhập và mật khẩu → `POST /login`.
3. Đúng: chuyển tới `/dashboard`. Sai: ở lại `/login`, báo lỗi bằng tiếng Việt, không nói rõ sai tên hay sai mật khẩu.
4. Đã đăng nhập mà vào `/login`: chuyển thẳng `/dashboard`.
5. Chưa đăng nhập mà vào trang nghiệp vụ: chuyển về `/login`.
6. Đăng nhập rồi nhưng sai vai trò: trang **403** tiếng Việt, có nút quay về dashboard.
7. Đăng xuất: `POST /logout` → về landing page `/`.

## 2. Vai trò [CHỐT]

`users.role`: `RECEPTIONIST` (lễ tân), `MANAGER` (quản lý).

## 3. Ma trận quyền

| Chức năng | Công khai | Lễ tân | Quản lý | Nguồn |
|---|:-:|:-:|:-:|---|
| Landing page `/` | ✓ | ✓ | ✓ | [ĐỀ XUẤT] |
| Dashboard | | ✓ | ✓ | [ĐỀ XUẤT] |
| Tìm phòng trống, đặt phòng, tìm / tạo khách hàng | | ✓ | ✓ | [CHỐT] |
| Hủy đặt phòng (trạng thái BOOKED) | | ✓ | ✓ | [ĐỀ XUẤT] (file chốt chưa nêu rõ ai hủy) |
| Check-in | | ✓ | ✓ | [CHỐT] |
| Thêm dịch vụ cho phòng đang ở | | ✓ | ✓ | [CHỐT] |
| Check-out, thanh toán, in hóa đơn | | ✓ | ✓ | [CHỐT] |
| Xem danh sách phòng | | ✓ | ✓ | [CHỐT] ("chỉ xem") |
| Thêm / sửa / xóa phòng và loại phòng | | | ✓ | [CHỐT] |
| Quản lý danh mục dịch vụ (FR-12) | | | ✓ | [ĐỀ XUẤT] (nháp FR chưa nêu vai trò) |
| Thống kê (UC07) | | | ✓ | [CHỐT] |

## 4. Triển khai [ĐỀ XUẤT]

- Spring Security, form login, trang `/login` tự làm bằng Thymeleaf theo design system.
- `BCryptPasswordEncoder`; cột `password_hash` lưu chuỗi băm, **không lưu mật khẩu thô** (NFR-02).
- Quyền kiểm tra ở **phía máy chủ** bằng quy tắc URL và `@PreAuthorize` (NFR-03); ẩn nút trên giao diện chỉ để gọn, không thay cho kiểm tra quyền.
- CSRF bật mặc định, mọi form POST kèm token Thymeleaf.
- Công khai: `/`, `/login`, `/css/**`, `/js/**`, `/images/**`, `/fonts/**`, endpoint đọc công khai của landing page. Mọi đường dẫn khác cần đăng nhập.

## 5. Tài khoản demo

| Tên đăng nhập | Vai trò | Họ tên mẫu |
|---|---|---|
| `letan` | RECEPTIONIST | Lễ tân Demo |
| `quanly` | MANAGER | Quản lý Demo |

Mật khẩu ví dụ `Ace@123` (đổi nếu muốn). Ghi vào README của repo; trong `sample-data.sql` chỉ lưu bản băm BCrypt.

## 6. Kiểm thử

- [ ] Đăng nhập đúng cả hai vai trò.
- [ ] Sai mật khẩu, bỏ trống, tên không tồn tại: đều báo lỗi đúng.
- [ ] Lễ tân gõ tay `/rooms/new` hoặc `/reports`: bị 403.
- [ ] Chưa đăng nhập gõ `/dashboard`, `/bookings`: về `/login`.
- [ ] Đăng xuất xong bấm nút Back không xem lại được trang nghiệp vụ.
- [ ] Trong CSDL không có mật khẩu dạng chữ thô.
