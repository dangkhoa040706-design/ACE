# 14. MA TRẬN TRUY VẾT YÊU CẦU (RTM), BỘ TEST CASES NGHIỆP VỤ & 20 CÂU HỎI ĐÁP BẢO VỆ

> **Mục đích:** Giải quyết hoàn chỉnh **Mục #12** trong [08-bao-cao-tai-lieu-con-thieu.md](file:///D:/ACE/docs/08-bao-cao-tai-lieu-con-thieu.md) và yêu cầu cốt lõi của đề thi: *"Các sản phẩm không được xây dựng rời rạc mà phải thể hiện tính nhất quán và khả năng truy vết từ yêu cầu đến thiết kế và demo."*

---

## 1. Ma trận Truy vết Yêu cầu 2 Chiều (Requirements Traceability Matrix – RTM)

| Mã FR | Tên yêu cầu chức năng | MoSCoW | Use Case | Quy tắc / Quyết định | Bảng CSDL liên quan | Màn hình (Wireframe) | Route & Controller (Java) | Test Case | Bước Demo |
|:---:|---|:---:|:---:|---|---|---|---|---|---|
| **FR-01** | Đăng nhập và đăng xuất | Must | `UC01` | `NFR-02`, `D14`, `D22` | `users` | `S01`, `S02`, `S03` | `AuthController`<br>`GET/POST /login`, `POST /logout` | `TC-01`..`TC-03` | Bước 1 |
| **FR-02** | Phân quyền Lễ tân / Quản lý | Must | `UC01` | `NFR-03` | `users` | Toàn bộ (`S01`–`S15`) | `RoleInterceptor` / `SecurityConfig` | `TC-04`, `TC-05` | Bước 1, Bước 5 |
| **FR-03** | Thêm, sửa, xóa, xem Loại phòng | Must | `UC02` | `D10`, `D16` | `room_types`, `rooms` | `S06` | `RoomTypeController`<br>`/manage/room-types/**` | `TC-06`..`TC-08` | Bước 5 |
| **FR-04** | Thêm, sửa, xóa, bảo trì Phòng | Must | `UC02` | `D2`, `D10`, `D11` | `rooms`, `room_types`, `bookings` | `S04`, `S05` | `RoomManageController`<br>`/manage/rooms/**` | `TC-09`..`TC-12` | Bước 5 |
| **FR-05** | Tìm kiếm, lọc phòng theo số/loại/trạng thái | Must | `UC02` | `D2`, `D13`, `D15` | `rooms`, `room_types` | `S04` | `RoomController`<br>`GET /rooms` | `TC-13` | Bước 3, Bước 5 |
| **FR-06** | Tìm phòng trống theo khoảng ngày & loại phòng | Must | `UC03` | `BR2`, `BR3`, `D18` | `rooms`, `room_types`, `bookings` | `S07` | `BookingController`<br>`GET /bookings/new` | `TC-14`..`TC-17` | Bước 2 |
| **FR-07** | Tìm khách hàng hoặc tạo mới khi đặt phòng | Must | `UC03` | `D12`, `D13` | `customers` | `S08` | `BookingController`<br>`GET /bookings/new/confirm` | `TC-18`, `TC-19` | Bước 2 |
| **FR-08** | Tạo đặt phòng và chặn trùng lịch | Must | `UC03` | `BR1`, `BR2`, `D5`, `D6`, `D18` | `bookings`, `rooms`, `customers`, `users` | `S08`, `S09` | `BookingController`<br>`POST /bookings` | `TC-20`, `TC-21` | Bước 2 |
| **FR-09** | Hủy đặt phòng ở trạng thái `BOOKED` | Should | `UC03` | `BR7`, `D19` | `bookings` | `S09` | `BookingController`<br>`POST /bookings/{id}/cancel` | `TC-22`, `TC-23` | Bước 2 |
| **FR-10** | Check-in nhận phòng theo đặt phòng | Must | `UC04` | `D6`, `D7`, `D8` | `bookings`, `rooms` | `S03`, `S09`, `S10` | `BookingController`<br>`GET/POST /bookings/{id}/check-in` | `TC-24`..`TC-26` | Bước 3 |
| **FR-11** | Thêm / xóa dịch vụ cho phòng đang ở | Must | `UC06` | `BR5`, `D3`, `D21` | `service_orders`, `services`, `bookings` | `S11` | `ServiceOrderController`<br>`/bookings/{id}/services/**` | `TC-27`, `TC-28` | Bước 4 |
| **FR-12** | Quản lý danh mục dịch vụ (CRUD) | Should | `UC06` | `D3`, `D10` | `services`, `service_orders` | `S14` | `ServiceManageController`<br>`/manage/services/**` | `TC-29` | Dự phòng |
| **FR-13** | Tự động tính tiền phòng và dịch vụ khi Check-out | Must | `UC05` | `BR4`, `BR6`, `D7`, `D16` | `bookings`, `room_types`, `service_orders` | `S12` | `BookingController`<br>`GET /bookings/{id}/check-out` | `TC-30` | Bước 4 |
| **FR-14** | Xác nhận thanh toán, lập hóa đơn, trả phòng | Must | `UC05` | `BR6`, `D9`, `NFR-04` | `invoices`, `bookings`, `rooms`, `users` | `S12`, `S13` | `BookingController`<br>`POST /bookings/{id}/pay` | `TC-31`, `TC-32` | Bước 4 |
| **FR-15** | Xem chi tiết và In/xuất hóa đơn | Could | `UC05` | `D5`, `D20` | `invoices`, `bookings`, `customers` | `S13` | `InvoiceController`<br>`GET /invoices/{id}` | `TC-33` | Bước 4 |
| **FR-16** | Xem doanh thu theo tháng và loại phòng | Should | `UC07` | *(Chỉ đặc tả)* | `invoices`, `bookings`, `room_types` | `S15` | `ReportController`<br>`GET /reports` *(nếu làm)* | — | Slide thuyết trình |
| **FR-17** | Xem công suất phòng theo khoảng ngày | Could | `UC07` | *(Chỉ đặc tả)* | `rooms`, `bookings` | `S15` | `ReportController`<br>`GET /reports` *(nếu làm)* | — | Slide thuyết trình |
| **FR-18** | Xem bảng Tổng quan (Dashboard) 4 chỉ số & lịch hôm nay | Should | `UC01` / `UC04` / `UC05` | `D1`, `D4`, `D17` | `rooms`, `bookings`, `service_orders` | `S03` | `DashboardController`<br>`GET /dashboard` | `TC-34` | Bước 1, Bước 3 |

