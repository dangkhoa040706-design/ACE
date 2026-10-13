# 08. TÀI LIỆU VÀ ĐẦU VÀO NGHIỆP VỤ CÒN THIẾU ĐỂ KHOA CODE

> **Mục đích:** File này xác định toàn bộ tài liệu đặc tả, thiết kế giao diện và quy tắc nghiệp vụ mà **Hoàng, Phong, Kiệt bắt buộc phải bàn giao cho Khoa trước 23:59 ngày 18/10/2026 (cuối Tuần 1)**.  
> **Nguyên tắc "Zero Guesswork":** Khoa là người duy nhất phụ trách code Java (Spring Boot + Thymeleaf + H2). Mọi luồng xử lý, ràng buộc dữ liệu, thông báo lỗi, quyền hạn nút bấm và kịch bản demo phải có sẵn trong tài liệu để Khoa không phải tự suy đoán nghiệp vụ khi code.

---

## 1. Chi tiết các đầu vào nghiệp vụ Khoa cần từ từng thành viên

### 1.1. Từ Hoàng (Phụ trách Phân tích & Nghiệp vụ Phòng / Đặt phòng)
* **Đặc tả chi tiết UC02 (Phòng & Loại phòng):**
  - Quy tắc xác thực dữ liệu (Validation): Tên loại phòng, đơn giá đêm ($> 0$), sức chứa ($\ge 1$), định dạng số phòng (không trùng lặp).
  - Quy tắc xóa dữ liệu (D10): Tuyệt đối không xóa phòng đã từng có booking; không xóa loại phòng nếu vẫn còn phòng trực thuộc.
  - Quy tắc đổi trạng thái thủ công (D11): Quản lý chỉ được chuyển đổi qua lại giữa `AVAILABLE` và `MAINTENANCE` khi phòng không có khách ở và không có lịch đặt hôm nay.
* **Đặc tả chi tiết UC03 (Tìm phòng trống & Đặt phòng):**
  - Công thức kiểm tra trùng lịch (BR2, D18): Truy vấn SQL kiểm tra không bị chồng chéo khoảng ngày (`cũ.check_in < mới.check_out AND cũ.check_out > mới.check_in`). Xác nhận cho phép đặt phòng mới nhận đúng ngày phòng cũ trả.
  - Quy tắc khách hàng (D12, D13): Trường bắt buộc (`full_name`, `phone`), trường tùy chọn (`id_number`, `email`), cơ chế kiểm tra trùng CCCD/CMND (`id_number` UNIQUE), phương thức tìm kiếm khách hàng (chứa chuỗi con, không phân biệt hoa thường).
  - Quy tắc hủy đặt phòng (D19, BR7): Chỉ hủy khi trạng thái là `BOOKED`, không thay đổi trạng thái phòng.
* **Bảng chuyển trạng thái (State Transition Matrix):**
  - Thống nhất cơ chế đồng bộ trạng thái 2 chiều giữa `bookings.status` và `rooms.status` (xem chi tiết mục 3).
* **Công thức tính 4 thẻ chỉ số Dashboard (S03 - D4):**
  - Công thức đếm cho: Phòng trống, Check-in hôm nay, Check-out hôm nay, Dịch vụ đi kèm.

### 1.2. Từ Phong (Phụ trách Thuyết trình, Nghiệp vụ Check-in/Out & Kịch bản Demo)
* **Đặc tả chi tiết UC04 (Check-in):**
  - Điều kiện nhận phòng: Booking ở trạng thái `BOOKED`, ngày nhận phòng $\le$ ngày hiện tại, phòng thực tế đang `AVAILABLE`.
  - Quy định xử lý khi khách đến sớm/trễ so với ngày đặt (D7).
  - Quy trình tiếp nhận khách vãng lai không đặt trước (D8): Hướng dẫn lễ tân tạo nhanh booking rồi check-in.
* **Đặc tả chi tiết UC05 (Check-out & Thanh toán):**
  - Công thức tính hóa đơn (BR4, BR6): $\text{Tổng thanh toán} = (\text{Số đêm} \times \text{Đơn giá phòng}) + \sum \text{Tiền dịch vụ}$.
  - Quy định tính số đêm khi khách trả phòng sớm hoặc trễ (D7): Cố định theo số đêm đăng ký ban đầu (không phụ thu/giảm giá).
  - Danh mục hình thức thanh toán (D9): Cố định 3 giá trị `CASH`, `CARD`, `TRANSFER`.
  - Cơ chế chống tạo trùng hóa đơn / chống nhấn nút thanh toán 2 lần (Idempotency).
