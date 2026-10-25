# 13. ĐẶC TẢ BỘ DỮ LIỆU MẪU (`sample-data.sql`) & KỊCH BẢN DEMO KHÉP KÍN (14 PHÚT)

> **Ánh xạ Báo cáo:** Phục vụ trực tiếp **Chương 7 (Mục 7.2 Bộ dữ liệu mẫu & Kịch bản chạy Demo khép kín)**.  
> **Phụ trách:** **Khoa** (Thiết kế `sample-data.sql` & Thao tác máy tính khi Demo) · **Phong** (Thuyết trình full 12 slide & Dẫn lời Demo).

---

## 1. Đặc tả Chi tiết Bộ Dữ liệu Mẫu (`sample-data.sql`)

### 1.1. Bảng `users` (3 Tài khoản Demo Chuẩn cho 3 Phân hệ)
Mật khẩu của các tài khoản trùng với tên đăng nhập (lưu dạng chuỗi băm `BCrypt` trong `sample-data.sql`):

| `user_id` | `username` | Mật khẩu | `full_name` | `role` | Điều hướng tự động sau Login |
|:---:|---|---|---|:---:|---|
| `1` | `admin` | `admin` | Quản Trị Viên Hệ Thống | `ADMIN` | `/admin/dashboard` (`WF-10`) |
| `2` | `nhanvien` | `nhanvien` | Nhân Viên Lễ Tân PMS | `RECEPTIONIST` | `/staff/dashboard` (`WF-05`) |
| `3` | `khach` | `khach` | Khách Hàng Thân Thiết | `CUSTOMER` | `/customer/dashboard` (`WF-03`) |

---

### 1.2. Bảng `room_types` (5 Hạng phòng chuẩn kèm Giá Ngày thường T2–T5 & Giá Cuối tuần T6–CN)
| `room_type_id` | `name` | `price_per_night` (T2–T5) | `weekend_price_per_night` (T6–CN) | `capacity` | Mô tả tiện nghi |
|:---:|---|---:|---:|:---:|---|
| `1` | **Standard** | `780000` | `950000` | `2` | Phòng tiêu chuẩn giường đôi, đầy đủ tiện nghi cơ bản |
| `2` | **Superior** | `950000` | `1150000` | `2` | Phòng diện tích rộng, cửa sổ thoáng nhìn ra phố |
| `3` | **Deluxe** | `1000000` | `1250000` | `2` | Phòng cao cấp ban công riêng, nội thất sang trọng |
| `4` | **Family** | `1850000` | `2200000` | `4` | Phòng gia đình 2 giường lớn, không gian sinh hoạt chung |
| `5` | **Suite** | `3200000` | `3800000` | `4` | Phòng hạng sang có phòng khách riêng biệt, bồn tắm nằm |

---

