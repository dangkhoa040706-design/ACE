# 13. ĐẶC TẢ BỘ DỮ LIỆU MẪU (`sample-data.sql`) & KỊCH BẢN DEMO TRỰC TIẾP (12–15 PHÚT)

> **Mục đích:** Giải quyết dứt điểm **Mục #11** trong [08-bao-cao-tai-lieu-con-thieu.md](file:///D:/ACE/docs/08-bao-cao-tai-lieu-con-thieu.md) và yêu cầu **Mục 3.2 & Mục 4** của đề thi [01 FinalExam - 502050 - YeuCau.pdf](file:///D:/ACE/docs/assets/01%20FinalExam%20-%20502050%20-%20YeuCau.pdf).  
> Toàn bộ tên khách hàng, số phòng, đơn giá và mã đặt phòng (`BK-2401`..`BK-2404`) dưới đây được **đồng bộ 100%** với ảnh thiết kế [ACE_Light_Mode.png](file:///D:/ACE/docs/assets/ACE_Light_Mode.png) và [ACE_Dark_Mode.png](file:///D:/ACE/docs/assets/ACE_Dark_Mode.png).

---

## 1. Đặc tả Chi tiết Bộ Dữ liệu Mẫu (`sample-data.sql`)

### 1.1. Bảng `users` (2 Tài khoản Demo chuẩn theo `D14`)
Mật khẩu gốc của cả 2 tài khoản đều là `123456` (được lưu dạng chuỗi băm `BCrypt` trong `sample-data.sql`):
| `user_id` | `username` | Mật khẩu gốc | `full_name` | `role` |
|:---:|---|---|---|:---:|
| `1` | `quanly01` | `123456` | Trần Hoàng Quản Lý | `MANAGER` |
| `2` | `letan01` | `123456` | Nguyễn Thảo Lễ Tân | `RECEPTIONIST` |

### 1.2. Bảng `room_types` (5 Loại phòng chuẩn)
| `room_type_id` | `name` | `price_per_night` (VNĐ) | `capacity` (Khách) | `description` |
|:---:|---|---:|:---:|---|
| `1` | **Standard** | `780000` | `2` | Phòng tiêu chuẩn giường đôi, đầy đủ tiện nghi cơ bản |
| `2` | **Superior** | `950000` | `2` | Phòng diện tích rộng, cửa sổ thoáng nhìn ra phố |
| `3` | **Deluxe** | `1250000` | `2` | Phòng cao cấp ban công riêng, nội thất sang trọng |
| `4` | **Family** | `1850000` | `4` | Phòng gia đình 2 giường lớn, không gian sinh hoạt chung |
| `5` | **Suite** | `3200000` | `4` | Phòng hạng sang có phòng khách riêng biệt, bồn tắm nằm |

### 1.3. Bảng `rooms` (12 Phòng trải đều 4 tầng)
| `room_id` | `room_number` | `floor` | `room_type_id` | Loại phòng | `status` ban đầu | Ghi chú kịch bản Demo |
|:---:|:---:|:---:|:---:|---|:---:|---|
| `1` | `101` | `1` | `1` | Standard (780.000đ) | `AVAILABLE` | Trống – dùng để Demo Đặt phòng mới (`UC03`) |
| `2` | `102` | `1` | `1` | Standard (780.000đ) | `AVAILABLE` | Có đơn `BK-2404` (Phạm Gia Huy) đã đặt |
| `3` | `103` | `1` | `2` | Superior (950.000đ) | `MAINTENANCE` | Đang bảo trì – minh họa không hiện khi tìm phòng trống |
| `4` | `201` | `2` | `2` | Superior (950.000đ) | `AVAILABLE` | Trống – sẵn sàng cho khách đặt |
| `5` | `202` | `2` | `2` | Superior (950.000đ) | `OCCUPIED` | Đang có khách ở (`BK-2405`) |
| `6` | `205` | `2` | `1` | Standard (780.000đ) | `AVAILABLE` | **Khớp ảnh UI:** Có đơn `BK-2402` (Trần Quốc Bảo) chờ **Check-in hôm nay** |
| `7` | `301` | `3` | `3` | Deluxe (1.250.000đ) | `OCCUPIED` | **Khớp ảnh UI & Wireframe S12:** Đơn `BK-2401` (Nguyễn Minh Anh) đang ở, dùng để **Demo Thêm dịch vụ & Check-out** |
| `8` | `302` | `3` | `3` | Deluxe (1.250.000đ) | `AVAILABLE` | **Khớp ảnh UI:** Phòng 302/301 trống để minh họa lọc phòng |
| `9` | `305` | `3` | `4` | Family (1.850.000đ) | `AVAILABLE` | Trống – dùng để Demo Quản lý chuyển Bảo trì (`UC02`) |
| `10` | `401` | `4` | `4` | Family (1.850.000đ) | `AVAILABLE` | Đã có lịch sử trả phòng (`BK-2406`) – minh họa chặn xóa phòng (`D10`) |
| `11` | `418` | `4` | `5` | Suite (3.200.000đ) | `OCCUPIED` | **Khớp ảnh UI:** Đơn `BK-2403` (Lê Thu Hà) đang ở trạng thái **Quá hạn** (`D1`) |
| `12` | `420` | `4` | `5` | Suite (3.200.000đ) | `AVAILABLE` | Phòng hạng Suite trống |

### 1.4. Bảng `services` (6 Dịch vụ đi kèm chuẩn)
| `service_id` | `name` | `unit_price` (VNĐ) | `unit` | Ghi chú |
|:---:|---|---:|:---:|---|
| `1` | **Ăn sáng Buffet** | `150000` | `Suất` | Khớp Wireframe S12 (`Ăn sáng ×2 · 150.000đ`) |
| `2` | **Giặt ủi nhanh** | `80000` | `Lần` | Khớp Wireframe S12 (`Giặt ủi ×1 · 80.000đ`) |
| `3` | **Đưa đón sân bay** | `350000` | `Chuyến` | Khớp tag dịch vụ trên ảnh UI ACE |
| `4` | **Spa thư giãn** | `450000` | `Liệu trình` | Khớp tag dịch vụ phòng 418 trên ảnh UI ACE |
| `5` | **Combo Minibar & Nước suối** | `60000` | `Combo` | Đang có sẵn đơn gọi dịch vụ |
| `6` | **Thuê xe máy theo ngày** | `180000` | `Ngày` | **Chưa có đơn gọi nào** – dùng để Demo Quản lý xóa dịch vụ thành công (`D10`) |

### 1.5. Bảng `customers` (8 Khách hàng mẫu)
| `customer_id` | `full_name` | `phone` | `id_number` (CCCD) | `email` |
|:---:|---|---|---|---|
| `1` | **Nguyễn Minh Anh** | `0901234567` | `079201001234` | `minhanh.nguyen@gmail.com` |
| `2` | **Trần Quốc Bảo** | `0912345678` | `079200005678` | `quocbao.tran@gmail.com` |
| `3` | **Lê Thu Hà** | `0987654321` | `001199004321` | `thuha.le@gmail.com` |
| `4` | **Phạm Gia Huy** | `0933445566` | `079202008899` | `giahuy.pham@gmail.com` |
| `5` | **Võ Hoàng Yến** | `0977889900` | `048198001122` | `hoangyen.vo@gmail.com` |
| `6` | **Đặng Minh Khôi** | `0944556677` | `079195003344` | `minhkhoi.dang@gmail.com` |
| `7` | **Bùi Thanh Trúc** | `0966112233` | `082203007788` | `thanhtruc.bui@gmail.com` |
| `8` | **Hoàng Đức Thịnh** | `0909887766` | `079197009900` | `ducthinh.hoang@gmail.com` |

### 1.6. Bảng `bookings`, `service_orders` & `invoices` (Khớp tuyệt đối với Kịch bản Demo)
*(Sử dụng ngày tương đối `CURRENT_DATE` trong SQL để dù chạy Demo vào bất kỳ ngày nào thì các đơn Check-in/Check-out hôm nay và Quá hạn vẫn luôn đúng với ngày chấm bài!)*

| `booking_id` (Mã hiển thị) | Khách hàng | Phòng | `check_in_date` | `check_out_date` | `status` | Dịch vụ đã gọi (`service_orders`) | Mục đích trong buổi Demo |
|:---:|---|---|---|---|:---:|---|---|
| `2401` (`BK-2401`) | Nguyễn Minh Anh | **301** (Deluxe · 1.250k) | `TODAY - 2` | `TODAY` (2 đêm) | `CHECKED_IN` | `Ăn sáng ×2` (300k)<br>`Giặt ủi ×1` (80k) | **Demo UC06 (Thêm dịch vụ)** & **Demo UC05 (Check-out 2 đêm = 2.500k + DV = 2.880.000đ)** |
| `2402` (`BK-2402`) | Trần Quốc Bảo | **205** (Standard · 780k) | `TODAY` | `TODAY + 1` (1 đêm) | `BOOKED` | *(Chưa có)* | **Demo UC04 (Check-in hôm nay):** Bấm 1 nút chuyển sang `CHECKED_IN`, phòng 205 sang `OCCUPIED` |
| `2403` (`BK-2403`) | Lê Thu Hà | **418** (Suite · 3.200k) | `TODAY - 3` | `TODAY - 1` (2 đêm) | `CHECKED_IN` | `Spa ×1` (450k)<br>`Giặt ủi ×1` (80k) | Minh họa trạng thái đỏ **"Quá hạn"** (`D1`) trên Dashboard & Danh sách đặt phòng |
| `2404` (`BK-2404`) | Phạm Gia Huy | **102** (Standard · 780k) | `TODAY + 1` | `TODAY + 3` (2 đêm) | `BOOKED` | *(Chưa có)* | **Demo UC03 (Hủy đặt phòng `BR7`)** và kiểm tra **Chưa đến ngày nhận (`MSG-23`)** |
| `2405` (`BK-2405`) | Võ Hoàng Yến | **202** (Superior · 950k) | `TODAY - 1` | `TODAY + 2` (3 đêm) | `CHECKED_IN` | `Minibar ×2` (120k) | Phòng đang ở trong hạn bình thường |
| `2406` (`BK-2406`) | Đặng Minh Khôi | **401** (Family · 1.850k) | `TODAY - 5` | `TODAY - 3` (2 đêm) | `CHECKED_OUT` | `Đưa đón ×1` (350k) | Đã có Hóa đơn `INV-1` (`total = 4.050.000đ`, `TRANSFER`) – minh họa lịch sử & xem lại hóa đơn |
| `2407` (`BK-2407`) | Bùi Thanh Trúc | **302** (Deluxe · 1.250k) | `TODAY - 10` | `TODAY - 8` (2 đêm) | `CANCELLED` | *(Không có)* | Minh họa đơn đã hủy không chặn lịch đặt phòng mới (`BR2`) |

---

## 2. Kịch bản Thuyết trình & Demo Trực tiếp (12–15 Phút)

> **Quy định bắt buộc của Đề thi (Mục 4):**  
> *"Thời lượng: 12–15 phút trình bày & demo; 5–8 phút trả lời câu hỏi. Tất cả thành viên nhóm đều phải tham gia trình bày (mỗi thành viên phụ trách ít nhất một phần nội dung) và có mặt để trả lời câu hỏi."*

### 2.1. Phân bổ Thời lượng 14 Phút cho 4 Thành viên
| Thứ tự | Thời lượng | Người trình bày | Nội dung trình bày / thao tác |
|:---:|:---:|:---:|---|
| **Phần 1** | `00:00 – 02:30` (2.5 phút) | **Phong** | Mở đầu, giới thiệu đề tài ACE, phạm vi hệ thống (Làm gì / Không làm gì), khảo sát hiện trạng và 5 bài toán thực tế tại quầy lễ tân. |
| **Phần 2** | `02:30 – 05:00` (2.5 phút) | **Hoàng** | Trình bày yêu cầu chức năng/phi chức năng (MoSCoW), 7 quy tắc nghiệp vụ (`BR1–BR7`), Biểu đồ Use Case tổng quát và quy tắc chống trùng lịch (`UC03`). |
| **Phần 3** | `05:00 – 07:30` (2.5 phút) | **Kiệt** | Trình bày thiết kế giao diện (Design System ACE Light/Dark Mode), Sitemap điều hướng, Ma trận phân quyền RBAC và quy trình dịch vụ đi kèm (`UC06`). |
| **Phần 4** | `07:30 – 14:00` (6.5 phút) | **Khoa** *(vừa thao tác máy vừa thuyết minh kỹ thuật)* + **Phong** *(dẫn luồng nghiệp vụ)* | Trình bày ngắn gọn Kiến trúc Spring Boot 3 lớp + ERD 8 bảng (1 phút), sau đó **Demo trực tiếp 5 chức năng chạy thật trên trình duyệt** (5.5 phút). |

---

### 2.2. Kịch bản Thao tác Demo Chi tiết Từng Bước (Dành cho Khoa & Phong)

#### Bước 0: Mở trang Giới thiệu `/` (`S01`) – 20 giây
* **Thao tác:** Mở `http://localhost:8080/`, bấm nút chuyển đổi **Light Mode / Dark Mode** trên thanh Header để hội đồng thấy bảng màu ACE hoạt động mượt mà, sau đó nhấn **[Đăng nhập hệ thống]**.

#### Bước 1: Demo `UC01` – Đăng nhập & Phân quyền (`FR-01`, `FR-02`) – 1 phút
1. **Thử đăng nhập sai:** Nhập `letan01` / mật khẩu `sai123` $\rightarrow$ Nhấn **[Đăng nhập]** $\rightarrow$ Hệ thống hiện thông báo đỏ: *"Sai tên đăng nhập hoặc mật khẩu."* (`MSG-01`).
2. **Đăng nhập vai trò Lễ tân:** Nhập `letan01` / `123456` $\rightarrow$ Vào **Tổng quan (`/dashboard` – `S03`)**.
   * Chỉ ra 4 thẻ chỉ số thực tế (`D4`): Phòng trống, Check-in hôm nay, Check-out hôm nay, Dịch vụ đi kèm.
   * Chỉ ra thanh Menu của Lễ tân **không có** các mục Quản lý loại phòng hay Danh mục dịch vụ.
3. **Kiểm chứng chặn quyền phía Server (`NFR-03`):** Gõ trực tiếp lên thanh địa chỉ `http://localhost:8080/manage/room-types` $\rightarrow$ Hệ thống chặn ngay với thông báo `403 / MSG-04`.

#### Bước 2: Demo `UC03` – Tìm phòng trống, Chống trùng lịch & Đặt phòng (`FR-06, 07, 08, 09`) – 1.5 phút
1. Vào menu **Đặt phòng mới (`/bookings/new` – `S07`)**.
2. **Kiểm tra lỗi ngày (`BR3`):** Chọn Ngày trả trùng hoặc trước Ngày nhận $\rightarrow$ Hệ thống báo lỗi `MSG-17`.
3. **Tìm phòng trống hợp lệ (`BR2`, `D18`):** Chọn Ngày nhận = `Hôm nay`, Ngày trả = `2 ngày tới`, Loại phòng = `Standard` $\rightarrow$ Nhấn **[Tìm phòng trống]**.
   * Giải thích: Phòng `103` đang `MAINTENANCE` và Phòng `205` đã có đơn `BK-2402` trùng ngày nên tự động bị loại khỏi danh sách; hệ thống hiển thị Phòng `101` đang trống.
4. Nhấn **[Đặt phòng]** tại Phòng `101` $\rightarrow$ Sang màn hình `S08`:
   * Tìm khách hàng gõ `"Trúc"` $\rightarrow$ Chọn khách **Bùi Thanh Trúc** (hoặc nhập khách mới) $\rightarrow$ Nhấn **[Xác nhận đặt phòng]**.
   * Hệ thống tạo đơn mới trạng thái `Đã đặt (BOOKED)` và chuyển về `/bookings` (`S09`).

#### Bước 3: Demo `UC04` – Check-in Nhận phòng (`FR-10`) – 45 giây
1. Tại bảng **Check-in / Check-out hôm nay** (`S03` hoặc `S09`), tìm đơn **`BK-2402` – Khách: Trần Quốc Bảo – Phòng 205 (Standard)** đang ở trạng thái `Đã đặt`.
2. Nhấn nút **[Check-in]** $\rightarrow$ Xác nhận tại màn hình `S10`.
3. Kết quả tức thì:
   * Đơn `BK-2402` đổi sang trạng thái **`Đang ở` (`CHECKED_IN`)**, ghi nhận giờ `actual_check_in` thực tế.
   * Mở tab **Phòng (`/rooms` – `S04`)**: Thẻ **Phòng 205** đã tự động chuyển sang badge xanh dương **`Đang ở` (`OCCUPIED`)**.

#### Bước 4: Demo `UC06` & `UC05` – Thêm Dịch vụ Đi kèm, Tính tiền Check-out & Xuất Hóa đơn (`FR-11, 13, 14, 15`) – 1.5 phút
1. Quay lại danh sách đặt phòng, chọn đơn **`BK-2401` – Khách: Nguyễn Minh Anh – Phòng 301 (Deluxe, 1.250.000đ/đêm, ở 2 đêm)**.
2. Nhấn nút **[Dịch vụ]** (`S11` – `/bookings/2401/services`):
   * Hiện đang có: `Ăn sáng Buffet ×2 (300.000đ)` và `Giặt ủi nhanh ×1 (80.000đ)` $\rightarrow$ Tổng dịch vụ đang là `380.000đ` (đúng như wireframe `S12`).
   * Chọn thêm dịch vụ **`Combo Minibar & Nước suối` (60.000đ)**, số lượng `1` $\rightarrow$ Nhấn **[+ Thêm vào phòng]** $\rightarrow$ Dòng dịch vụ mới hiện ngay lập tức (`440.000đ`), sau đó thử bấm **[Xóa]** dòng vừa thêm (`D21`) để tổng về lại đúng **`2.880.000đ`**.
3. Nhấn chuyển sang **[Check-out & Thanh toán]** (`S12` – `/bookings/2401/check-out`):
   * Hệ thống tự động hiển thị: Tiền phòng `2 đêm × 1.250.000đ = 2.500.000đ` + Tiền dịch vụ `380.000đ` = **TỔNG CỘNG `2.880.000đ`**.
   * Chọn hình thức thanh toán: **`Chuyển khoản (TRANSFER)`** $\rightarrow$ Nhấn **[Xác nhận thanh toán & Trả phòng]**.
4. Hệ thống chuyển sang màn hình **Hóa đơn (`S13`)**:
   * Nhấn **[In hóa đơn]** (`window.print()`) để minh họa bản in hóa đơn sạch đẹp.
   * Kiểm tra lại Phòng `301`: Đã tự động quay về trạng thái **`Phòng trống` (`AVAILABLE`)**.

#### Bước 5: Demo `UC02` – Quyền Quản lý Phòng & Bảo toàn Dữ liệu (`FR-03, 04, 05`, `D10`) – 45 giây
1. Đăng xuất `letan01`, đăng nhập tài khoản Quản lý: **`quanly01` / `123456`**.
2. Vào **Phòng (`/rooms` – `S04`)**: Các nút **[+ Thêm phòng]**, **[Quản lý loại phòng]**, **[Sửa]**, **[Bảo trì]**, **[Xóa]** xuất hiện đầy đủ.
3. Thử nhấn **[Xóa]** trên Phòng `301` (phòng đã có lịch sử booking): Hệ thống chặn ngay và hiện thông báo `MSG-13` (*"Không thể xóa phòng đã có lịch sử đặt phòng..."*), chứng minh tính toàn vẹn dữ liệu `NFR-04` & `D10`.
4. Thêm mới nhanh 1 phòng (ví dụ Phòng `306`) và xóa thử phòng `306` vừa tạo (chưa có booking) $\rightarrow$ Xóa thành công!