* **Kịch bản Demo chi tiết (Demo Script) & Yêu cầu dữ liệu mẫu:**
  - Danh sách dữ liệu mẫu cần chuẩn bị sẵn để khớp với bài nói 12-15 phút: số lượng phòng, danh mục phòng mẫu, khách hàng mẫu, booking mẫu để demo thêm dịch vụ và trả phòng trực tiếp.

### 1.3. Từ Kiệt (Phụ trách Wireframe UI, Phân quyền & Định dạng)
* **Wireframe chi tiết toàn bộ các màn hình (S01 - S14):**
  - Hoàn thiện chi tiết layout cho 11 màn hình còn thiếu (tài liệu 04 mới chỉ có phác thảo ASCII cho S02, S03, S07, S12): S04, S05, S06, S08, S09, S10, S11, S13, S14.
  - Thể hiện rõ cấu trúc bảng, các trường nhập liệu (input, dropdown, datepicker) và trạng thái nút bấm (enabled, disabled).
* **Ma trận phân quyền theo Route và Nút bấm (RBAC Matrix):**
  - Danh sách URL chặn theo vai trò (`/manage/**` chỉ dành cho `MANAGER`).
  - Danh sách các nút ẩn/hiện trên từng giao diện theo vai trò (Lễ tân chỉ xem danh sách phòng, Quản lý thấy nút Thêm/Sửa/Bảo trì).
  - Cơ chế xử lý khi người dùng không đủ quyền truy cập (chuyển hướng 403 hoặc quay lại trang chính).
* **Danh sách thông báo hệ thống chuẩn tiếng Việt:**
  - Thông báo lỗi nghiệp vụ, thông báo thao tác thành công, nội dung hộp thoại xác nhận khi thực hiện hành động nguy hiểm.
* **Đặc tả chi tiết UC01 (Đăng nhập) & UC06 (Dịch vụ đi kèm):**
  - UC01: Thông báo lỗi chung "Sai tên đăng nhập hoặc mật khẩu", xử lý hết phiên làm việc.
  - UC06: Thao tác thêm dịch vụ; lưu đơn giá tại thời điểm thêm vào `service_orders.unit_price`; xác nhận cho phép xóa dòng dịch vụ đã thêm nhầm khi chưa thanh toán (D21).

---

## 2. Bảng 22 quyết định nghiệp vụ cần thống nhất (Chốt trước 14/10)

