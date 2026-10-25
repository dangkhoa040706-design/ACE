# CHƯƠNG 3. XÁC ĐỊNH VÀ ĐẶC TẢ YÊU CẦU (BR, FR, NFR & MOSCOW)

> **Người phụ trách:** **Hoàng** (File giao nộp: `chuong-3-hoang.docx`).  
> **Ánh xạ Báo cáo:** Nội dung chuẩn hóa cho toàn bộ **Chương 3 (Mục 3.1, 3.2, 3.3, 3.4)** trong quyển báo cáo cuối kỳ môn `502050`.

---

## 3.1. Danh sách các Quy tắc Nghiệp vụ Cốt lõi (`BR1` – `BR7`)

| Mã quy tắc | Tên quy tắc nghiệp vụ | Mô tả chi tiết quy tắc áp dụng trên hệ thống ACE Hotel |
|:---:|---|---|
| **BR1** | Kiểm tra tồn kho Hạng phòng (*Room Type Quota*) | Khi khách hàng đặt phòng trực tuyến (`UC03`), đơn đặt ghi nhận theo `room_type_id` và chưa gán phòng vật lý (`room_id = NULL`). Một hạng phòng chỉ được phép đặt trong khoảng ngày nửa mở `[checkIn, checkOut)` khi số suất khả dụng $N_{\text{available}} = N_{\text{total}}(\text{status} \neq \text{'MAINTENANCE'}) - N_{\text{booked}}(\text{status} \in \{\text{'RESERVED'}, \text{'CHECKED\_IN'}\} \text{ giao ngày}) \ge 1$. |
| **BR2** | Khóa an toàn khi Gán phòng vật lý (*Clean Room Guard*) | Khi làm thủ tục Check-in (`UC04`), nhân viên lễ tân chỉ được phép gán khách vào phòng vật lý đang ở trạng thái **`AVAILABLE` (Trống & Đã dọn sạch)**. Các phòng đang ở trạng thái `OCCUPIED`, `DIRTY` hoặc `MAINTENANCE` bị khóa hoàn toàn. |
| **BR3** | Khoảng ngày lưu trú & Định giá linh hoạt (*Dynamic Pricing*) | Ngày trả phòng dự kiến phải sau ngày nhận phòng dự kiến ít nhất 1 đêm (`check_out_date > check_in_date`). Tiền phòng gốc (`room_charge`) được cộng dồn theo từng đêm: đêm Thứ 2 đến Thứ 5 áp dụng `price_per_night`, đêm Thứ 6 đến Chủ Nhật áp dụng `weekend_price_per_night`. |
| **BR4** | Quy tắc Đặt cọc & Quyết toán (*Online 30/70 vs Walk-in 0/100*) | • **Khách đặt trực tuyến (Online):** Thanh toán **cọc 30%** tổng gói phòng ước tính (`++`) để giữ chỗ (`status = 'RESERVED'`); khi Check-out thanh toán **70% tiền phòng còn lại + 100% dịch vụ phát sinh & phụ thu**.<br>• **Khách vãng lai (Walk-in):** Nhận phòng trực tiếp tại quầy với **tiền cọc `0đ` (`deposit_amount = 0`)**; khi Check-out thanh toán **100% toàn bộ hóa đơn**. |
| **BR5** | Quản lý Room Folio & Đóng băng đơn giá (*Price Snapshot*) | Chỉ cho phép thêm hoặc xóa dòng dịch vụ (`service_orders`) khi đơn đặt phòng đang ở trạng thái `CHECKED_IN`. Khi ghi nợ dịch vụ, hệ thống sao chép `services.unit_price` hiện hành vào `service_orders.unit_price` để bảo toàn lịch sử giá. |
| **BR6** | Công thức Quyết toán Biểu giá `++` (`SVC 5%` + `VAT 10%`) | Mọi hóa đơn quyết toán (`invoices`) đều tính tuần tự theo kiểu số nguyên `BIGINT`:<br>`subtotal = room_charge + service_charge_total + early_checkin_fee + late_checkout_fee`<br>`SVC = ROUND(subtotal × 5%)` $\rightarrow$ `VAT = ROUND((subtotal + SVC) × 10%)`<br>`grand_total = subtotal + SVC + VAT` $\rightarrow$ `balance_due = grand_total - deposit_deducted`. |
| **BR7** | Vòng đời Dọn phòng (`DIRTY`) & Bảo toàn Lịch sử | Ngay sau khi Check-out, phòng vật lý tự động chuyển sang **`DIRTY` (Chờ dọn dẹp)** và chỉ về lại `AVAILABLE` khi lễ tân bấm xác nhận đã dọn sạch. Chỉ hủy được đơn đang `RESERVED`. Chặn xóa các bản ghi danh mục (`room_types`, `rooms`, `services`) đã phát sinh dữ liệu giao dịch. |

