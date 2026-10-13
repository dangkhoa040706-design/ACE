# 03. YÊU CẦU CHI TIẾT: USER STORY VÀ TIÊU CHÍ NGHIỆM THU (SRS RÚT GỌN)

> Đây là **bản tóm tắt để Khoa code**, không thay thế đặc tả use case đầy đủ (actor, tiền/hậu điều kiện, luồng chính/phụ/ngoại lệ) mà Hoàng, Phong, Kiệt viết trong quyển báo cáo (mục 2.5).

## Quy tắc nghiệp vụ dùng chung
- BR1: một đặt phòng gồm **một phòng** cho một khoảng ngày.
- BR2: không đặt trùng khoảng ngày với đặt phòng chưa hủy (trùng khi `cũ.check_in < mới.check_out` và `cũ.check_out > mới.check_in`).
- BR3: ngày check-out sau ngày check-in ít nhất 1 đêm.
- BR4: tiền phòng = số đêm × giá một đêm của loại phòng (tối thiểu 1 đêm).
- BR5: chỉ thêm dịch vụ khi booking ở CHECKED_IN.
- BR6: hóa đơn = tiền phòng + tổng tiền dịch vụ (không giảm giá, phụ thu, thuế).
- BR7: chỉ hủy được booking ở BOOKED.
- Trạng thái: `rooms.status` AVAILABLE/OCCUPIED/MAINTENANCE; `bookings.status` BOOKED/CHECKED_IN/CHECKED_OUT/CANCELLED; `users.role` RECEPTIONIST/MANAGER.

---
## UC01 – Đăng nhập và phân quyền (Kiệt) · FR-01, FR-02
**Story:** Là lễ tân hoặc quản lý, tôi đăng nhập để dùng đúng chức năng theo vai trò.
- Đúng tài khoản → vào trang tổng quan; sai → hiện "Sai tên đăng nhập hoặc mật khẩu", không nói rõ sai trường nào.
- Lễ tân không thấy menu quản lý phòng/thống kê; gõ trực tiếp URL cũng bị chặn (403 hoặc chuyển về trang chính).
- Chưa đăng nhập mà vào trang nghiệp vụ → chuyển về trang đăng nhập.
- Mật khẩu lưu dạng băm BCrypt.

## UC02 – Quản lý phòng và loại phòng (Hoàng) · FR-03, 04, 05
**Story:** Là quản lý, tôi thêm/sửa/xóa/tìm phòng và loại phòng để cập nhật danh mục khách sạn.
- Thêm loại phòng với tên, giá > 0, sức chứa ≥ 1; thiếu trường → báo lỗi từng trường.
- Số phòng không được trùng.
- Không xóa phòng đang có booking BOOKED/CHECKED_IN; không xóa loại phòng đang có phòng.
- Lọc theo số phòng, loại phòng, trạng thái; không có kết quả → hiện "Không tìm thấy phòng".
- Lễ tân chỉ xem danh sách, không thấy nút thêm/sửa/xóa.

## UC03 – Tìm phòng trống và đặt phòng (Hoàng) · FR-06, 07, 08, 09
**Story:** Là lễ tân, tôi tìm phòng trống theo ngày và đặt cho khách.
- Chọn ngày nhận, trả (trả > nhận) và loại phòng → danh sách chỉ gồm phòng không bị trùng BR2 và không MAINTENANCE.
- Tìm khách theo tên, SĐT hoặc số giấy tờ; chưa có → tạo mới ngay trong luồng đặt phòng.
- Tạo booking → trạng thái BOOKED, ghi `created_by` là lễ tân đang đăng nhập.
- Nếu phòng vừa bị người khác đặt trùng lúc bấm xác nhận → từ chối, báo lỗi, quay lại danh sách.
- Hủy booking BOOKED → CANCELLED; booking khác BOOKED không có nút hủy.

## UC04 – Check-in (Phong) · FR-10
**Story:** Là lễ tân, tôi nhận khách theo đặt phòng đã có.
- Chỉ booking `BOOKED` và có `check_in_date <= LocalDate.now()` mới check-in được (`D7`).
- Check-in → `actual_check_in` = giờ hiện tại (`NOW()`), booking `CHECKED_IN`, phòng `OCCUPIED`.
- Phòng đang `MAINTENANCE` hoặc `OCCUPIED` → từ chối, báo lý do (`MSG-24`).
- Khách vãng lai không đặt trước (`D8`): Lễ tân tạo booking ngày hôm nay (`UC03`) rồi bấm Check-in ngay (`UC04`).

## UC05 – Check-out và thanh toán (Phong) · FR-13, 14, 15
**Story:** Là lễ tân, tôi tính tiền và thanh toán khi khách trả phòng.
- Hiển thị: số đêm cố định theo lịch đặt ban đầu (`D7`), đơn giá, tiền phòng, từng dịch vụ, tổng dịch vụ, **tổng cộng** (`BR4`, `BR6`).
- Chọn hình thức thanh toán (`CASH`, `CARD`, `TRANSFER` – `D9`) rồi xác nhận → tạo hóa đơn (1-1 với booking), `actual_check_out = NOW()`, booking `CHECKED_OUT`, phòng về `AVAILABLE`.
- Một booking không có hai hóa đơn (bấm xác nhận hai lần không tạo trùng – Idempotency).
- Xem chi tiết hóa đơn tại `S13` và hỗ trợ nút in `window.print()` (`FR-15`, `D20`).

## UC06 – Đăng ký dịch vụ đi kèm (Kiệt) · FR-11, 12
**Story:** Là lễ tân, tôi thêm dịch vụ cho phòng đang ở để cộng vào hóa đơn.
- Chỉ chọn được booking `CHECKED_IN` (`BR5`).
- Số lượng ≥ 1; đơn giá lấy từ danh mục tại thời điểm thêm và **lưu vào `service_orders.unit_price`** (đổi giá sau không ảnh hưởng đơn cũ).
- Cho phép **xóa dòng dịch vụ** thêm nhầm khi booking còn `CHECKED_IN` (`D21`).
- Dịch vụ vừa thêm hiện ngay trong chi tiết booking và được cộng vào tổng ở `UC05`.

## UC07 – Thống kê (Khoa đặc tả, không code) · FR-16, 17
**Story:** Là quản lý, tôi xem doanh thu theo tháng/loại phòng và công suất phòng.
- Doanh thu tính từ `invoices.paid_at` theo tháng và nhóm theo loại phòng.
- Công suất = số đêm phòng đã ở ÷ (số phòng × số ngày trong khoảng) theo khoảng ngày chọn.
- Chỉ quản lý truy cập.

## Yêu cầu phi chức năng (tóm tắt, chi tiết xem file FR/NFR)
Must: NFR-02 băm mật khẩu, NFR-03 kiểm quyền phía server, NFR-04 toàn vẹn dữ liệu, NFR-07 chạy JDK 21 trên Chrome/Edge.