| Mã | Vấn đề cần chốt | Giải pháp thống nhất để code | Phạm vi ảnh hưởng |
|---|---|---|---|
| **D1** | Trạng thái **"Quá hạn"** trên giao diện | **Không thêm cột vào CSDL.** Tính toán động trên code: Booking đang `CHECKED_IN` mà `check_out_date < LocalDate.now()`. | UC04, UC05, S03, S09 |
| **D2** | Trạng thái phòng **"Đã đặt"** trên giao diện | **Cột `rooms.status` giữ nguyên 3 giá trị** (`AVAILABLE`, `OCCUPIED`, `MAINTENANCE`). Trạng thái "Đã đặt" trên giao diện suy ra từ việc có booking `BOOKED` trong ngày xem. | UC02, UC03, S04 |
| **D3** | Công tắc **Late check-out**, tag **Ăn sáng / Đưa đón** | **Bỏ Late check-out** (phạm vi không có phụ thu). "Ăn sáng", "Đưa đón" là các **dịch vụ** trong bảng `services` (UC06), không cấu hình cứng vào phòng. | Wireframe, Bảng `services` |
| **D4** | **Dashboard (S03):** Cách tính 4 thẻ chỉ số | - *Phòng trống:* `rooms.status = 'AVAILABLE'`<br>- *Check-in hôm nay:* `bookings.status = 'BOOKED'` và `check_in_date = hôm nay`<br>- *Check-out hôm nay:* `bookings.status = 'CHECKED_IN'` và `check_out_date = hôm nay`<br>- *Dịch vụ đi kèm:* Tổng số dòng `service_orders` của các booking đang `CHECKED_IN`. | Màn hình S03 |
| **D5** | **Mã đặt phòng** hiển thị | Hiển thị dạng chuỗi: `BK-` + `booking_id` (tự sinh khi hiển thị, không thêm cột). | Toàn bộ giao diện có Booking |
| **D6** | **Giờ check-in / check-out** | Đặt phòng chỉ chọn ngày (`DATE`). Giờ thực tế chỉ lưu vào `actual_check_in` / `actual_check_out` (`TIMESTAMP`) khi bấm nút Check-in / Check-out. | Bảng `bookings`, S09 |
| **D7** | **Quy tắc nhận phòng sớm/trễ, trả sớm/muộn** | - Check-in khi `check_in_date <= hôm nay`.<br>- Check-out bất kỳ lúc nào khi đang `CHECKED_IN`.<br>- **Tiền phòng tính cố định theo số đêm đặt ban đầu** (không phạt trễ, không giảm tiền về sớm). | UC04, UC05, Logic tính tiền |
| **D8** | **Khách vãng lai không đặt trước** | Không làm luồng tắt. Lễ tân thực hiện 2 thao tác: Tạo booking ngày hôm nay (UC03) $\rightarrow$ Bấm Check-in ngay lập tức (UC04). | UC03, UC04 |
| **D9** | **Phương thức thanh toán (`payment_method`)** | Dùng ENUM cố định 3 giá trị: `CASH`, `CARD`, `TRANSFER`. Không kết nối cổng thanh toán trực tuyến. | Bảng `invoices`, UC05 |
| **D10** | **Quy tắc xóa dữ liệu** | - *Phòng:* Không xóa nếu đã từng có booking (chuyển sang `MAINTENANCE`).<br>- *Loại phòng:* Không xóa nếu còn phòng trực thuộc.<br>- *Dịch vụ:* Không xóa nếu đã có trong `service_orders`.<br>- *Khách hàng:* Không hỗ trợ xóa. | NFR-04, Tầng Service xóa |
| **D11** | **Chuyển trạng thái phòng thủ công (Quản lý)** | Quản lý chỉ được chuyển đổi giữa `AVAILABLE` và `MAINTENANCE`. Trạng thái `OCCUPIED` do hệ thống tự gán khi Check-in. Không cho bảo trì phòng đang có khách hoặc có hẹn nhận hôm nay. | UC02, S04, S05 |
| **D12** | **Trường dữ liệu khách hàng** | Bắt buộc: `full_name`, `phone`. Tùy chọn: `id_number`, `email`. `id_number` là UNIQUE nếu có nhập. `phone` cho phép trùng. | Bảng `customers`, Form đặt phòng |
| **D13** | **Cơ chế tìm kiếm** | Khớp chuỗi con (`LIKE %keyword%`), không phân biệt hoa thường. | UC02, UC03, JPA Repository |
| **D14** | **Tài khoản Demo hệ thống** | Cố định 2 tài khoản nạp sẵn trong `sample-data.sql`:<br>1. Lễ tân: `letan01` / pass: `123456` (`RECEPTIONIST`)<br>2. Quản lý: `quanly01` / pass: `123456` (`MANAGER`) | UC01, Dữ liệu mẫu |
| **D15** | **Phân trang danh sách** | Không phân trang (quy mô dữ liệu nhỏ). Danh sách hỗ trợ cuộn, sắp xếp mặc định theo ID hoặc ngày mới nhất. | Toàn bộ danh sách |
| **D16** | **Quy ước tiền tệ** | Tiền VND số nguyên (`DECIMAL(12,0)` hoặc `BIGINT`), hiển thị phân cách dấu chấm: `1.250.000đ`. | Các trường hiển thị giá |
| **D17** | **Múi giờ hệ thống ("Hôm nay")** | Sử dụng múi giờ Việt Nam: `ZoneId.of("Asia/Ho_Chi_Minh")`, lấy theo `LocalDate.now()`. | Dashboard, Check-in/out |
| **D18** | **Biên trùng lịch đặt phòng (BR2)** | Cho phép đặt phòng nhận vào đúng ngày khách cũ trả (Ngày trả cũ = Ngày nhận mới **không tính trùng**). Khớp biểu thức: `cũ.in < mới.out AND cũ.out > mới.in`. | Logic kiểm tra phòng trống |
| **D19** | **Hủy đặt phòng** | Chỉ hủy khi booking ở trạng thái `BOOKED` (chuyển sang `CANCELLED`). Trạng thái phòng không đổi. | UC03 |
| **D20** | **Hình thức hiển thị hóa đơn** | Xem chi tiết hóa đơn trực tiếp trên màn hình (S13) là bắt buộc. Nút in hỗ trợ lệnh `window.print()` của trình duyệt. | S13, UC05 |
| **D21** | **Xóa dòng dịch vụ đã thêm** | Không hỗ trợ sửa số lượng. Cho phép **xóa dòng dịch vụ** (`service_orders`) nếu booking còn đang `CHECKED_IN` (chưa thanh toán). | UC06, S11 |
| **D22** | **Quản lý tài khoản người dùng** | Không làm giao diện thêm/sửa user. Tài khoản nhân viên chỉ nạp sẵn qua script CSDL. | Phạm vi hệ thống |

