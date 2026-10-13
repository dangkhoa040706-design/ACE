# 04. Màn hình nghiệp vụ (UC02 đến UC07)

Mỗi màn hình ghi: use case, FR, ai dùng, trường, kiểm tra hợp lệ, ghi vào bảng, tiêu chí xong. Tên bảng và giá trị trạng thái theo `05-csdl.md`. Màn hình cuối cùng phải **bám wireframe của Kiệt** (cuối Tuần 1).

## Quy tắc nghiệp vụ dùng chung [CHỐT]

1. Một đặt phòng gồm **một phòng** cho một khoảng ngày.
2. Một phòng **không được đặt trùng** khoảng ngày với đặt phòng khác chưa hủy.
3. Ngày check-out phải sau ngày check-in ít nhất 1 đêm.
4. Tiền phòng = số đêm × giá một đêm của loại phòng; số đêm tối thiểu là 1.
5. Chỉ thêm dịch vụ khi đặt phòng đang ở trạng thái CHECKED_IN.
6. Hóa đơn = tiền phòng + tổng tiền dịch vụ. Không giảm giá, phụ thu, thuế.
7. Chỉ hủy được đặt phòng ở trạng thái BOOKED.

Cách hiểu cụ thể cho rule 2 và rule 4 xem `10-cau-hoi-mo.md` (mục 1 và 2).

---

## UC02 — Quản lý phòng và loại phòng (P0)

### S02. Danh sách phòng — `/rooms`
FR-04, FR-05. Lễ tân và quản lý xem; chỉ quản lý có nút Thêm / Sửa / Xóa.
- Bộ lọc: số phòng (tìm gần đúng), loại phòng, trạng thái.
- Cột: số phòng, tầng, loại, giá một đêm, trạng thái.
- [ ] Lọc kết hợp nhiều điều kiện đúng.

### S03. Form phòng — `/rooms/new`, `/rooms/{id}/edit` (quản lý)
- Trường: số phòng, tầng, loại phòng (chọn), trạng thái (AVAILABLE, OCCUPIED, MAINTENANCE).
- Kiểm tra: số phòng bắt buộc và **duy nhất**; loại phòng bắt buộc; tầng là số nguyên không âm.
- Xóa: **chặn** nếu phòng đã có đặt phòng (NFR-04), báo "Phòng đã có đặt phòng, không thể xóa".
- [ ] Thêm, sửa, xóa hoạt động; xóa bị chặn đúng.

### S04. Loại phòng — `/room-types` (quản lý)
FR-03.
- Trường: tên, giá một đêm (> 0), sức chứa (≥ 1), mô tả.
- Xóa: chặn nếu còn phòng thuộc loại này.
- [ ] Giá hiển thị dạng `1.250.000đ`.

---

## UC03 — Tìm phòng trống và đặt phòng (P0)

### S05. Tìm phòng trống — `/bookings/search`
FR-06.
- Nhập: ngày nhận, ngày trả, loại phòng (tùy chọn).
- Kiểm tra: ngày trả sau ngày nhận ít nhất 1 đêm; ngày nhận không ở quá khứ [ĐỀ XUẤT].
- Kết quả: các phòng **không bảo trì** và **không trùng** khoảng ngày với đặt phòng chưa hủy; mỗi dòng có số phòng, loại, sức chứa, giá, số đêm, tạm tính tiền phòng, nút **Đặt**.
- [ ] Kết quả đúng với bộ dữ liệu mẫu có sẵn các đặt phòng chồng lịch.

### S06. Khách hàng — trong luồng đặt phòng
FR-07.
- Tìm theo tên, số điện thoại, số giấy tờ; không có thì **tạo mới** ngay trên màn hình.
- Trường: họ tên (bắt buộc), số điện thoại (bắt buộc), số giấy tờ, email (tùy chọn).
- [CẦN XÁC NHẬN] số giấy tờ có bắt buộc và duy nhất không; đề xuất duy nhất nếu có nhập.