### 1.3. Bảng `rooms` (12 Phòng vật lý trải đều 4 tầng – Đủ 4 trạng thái vòng đời)
| `room_id` | `room_number` | `floor` | `room_type_id` | Hạng phòng | `status` ban đầu | Vai trò trong Kịch bản Demo |
|:---:|:---:|:---:|:---:|---|:---:|---|
| `1` | `101` | `1` | `1` | Standard | `AVAILABLE` | 🟢 Trống sạch – Dùng để Demo tiếp nhận **Khách Walk-in (0đ cọc)** (`UC04`) |
| `2` | `102` | `1` | `1` | Standard | **`DIRTY`** | 🟠 Chờ dọn dẹp – Dùng để Demo nút **[✓ Xác nhận đã dọn sạch]** (`UC05`) |
| `3` | `103` | `1` | `2` | Superior | `MAINTENANCE` | ⚪ Bảo trì – Minh họa khóa phòng & **loại trừ khỏi mẫu số `Occupancy Rate`** (`UC07`) |
| `4` | `201` | `2` | `3` | Deluxe | `AVAILABLE` | 🟢 Trống sạch – Dùng để **Gán phòng vật lý sạch** khi khách Online (`BK-2402`) đến Check-in (`UC04`) |
| `5` | `202` | `2` | `2` | Superior | `OCCUPIED` | 🔵 Đang ở – Đơn khách Walk-in (`BK-2405`, cọc `0đ`), dùng để Demo **Check-out Walk-in thu 100%** |
| `6` | `205` | `2` | `1` | Standard | `AVAILABLE` | 🟢 Trống sạch – Sẵn sàng đón khách |
| `7` | `301` | `3` | `3` | Deluxe | `OCCUPIED` | 🔵 Đang ở – Đơn khách Online (`BK-2401`, đã cọc 30%), dùng để Demo **Room Folio (`UC06`) & Check-out thu 70% còn lại + DV (`UC05`)** |
| `8` | `302` | `3` | `3` | Deluxe | `AVAILABLE` | 🟢 Trống sạch – Phòng Deluxe dự phòng để lọc khi gán phòng |
| `9` | `305` | `3` | `4` | Family | `AVAILABLE` | 🟢 Trống sạch – Dùng để Demo Admin bật/tắt Bảo trì (`UC02`) |
| `10` | `401` | `4` | `4` | Family | `DIRTY` | 🟠 Vừa trả phòng (`BK-2406`) – Minh họa chặn xóa phòng đã có lịch sử (`BR7`) |
| `11` | `418` | `4` | `5` | Suite | `OCCUPIED` | 🔴 Đang ở nhưng `check_out_date < TODAY` (`BK-2403`) – Minh họa badge đỏ **"Quá hạn"** |
| `12` | `420` | `4` | `5` | Suite | `AVAILABLE` | 🟢 Phòng hạng Suite trống sạch |

*(Tổng kết 12 phòng ban đầu: 6 phòng `AVAILABLE`, 3 phòng `OCCUPIED`, 2 phòng `DIRTY`, 1 phòng `MAINTENANCE` $\rightarrow$ Tổng số phòng kinh doanh $R_{\text{sellable}} = 6 + 3 + 2 = 11$ phòng; `Occupancy Rate` ban đầu $= 3 / 11 \approx 27.3\%$).*

---

### 1.4. Bảng `services` (8 Dịch vụ chuẩn chia đều vào 4 Nhóm `category`)
| `service_id` | `name` | `category` | `unit_price` (VNĐ) | `unit` | Ghi chú kịch bản Demo |
|:---:|---|:---:|---:|:---:|---|
| `1` | **Nước ngọt & Bia Tiger lon** | `MINIBAR` | `35000` | `Lon` | Nhóm 1: Minibar đồ uống |
| `2` | **Combo Snack & Hạt điều cao cấp** | `MINIBAR` | `50000` | `Gói` | Nhóm 1: Minibar đồ ăn nhẹ |
| `3` | **Giặt ủi nhanh bộ trang phục** | `LAUNDRY` | `80000` | `Bộ` | Nhóm 2: Giặt ủi lấy ngay |
| `4` | **Giặt hấp / Giặt khô Áo Vest** | `LAUNDRY` | `150000` | `Chiếc` | Nhóm 2: Giặt khô cao cấp |
| `5` | **Bữa sáng Buffet gọi thêm** | `FB` | `150000` | `Suất` | Nhóm 3: Ẩm thực nhà hàng |
| `6` | **Phục vụ món nóng tại phòng (In-room Dining)** | `FB` | `220000` | `Phần` | Nhóm 3: Ẩm thực tại phòng |
| `7` | **Đền bù vỡ ly thủy tinh / cốc sứ** | `DAMAGES` | `100000` | `Cái` | Nhóm 4: Đền bù tài sản |
| `8` | **Đền bù mất thẻ từ chìa khóa phòng** | `DAMAGES` | `200000` | `Thẻ` | **Chưa có đơn gọi nào** – Dùng để Demo Admin xóa dịch vụ thành công (`UC02`) |

---

### 1.5. Bảng `bookings`, `service_orders` & `invoices` (Sử dụng ngày tương đối `CURRENT_DATE`)