---

## 3. Bảng chuyển trạng thái (State Transition Table)

| Thao tác nghiệp vụ | Điều kiện tiên quyết | Trạng thái Booking sau | Trạng thái Phòng sau | Ghi nhận thời gian | Vai trò thực hiện |
|---|---|:---:|:---:|---|---|
| **Tạo đặt phòng mới** | Không trùng lịch (D18), phòng không ở trạng thái `MAINTENANCE`. | `BOOKED` | Giữ nguyên (`AVAILABLE`) | `created_at = NOW()` | Lễ tân, Quản lý |
| **Hủy đặt phòng** | Booking đang ở `BOOKED`. | `CANCELLED` | Giữ nguyên | Không | Lễ tân, Quản lý |
| **Check-in nhận phòng** | Booking là `BOOKED`, `check_in_date <= hôm nay`, phòng `AVAILABLE`. | `CHECKED_IN` | `OCCUPIED` | `actual_check_in = NOW()` | Lễ tân, Quản lý |
| **Thêm dịch vụ** | Booking đang ở `CHECKED_IN`. | `CHECKED_IN` | `OCCUPIED` | `ordered_at = NOW()` | Lễ tân, Quản lý |
| **Xóa dòng dịch vụ** | Booking đang ở `CHECKED_IN`. | `CHECKED_IN` | `OCCUPIED` | Xóa dòng trong DB | Lễ tân, Quản lý |
| **Check-out & Thanh toán** | Booking đang `CHECKED_IN`, chưa có hóa đơn. | `CHECKED_OUT` | `AVAILABLE` | `actual_check_out = NOW()`, `paid_at = NOW()` | Lễ tân, Quản lý |
| **Chuyển phòng bảo trì** | Phòng `AVAILABLE`, không có booking `BOOKED` sắp tới. | Không đổi | `MAINTENANCE` | Không | **Chỉ Quản lý** |
| **Hoàn tất bảo trì** | Phòng đang `MAINTENANCE`. | Không đổi | `AVAILABLE` | Không | **Chỉ Quản lý** |

---

## 4. Kế hoạch bàn giao đầu vào cho Khoa (Tuần 1)

* **Trước 23:59 ngày 14/10/2026:** Cả nhóm duyệt chính thức Bảng 22 quyết định nghiệp vụ (D1 - D22) và Bảng chuyển trạng thái. Khoa chủ động dựng project Spring Boot, viết DDL `schema.sql` và cấu hình H2.
* **Trước 23:59 ngày 16/10/2026:** Kiệt nộp Wireframe đầy đủ (S01 - S14), Ma trận phân quyền và Danh sách thông báo tiếng Việt. Phong gửi bản nháp kịch bản demo và danh mục dữ liệu mẫu cần có.
* **Trước 23:59 ngày 18/10/2026 (Hạn chót M1):** Hoàng, Phong, Kiệt bàn giao toàn bộ bản Đặc tả Use Case chi tiết (UC01 - UC06). Khoa rà soát lần cuối, tạo dữ liệu `sample-data.sql` và sẵn sàng code từ 19/10/2026.

---

## 5. BẢNG TỔNG HỢP TÓM TẮT TÀI LIỆU ĐẦU VÀO ĐỂ CODE (TRẠNG THÁI: ĐÃ HOÀN TẤT 100%)