---

## 2. Bộ Kịch bản Kiểm thử Nghiệp vụ (Business Test Cases)

| Mã TC | Mục tiêu kiểm thử | UC / Quy tắc | Điều kiện đầu vào / Thao tác | Kết quả kỳ vọng (Pass Criteria) |
|:---:|---|:---:|---|---|
| **TC-01** | Đăng nhập hợp lệ với Lễ tân | `UC01`, `D14` | Nhập `letan01` / `123456` tại `/login` | Chuyển tới `/dashboard`; Header hiện tên *"Nguyễn Thảo Lễ Tân (Lễ tân)"*; ẩn menu Quản lý. |
| **TC-02** | Đăng nhập hợp lệ với Quản lý | `UC01`, `D14` | Nhập `quanly01` / `123456` tại `/login` | Chuyển tới `/dashboard`; hiển thị đầy đủ menu Quản lý loại phòng, Dịch vụ, Thống kê. |
| **TC-03** | Đăng nhập sai mật khẩu | `UC01`, `NFR-02` | Nhập `letan01` / `wrongpass` | Ở lại `/login`, hiện đúng thông báo `MSG-01`: *"Sai tên đăng nhập hoặc mật khẩu."* |
| **TC-04** | Chặn truy cập khi chưa đăng nhập | `UC01`, `NFR-03` | Mở trình duyệt ẩn danh, truy cập `/bookings` | Tự động chuyển hướng (`302`) về `/login`. |
| **TC-05** | Chặn Lễ tân truy cập URL Quản lý | `UC01`, `NFR-03` | Đăng nhập `letan01`, gõ URL `/manage/room-types` | Hệ thống chặn truy cập (403 hoặc redirect `/dashboard` kèm lỗi `MSG-04`). |
| **TC-06** | Thêm loại phòng với giá $\le 0$ hoặc sức chứa $< 1$ | `UC02`, `FR-03` | Quản lý nhập giá `-50000` hoặc sức chứa `0` | Hệ thống từ chối lưu, báo lỗi `MSG-10` / `MSG-11`. |
| **TC-07** | Chặn xóa loại phòng đang có phòng | `UC02`, `D10` | Quản lý bấm Xóa loại phòng `Standard` (đang có phòng `101`, `102`, `205`) | Hệ thống từ chối xóa, hiển thị lỗi `MSG-12`, dữ liệu `Standard` còn nguyên. |
| **TC-08** | Thêm và xóa loại phòng mới chưa có phòng | `UC02`, `FR-03` | Quản lý tạo loại phòng `Penthouse` (5.000.000đ, 4 khách), sau đó bấm Xóa | Tạo mới thành công (`MSG-05`) và xóa thành công (`MSG-06`). |
| **TC-09** | Chặn tạo phòng trùng `room_number` | `UC02`, `FR-04` | Quản lý tạo phòng mới có số phòng `301` | Hệ thống báo lỗi trùng số phòng `MSG-09`. |
| **TC-10** | Chặn xóa phòng đã từng có booking | `UC02`, `D10` | Quản lý bấm Xóa phòng `301` hoặc `401` | Hệ thống từ chối xóa và hiện lỗi `MSG-13`. |
| **TC-11** | Chuyển trạng thái Bảo trì phòng trống | `UC02`, `D11` | Quản lý bấm **[Bảo trì]** trên phòng `305` (đang `AVAILABLE`, không có lịch đặt hôm nay) | Trạng thái phòng `305` chuyển sang `MAINTENANCE`, chỉ số Phòng trống trên Dashboard giảm 1. |
| **TC-12** | Chặn chuyển Bảo trì phòng đang có khách / hẹn hôm nay | `UC02`, `D11` | Quản lý thử chuyển bảo trì phòng `301` (`OCCUPIED`) hoặc `205` (hẹn nhận hôm nay) | Hệ thống từ chối và báo lỗi `MSG-14`. |
| **TC-13** | Lọc danh sách phòng kết hợp | `UC02`, `FR-05`, `D13` | Tại `/rooms`, nhập số phòng `"30"`, chọn loại `Deluxe` | Hiển thị đúng phòng `301` và `302`. |
| **TC-14** | Kiểm tra `BR3`: Ngày trả $\le$ Ngày nhận | `UC03`, `BR3` | Tại `/bookings/new`, chọn Nhận `15/10`, Trả `15/10` (hoặc `14/10`) | Hệ thống chặn tìm kiếm và báo lỗi `MSG-17`. |
| **TC-15** | Kiểm tra `BR2`: Ẩn phòng đã đặt trùng khoảng ngày | `UC03`, `BR2` | Phòng `102` đã có `BK-2404` (`TODAY+1` đến `TODAY+3`). Tìm phòng từ `TODAY+1` đến `TODAY+2` | Danh sách kết quả **không** hiển thị phòng `102`. |
| **TC-16** | Kiểm tra `D18`: Nhận phòng đúng ngày khách cũ trả | `UC03`, `D18` | Phòng `102` có đơn trả vào `TODAY+3`. Tìm phòng nhận từ `TODAY+3` đến `TODAY+5` | Danh sách kết quả **có** hiển thị phòng `102` (vì ngày trả cũ = ngày nhận mới không tính là trùng). |
| **TC-17** | Ẩn phòng đang `MAINTENANCE` khi tìm phòng trống | `UC03` | Tìm phòng hạng `Superior` bất kỳ ngày nào | Phòng `103` (đang `MAINTENANCE`) không bao giờ xuất hiện trong kết quả. |
| **TC-18** | Tạo khách hàng mới bị trùng số CCCD | `UC03`, `D12` | Khi đặt phòng, chọn Tạo khách mới và nhập CCCD `079201001234` (của Nguyễn Minh Anh) | Hệ thống báo lỗi `MSG-19`, yêu cầu chọn khách hàng đã có. |
| **TC-19** | Tạo khách hàng mới có trùng SĐT nhưng khác CCCD | `UC03`, `D12` | Nhập khách mới cùng SĐT người thân nhưng CCCD mới (hoặc để trống CCCD) | Hệ thống cho phép tạo khách hàng và đặt phòng bình thường (`D12`). |
| **TC-20** | Tạo đặt phòng thành công | `UC03`, `FR-08` | Chọn phòng `101`, chọn khách hàng, nhấn Xác nhận | Tạo bản ghi `bookings` với `status = 'BOOKED'`, `created_by` đúng user, hiện thông báo `MSG-15`. |
| **TC-21** | Chống đặt trùng khi bấm xác nhận (Transaction check) | `UC03`, `BR2` | Mở 2 tab cùng chọn phòng `101` cùng ngày; Tab 1 bấm xác nhận trước, Tab 2 bấm sau | Tab 1 thành công; Tab 2 bị chặn trong Transaction và báo lỗi `MSG-20`. |
| **TC-22** | Hủy đặt phòng đang ở trạng thái `BOOKED` | `UC03`, `BR7`, `D19` | Bấm **[Hủy đặt]** trên đơn `BK-2404` (`BOOKED`) | Đơn chuyển sang `CANCELLED` (`MSG-16`); phòng `102` lập tức đặt lại được trong ngày đó. |
| **TC-23** | Chặn hủy đặt phòng đang `CHECKED_IN` hoặc `CHECKED_OUT` | `UC03`, `BR7` | Gửi POST `/bookings/2401/cancel` (đang `CHECKED_IN`) | Hệ thống từ chối và báo lỗi `MSG-21`. |
| **TC-24** | Check-in thành công đúng ngày hẹn | `UC04`, `FR-10` | Bấm **[Check-in]** trên đơn `BK-2402` (Phòng `205`, `check_in_date = TODAY`) | `BK-2402` sang `CHECKED_IN`, `actual_check_in` khác null, Phòng `205` sang `OCCUPIED`. |
| **TC-25** | Chặn Check-in sớm trước ngày đặt (`D7`) | `UC04`, `D7` | Thử Check-in đơn `BK-2404` (có `check_in_date = TODAY + 1`) | Hệ thống chặn và báo lỗi `MSG-23`. |
| **TC-26** | Quy trình khách vãng lai (Walk-in) | `UC03` + `UC04`, `D8` | Tạo booking nhận `TODAY` cho phòng `201` $\rightarrow$ Bấm **[Check-in]** ngay | Cả 2 bước hoàn tất trong dưới 1 phút, phòng `201` sang `OCCUPIED`. |
| **TC-27** | Thêm dịch vụ đi kèm và đóng băng đơn giá | `UC06`, `BR5` | Thêm `Ăn sáng ×2` (150k) cho `BK-2401`; sau đó Quản lý đổi giá Ăn sáng lên `200k` | Đơn dịch vụ của `BK-2401` vẫn giữ nguyên `unit_price = 150.000đ` (thành tiền `300.000đ`). |
| **TC-28** | Xóa dòng dịch vụ ghi nhầm khi đang ở (`D21`) | `UC06`, `D21` | Tại `S11` của `BK-2401`, thêm 1 dịch vụ rồi bấm **[Xóa]** dòng đó | Dòng dịch vụ bị xóa, tổng tiền dịch vụ tạm tính giảm tương ứng (`MSG-29`). |
| **TC-29** | Chặn xóa danh mục dịch vụ đã từng được gọi | `UC06`, `D10` | Quản lý xóa dịch vụ `Ăn sáng Buffet` tại `/manage/services` | Hệ thống từ chối xóa và hiện lỗi `MSG-33`. (Xóa `Thuê xe máy` chưa có đơn thì thành công). |
| **TC-30** | Tính tiền Check-out chuẩn xác (`BR4`, `BR6`, `D7`) & Chống trùng Hóa đơn | `UC05`, `FR-13`, `FR-14` | Mở Check-out `BK-2401` (2 đêm $\times$ 1.250k + 380k DV), chọn `TRANSFER`, bấm Thanh toán | Tổng tiền đúng `2.880.000đ`; tạo đúng 1 bản ghi `invoices`; `BK-2401` sang `CHECKED_OUT`; Phòng `301` về `AVAILABLE`. |