### S07. Xác nhận đặt phòng — `POST /bookings`
FR-08.
- Hiển thị tóm tắt: khách, phòng, ngày, số đêm, tạm tính.
- Tạo `bookings` trạng thái BOOKED, `created_by` = người đăng nhập, `created_at` = bây giờ.
- **Kiểm tra trùng lịch lại ở máy chủ trong cùng giao dịch** trước khi lưu; nếu vừa bị đặt mất thì báo lỗi rõ ràng và quay về kết quả tìm.
- [ ] Hai đặt phòng chồng lịch cùng phòng: cái thứ hai bị từ chối.

### S08. Danh sách và chi tiết đặt phòng — `/bookings`, `/bookings/{id}`
FR-09.
- Lọc theo trạng thái, ngày, tên khách; chi tiết có các dịch vụ và hóa đơn (nếu có).
- Nút **Hủy** chỉ hiện khi trạng thái BOOKED, có hộp xác nhận.
- [ ] Hủy xong: trạng thái CANCELLED, phòng lại tìm thấy trong khoảng ngày đó.

---

## UC04 — Check-in (P0)

### S09. Check-in — `/bookings/{id}/check-in`
FR-10.
- Vào từ dashboard, danh sách đặt phòng hoặc tìm theo mã / tên khách.
- Xác nhận: đặt phòng → CHECKED_IN, ghi `actual_check_in` = bây giờ, phòng → OCCUPIED. Một giao dịch.
- Chỉ áp dụng cho đặt phòng BOOKED. Nếu ngày hôm nay khác `check_in_date`, hiển thị cảnh báo nhưng vẫn cho thực hiện [ĐỀ XUẤT, xem file 10 mục 10].
- [ ] Check-in hai lần cùng một đặt phòng bị chặn.

---

## UC06 — Dịch vụ đi kèm (P0)

### S10. Thêm dịch vụ — `/bookings/{id}/services`
FR-11.
- Chỉ mở với đặt phòng **CHECKED_IN** (rule 5).
- Chọn dịch vụ từ danh mục, số lượng ≥ 1; **đơn giá được sao chép từ `services.unit_price` vào `service_orders.unit_price`** để giá cũ không đổi khi danh mục đổi giá.
- Hiển thị danh sách dịch vụ đã dùng và tổng tiền dịch vụ.
- [ ] Dịch vụ thêm xong xuất hiện ở màn hình check-out.

### S12. Danh mục dịch vụ — `/services` (quản lý, P1)
FR-12. Thêm, sửa, xóa, xem: tên, đơn giá, đơn vị. Chặn xóa dịch vụ đã có trong `service_orders`.

---

## UC05 — Check-out và thanh toán (P0)

### S11. Check-out — `/bookings/{id}/check-out`
FR-13, FR-14, FR-15. Đây là màn hình có nhiều xử lý nhất; làm kỹ và kiểm thử nhiều.
- Hiển thị: khách, phòng, ngày, số đêm, **tiền phòng** (số đêm × giá), từng dòng dịch vụ, **tiền dịch vụ**, **tổng**.
- Chọn hình thức thanh toán (Tiền mặt, Thẻ, Chuyển khoản) [ĐỀ XUẤT]; đây chỉ là ghi nhận ở quầy, không có thanh toán trực tuyến.
- Xác nhận, trong **một giao dịch**: tạo `invoices` (1 – 1 với đặt phòng), ghi `paid_at`, đặt phòng → CHECKED_OUT, ghi `actual_check_out`, phòng → AVAILABLE.
- Sau khi xong: trang hóa đơn, nút **In** dùng `window.print()` với CSS in (P2, FR-15).
- [ ] Số tiền trên màn hình, trong hóa đơn và trong CSDL trùng nhau.
- [ ] Check-out hai lần cùng một đặt phòng bị chặn (không tạo hóa đơn thứ hai).

---

## UC07 — Thống kê (P2, chỉ cần đặc tả; làm thêm nếu còn thời gian)

### S13. Thống kê — `/reports` (quản lý)
FR-16, FR-17.
- Doanh thu theo tháng và theo loại phòng (từ `invoices`).
- Công suất phòng: số đêm đã bán ÷ (số phòng × số đêm trong kỳ) theo khoảng ngày.
- Bản tối thiểu: hai bảng số liệu; bản đẹp thêm biểu đồ cột đơn giản.
