# 02. DANH SÁCH CHỨC NĂNG (PRODUCT BACKLOG)

Nguồn: `yeu-cau-fr-nfr-moscow.md` và `chot-pham-vi-actor-usecase-bang.md`. Đề yêu cầu tối thiểu 10-15 FR; bản nháp có 17 (có thể gộp).

## 1. Chức năng theo module (đã nhóm theo use case)
| Module | UC | Mô tả | Người phụ trách | Cài Java |
|---|---|---|---|---|
| Đăng nhập, phân quyền | UC01 | Đăng nhập/đăng xuất, hiển thị menu theo vai trò | Kiệt | Có |
| Phòng và loại phòng | UC02 | Thêm/sửa/xóa/xem/tìm phòng và loại phòng | Hoàng | Có |
| Tìm phòng trống, đặt phòng | UC03 | Chọn ngày, xem phòng trống, tìm/tạo khách, tạo đặt phòng, hủy | Hoàng | Có |
| Check-in | UC04 | Nhận khách theo đặt phòng | Phong | Có |
| Check-out, thanh toán | UC05 | Tính tiền, thanh toán, hóa đơn | Phong | Có (gộp với UC04) |
| Dịch vụ đi kèm | UC06 | Thêm dịch vụ cho phòng đang ở | Kiệt | Có |
| Thống kê | UC07 | Doanh thu theo tháng/loại phòng, công suất | Khoa (đặc tả) | Chỉ đặc tả |

## 2. Backlog ưu tiên (MoSCoW)
| Mã | Yêu cầu (rút gọn) | UC | MoSCoW | Sprint |
|---|---|---|---|---|
| FR-01 | Đăng nhập/đăng xuất | UC01 | Must | Tuần 2 |
| FR-02 | Phân quyền lễ tân/quản lý | UC01 | Must | Tuần 2 |
| FR-03 | CRUD loại phòng | UC02 | Must | Tuần 2 |
| FR-04 | CRUD phòng | UC02 | Must | Tuần 2 |
| FR-05 | Tìm kiếm, lọc phòng | UC02 | Must | Tuần 2 |
| FR-06 | Tìm phòng trống theo ngày, loại phòng | UC03 | Must | Tuần 2 |
| FR-07 | Tìm/tạo khách hàng | UC03 | Must | Tuần 2 |
| FR-08 | Tạo đặt phòng, chặn trùng lịch | UC03 | Must | Tuần 2 |
| FR-09 | Hủy đặt phòng (trạng thái BOOKED) | UC03 | Should | Tuần 3 |
| FR-10 | Check-in | UC04 | Must | Tuần 2 |
| FR-11 | Thêm dịch vụ cho phòng đang ở | UC06 | Must | Tuần 2 (cắt đầu tiên nếu trễ) |
| FR-12 | Quản lý danh mục dịch vụ | UC06 | Should | Tuần 3 |
| FR-13 | Tự tính tiền khi check-out | UC05 | Must | Tuần 2 |
| FR-14 | Thanh toán, lập hóa đơn, trả phòng | UC05 | Must | Tuần 2 |
| FR-15 | In/xuất hóa đơn | UC05 | Could | Nếu còn thời gian |
| FR-16 | Doanh thu theo tháng, loại phòng | UC07 | Should | Chỉ đặc tả |
| FR-17 | Công suất phòng | UC07 | Could | Chỉ đặc tả |
| FR-18 | Trang tổng quan (Dashboard): 4 thẻ chỉ số & danh sách Check-in/out hôm nay | UC01/04/05 | Should | Tuần 2 |

## 3. Won't have (trong phạm vi này)
Khách tự đặt online; thanh toán trực tuyến; kênh bên ngoài (Booking, Agoda); nhiều chi nhánh; app di động; giảm giá/phụ thu/thuế; nhiều phòng một đơn.

## 4. Chức năng trên giao diện ACE đã chốt quyết định (D1 – D4)
Các mục xuất hiện trong thiết kế giao diện (`assets/ACE_Light_Mode.png`, `assets/ACE_Dark_Mode.png`) đã được chốt chính thức tại [12-phan-quyen-rbac-trang-thai-thong-bao.md](file:///D:/ACE/docs/12-phan-quyen-rbac-trang-thai-thong-bao.md):
| Mục trên giao diện | Quyết định đã chốt |
|---|---|
| Dashboard: Phòng trống / Check-in hôm nay / Check-out hôm nay / Dịch vụ đi kèm | Đã thêm **FR-18 "Trang tổng quan"** (Should) với công thức SQL chuẩn theo **D4** |
| Trạng thái "Quá hạn" & "Đã đặt" | Giữ nguyên cột `status` trong DB; suy diễn động trên giao diện theo **D1** và **D2** |
| Công tắc "Late check-out", tag "Ăn sáng / Đưa đón" | Bỏ công tắc "Late check-out"; chuyển "Ăn sáng / Đưa đón / Spa / Giặt ủi" thành danh mục bảng `services` theo **D3** |