---

## 3. Bộ 20 Câu hỏi & Gợi ý Trả lời Phỏng vấn / Bảo vệ Đồ án Cuối kỳ

> **Mục đích:** Theo Mục 4 của đề thi, tiêu chí **Response to Questions chiếm tới 20% tổng điểm** (tương đương toàn bộ phần tài liệu phân tích thiết kế trong quyển báo cáo). Cả 4 thành viên (**Khoa, Hoàng, Phong, Kiệt**) cần đọc kỹ 20 câu hỏi dưới đây.

### Nhóm A: Câu hỏi về Phạm vi, Actor & Phân tích Yêu cầu (Trọng tâm: Hoàng, Phong)

1. **Tại sao nhóm không đưa "Khách hàng" làm tác nhân đăng nhập trực tiếp vào hệ thống?**
   * *Trả lời:* Nhóm xác định phạm vi là hệ thống quản lý nội bộ tại quầy lễ tân cho khách sạn vừa và nhỏ. Việc tập trung vào 2 tác nhân nội bộ (`Lễ tân`, `Quản lý`) giúp kiểm soát chặt chẽ nghiệp vụ cốt lõi (chống trùng lịch, nhận/trả phòng thực tế, dịch vụ phát sinh và hóa đơn). Tính năng cổng đặt phòng trực tuyến cho khách (Booking Portal) được nhóm đưa vào mục Hướng phát triển.