| Mã `booking_code` | Khách hàng (Loại khách) | Hạng đặt (`room_type_id`) | Phòng gán (`room_id`) | Lịch lưu trú (`check_in` $\rightarrow$ `check_out`) | Tiền phòng gốc (`room_charge`) | Tiền đã cọc (`deposit_amount`) | `status` | Mục đích trong buổi Demo |
|:---:|---|---|:---:|---|---:|---:|:---:|---|
| **`BK-2401`** | Nguyễn Minh Anh *(Khách Online)* | Deluxe (`3`) | **`P.301`** | `TODAY - 2` $\rightarrow$ `TODAY` (2 đêm) | `2.000.000đ` | **`693.000đ`** *(Đã cọc 30% của 2.310k)* | `CHECKED_IN` | Đang có sẵn `Room Folio = 300.000đ`. Dùng để **Demo UC06 (Thêm/Xóa dịch vụ)** & **Demo UC05 (Check-out Online: Tổng `2.656.500đ` trừ cọc `693.000đ` = Thực thu `1.963.500đ`, phòng 301 chuyển sang `DIRTY`)** |
| **`BK-2402`** | Trần Quốc Bảo *(Khách Online)* | Deluxe (`3`) | **`NULL`** *(Chưa gán)* | `TODAY` $\rightarrow$ `TODAY + 2` (2 đêm) | `2.000.000đ` | **`693.000đ`** *(Đã cọc 30%)* | `RESERVED` | **Demo UC04 (Check-in khách đặt trước):** Quét/nhập mã `BK-2402` $\rightarrow$ Hệ thống lọc phòng Deluxe sạch (`P.201`, `P.302`) $\rightarrow$ Gán **`P.201`** chuyển sang `OCCUPIED` |
| **`BK-2403`** | Lê Thu Hà *(Khách Online)* | Suite (`5`) | **`P.418`** | `TODAY - 3` $\rightarrow$ `TODAY - 1` (2 đêm) | `6.400.000đ` | `2.217.600đ` *(Đã cọc 30%)* | `CHECKED_IN` | Minh họa cảnh báo đỏ **"Quá hạn (`OVERDUE`)"** trên Dashboard Lễ tân (`WF-05`) |
| **`BK-2404`** | Nguyễn Minh Anh *(Khách Online)* | Standard (`1`) | **`NULL`** *(Chưa gán)* | `TODAY + 3` $\rightarrow$ `TODAY + 5` (2 đêm) | `1.560.000đ` | `540.540đ` *(Đã cọc 30%)* | `RESERVED` | Hiển thị sẵn Phiếu xác nhận QR trên `/customer/dashboard` (`WF-03`) & dùng để Demo **Hủy đơn giữ chỗ (`UC03`)** |
| **`BK-2405`** | Phạm Gia Huy *(Khách Walk-in)* | Superior (`2`) | **`P.202`** | `TODAY - 1` $\rightarrow$ `TODAY` (1 đêm) | `950.000đ` | **`0đ`** *(Walk-in không cọc)* | `CHECKED_IN` | Minh họa **Khách Walk-in cọc `0đ`**: Khi Check-out hệ thống thu **100% toàn bộ hóa đơn** (`balance_due = grand_total`) |
| **`BK-2406`** | Đặng Minh Khôi *(Khách Online)* | Family (`4`) | **`P.401`** | `TODAY - 4` $\rightarrow$ `TODAY - 2` (2 đêm) | `3.700.000đ` | `1.282.050đ` | `CHECKED_OUT` | Đã có Hóa đơn `INV-1` hoàn tất; phòng `P.401` đang ở trạng thái **`DIRTY`** và dùng để minh họa số liệu báo cáo `UC07` |

---

## 2. Kịch bản Thuyết trình 12 Slide & Demo Trực tiếp (Tổng 14 Phút)

