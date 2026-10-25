# 08. CHỐT 22 QUYẾT ĐỊNH NGHIỆP VỤ (D1–D22), MA TRẬN TRẠNG THÁI & CHECKLIST ĐẦU VÀO ĐỂ KHOA CODE

> **Nguyên tắc "Zero Guesswork":** Khoa là người duy nhất phụ trách code full ứng dụng Spring Boot. Toàn bộ 22 quyết định nghiệp vụ, bảng chuyển trạng thái và 12 hạng mục tài liệu dưới đây đã được chuẩn hóa 100% theo `quy-trinh-nghiep-vu-moi.md` để Khoa cài đặt chính xác mà không phải tự suy đoán bất kỳ chi tiết nào.

---

## 1. Bảng 22 Quyết định Nghiệp vụ & Kỹ thuật Đã Chốt (`D1` – `D22`)

| Mã | Vấn đề nghiệp vụ / kỹ thuật | Giải pháp thống nhất áp dụng vào Báo cáo & Code Java | Phạm vi ảnh hưởng |
|:---:|---|---|---|
| **D1** | **Kiến trúc Không gian Giao diện** | Thiết kế **4 phân hệ giao diện độc lập**: (1) Landing Storytelling (`/`), (2) Dashboard Khách hàng (`/customer/dashboard`), (3) Dashboard Lễ tân (`/staff/dashboard`), (4) Dashboard Admin (`/admin/dashboard`). | Toàn hệ thống, Chương 6 |
| **D2** | **Phân quyền & Tự động điều hướng sau Đăng nhập** | Hệ thống có **3 Role**: `ADMIN`, `RECEPTIONIST`, `CUSTOMER`. Sau khi đăng nhập tại `/login`, `AuthenticationSuccessHandler` tự động chuyển hướng về đúng Dashboard của Role đó. | `UC01`, `SecurityConfig` |
| **D3** | **Đăng ký Khách hàng Online vs Tạo Khách Walk-in** | • **Khách Online (`/register`):** Nhập 5 trường (`full_name`, `date_of_birth`, `phone`, `username`, `password`) $\rightarrow$ tạo bản ghi `users` (`CUSTOMER`) và `customers` (`user_id` khác null).<br>• **Khách Walk-in (tại quầy):** Lễ tân chỉ nhập `full_name`, `phone`, `id_number` (CCCD) $\rightarrow$ lưu vào `customers` với `user_id = NULL` và `date_of_birth = NULL`. | `UC01`, `UC04`, Bảng `customers` |
| **D4** | **Cơ chế Đặt phòng Online theo Hạng phòng (`Quota`)** | Khi khách đặt Online (`UC03`), đơn lưu `room_type_id` và **`room_id = NULL`**. Hạng phòng còn đặt được khi tồn kho khả dụng $N_{\text{available}} = N_{\text{total}}(\neq \text{'MAINTENANCE'}) - N_{\text{booked}}(\in \{\text{'RESERVED'}, \text{'CHECKED\_IN'}\}) \ge 1$. | `UC03`, Bảng `bookings` |
| **D5** | **Định giá Linh hoạt Ngày thường / Cuối tuần** | Bảng `room_types` lưu 2 mức giá: `price_per_night` (áp dụng cho đêm Thứ 2 $\rightarrow$ Thứ 5) và `weekend_price_per_night` (áp dụng cho đêm Thứ 6, Thứ 7, Chủ Nhật). | `UC02`, `UC03`, Bảng `room_types` |
| **D6** | **Quy tắc Đặt cọc & Quyết toán (`Online 30/70` vs `Walk-in 0/100`)** | • **Khách Đặt Online (`UC03`):** Cọc trước **30%** tổng gói phòng ước tính (`++`) để giữ chỗ (`status = 'RESERVED'`). Khi Check-out thu **70% tiền phòng còn lại + 100% dịch vụ & phụ thu**.<br>• **Khách Walk-in (`UC04`):** Nhận phòng ngay tại quầy với **tiền cọc `0đ` (`deposit_amount = 0`)**. Khi Check-out thu **100% toàn bộ hóa đơn**. | `UC03`, `UC04`, `UC05` |
| **D7** | **Phiếu Xác nhận Đặt phòng & Mã QR Check-in** | Sau khi thanh toán cọc 30%, hệ thống sinh mã `booking_code` duy nhất (ví dụ `BK-8K4P9X2M`) và hiển thị **Phiếu xác nhận đặt phòng kèm mã QR**. Lễ tân dùng ô nhập/quét mã này trên `/staff/dashboard` để mở thủ tục Check-in. | `UC03`, `UC04`, `WF-03`, `WF-05` |
| **D8** | **Khóa an toàn khi Gán phòng vật lý (`Clean Room Guard`)** | Khi Check-in (`UC04`), hệ thống chỉ lọc các phòng vật lý thuộc đúng hạng phòng đang có **`rooms.status = 'AVAILABLE'` (Trống sạch)**. Tuyệt đối khóa các phòng `DIRTY`, `OCCUPIED`, `MAINTENANCE`. | `UC04`, `WF-06` |
| **D9** | **Chính sách Phụ thu Nhận phòng sớm (`Early Check-in`)** | Giờ nhận phòng chuẩn là `14:00`. Nếu nhận sớm: Trước 06:00 phụ thu `100%` giá 1 đêm; 06:00–09:00 phụ thu `50%`; 09:00–14:00 phụ thu `30%` (hoặc Lễ tân chọn miễn phí ưu đãi `0đ`). Lưu vào `bookings.early_checkin_fee`. | `UC04`, `UC05` |
| **D10** | **Chính sách Phụ thu Trả phòng trễ (`Late Check-out`)** | Giờ trả phòng chuẩn là trước `12:00` trưa. Nếu trả trễ: 12:00–15:00 phụ thu `30%` giá 1 đêm; 15:00–18:00 phụ thu `50%`; sau 18:00 phụ thu `100%` (Lễ tân có thể xác nhận hoặc miễn phí `0đ`). Lưu vào `bookings.late_checkout_fee`. | `UC05`, `WF-08` |
| **D11** | **Phân loại 4 Nhóm Dịch vụ & Đóng băng giá Room Folio** | Bảng `services` chia thành 4 nhóm `category IN ('MINIBAR', 'LAUNDRY', 'FB', 'DAMAGES')`. Khi ghi nợ vào `service_orders`, hệ thống sao chép `unit_price` lúc gọi và lưu `created_by` là lễ tân thực hiện. Cho phép xóa dòng ghi nhầm khi đang `CHECKED_IN`. | `UC02`, `UC06`, `WF-07` |
| **D12** | **Công thức Làm tròn Số nguyên `BIGINT` cho Biểu giá `++`** | Tính tuần tự trong Java Service:<br>1. `subtotal = room_charge + service_charge_total + early_checkin_fee + late_checkout_fee`<br>2. `service_charge_amount = Math.round(subtotal * 0.05)` *(SVC 5%)*<br>3. `vat_amount = Math.round((subtotal + service_charge_amount) * 0.10)` *(VAT 10%)*<br>4. `grand_total = subtotal + service_charge_amount + vat_amount`<br>5. `balance_due = grand_total - deposit_deducted` | `UC03`, `UC05`, Bảng `invoices` |
| **D13** | **Vòng đời Buồng phòng & Trạng thái `DIRTY` (Chờ dọn dẹp)** | Ngay sau khi Check-out (`UC05`), phòng vật lý tự động chuyển sang **`DIRTY` (màu cam)**. Trên `/staff/dashboard`, Lễ tân bấm nút **[Xác nhận đã dọn sạch]** để chuyển phòng từ `DIRTY` về lại **`AVAILABLE` (màu xanh lá)**. | `UC05`, `WF-05`, Bảng `rooms` |
| **D14** | **Nhãn hiển thị động `"Quá hạn"` (`OVERDUE`)** | Không thêm vào cột `status` của CSDL. Tính động bằng `booking.isOverdue()`: trả về `true` khi `status == 'CHECKED_IN'` và `check_out_date < LocalDate.now()`. | `UC04`, `UC05`, `WF-05` |
| **D15** | **Phương thức Thanh toán & Chống trùng Hóa đơn** | `PaymentMethod` gồm 3 giá trị: `CASH`, `CARD`, `TRANSFER`. Bảng `invoices` có ràng buộc `UNIQUE(booking_id)` và kiểm tra Idempotency trong `@Transactional` để bấm đúp chuột không tạo 2 hóa đơn. | `UC05`, Bảng `invoices` |
| **D16** | **Quy tắc Hủy đơn & Bảo toàn Lịch sử Dữ liệu** | Chỉ hủy được đơn đang ở trạng thái `RESERVED` (chuyển sang `CANCELLED`). Chặn xóa `rooms` đã có `bookings`, chặn xóa `room_types` đang có `rooms`/`bookings`, chặn xóa `services` đã có trong `service_orders`. | `UC02`, `UC03`, `NFR-04` |
| **D17** | **Công thức Tính `Occupancy Rate`, `ADR`, `RevPAR` (`UC07`)** | Tổng số phòng kinh doanh $R_{\text{sellable}} = \text{AVAILABLE} + \text{OCCUPIED} + \text{DIRTY}$ (**bắt buộc loại trừ phòng `MAINTENANCE` khỏi mẫu số**). $\text{OR (\%)} = (\text{OCCUPIED} / R_{\text{sellable}}) \times 100\%$. | `UC07`, `WF-10` |
| **D18** | **Biên Trùng lịch Khoảng ngày Nửa mở `[nhận, trả)`** | Cho phép khách mới nhận phòng đúng vào ngày khách cũ trả phòng (`existing.check_in_date < new.check_out_date AND existing.check_out_date > new.check_in_date`). | `UC03`, `UC04` |
| **D19** | **Tài khoản Demo Chuẩn nạp sẵn trong `sample-data.sql`** | • Admin: `admin` / `admin` (`ADMIN`)<br>• Lễ tân: `nhanvien` / `nhanvien` (`RECEPTIONIST`)<br>• Khách hàng mẫu: `khach` / `khach` (`CUSTOMER`) | `UC01`, `sample-data.sql` |
| **D20** | **Định dạng Tiền tệ, Ngày giờ & Múi giờ** | Tiền tệ kiểu `BIGINT` hiển thị `1.250.000đ`; ngày định dạng `dd/MM/yyyy`; múi giờ cố định `Asia/Ho_Chi_Minh`. | Toàn bộ giao diện |
| **D21** | **Hệ thống Màu & Theme (`ACE Color System`)** | Mặc định **Dark Mode**, có nút chuyển đổi **Light Mode** lưu vào `localStorage ('ace-theme')`; giữ nguyên màu vàng thương hiệu `#FDBF2D`. | `tokens.css`, `theme.js` |
| **D22** | **Cấu trúc Quyển Báo cáo & Bộ Sơ đồ UML** | Báo cáo gồm **8 Chương (Chương 1 $\rightarrow$ Chương 8) và Tài liệu tham khảo, bỏ Phụ lục**. Chương 4 có đầy đủ **24 sơ đồ UML** (1 UC tổng quát + 7 UC chi tiết + 7 Activity + 7 Sequence + 1 Class + 1 ERD). | Quyển báo cáo `bao-cao.docx` |