2. **Tại sao mỗi đơn đặt phòng (`bookings`) chỉ gắn với 1 phòng (`room_id`) mà không làm bảng trung gian `booking_rooms` nhiều–nhiều?**
   * *Trả lời:* Đây là quyết định thiết kế có chủ đích (`BR1`) phù hợp với quy mô khách sạn vừa và nhỏ: mỗi phòng có thời điểm nhận/trả thực tế (`actual_check_in`, `actual_check_out`) và các dịch vụ phát sinh (`service_orders`) gắn trực tiếp theo phòng đó. Nhờ gộp 1–1 giữa đơn đặt và phòng, công thức kiểm tra trùng lịch (`BR2`) và đồng bộ trạng thái phòng khi Check-in/Check-out rất rõ ràng, tránh lỗi bán phần (một đơn có 2 phòng nhưng chỉ 1 phòng đến nhận).
3. **Nếu khách vãng lai (Walk-in) đến trực tiếp quầy mà chưa đặt trước thì hệ thống xử lý thế nào?**
   * *Trả lời:* Theo quyết định `D8`, thay vì tạo một nhánh dữ liệu riêng biệt làm phân mảnh lịch sử đặt phòng, Lễ tân thực hiện quy trình chuẩn gồm 2 bước liên tiếp: (1) Tạo đặt phòng với ngày nhận là hôm nay (`UC03` – hệ thống tự lọc phòng trống), sau đó (2) Bấm nút **[Check-in]** ngay lập tức tại danh sách đặt phòng (`UC04`). Toàn bộ thao tác chỉ mất khoảng 30 giây và đảm bảo mọi lượt lưu trú đều có đầy đủ lịch sử `bookings` $\rightarrow$ `invoices`.