> **Phân công thực hiện tại buổi bảo vệ:**  
> - **Phong:** Một mình thuyết trình xuyên suốt toàn bộ **12 Slide** và dẫn lời từng bước trong lúc chạy Demo trực tiếp (14 phút).  
> - **Khoa:** Trực tiếp điều khiển máy tính, thao tác mượt mà qua 4 phân hệ giao diện trên trình duyệt khớp từng câu dẫn của Phong (và mở video dự phòng ở Slide 10 nếu cần).  
> - **Cả 4 thành viên (Khoa, Hoàng, Phong, Kiệt):** Cùng đứng trên bục trả lời câu hỏi phản biện của hội đồng (5–8 phút).

---

### 2.1. Phân bổ Thời lượng 14 Phút theo 12 Slide

| Slide | Thời lượng | Nội dung trình bày (Phong thuyết trình) | Phối hợp trên máy (Khoa) |
|:---:|:---:|---|---|
| **Slide 1–3** | `00:00 – 02:00` (2.0 phút) | Giới thiệu đề tài **ACE Hotel**, phạm vi 2 phân hệ (*Public Booking Portal* & *Backoffice PMS*), khảo sát hiện trạng và 5 bài toán thực tế cần giải quyết (Overbooking, phòng bẩn chưa dọn, thất thoát Folio, lệch thuế phí `++`). | Chiếu Slide 1 $\rightarrow$ 3 |
| **Slide 4–5** | `02:00 – 04:00` (2.0 phút) | Trình bày 7 quy tắc nghiệp vụ (`BR1–BR7`), bảng MoSCoW (`18 FR`, `9 NFR`), Biểu đồ Use Case tổng quát (3 Actor, 7 UC) và cơ chế kiểm tra tồn kho Hạng phòng (*Quota*) + Cọc 30%. | Chiếu Slide 4 $\rightarrow$ 5 |
| **Slide 6–8** | `04:00 – 06:00` (2.0 phút) | Trình bày Mô hình `ERD` & `Class Diagram` 8 bảng, vòng đời buồng phòng `AVAILABLE` $\rightarrow$ `OCCUPIED` $\rightarrow$ `DIRTY`, kiến trúc 4 Phân hệ Giao diện (`ACE Color System`) và kiến trúc Spring Boot 3 lớp. | Chiếu Slide 6 $\rightarrow$ 8 |
| **Slide 9–10** | `06:00 – 12:30` (**6.5 phút**) | **DEMO TRỰC TIẾP TRÊN TRÌNH DUYỆT QUA 6 BƯỚC KHÉP KÍN:** Đi trọn vòng đời từ Khách hàng đặt Online cọc 30% $\rightarrow$ Lễ tân Check-in gán phòng sạch & tiếp nhận Walk-in 0đ cọc $\rightarrow$ Ghi nợ Room Folio $\rightarrow$ Check-out quyết toán `++` (Online trả 70% còn lại + DV; Walk-in trả 100%) $\rightarrow$ Phòng sang `DIRTY` & xác nhận dọn sạch $\rightarrow$ Admin xem báo cáo `Occupancy Rate` & Doanh thu. | **Khoa thao tác trực tiếp trên trình duyệt Chrome/Edge** |
| **Slide 11–12** | `12:30 – 14:00` (1.5 phút) | Đối chiếu kết quả đạt được với 4 Trọng tâm phản biện, Ma trận truy vết RTM, hạn chế, hướng phát triển và mời hội đồng đặt câu hỏi. | Chiếu Slide 11 $\rightarrow$ 12 |

---

### 2.2. Kịch bản Thao tác Demo Trực tiếp 6 Bước (Dành cho Khoa bấm máy & Phong dẫn lời)