---

## 2. Bảng Chuyển Trạng thái Đồng bộ (`bookings.status` $\leftrightarrow$ `rooms.status`)

| Thao tác nghiệp vụ | Điều kiện tiên quyết | `bookings.status` sau | `rooms.status` sau | Ghi nhận dữ liệu / thời gian | Vai trò thực hiện |
|---|---|:---:|:---:|---|---|
| **1. Khách đặt phòng Online & Cọc 30% (`UC03`)** | Hạng phòng còn Quota ($N_{\text{available}} \ge 1$), `check_out > check_in` | `RESERVED` | *(Chưa gán `room_id`)* | `deposit_amount = 30%`, sinh `booking_code` & Phiếu QR | `CUSTOMER` |
| **2. Hủy đặt phòng (`UC03`)** | Đơn đang ở trạng thái `RESERVED` | `CANCELLED` | *(Không đổi)* | Hoàn trả 1 suất Quota cho hạng phòng | `CUSTOMER`, `RECEPTIONIST`, `ADMIN` |
| **3A. Check-in khách đặt trước (`UC04`)** | Đơn `RESERVED`, chọn phòng vật lý thuộc hạng đó đang `AVAILABLE` | `CHECKED_IN` | `OCCUPIED` | Gán `room_id`, `actual_check_in = NOW()`, ghi `early_checkin_fee` (nếu có) | `RECEPTIONIST`, `ADMIN` |
| **3B. Tiếp nhận khách Walk-in (`UC04`)** | Chọn phòng vật lý đang `AVAILABLE`, nhập thông tin khách & CCCD | `CHECKED_IN` | `OCCUPIED` | Tạo `bookings` với `deposit_amount = 0`, `actual_check_in = NOW()` | `RECEPTIONIST`, `ADMIN` |
| **4. Ghi / Xóa dịch vụ Room Folio (`UC06`)** | Đơn đang ở trạng thái `CHECKED_IN` | `CHECKED_IN` | `OCCUPIED` | Lưu `service_orders` (đóng băng `unit_price`, lưu `created_by`) | `RECEPTIONIST`, `ADMIN` |
| **5. Check-out & Quyết toán `++` (`UC05`)** | Đơn đang `CHECKED_IN`, chưa có bản ghi trong `invoices` | `CHECKED_OUT` | **`DIRTY`** *(Chờ dọn)* | Tạo `invoices` (`++`, trừ cọc), `actual_check_out = NOW()`, `paid_at = NOW()` | `RECEPTIONIST`, `ADMIN` |
| **6. Xác nhận đã dọn sạch phòng (`UC05`)** | Phòng vật lý đang ở trạng thái `DIRTY` | *(Đã đóng)* | **`AVAILABLE`** *(Trống sạch)* | Mở khóa phòng để sẵn sàng gán cho lượt khách mới | `RECEPTIONIST`, `ADMIN` |
| **7. Bật / Tắt trạng thái Bảo trì (`UC02`)** | Phòng đang `AVAILABLE` (không có khách ở) | *(Không đổi)* | `MAINTENANCE` $\leftrightarrow$ `AVAILABLE` | Loại trừ phòng `MAINTENANCE` khỏi mẫu số `Occupancy Rate` | **Chỉ `ADMIN`** |