4. **Quy tắc chống trùng lịch đặt phòng (`BR2`) hoạt động như thế nào? Nếu khách cũ trả phòng ngày 14/10 thì khách mới có đặt nhận phòng ngày 14/10 được không?**
   * *Trả lời:* Hoàn toàn được (`D18`). Trong nghiệp vụ khách sạn, khách cũ trả phòng trước 12:00 trưa ngày 14/10 và khách mới nhận phòng từ 14:00 chiều ngày 14/10. Do đó công thức kiểm tra giao nhau dùng dấu bất đẳng thức nghiêm ngặt: `existing.check_in_date < new.check_out_date AND existing.check_out_date > new.check_in_date`. Khi `new.check_in_date == existing.check_out_date` (cùng bằng `14/10`), biểu thức trả về `false` (không trùng) nên phòng vẫn được phép đặt.
5. **Nếu khách trả phòng sớm hơn hoặc muộn hơn ngày dự kiến thì tiền phòng tính ra sao?**
   * *Trả lời:* Theo quy tắc `BR4` và quyết định `D7` trong phạm vi đồ án (không làm phụ thu/giảm giá), tiền phòng được tính cố định theo số đêm đã cam kết trong đơn đặt phòng ban đầu ($\text{Số đêm} = \text{check\_out\_date} - \text{check\_in\_date}$, tối thiểu 1 đêm). Giờ nhận và trả thực tế vẫn được ghi nhận chính xác vào `actual_check_in` và `actual_check_out` để phục vụ đối soát.