#### Bước 1: Mở Landing Page Storytelling (`WF-01`) & Đăng nhập Cổng Khách hàng (`UC01`) – 45 giây
1. Mở `http://localhost:8080/` (`WF-01`): Cuộn nhanh qua các section câu chuyện thương hiệu, bảng giá ngày thường/cuối tuần của 5 hạng phòng và bấm nút đổi **Dark Mode / Light Mode** trên Header.
2. Nhấn **[Đăng nhập]** (`WF-02`): Thử nhập sai mật khẩu để hiện thông báo `MSG-01`, sau đó đăng nhập tài khoản khách hàng **`khach` / `khach`** (hoặc tạo nhanh 1 tài khoản khách mới ở tab Đăng ký).
3. Hệ thống tự động nhận diện `role = CUSTOMER` và chuyển thẳng vào **Dashboard Khách hàng (`/customer/dashboard` – `WF-03`)**.

#### Bước 2: Demo `UC03` (⭐ Trọng tâm 1) – Tìm Hạng phòng theo Quota, Cọc 30% & Nhận Phiếu QR – 1 phút 15 giây
1. Từ Dashboard Khách hàng, chọn tìm phòng từ `Hôm nay` đến `2 ngày tới`, số khách `2` $\rightarrow$ Nhấn **[Tìm hạng phòng trống]** (`WF-04`).
2. Chọn hạng phòng **Deluxe Room** $\rightarrow$ Nhấn **[Tiếp tục đặt phòng]**:
   - **Pop-up Modal Xác nhận** hiện ra: Tiền phòng gốc `2.000.000đ` + `SVC 5% (100.000đ)` + `VAT 10% (210.000đ)` = Tổng gói phòng `2.310.000đ` $\rightarrow$ **Cần thanh toán cọc 30% ngay: `693.000đ`** (Còn lại 70% = `1.617.000đ` trả tại quầy).
3. Nhấn **[Xác nhận & Thanh toán cọc 30%]**:
   - Hệ thống tạo đơn trạng thái `RESERVED` (`room_id = NULL`) và trả về **Phiếu xác nhận đặt phòng kèm Mã QR** ngay trên `/customer/dashboard`.

#### Bước 3: Demo `UC04` (⭐ Trọng tâm 2) – Lễ tân Quét QR Gán phòng sạch & Tiếp nhận Khách Walk-in (0đ cọc) – 1 phút 15 giây
1. Đăng xuất khách hàng, đăng nhập tài khoản Lễ tân **`nhanvien` / `nhanvien`** $\rightarrow$ Hệ thống tự động đưa vào **Dashboard Lễ tân (`/staff/dashboard` – `WF-05`)**.
2. Chỉ ra **Sơ đồ lưới 12 phòng với 4 màu trạng thái** (`AVAILABLE` Xanh lá, `OCCUPIED` Xanh dương, `DIRTY` Cam, `MAINTENANCE` Xám).
3. **Nhánh A – Check-in khách đặt trước (`BK-2402`):**
   - Nhập/quét mã `BK-2402` vào ô tra cứu Check-in (`WF-06`) $\rightarrow$ Hệ thống chỉ lọc ra các phòng Deluxe đang **`AVAILABLE` (Trống sạch)** là `P.201` và `P.302` (phòng bẩn/đang ở bị khóa).
   - Chọn gán **`P.201`**, bấm **[Xác nhận Check-in]** $\rightarrow$ Phòng `P.201` lập tức chuyển sang màu xanh dương **`OCCUPIED`**.
4. **Nhánh B – Tiếp nhận nhanh Khách vãng lai (`Walk-in` – 0đ cọc):**
   - Bấm vào phòng **`P.101` (Standard – Trống sạch)**, chọn tab **Khách Walk-in**, nhập tên khách + SĐT + CCCD $\rightarrow$ Bấm **[Hoàn tất Check-in Walk-in]** $\rightarrow$ Nhận phòng ngay với **`deposit_amount = 0đ`** (sẽ thu đủ 100% khi trả phòng).