| STT | Tên tài liệu / Đầu vào cần bàn giao | Người phụ trách | Mức độ ưu tiên | Hạn chót bàn giao | Tình trạng & File lưu trữ chính thức |
|:---:|---|:---:|:---:|:---:|---|
| **1** | **Xác nhận 22 quyết định nghiệp vụ (D1 - D22)** | Cả nhóm (Hoàng chủ trì) | **Mức A** (Chặn code) | 14/10/2026 | ✅ **Đã chốt** tại [12-phan-quyen-rbac-trang-thai-thong-bao.md](file:///D:/ACE/docs/12-phan-quyen-rbac-trang-thai-thong-bao.md) |
| **2** | **Bảng chuyển trạng thái chuẩn (Booking & Phòng)** | Hoàng + Phong | **Mức A** (Chặn code) | 14/10/2026 | ✅ **Đã chốt** tại [12-phan-quyen-rbac-trang-thai-thong-bao.md](file:///D:/ACE/docs/12-phan-quyen-rbac-trang-thai-thong-bao.md) |
| **3** | **Wireframe chi tiết toàn bộ 15 màn hình (S01-S15)** | Kiệt | **Mức A** (Chặn code) | 16/10/2026 | ✅ **Đã hoàn thành** tại [04-sitemap-wireframe.md](file:///D:/ACE/docs/04-sitemap-wireframe.md) |
| **4** | **Ma trận phân quyền theo Route URL và Nút bấm (RBAC)** | Kiệt | **Mức A** (Chặn code) | 16/10/2026 | ✅ **Đã hoàn thành** tại [12-phan-quyen-rbac-trang-thai-thong-bao.md](file:///D:/ACE/docs/12-phan-quyen-rbac-trang-thai-thong-bao.md) |
| **5** | **Đặc tả chi tiết UC02 (Phòng & Loại phòng) + Class Diagram** | Hoàng | **Mức A** (Chặn code) | 18/10/2026 | ✅ **Đã hoàn thành** tại [09-dac-ta-use-case-he-thong-va-phong.md](file:///D:/ACE/docs/09-dac-ta-use-case-he-thong-va-phong.md) |
| **6** | **Đặc tả chi tiết UC03 (Tìm phòng & Đặt phòng) + Sequence/Activity** | Hoàng | **Mức A** (Chặn code) | 18/10/2026 | ✅ **Đã hoàn thành** tại [10-dac-ta-use-case-dat-phong-checkin-out.md](file:///D:/ACE/docs/10-dac-ta-use-case-dat-phong-checkin-out.md) |
| **7** | **Đặc tả chi tiết UC04 (Check-in)** | Phong | **Mức A** (Chặn code) | 18/10/2026 | ✅ **Đã hoàn thành** tại [10-dac-ta-use-case-dat-phong-checkin-out.md](file:///D:/ACE/docs/10-dac-ta-use-case-dat-phong-checkin-out.md) |
| **8** | **Đặc tả chi tiết UC05 (Check-out & Thanh toán) + Sequence/Activity** | Phong | **Mức A** (Chặn code) | 18/10/2026 | ✅ **Đã hoàn thành** tại [10-dac-ta-use-case-dat-phong-checkin-out.md](file:///D:/ACE/docs/10-dac-ta-use-case-dat-phong-checkin-out.md) |
| **9** | **Đặc tả chi tiết UC01 (Đăng nhập), UC06 (Dịch vụ) & UC07 (Thống kê)** | Kiệt + Khoa | **Mức A** (Chặn code) | 18/10/2026 | ✅ **Đã hoàn thành** tại [09-...](file:///D:/ACE/docs/09-dac-ta-use-case-he-thong-va-phong.md) & [11-dac-ta-use-case-dich-vu-va-thong-ke.md](file:///D:/ACE/docs/11-dac-ta-use-case-dich-vu-va-thong-ke.md) |
| **10** | **Danh sách 35 thông báo hệ thống chuẩn bằng tiếng Việt** | Kiệt | **Mức B** (Cần khi code) | 16/10/2026 | ✅ **Đã hoàn thành** tại [12-phan-quyen-rbac-trang-thai-thong-bao.md](file:///D:/ACE/docs/12-phan-quyen-rbac-trang-thai-thong-bao.md) |
| **11** | **Kịch bản Demo chi tiết (15 phút) & Đặc tả bộ dữ liệu mẫu** | Phong | **Mức B** (Cần khi code) | 16/10/2026 | ✅ **Đã hoàn thành** tại [13-kich-ban-demo-va-du-lieu-mau.md](file:///D:/ACE/docs/13-kich-ban-demo-va-du-lieu-mau.md) |
| **12** | **Ma trận truy vết (RTM), 30 Test Cases (BR1-BR7) & 20 câu hỏi đáp** | Hoàng + Phong | **Mức C** (Cần khi test) | 22/10/2026 | ✅ **Đã hoàn thành** tại [14-kiem-thu-nghiep-vu-va-ma-tran-truy-vet.md](file:///D:/ACE/docs/14-kiem-thu-nghiep-vu-va-ma-tran-truy-vet.md) |