---

## 3.2. Danh sách Yêu cầu Chức năng (`FR-01` đến `FR-18`)

| Mã FR | Mô tả chi tiết Yêu cầu chức năng | Use Case | Bảng CSDL liên quan | Mức MoSCoW |
|:---:|---|:---:|---|:---:|
| **FR-01** | Hệ thống cho phép khách hàng mới đăng ký tài khoản trực tuyến bằng 5 thông tin bắt buộc (Họ và tên, Ngày sinh, Số điện thoại, Tên đăng nhập, Mật khẩu) và gán vai trò `CUSTOMER`. | `UC01` | `users`, `customers` | **Must** |
| **FR-02** | Hệ thống cho phép người dùng đăng nhập/đăng xuất, kiểm tra mật khẩu băm `BCrypt` và tự động điều hướng về đúng Dashboard theo 3 vai trò (`ADMIN`, `RECEPTIONIST`, `CUSTOMER`). | `UC01` | `users` | **Must** |
| **FR-03** | Hệ thống cho phép Quản trị viên thêm, sửa, xóa, xem Hạng phòng kèm cấu hình **Giá ngày thường (Thứ 2–Thứ 5)** và **Giá cuối tuần/lễ (Thứ 6–Chủ Nhật)**. | `UC02` | `room_types`, `rooms`, `bookings` | **Must** |
| **FR-04** | Hệ thống cho phép Quản trị viên thêm, sửa, xóa, xem Phòng vật lý (số phòng, tầng, hạng phòng) và bật/tắt trạng thái bảo trì (`MAINTENANCE`). | `UC02` | `rooms`, `room_types`, `bookings` | **Must** |
| **FR-05** | Hệ thống cho phép Quản trị viên thêm, sửa, xóa, xem Danh mục Dịch vụ lưu trú phân theo 4 nhóm chuẩn (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`). | `UC02` | `services`, `service_orders` | **Must** |
| **FR-06** | Hệ thống cho phép tìm kiếm các Hạng phòng còn suất trống theo khoảng ngày nhận/trả và số lượng khách dựa trên công thức tồn kho Hạng phòng (*Room Type Quota*) và tính giá động. | `UC03` | `room_types`, `rooms`, `bookings` | **Must** |
| **FR-07** | Hệ thống hiển thị Pop-up Modal xác nhận chi phí minh bạch theo biểu giá `++`, xử lý thanh toán **tiền cọc 30%** và tạo đơn giữ chỗ ở trạng thái `RESERVED` (`room_id = NULL`). | `UC03` | `bookings`, `customers` | **Must** |
| **FR-08** | Hệ thống tự động sinh **Phiếu xác nhận đặt phòng kèm Mã QR** (`booking_code`) ngay sau khi đặt cọc thành công và lưu trữ trên Dashboard Khách hàng. | `UC03` | `bookings`, `customers` | **Must** |
| **FR-09** | Hệ thống cho phép khách hàng (hoặc lễ tân) hủy đơn đặt phòng đang ở trạng thái giữ chỗ `RESERVED` và hoàn trả lại suất trống cho hạng phòng. | `UC03` | `bookings` | **Should** |
| **FR-10** | Hệ thống cho phép Lễ tân quét/nhập mã QR đơn đặt trước, lọc danh sách các phòng vật lý thuộc đúng hạng phòng đang ở trạng thái **`AVAILABLE` (Trống sạch)** để gán phòng, ghi nhận phụ thu nhận sớm (nếu có) và chuyển phòng sang `OCCUPIED`. | `UC04` | `bookings`, `rooms`, `customers` | **Must** |
| **FR-11** | Hệ thống cho phép Lễ tân tiếp nhận và Check-in trực tiếp cho **Khách vãng lai (Walk-in)** tại quầy: chọn phòng sạch nhận ngay với **tiền cọc `0đ` (`deposit_amount = 0`)**. | `UC04` | `customers`, `bookings`, `rooms` | **Must** |
| **FR-12** | Hệ thống cho phép Lễ tân ghi nhận dịch vụ lưu trú (4 nhóm) vào tài khoản phòng đang ở (**Room Folio**), đóng băng `unit_price` tại thời điểm gọi và hỗ trợ xóa dòng dịch vụ ghi nhầm khi chưa thanh toán. | `UC06` | `service_orders`, `services`, `bookings` | **Must** |
| **FR-13** | Hệ thống tự động tổng hợp quyết toán Check-out theo biểu giá `++`: cộng tiền phòng, tổng dịch vụ Room Folio, phụ thu trả phòng trễ (sau 12:00), tính **Phí phục vụ 5% (`SVC`)**, **Thuế GTGT 10% (`VAT`)** và **khấu trừ tiền đã cọc** (Khách Online trả 70% tiền phòng còn lại + dịch vụ/phụ thu; Khách Walk-in trả 100%). | `UC05` | `bookings`, `service_orders`, `invoices` | **Must** |
| **FR-14** | Hệ thống cho phép xác nhận hoàn tất thanh toán (`CASH`, `CARD`, `TRANSFER`), lập và in Hóa đơn VAT (`invoices`) và tự động chuyển phòng vật lý sang trạng thái **`DIRTY` (Chờ dọn dẹp)**. | `UC05` | `invoices`, `bookings`, `rooms` | **Must** |
| **FR-15** | Hệ thống khóa không cho gán phòng `DIRTY` cho khách mới và cung cấp nút **[✓ Xác nhận đã dọn sạch]** trên Dashboard Lễ tân để chuyển phòng từ `DIRTY` về lại `AVAILABLE`. | `UC05` | `rooms` | **Must** |
| **FR-16** | Hệ thống hiển thị trên Dashboard Lễ tân sơ đồ lưới phòng trực quan mã hóa màu theo 4 trạng thái (`AVAILABLE`, `OCCUPIED`, `DIRTY`, `MAINTENANCE`) và danh sách đơn cần xử lý trong ngày. | `UC04`, `UC05` | `rooms`, `bookings` | **Should** |
| **FR-17** | Hệ thống cho phép Quản trị viên xem báo cáo cơ cấu doanh thu đa chiều theo thời gian (tách bạch Tiền phòng, 4 nhóm Dịch vụ, Phụ thu, Phí phục vụ `SVC 5%` và Thuế `VAT 10%`). | `UC07` | `invoices`, `service_orders`, `bookings` | **Should** |
| **FR-18** | Hệ thống tự động tính toán và biểu đồ hóa các chỉ số hiệu quả khai thác khách sạn trên Dashboard Admin: **Tỷ lệ lấp đầy phòng (`Occupancy Rate`)** (loại trừ phòng `MAINTENANCE`), **ADR** và **RevPAR**. | `UC07` | `rooms`, `bookings`, `invoices` | **Could** |

---

## 3.3. Danh sách Yêu cầu Phi chức năng (`NFR-01` đến `NFR-09`)

| Mã NFR | Nhóm chất lượng | Nội dung Yêu cầu phi chức năng | Mức MoSCoW |
|:---:|---|---|:---:|
| **NFR-01** | **Hiệu năng** | Các thao tác tìm hạng phòng theo Quota, gán phòng Check-in, ghi nợ Room Folio và tính toán quyết toán `++` phản hồi trong vòng dưới **2 giây** với quy mô dữ liệu demo. | **Should** |
| **NFR-02** | **Bảo mật xác thực** | Mật khẩu của mọi tài khoản (`ADMIN`, `RECEPTIONIST`, `CUSTOMER`) bắt buộc băm bằng thuật toán **`BCrypt`**, tuyệt đối không lưu mật khẩu dạng văn bản thô trong CSDL. | **Must** |
| **NFR-03** | **Bảo mật phân quyền** | Kiểm tra quyền truy cập ở phía máy chủ (`Server-side RBAC`) cho từng phân hệ (`/admin/**`, `/staff/**`, `/customer/**`); chặn mọi truy cập trái quyền bằng mã lỗi `403 Forbidden`. | **Must** |
| **NFR-04** | **Toàn vẹn dữ liệu** | Đảm bảo đầy đủ khóa chính, khóa ngoại, ràng buộc `UNIQUE` và `CHECK`; xử lý giao dịch `@Transactional` chống đặt vượt Quota, chống gán phòng bẩn (`DIRTY`) và chống tạo trùng hóa đơn. | **Must** |
| **NFR-05** | **Tính khả dụng & Giao diện** | Giao diện 100% tiếng Việt, tuân thủ hệ thống màu **`ACE Color System`** hỗ trợ chuyển đổi mượt mà **Dark Mode / Light Mode**; hoàn tất mỗi quy trình chính trong tối đa 3–4 bước. | **Should** |
| **NFR-06** | **Khả năng mở rộng** | Kiến trúc mã nguồn tách 3 lớp (`Controller`, `Service`, `Repository`); chuyển đổi từ H2 Database sang MySQL/PostgreSQL chỉ cần thay đổi cấu hình `application.properties`. | **Should** |
| **NFR-07** | **Tương thích môi trường** | Hệ thống chạy ổn định trên Windows/macOS với **JDK 21** và hiển thị chuẩn xác trên trình duyệt Google Chrome / Microsoft Edge phiên bản mới. | **Must** |
| **NFR-08** | **Độ tin cậy & Lưu trữ bền** | Cơ sở dữ liệu H2 cấu hình ở chế độ lưu file vật lý (`./data/ace_hotel_db`), đảm bảo dữ liệu không bị mất khi tắt và khởi động lại ứng dụng; có cờ nạp lại dữ liệu mẫu sạch phục vụ demo. | **Should** |
| **NFR-09** | **Khả năng bảo trì** | Mã nguồn đặt tên rõ ràng theo chuẩn Java, có chú thích ở các công thức nghiệp vụ (`Quota`, `++`, `Occupancy Rate`) và kèm tài liệu `README.md` hướng dẫn chạy trong 5 phút. | **Should** |

---

## 3.4. Bảng Tổng hợp Ưu tiên MoSCoW

| Mức độ ưu tiên | Yêu cầu chức năng (`FR`) | Yêu cầu phi chức năng (`NFR`) |
|---|---|---|
| **Must Have** *(Bắt buộc phải có)* | `FR-01`, `FR-02`, `FR-03`, `FR-04`, `FR-05`, `FR-06`, `FR-07`, `FR-08`, `FR-10`, `FR-11`, `FR-12`, `FR-13`, `FR-14`, `FR-15` *(14 FR cốt lõi)* | `NFR-02`, `NFR-03`, `NFR-04`, `NFR-07` |
| **Should Have** *(Quan trọng, nên có)* | `FR-09` (Hủy đơn `RESERVED`), `FR-16` (Sơ đồ lưới 4 trạng thái), `FR-17` (Báo cáo doanh thu đa chiều `++`) | `NFR-01`, `NFR-05`, `NFR-06`, `NFR-08`, `NFR-09` |
| **Could Have** *(Có thêm để hoàn thiện)* | `FR-18` (Chỉ số `Occupancy Rate`, `ADR`, `RevPAR`) | — |
| **Won't Have** *(Không làm trong phạm vi này)* | Kết nối sàn OTA bên ngoài (Booking.com, Agoda); cổng thanh toán ngân hàng thật; quản lý nhân sự/lương/kho bếp; chuỗi nhiều chi nhánh; đặt gộp nhiều phòng trong 1 mã đơn. | — |