#### Bước 4: Demo `UC06` – Ghi nhận Dịch vụ Lưu trú vào Room Folio (Đóng băng đơn giá) – 45 giây
1. Trên sơ đồ phòng `WF-05`, bấm vào **Phòng `P.301` (`BK-2401` – Khách Online Nguyễn Minh Anh)** đang `OCCUPIED` $\rightarrow$ Chọn **[Room Folio]** (`WF-07`).
2. Hiện tại phòng đang có sẵn `300.000đ` dịch vụ (Ăn sáng, Giặt ủi, Minibar).
3. Chọn nhóm `MINIBAR`, thêm `1 Combo Snack (50.000đ)` $\rightarrow$ Tổng Folio nhảy lên `350.000đ`; sau đó thử bấm **[Xóa]** dòng vừa thêm nhầm để tổng Folio về lại đúng **`300.000đ`**.

#### Bước 5: Demo `UC05` (⭐ Trọng tâm 3) – Check-out Quyết toán `++`, Trừ cọc & Vòng đời Dọn phòng `DIRTY` – 1 phút 30 giây
1. Từ Room Folio của **Phòng `P.301` (`BK-2401`)**, nhấn chuyển sang **[Check-out & Quyết toán]** (`WF-08`):
   - Hệ thống tự động tính: Tiền phòng `2.000.000đ` + Dịch vụ Room Folio `300.000đ` = **Subtotal `2.300.000đ`**.
   - Cộng **Phí phục vụ `SVC 5% (115.000đ)`** + **Thuế `VAT 10% (241.500đ)`** = **Grand Total `2.656.500đ`**.
   - Khấu trừ **Tiền cọc 30% đã đóng (`- 693.000đ`)** $\rightarrow$ **Số tiền thực thu tại quầy (`Balance Due`) = `1.963.500đ`** *(đúng bằng 70% tiền phòng còn lại + 100% dịch vụ `++`)*.
   *(Giải thích nhanh: Ngược lại nếu mở Check-out của phòng Walk-in `P.202`, dòng khấu trừ cọc là `0đ` nên khách thanh toán đủ 100% Grand Total).*
2. Chọn phương thức `TRANSFER` (VietQR) $\rightarrow$ Nhấn **[Xác nhận Hoàn tất & Chuyển Phòng DIRTY]** $\rightarrow$ Mở Hóa đơn VAT và bấm **[In Hóa đơn]** (`window.print()`).
3. Quay về **Sơ đồ phòng (`WF-05`)**:
   - Thẻ **Phòng `P.301`** (và `P.102`) đang hiển thị màu cam **`🟠 DIRTY (Chờ dọn dẹp)`** và bị khóa không cho gán khách mới.
   - Khi buồng phòng báo đã dọn xong, Lễ tân bấm nút **[✓ Xác nhận đã dọn sạch]** ngay trên thẻ phòng $\rightarrow$ Thẻ phòng lập tức đổi về màu xanh lá **`🟢 AVAILABLE (Trống sạch)`**!

#### Bước 6: Demo `UC02` & `UC07` (⭐ Trọng tâm 4) – Dashboard Admin, Công suất `Occupancy Rate` & Bảo toàn Lịch sử – 1 phút
1. Đăng xuất Lễ tân, đăng nhập tài khoản Quản trị viên **`admin` / `admin`** $\rightarrow$ Hệ thống tự động đưa vào **Dashboard Admin (`/admin/dashboard` – `WF-10`)**.
2. Chỉ ra 3 thẻ chỉ số chuẩn quốc tế: **Tỷ lệ lấp đầy (`Occupancy Rate %`)** (đã tự động loại trừ phòng `103` đang `MAINTENANCE` khỏi mẫu số), **ADR** và **RevPAR**, cùng bảng **Cơ cấu Doanh thu Đa chiều (`++`)** vừa cộng thêm hóa đơn của phòng `301`.
3. Chuyển sang màn hình **Quản trị Danh mục (`/admin/catalog` – `WF-09`)**:
   - Chỉ ra cấu hình **Giá ngày thường (T2–T5)** và **Giá cuối tuần (T6–CN)**.
   - Thử bấm **[Xóa]** trên Phòng `301` (đã có lịch sử booking) $\rightarrow$ Hệ thống chặn xóa ngay lập tức (`MSG-15`), chứng minh tính toàn vẹn dữ liệu `BR7`.