---

## 3. Bảng Tổng hợp Trạng thái 12 Đầu vào Nghiệp vụ & Kỹ thuật (Hoàn tất 100%)

| STT | Hạng mục tài liệu / Đầu vào | Phụ trách Báo cáo | Trạng thái & File lưu trữ trong `docs/` |
|:---:|---|:---:|---|
| **1** | Quy trình nghiệp vụ mới chuẩn PMS & Online Booking | Cả nhóm (Khoa chủ trì) | ✅ Đã chốt tại `quy-trinh-nghiep-vu-moi.md` |
| **2** | Mục lục Chương 1–8 (bỏ Phụ lục) & Hướng dẫn 7 Use Case | Khoa + Hoàng | ✅ Đã hoàn thành tại `00-muc-luc-va-huong-dan-7-uc.md` |
| **3** | Tổng quan dự án, Backlog 18 FR & 7 Quy tắc `BR1–BR7` | Hoàng (Chương 1, 3) | ✅ Đã hoàn thành tại `01-project-brief.md`, `02-...`, `03-...` |
| **4** | Sitemap 4 Phân hệ & 10 Wireframe (`WF-01` – `WF-10`) | Kiệt (Chương 6) | ✅ Đã hoàn thành tại `04-sitemap-wireframe.md` & `05-...` |
| **5** | Class Diagram, ERD, Từ điển 8 bảng & SQL (`Chương 5`) | Khoa (Chương 4, 5) | ✅ Đã hoàn thành tại `06-tai-lieu-ky-thuat.md` |
| **6** | Kế hoạch 4 tuần & Bộ 4 ảnh phân công (24 sơ đồ UML) | Cả nhóm | ✅ Đã hoàn thành tại `07-ke-hoach-timeline.md` & 4 ảnh `.png` |
| **7** | Khảo sát hiện trạng, Đặc tả & Bộ sơ đồ `UC01`, `UC02` | Phong, Kiệt, Hoàng | 🔄 Sẵn sàng xuất ở đợt tiếp theo (`09-...`) |
| **8** | Đặc tả & Bộ sơ đồ `UC03`, `UC04`, `UC05` | Hoàng, Phong | 🔄 Sẵn sàng xuất ở đợt tiếp theo (`10-...`) |
| **9** | Đặc tả & Bộ sơ đồ `UC06`, `UC07` | Kiệt, Khoa | 🔄 Sẵn sàng xuất ở đợt tiếp theo (`11-...`) |
| **10** | Ma trận RBAC 3 Role & Từ điển thông báo tiếng Việt | Kiệt, Khoa | 🔄 Sẵn sàng xuất ở đợt tiếp theo (`12-...`) |
| **11** | Bộ dữ liệu mẫu (`sample-data.sql`) & Kịch bản Demo 14p | Khoa, Phong | 🔄 Sẵn sàng xuất ở đợt tiếp theo (`13-...`) |
| **12** | Ma trận truy vết RTM (Mục 7.4), Test Cases & 20 Câu hỏi | Hoàng, Phong, Khoa | 🔄 Sẵn sàng xuất ở đợt tiếp theo (`14-...`) |