### Nhóm B: Câu hỏi về Mô hình UML, Thiết kế CSDL & Kỹ thuật Java (Trọng tâm: Khoa, Hoàng)

6. **Tại sao trong bảng `service_orders` lại có cột `unit_price` trong khi bảng `services` đã có `unit_price` rồi? Có bị dư thừa dữ liệu không?**
   * *Trả lời:* Đây không phải là dư thừa mà là mẫu thiết kế chuẩn để **bảo toàn lịch sử giá (Price Snapshot)**. Cột `services.unit_price` là giá niêm yết hiện hành; còn `service_orders.unit_price` lưu lại đúng mức giá tại thời điểm khách gọi dịch vụ. Nếu ngày mai Quản lý tăng giá *Ăn sáng* từ `150.000đ` lên `200.000đ`, các hóa đơn cũ và các đơn dịch vụ khách đã dùng hôm qua không bị biến đổi số tiền.
7. **Tại sao trạng thái "Quá hạn" và "Đã đặt" có trên màn hình giao diện nhưng lại không có trong cột `rooms.status` hay `bookings.status` dưới CSDL?**
   * *Trả lời:* Theo quyết định `D1` và `D2`, việc tách bạch giữa **trạng thái lưu trữ vật lý** và **nhãn hiển thị suy diễn theo thời gian** giúp tránh phải chạy cron-job cập nhật CSDL mỗi nửa đêm:
     * `"Quá hạn"` chỉ là một booking đang `CHECKED_IN` mà `check_out_date < LocalDate.now()`.
     * `"Đã đặt"` trên thẻ phòng chỉ là một phòng đang `AVAILABLE` nhưng có đơn `BOOKED` phủ ngày đang xem.
8. **Làm sao hệ thống ngăn chặn việc xóa nhầm một phòng hoặc loại phòng làm hỏng các hóa đơn cũ?**
   * *Trả lời:* Hệ thống áp dụng kết hợp 2 lớp bảo vệ (`NFR-04` và `D10`):
     * Ở tầng CSDL: Khóa ngoại không dùng `ON DELETE CASCADE`.
     * Ở tầng `Service`: Trước khi xóa phòng, hệ thống kiểm tra `bookingRepository.existsByRoomId(roomId)`. Nếu phòng đã từng có booking, hệ thống chặn xóa và hướng dẫn Quản lý chuyển phòng sang trạng thái `MAINTENANCE`.
9. **Làm sao chống được lỗi tạo 2 hóa đơn cho cùng 1 đặt phòng khi Lễ tân bấm đúp chuột vào nút Thanh toán?**
   * *Trả lời:* Ở tầng CSDL, cột `invoices.booking_id` được khai báo ràng buộc `UNIQUE` (quan hệ 1–1). Ở tầng `BookingService`, phương thức thanh toán đặt trong `@Transactional` và kiểm tra trước `invoiceRepository.findByBookingId(bookingId)`: nếu đã có hóa đơn thì trả về hóa đơn đó ngay (Idempotency) chứ không `INSERT` lần hai.
10. **Nếu muốn chuyển CSDL từ H2 sang MySQL hoặc PostgreSQL trong thực tế thì phải sửa những gì?**
    * *Trả lời:* Nhờ kiến trúc phân lớp sử dụng **Spring Data JPA / Hibernate** (`NFR-06`), toàn bộ tầng `Controller`, `Service`, `Repository` và `Entity` giữ nguyên 100%, chỉ cần thêm dependency MySQL Driver vào `pom.xml` và đổi 3 dòng cấu hình `spring.datasource.url/username/password` trong `application.properties`.

### Nhóm C: Câu hỏi về Giao diện, Phân quyền & Đóng góp Nhóm (Trọng tâm: Kiệt, Phong, Cả nhóm)

11. **Nếu Lễ tân biết đường dẫn `/manage/rooms/new` và gõ thẳng lên thanh địa chỉ trình duyệt thì sao? Ẩn nút trên giao diện có đủ an toàn không?**
    * *Trả lời:* Chỉ ẩn nút trên giao diện (`th:if`) là không đủ. Theo `NFR-03`, hệ thống kiểm tra quyền ở phía máy chủ (Server-side Security / Interceptor) cho mọi request `/manage/**` và `/reports`. Nếu vai trò trong Session là `RECEPTIONIST`, server lập tức chặn và trả về mã `403 Forbidden` (kèm thông báo `MSG-04`).
12. **Tại sao nhóm chọn bảng màu Vàng `#FDBF2D` kết hợp Đen/Trắng cho giao diện ACE? Chữ màu vàng trên nền trắng có bị khó đọc không?**
    * *Trả lời:* Màu vàng ấm (`#FDBF2D`) gợi cảm giác hiếu khách, sang trọng của ngành khách sạn (Hospitality). Để đảm bảo độ tương phản chuẩn Accessibility (WCAG) trên nền sáng (`Light Mode`), nhóm đã định nghĩa riêng token `Primary text = #B45309` (vàng nâu đậm) cho chữ và liên kết, còn màu `#FDBF2D` dùng cho nền nút chính với chữ đen `#0A0A0A`.
13. **Mối liên hệ giữa biểu đồ Sequence Diagram trong báo cáo và code Java thực tế của nhóm là gì?**
    * *Trả lời:* Các thành phần lifelines trong Sequence Diagram (`BookingController` $\rightarrow$ `BookingService` $\rightarrow$ `RoomRepository` / `BookingRepository` / `InvoiceRepository`) trùng khớp 1–1 với tên class và phương thức trong source code Spring Boot của nhóm.
14. **Nếu khách sạn muốn mở rộng thêm tính năng đặt nhiều phòng trong 1 đoàn khách (Group Booking) và tính phụ thu trả phòng trễ thì thiết kế hiện tại mở rộng thế nào?**
    * *Trả lời:* (1) Tách mối quan hệ 1–N giữa `bookings` và `rooms` thành bảng chi tiết `booking_rooms(booking_room_id, booking_id, room_id, price_per_night, actual_check_in, actual_check_out)`; (2) Thêm cột `surcharge` (phụ thu) và `discount` (giảm giá) vào bảng `invoices`, đồng thời cập nhật công thức tại `BookingService.calculateCheckout()`.
15. **Mỗi thành viên hãy nêu rõ phần việc cá nhân mình đã làm và cách nhóm phối hợp để không bị lệch giữa Báo cáo và Code?**
    * *Trả lời:* Ngay đầu Tuần 1, cả nhóm đã chốt văn bản [chot-pham-vi-actor-usecase-bang.md](file:///D:/ACE/docs/chot-pham-vi-actor-usecase-bang.md) và 22 quyết định nghiệp vụ (`D1`–`D22`). **Hoàng** phụ trách FR/NFR/MoSCoW, UC02, UC03 và ma trận truy vết; **Phong** phụ trách khảo sát hiện trạng, UC04, UC05 và kịch bản demo; **Kiệt** phụ trách Wireframe S01–S15, RBAC, UC01, UC06, Slide và định dạng Word; **Khoa** phụ trách ERD, Từ điển dữ liệu, UC07 và cài đặt toàn bộ ứng dụng Spring Boot bám sát đặc tả của 3 bạn.
