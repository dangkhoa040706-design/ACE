# HƯỚNG DẪN MỤC LỤC BÁO CÁO (CHƯƠNG 1–8) & CẨM NANG LÀM 7 USE CASE — DỰ ÁN ACE HOTEL

> **Học phần:** Phân tích và Thiết kế Yêu cầu (`502050`) — ĐH Tôn Đức Thắng[cite: 38]  
> **Đề tài:** Hệ thống Quản lý Khách sạn & Đặt phòng Trực tuyến **ACE Hotel** (*Accommodation, Check-in & Extras*)[cite: 20, 53]  
> **Mục đích tài liệu:** Giúp cả nhóm (Khoa, Hoàng, Phong, Kiệt) nắm rõ **cấu trúc quyển báo cáo mới (8 Chương, không Phụ lục)** và **hướng dẫn chi tiết từng Use Case (`UC01` $\rightarrow$ `UC07`)** để vẽ sơ đồ UML và viết đặc tả nhanh, thống nhất 100% không bị lệch tên.

---

## PHẦN 1. PHÂN VAI 4 TRỤ CỘT CHÍNH & QUY ƯỚC CHUNG

### 1.1. Bốn trụ cột phụ trách chính của nhóm
| Thành viên | Trụ cột đầu ra cuối cùng | Phần viết trong Báo cáo Word (`bao-cao.docx`) | Số sơ đồ UML phụ trách |
|---|---|---|:---:|
| **KHOA** | **Code full** ứng dụng Web Java (4 phân hệ) & **Thao tác thiết bị Demo**[cite: 33, 35] | • **Mục 4.2.7:** Đặc tả `UC07`<br>• **Mục 4.3 & 4.4:** `Class Diagram` & `ERD`<br>• **Chương 5 (`5.1`–`5.3`):** Thiết kế CSDL & Từ điển dữ liệu 8 bảng[cite: 24, 35] | **5 sơ đồ**<br>(`erd`, `class-diagram`, `uc-07`, `act-uc07`, `seq-uc07`) |
| **HOÀNG** | **Chủ trì Báo cáo Word:** Viết phần lớn các chương, **gộp toàn bộ bài** và xuất `bao-cao.docx` / `bao-cao.pdf` để nộp | • **Phần đầu báo cáo:** Khung Word, Bìa, Mục lục, Danh mục hình/bảng/từ viết tắt[cite: 38, 55]<br>• **Chương 3 (`3.1`–`3.4`):** Quy tắc BR1–BR7, 18 FR, 9 NFR, MoSCoW[cite: 32, 35]<br>• **Mục 4.1, 4.2.2, 4.2.3:** UC tổng quát, Đặc tả `UC02` & `UC03`[cite: 35]<br>• **Chương 7 (`7.1`–`7.4`):** Kết quả cài đặt, ảnh chụp app & Ma trận RTM[cite: 22, 35]<br>• **Chương 8 (`8.1`–`8.3`):** Kết luận, hạn chế, hướng phát triển[cite: 40, 57]<br>• **Danh mục Tài liệu tham khảo**[cite: 41] | **7 sơ đồ**<br>(`usecase-tong-quat`, bộ 3 sơ đồ `UC02`, bộ 3 sơ đồ `UC03`) |
| **PHONG** | **Thuyết trình full 12 slide (14 phút)** + Dẫn lời Demo + Kịch bản & 20 câu hỏi phản biện[cite: 33, 36] | • **Chương 1 (`1.1`–`1.3`):** Giới thiệu & Phạm vi đề tài[cite: 36, 39, 56]<br>• **Chương 2 (`2.1`–`2.4`):** Khảo sát & Mô tả hiện trạng[cite: 36, 39, 56]<br>• **Mục 4.2.4 & 4.2.5:** Đặc tả `UC04` & `UC05`[cite: 36]<br>*(Xong trong Tuần 1–2 để Tuần 3–4 tập trung 100% kịch bản & thuyết trình)* | **6 sơ đồ**<br>(Bộ 3 sơ đồ `UC04`, bộ 3 sơ đồ `UC05`) |
| **KIỆT** | **Làm full Slide tiếng Anh (12 slide)** + Vẽ 10 Wireframe & Sơ đồ điều hướng[cite: 36] | • **Mục 4.2.1 & 4.2.6:** Đặc tả `UC01` & `UC06`[cite: 36]<br>• **Chương 6 (`6.1`–`6.3`):** Thiết kế giao diện, RBAC & Mô tả 10 Wireframe[cite: 36, 39, 56]<br>*(Xong trong Tuần 1–2 để Tuần 3–4 tập trung 100% làm Slide tiếng Anh)* | **6 sơ đồ UML**<br>(Bộ 3 sơ đồ `UC01`, bộ 3 sơ đồ `UC06`) + *10 WF & 1 ĐH* |

---

### 1.2. Tóm tắt Luồng Nghiệp vụ Mới (Cả nhóm dùng thống nhất 100%)
1. **3 Actor:** `Khách hàng (CUSTOMER)`, `Lễ tân (RECEPTIONIST)`, `Quản trị viên (ADMIN)`[cite: 53].
2. **4 Không gian Giao diện:**
   - **(1) Landing Page Storytelling (`/`):** Giới thiệu khách sạn, hạng phòng, dịch vụ và thanh tìm kiếm đặt phòng[cite: 53].
   - **(2) Dashboard Khách hàng (`/customer/dashboard`):** Quản lý đơn đặt cá nhân, lưu **Phiếu xác nhận đặt phòng kèm mã QR**, hủy đơn `RESERVED`[cite: 53].
   - **(3) Dashboard Lễ tân (`/staff/dashboard`):** Sơ đồ phòng 4 trạng thái (`AVAILABLE`, `OCCUPIED`, `DIRTY`, `MAINTENANCE`), quét/nhập mã QR Check-in gán phòng sạch, tiếp nhận khách Walk-in, ghi nợ dịch vụ vào **Room Folio**, Check-out quyết toán `++` và nút **[Xác nhận đã dọn sạch]**[cite: 53].
   - **(4) Dashboard Admin (`/admin/dashboard`):** Cấu hình hạng phòng, giá ngày thường (T2–T5) / cuối tuần (T6–CN), quản lý phòng vật lý, danh mục 4 nhóm dịch vụ và xem báo cáo doanh thu đa chiều + **Occupancy Rate, ADR, RevPAR**[cite: 53].
3. **Quy tắc Đặt cọc & Quyết toán khi Check-out (Điểm cốt lõi cần nhớ):**
   - **Khách Đặt Online (`UC03`):** Đặt theo **Hạng phòng (`room_type_id`, chưa gán `room_id`)** dựa trên tồn kho hạng phòng (*Quota*), thanh toán **cọc trước 30%** (`deposit_amount > 0`) để lấy **Phiếu xác nhận QR (`RESERVED`)**[cite: 53]. Khi Check-out (`UC05`) sẽ thanh toán **70% tiền phòng còn lại + 100% dịch vụ phát sinh & phụ thu** (đã tính SVC 5% + VAT 10%)[cite: 53].
   - **Khách Vãng lai Walk-in (`UC04`):** Đến trực tiếp quầy được Lễ tân chọn phòng sạch (`AVAILABLE`) cho nhận phòng ngay, **không cần đóng cọc lúc vào (`deposit_amount = 0`)**. Khi Check-out (`UC05`) sẽ thanh toán **100% toàn bộ hóa đơn** (100% tiền phòng + dịch vụ + phụ thu + SVC 5% + VAT 10%).
   - **Công thức quyết toán chung (`UC05`):**
     $$\text{Subtotal} = \text{Tiền phòng} + \text{Tổng dịch vụ (Room Folio)} + \text{Phụ thu (Nhận sớm / Trả trễ)}$$
     $$\text{SVC (5\%)} = \text{ROUND}(\text{Subtotal} \times 5\%), \qquad \text{VAT (10\%)} = \text{ROUND}((\text{Subtotal} + \text{SVC}) \times 10\%)$$
     $$\text{Grand Total} = \text{Subtotal} + \text{SVC} + \text{VAT}$$
     $$\text{Thực thu tại quầy (`balance\_due`)} = \text{Grand Total} - \text{Tiền đã cọc (`deposit\_deducted`)} \quad[cite: 53]$$
   - **Sau khi Check-out:** Phòng vật lý tự động chuyển sang **`DIRTY` (Chờ dọn dẹp)** để khóa không cho gán nhầm khách mới; khi buồng phòng báo dọn xong, Lễ tân bấm **[Xác nhận đã dọn sạch]** để phòng về lại **`AVAILABLE`**[cite: 53].

---

## PHẦN 2. MỤC LỤC CHI TIẾT QUYỂN BÁO CÁO (CHƯƠNG 1 $\rightarrow$ CHƯƠNG 8, KHÔNG PHỤ LỤC)

> **Lưu ý định dạng (Mục 5 đề thi):** Font Times New Roman 13pt, giãn dòng 1.5 lines, lề Trên 3.5 cm – Dưới 3 cm – Trái 3.5 cm – Phải 2 cm, đánh số trang ở giữa phía trên đầu trang giấy[cite: 36, 41]. **Không sử dụng Phụ lục.**

* **PHẦN ĐẦU BÁO CÁO *(Hoàng phụ trách)***[cite: 38, 55]
  * Trang bìa & Trang phụ bìa[cite: 38, 55]
  * Mục lục[cite: 38, 55]
  * Danh mục hình ảnh *(24 sơ đồ UML + 10 Wireframe + 1 Sơ đồ điều hướng + Ảnh chụp màn hình Demo)*[cite: 35, 36, 38, 55]
  * Danh mục bảng biểu[cite: 38, 55]
  * Danh mục từ viết tắt và thuật ngữ nghiệp vụ[cite: 53]

* **CHƯƠNG 1. GIỚI THIỆU VÀ PHẠM VI ĐỀ TÀI *(Phong phụ trách — File: `chuong-1-2-phong.docx`)***[cite: 36, 39, 56]
  * **1.1.** Lý do chọn đề tài[cite: 39, 56]
  * **1.2.** Mục tiêu của hệ thống ACE Hotel (*Accommodation, Check-in & Extras*)[cite: 20, 39, 53, 56]
    * 1.2.1. Mục tiêu nghiệp vụ (Khép kín 2 phân hệ: Đặt phòng trực tuyến & Vận hành PMS tại quầy)[cite: 53]
    * 1.2.2. Mục tiêu kỹ thuật và tính nhất quán truy vết[cite: 20, 38]
  * **1.3.** Phạm vi hệ thống (System Scope)[cite: 23, 39, 56]
    * 1.3.1. Các phân hệ và chức năng hệ thống thực hiện[cite: 23, 39, 53, 56]
    * 1.3.2. Các nội dung nằm ngoài phạm vi thực hiện[cite: 23, 39, 56]

* **CHƯƠNG 2. KHẢO SÁT VÀ MÔ TẢ HIỆN TRẠNG *(Phong phụ trách — File: `chuong-1-2-phong.docx`)***[cite: 36, 39, 56]
  * **2.1.** Mô tả nghiệp vụ thực tế tại khách sạn vừa và nhỏ[cite: 27, 39, 56]
  * **2.2.** Các đối tượng liên quan (Stakeholders) và Tác nhân (Actors)[cite: 27, 38, 39, 56]
    * 2.2.1. Ba tác nhân trực tiếp: Khách hàng (`CUSTOMER`), Lễ tân (`RECEPTIONIST`), Quản trị viên (`ADMIN`)[cite: 53]
    * 2.2.2. Các bộ phận phối hợp vận hành: Buồng phòng (*Housekeeping*) và Kế toán[cite: 23, 27, 53]
  * **2.3.** Quy trình nghiệp vụ hiện tại[cite: 27, 39, 56]
  * **2.4.** Các vấn đề và hạn chế cần giải quyết *(Overbooking, thất thoát dịch vụ, gán nhầm phòng bẩn chưa dọn, sai lệch thuế phí `++` và độ trễ báo cáo)*[cite: 27, 39, 53, 56]

* **CHƯƠNG 3. XÁC ĐỊNH VÀ ĐẶC TẢ YÊU CẦU *(Hoàng phụ trách — File: `chuong-3-hoang.docx`)***[cite: 35, 39, 56]
  * **3.1.** Danh sách các quy tắc nghiệp vụ cốt lõi (`BR1` – `BR7`)[cite: 17, 30]
  * **3.2.** Danh sách yêu cầu chức năng (`FR-01` đến `FR-18`)[cite: 32, 39, 56]
  * **3.3.** Danh sách yêu cầu phi chức năng (`NFR-01` đến `NFR-09`)[cite: 32, 39, 56]
  * **3.4.** Bảng ưu tiên hóa yêu cầu theo phương pháp MoSCoW (`Must`, `Should`, `Could`, `Won't`)[cite: 32, 39, 56]

* **CHƯƠNG 4. MÔ HÌNH HÓA YÊU CẦU BẰNG UML *(Cả 4 thành viên — Tổng cộng 24 Sơ đồ UML)***[cite: 35, 36, 39, 56]
  * **4.1.** Biểu đồ Use Case tổng quát toàn hệ thống (`usecase-tong-quat.png`) *(Hoàng)*[cite: 35, 39, 56]
  * **4.2.** Đặc tả chi tiết và Bộ 3 sơ đồ của 7 Use Case chính (`UC01` – `UC07`):[cite: 39, 56]
    * **4.2.1.** `UC01` – Đăng ký, Đăng nhập và Điều hướng vai trò *(Kiệt: `uc-01`, Bảng đặc tả, `act-uc01`, `seq-uc01`)*[cite: 36, 53]
    * **4.2.2.** `UC02` – Quản lý Danh mục Phòng, Biểu giá và Dịch vụ *(Hoàng: `uc-02`, Bảng đặc tả, `act-uc02`, `seq-uc02`)*[cite: 35, 53]
    * **4.2.3.** `UC03` – Tìm kiếm và Đặt phòng trực tuyến theo Hạng phòng, Đặt cọc 30% & Mã QR *(Hoàng: `uc-03`, Bảng đặc tả, `act-uc03`, `seq-uc03`)*[cite: 35, 53]
    * **4.2.4.** `UC04` – Quản lý Check-in, Gán phòng vật lý sạch và Tiếp nhận Khách Walk-in *(Phong: `uc-04`, Bảng đặc tả, `act-uc04`, `seq-uc04`)*[cite: 36, 53]
    * **4.2.5.** `UC05` – Check-out, Quyết toán hóa đơn biểu giá `++` và Chuyển trạng thái dọn phòng *(Phong: `uc-05`, Bảng đặc tả, `act-uc05`, `seq-uc05`)*[cite: 36, 53]
    * **4.2.6.** `UC06` – Ghi nhận sử dụng dịch vụ lưu trú vào Room Folio *(Kiệt: `uc-06`, Bảng đặc tả, `act-uc06`, `seq-uc06`)*[cite: 36, 53]
    * **4.2.7.** `UC07` – Báo cáo thống kê doanh thu đa chiều và Công suất khai thác phòng *(Khoa: `uc-07`, Bảng đặc tả, `act-uc07`, `seq-uc07`)*[cite: 35, 53]
  * **4.3.** Biểu đồ Lớp phân tích và thiết kế (`class-diagram.png` – 8 lớp thực thể) *(Khoa)*[cite: 27, 39, 56]
  * **4.4.** Mô hình Thực thể – Kết hợp (`erd.png` – 8 bảng) và Sơ đồ vòng đời trạng thái phòng *(Khoa)*[cite: 24, 39, 53, 56]

* **CHƯƠNG 5. THIẾT KẾ CƠ SỞ DỮ LIỆU *(Khoa phụ trách — File: `chuong-5-csdl-khoa.docx`)***[cite: 24, 35, 39, 56]
  * **5.1.** Lược đồ quan hệ tổng thể (8 bảng, Khóa chính, Khóa ngoại và Bội số quan hệ)[cite: 24, 39, 56]
  * **5.2.** Đặc tả Từ điển dữ liệu chi tiết (`users`, `room_types`, `rooms`, `customers`, `bookings`, `services`, `service_orders`, `invoices`)[cite: 24, 39, 53, 56]
  * **5.3.** Các ràng buộc toàn vẹn (`CHECK`, `UNIQUE`, `FK`) và Truy vấn SQL trọng tâm[cite: 24, 39, 56]

* **CHƯƠNG 6. THIẾT KẾ GIAO DIỆN NGƯỜI DÙNG *(Kiệt phụ trách — File: `chuong-6-giao-dien-kiet.docx`)***[cite: 36, 39, 56]
  * **6.1.** Hệ thống thiết kế nhận diện thương hiệu (`ACE Color System` – Dark Mode & Light Mode)[cite: 11, 37, 46, 53]
  * **6.2.** Kiến trúc 4 phân hệ giao diện, Sơ đồ luồng điều hướng (`dieu-huong.png`) và Ma trận phân quyền RBAC[cite: 18, 30, 36, 39, 53, 56]
  * **6.3.** Thiết kế Wireframe chi tiết và Giải thích thao tác cho 10 màn hình chính (`WF-01` đến `WF-10`)[cite: 36, 39, 56]

* **CHƯƠNG 7. KẾ HOẠCH VÀ KẾT QUẢ XÂY DỰNG PROTOTYPE, CÀI ĐẶT JAVA & DEMO *(Hoàng phụ trách — File: `chuong-7-8-hoang.docx`)***[cite: 35, 39, 40, 56, 57]
  * **7.1.** Kiến trúc kỹ thuật ứng dụng Java Web (`Spring Boot 3.5` + `Thymeleaf` + `Spring Security` + `H2` trên `JDK 21`)[cite: 24, 40, 52, 57]
  * **7.2.** Bộ dữ liệu mẫu chuẩn (`sample-data.sql`) và Kịch bản chạy Demo khép kín[cite: 21, 40, 57]
  * **7.3.** Kết quả cài đặt các chức năng chính và Minh họa giao diện thực tế (Ảnh chụp ứng dụng chạy thật)[cite: 35, 39, 40, 56, 57]
  * **7.4.** Ma trận truy vết yêu cầu hai chiều (RTM) và Kết quả kiểm thử nghiệp vụ[cite: 22, 35, 38]

* **CHƯƠNG 8. KẾT LUẬN *(Hoàng phụ trách — File: `chuong-7-8-hoang.docx`)***[cite: 40, 57]
  * **8.1.** Tự đánh giá kết quả đạt được[cite: 40, 57]
  * **8.2.** Các hạn chế của hệ thống[cite: 40, 57]
  * **8.3.** Hướng phát triển và mở rộng trong tương lai[cite: 40, 57]

* **DANH MỤC TÀI LIỆU THAM KHẢO *(Hoàng phụ trách)***[cite: 36, 41]

---

## PHẦN 3. CẨM NANG HƯỚNG DẪN CHI TIẾT 7 USE CASE (`UC01` $\rightarrow$ `UC07`) CHO TỪNG BẠN

### Quy chuẩn bắt buộc cho mỗi Use Case trong Chương 4 (Mục 4.2.1 $\rightarrow$ 4.2.7):
Mỗi Use Case đều phải trình bày theo đúng thứ tự **4 thành phần** sau:
1. **Hình 1 — Sơ đồ Use Case chi tiết (`uc-0x.png`):** Vẽ Actor nối vào Use Case chính ở giữa, từ Use Case chính vẽ mũi tên nét đứt `<<include>>` sang các bước con bắt buộc và `<<extend>>` từ các nhánh mở rộng/tùy chọn trỏ vào Use Case chính[cite: 27, 35].
2. **Bảng — Đặc tả chi tiết Use Case (Mẫu chuẩn 9 dòng):**
   - (1) Mã & Tên Use Case | (2) Tác nhân (Actor) | (3) Mô tả ngắn | (4) Tiền điều kiện (Pre-conditions) | (5) Hậu điều kiện (Post-conditions) | (6) Yêu cầu & Bảng CSDL liên quan | (7) Luồng sự kiện chính (Main Flow) | (8) Luồng sự kiện phụ (Alternative Flows) | (9) Luồng ngoại lệ (Exception Flows)[cite: 27, 35, 36, 39].
3. **Hình 2 — Biểu đồ Hoạt động (`act-uc0x.png`):** Có 2 làn bơi (*Swimlanes*: **Tác nhân** bên trái, **Hệ thống ACE** bên phải), thể hiện rõ các hình thoi kiểm tra điều kiện (Đúng/Sai)[cite: 28, 36].
4. **Hình 3 — Biểu đồ Tuần tự (`seq-uc0x.png`):** Thống nhất các cột Lifeline từ trái sang phải: `Actor` $\rightarrow$ `Boundary/UI (Thymeleaf)` $\rightarrow$ `Controller` $\rightarrow$ `Service` $\rightarrow$ `Repository` $\rightarrow$ `H2 Database` (có khung `alt/else` cho nhánh thành công và ngoại lệ)[cite: 28, 29, 35, 36].

---

### 1. Hướng dẫn làm `UC01`: Đăng ký, Đăng nhập và Điều hướng vai trò
* **Người phụ trách:** **KIỆT** (Mục `4.2.1` — File `chuong-4-uc01-uc06-kiet.docx`)[cite: 36].
* **Actor:** `Khách hàng (CUSTOMER)`, `Lễ tân (RECEPTIONIST)`, `Quản trị viên (ADMIN)`[cite: 53].
* **Bảng CSDL liên quan:** `users`, `customers`[cite: 53].
* **Màn hình Wireframe:** `WF-02` (`/login`, `/register`), điều hướng tới `WF-03`, `WF-05`, `WF-10`[cite: 53].
* **Cách vẽ 3 sơ đồ của `UC01`:**
  1. **Sơ đồ UC chi tiết (`uc-01.png`):**
     - Bubble chính: `UC01: Đăng ký, Đăng nhập và Điều hướng vai trò`.
     - `<<include>>`: `Xác thực mật khẩu BCrypt`, `Tự động điều hướng Dashboard theo Role`.
     - `<<extend>>`: `Đăng ký tài khoản khách hàng mới` (chỉ dành cho Actor `Khách hàng`), `Đăng xuất hệ thống`.
  2. **Bảng đặc tả & Activity Diagram (`act-uc01.png`):**
     - **Luồng chính (Đăng nhập & Điều hướng tự động):** Nhập `username`, `password` $\rightarrow$ Hệ thống tìm trong bảng `users` và kiểm tra `BCryptPasswordEncoder.matches()`[cite: 27] $\rightarrow$ Nếu đúng, kiểm tra `role`[cite: 53]:
       - `ADMIN` $\rightarrow$ chuyển tới `/admin/dashboard`[cite: 53].
       - `RECEPTIONIST` $\rightarrow$ chuyển tới `/staff/dashboard`[cite: 53].
       - `CUSTOMER` $\rightarrow$ chuyển tới `/customer/dashboard`[cite: 53].
     - **Luồng phụ A1 (Khách hàng Đăng ký tài khoản mới `/register`):** Nhập 5 trường bắt buộc (`full_name`, `date_of_birth`, `phone`, `username`, `password`)[cite: 53] $\rightarrow$ Kiểm tra `username` chưa tồn tại $\rightarrow$ Lưu bảng `users` (`role = 'CUSTOMER'`, mật khẩu băm `BCrypt`) và lưu bảng `customers` (gắn `user_id`) $\rightarrow$ Tự động đăng nhập vào `/customer/dashboard`[cite: 53].
     - **Luồng ngoại lệ:** Sai tên đăng nhập/mật khẩu (báo lỗi chung *"Sai tên đăng nhập hoặc mật khẩu"*), trùng `username` khi đăng ký, hoặc truy cập trái quyền (chặn `403 Forbidden`)[cite: 27, 30].
  3. **Sequence Diagram (`seq-uc01.png`):**
     - Lifelines: `Người dùng` $\rightarrow$ `AuthView (WF-02)` $\rightarrow$ `AuthController` $\rightarrow$ `AuthService` $\rightarrow$ `UserRepository` / `CustomerRepository`.

---

### 2. Hướng dẫn làm `UC02`: Quản lý Danh mục Phòng, Biểu giá và Dịch vụ
* **Người phụ trách:** **HOÀNG** (Mục `4.2.2` — File `chuong-4-uc02-uc03-hoang.docx`)[cite: 35].
* **Actor:** `Quản trị viên (ADMIN)` (toàn quyền CRUD & cấu hình giá); `Lễ tân (RECEPTIONIST)` (chỉ xem danh sách phòng & giá)[cite: 23, 53].
* **Bảng CSDL liên quan:** `room_types`, `rooms`, `services`, `bookings`, `service_orders`[cite: 53].
* **Màn hình Wireframe:** `WF-09` (`/admin/room-types`, `/admin/rooms`, `/admin/services`).
* **Cách vẽ 3 sơ đồ của `UC02`:**
  1. **Sơ đồ UC chi tiết (`uc-02.png`):**
     - Bubble chính: `UC02: Quản lý Danh mục Phòng, Biểu giá và Dịch vụ`.
     - Các nhánh phân rã / `<<include>>`: `Cấu hình Hạng phòng & Giá ngày thường / cuối tuần`, `Quản lý Phòng vật lý & Trạng thái bảo trì`, `Quản lý Danh mục 4 nhóm dịch vụ`, `Kiểm tra ràng buộc toàn vẹn lịch sử trước khi xóa`.
  2. **Bảng đặc tả & Activity Diagram (`act-uc02.png`):**
     - **Luồng 2A (Quản lý Hạng phòng & Biểu giá linh hoạt):** Admin thêm/sửa hạng phòng (`name`, `capacity >= 1`, `price_per_night > 0` cho Thứ 2–Thứ 5, `weekend_price_per_night > 0` cho Thứ 6–Chủ Nhật, `description`)[cite: 27, 53]. Khi xóa hạng phòng: kiểm tra chưa có phòng vật lý hoặc đơn đặt nào tham chiếu mới cho xóa[cite: 27, 30].
     - **Luồng 2B (Quản lý Phòng vật lý & Bảo trì):** Admin thêm/sửa phòng (`room_number` duy nhất, `floor >= 1`, `room_type_id`, trạng thái `AVAILABLE` hoặc `MAINTENANCE`)[cite: 27, 53]. Khi xóa phòng: kiểm tra phòng chưa từng có bản ghi trong `bookings`[cite: 27, 30].
     - **Luồng 2C (Quản lý Danh mục 4 nhóm Dịch vụ):** Admin thêm/sửa/xóa dịch vụ thuộc 4 nhóm `category IN ('MINIBAR', 'LAUNDRY', 'FB', 'DAMAGES')`, `unit`, `unit_price >= 0`[cite: 53]. Chặn xóa nếu dịch vụ đã phát sinh trong `service_orders`[cite: 29, 30].
  3. **Sequence Diagram (`seq-uc02.png`):**
     - Lifelines: `Admin` $\rightarrow$ `CatalogManageView (WF-09)` $\rightarrow$ `AdminCatalogController` $\rightarrow$ `CatalogService` $\rightarrow$ `RoomTypeRepository` / `RoomRepository` / `ServiceRepository` / `BookingRepository`.

---

### 3. Hướng dẫn làm `UC03`: Tìm kiếm và Đặt phòng trực tuyến (⭐ Trọng tâm 1)
* **Người phụ trách:** **HOÀNG** (Mục `4.2.3` — File `chuong-4-uc02-uc03-hoang.docx`)[cite: 35, 53].
* **Actor:** `Khách hàng (CUSTOMER)`[cite: 53].
* **Bảng CSDL liên quan:** `room_types`, `rooms`, `bookings`, `customers`[cite: 53].
* **Màn hình Wireframe:** `WF-01` (Thanh tìm kiếm), `WF-04` (Chọn hạng phòng & Pop-up xác nhận cọc 30%), `WF-03` (Dashboard Khách hàng & Phiếu xác nhận QR)[cite: 53].
* **Cách vẽ 3 sơ đồ của `UC03`:**
  1. **Sơ đồ UC chi tiết (`uc-03.png`):**
     - Bubble chính: `UC03: Tìm kiếm và Đặt phòng trực tuyến`.
     - `<<include>>`: `Kiểm tra tồn kho Hạng phòng (Quota Check)`, `Tính giá phòng ngày thường / cuối tuần & biểu giá ++`, `Xác nhận Pop-up & Thanh toán cọc 30%`, `Sinh Phiếu xác nhận đặt phòng kèm mã QR`.
     - `<<extend>>`: `Hủy đơn đặt phòng (trạng thái RESERVED)`.
  2. **Bảng đặc tả & Activity Diagram (`act-uc03.png`):**
     - **Bước 1 (Tìm hạng phòng theo Quota):** Khách chọn `check_in_date`, `check_out_date` (`> check_in_date`), `guest_count`[cite: 53]. Hệ thống tính tồn kho khả dụng cho từng hạng phòng:
       $$N_{\text{available}} = \text{COUNT}(\text{rooms } \neq \text{'MAINTENANCE'}) - \text{COUNT}(\text{bookings } \in \{\text{'RESERVED'}, \text{'CHECKED\_IN'}\} \text{ giao ngày})$$
       Chỉ hiển thị hạng phòng có $N_{\text{available}} \ge 1$ và `capacity >= guest_count`.
     - **Bước 2 (Tính tiền phòng & Pop-up xác nhận cọc 30%):** Duyệt từng đêm lưu trú (T2–T5 lấy `price_per_night`, T6–CN lấy `weekend_price_per_night`) $\rightarrow$ tính `est_svc (5%)`, `est_vat (10%)`, `est_grand_total` $\rightarrow$ tính **Tiền cọc 30% (`deposit_amount`)** và **70% còn lại thanh toán tại quầy** $\rightarrow$ Mở Pop-up xác nhận minh bạch[cite: 53].
     - **Bước 3 (Thanh toán cọc 30% & Giữ chỗ):** Khách bấm xác nhận thanh toán cọc $\rightarrow$ Trong `@Transactional`, kiểm tra lại $N_{\text{available}} \ge 1$ $\rightarrow$ Lưu `bookings` với mã `booking_code` (`BK-XXXX`), `room_type_id`, **`room_id = NULL`**, `deposit_amount` (30%), `status = 'RESERVED'`[cite: 53].
     - **Bước 4 (Xuất Phiếu xác nhận QR):** Trả về Phiếu xác nhận đặt phòng có mã QR và lưu vào `/customer/dashboard`[cite: 53].
     - **Luồng phụ (Hủy đặt phòng):** Khách hủy đơn đang ở trạng thái `RESERVED` $\rightarrow$ chuyển sang `CANCELLED` (nhả lại suất Quota cho hạng phòng đó)[cite: 28, 53].
  3. **Sequence Diagram (`seq-uc03.png`):**
     - Lifelines: `Khách hàng` $\rightarrow$ `BookingPortalView (WF-04/WF-03)` $\rightarrow$ `CustomerBookingController` $\rightarrow$ `BookingService` $\rightarrow$ `RoomRepository` / `BookingRepository`.

---

### 4. Hướng dẫn làm `UC04`: Quản lý Check-in, Gán phòng sạch & Tiếp nhận Walk-in (⭐ Trọng tâm 2)
* **Người phụ trách:** **PHONG** (Mục `4.2.4` — File `chuong-4-uc04-uc05-phong.docx`)[cite: 36, 53].
* **Actor:** `Lễ tân (RECEPTIONIST)` (và `Khách hàng` là tác nhân phối hợp tại quầy)[cite: 23, 53].
* **Bảng CSDL liên quan:** `bookings`, `rooms`, `customers`, `room_types`[cite: 53].
* **Màn hình Wireframe:** `WF-05` (Dashboard Lễ tân), `WF-06` (Màn hình Check-in gán phòng sạch & Form Walk-in)[cite: 53].
* **Cách vẽ 3 sơ đồ của `UC04`:**
  1. **Sơ đồ UC chi tiết (`uc-04.png`):**
     - Bubble chính: `UC04: Quản lý Check-in và Gán phòng vật lý`.
     - `<<include>>`: `Tra cứu mã Booking / Quét mã QR`, `Lọc và Gán phòng vật lý trống sạch (AVAILABLE)`, `Kích hoạt tài khoản phòng (Room Folio)`.
     - `<<extend>>`: `Tiếp nhận khách vãng lai (Walk-in - 0đ cọc)`, `Ghi nhận phụ thu nhận phòng sớm (Early Check-in)`.
  2. **Bảng đặc tả & Activity Diagram (`act-uc04.png`):**
     - **Luồng chính 4A (Check-in cho Khách Đặt Online đã cọc 30%):**
       1. Khách trình Phiếu xác nhận QR $\rightarrow$ Lễ tân quét hoặc nhập `booking_code` (`BK-XXXX`) / SĐT[cite: 53].
       2. Hệ thống hiển thị đơn đặt (`status == 'RESERVED'`, hạng phòng đã đặt, tiền cọc 30% đã đóng)[cite: 53].
       3. Hệ thống lọc danh sách các phòng vật lý thuộc đúng `room_type_id` đang ở trạng thái **`AVAILABLE` (Trống & Đã dọn sạch)** (khóa hoàn toàn các phòng `DIRTY`, `OCCUPIED`, `MAINTENANCE`)[cite: 53].
       4. Lễ tân chọn 1 phòng cụ thể (ví dụ `P.201`), đối chiếu/nhập CCCD (`id_number`), chọn phụ thu nhận sớm (nếu khách đến trước `14:00`: trước 6h = 100%, 6h–9h = 50%, 9h–14h = 30% hoặc miễn phí `0đ`)[cite: 53].
       5. Bấm **[Xác nhận Check-in]** $\rightarrow$ Cập nhật `bookings.room_id = 201`, `bookings.status = 'CHECKED_IN'`, `actual_check_in = NOW()`, `rooms.status = 'OCCUPIED'` và mở Room Folio[cite: 53].
     - **Luồng phụ 4B (Tiếp nhận Khách vãng lai Walk-in — Không thu cọc lúc vào):**
       1. Lễ tân chọn phòng vật lý đang `AVAILABLE` trên sơ đồ phòng (đảm bảo hạng phòng còn đủ Quota cho khoảng ngày khách ở)[cite: 53].
       2. Nhập thông tin khách (`full_name`, `phone`, `id_number` CCCD bắt buộc) và `check_out_date`[cite: 53].
       3. Bấm **[Hoàn tất Check-in Walk-in]** $\rightarrow$ Tạo hồ sơ `customers` (`user_id = NULL`), tạo `bookings` với **`deposit_amount = 0`**, `status = 'CHECKED_IN'`, gán `room_id`, đổi phòng sang `OCCUPIED` (khách Walk-in sẽ thanh toán 100% khi Check-out).
  3. **Sequence Diagram (`seq-uc04.png`):**
     - Lifelines: `Lễ tân` $\rightarrow$ `CheckInView (WF-06)` $\rightarrow$ `StaffCheckInController` $\rightarrow$ `CheckInService` $\rightarrow$ `BookingRepository` / `RoomRepository` / `CustomerRepository`.

---

### 5. Hướng dẫn làm `UC05`: Check-out, Quyết toán `++` & Chuyển trạng thái Dọn phòng (⭐ Trọng tâm 3)
* **Người phụ trách:** **PHONG** (Mục `4.2.5` — File `chuong-4-uc04-uc05-phong.docx`)[cite: 36, 53].
* **Actor:** `Lễ tân (RECEPTIONIST)`[cite: 53].
* **Bảng CSDL liên quan:** `bookings`, `service_orders`, `invoices`, `rooms`[cite: 53].
* **Màn hình Wireframe:** `WF-08` (Màn hình Check-out, Quyết toán `++` & Hóa đơn VAT), `WF-05` (Sơ đồ phòng trạng thái `DIRTY` & nút `[Xác nhận đã dọn sạch]`)[cite: 53].
* **Cách vẽ 3 sơ đồ của `UC05`:**
  1. **Sơ đồ UC chi tiết (`uc-05.png`):**
     - Bubble chính: `UC05: Check-out, Quyết toán hóa đơn và Dọn phòng`.
     - `<<include>>`: `Tổng hợp tiền phòng, Room Folio & Phụ thu trễ giờ`, `Tính phí phục vụ (SVC 5%), VAT (10%) & Khấu trừ tiền cọc`, `Lập hóa đơn quyết toán & Chuyển phòng sang Chờ dọn (DIRTY)`.
     - `<<extend>>`: `In hóa đơn VAT (window.print)`, `Xác nhận đã dọn sạch phòng (DIRTY -> AVAILABLE)`.
  2. **Bảng đặc tả & Activity Diagram (`act-uc05.png`):**
     - **Bước 1:** Lễ tân chọn phòng đang ở (`CHECKED_IN`) báo trả phòng $\rightarrow$ Hệ thống kiểm tra giờ trả thực tế so với mốc `12:00` trưa để gợi ý `late_checkout_fee` (trước 12h: `0%`; 12h–15h: `30%`; 15h–18h: `50%`; sau 18h: `100%` giá 1 đêm)[cite: 53].
     - **Bước 2 (Tính toán quyết toán tự động `++`):**
       - `subtotal = room_charge + service_charge_total + early_checkin_fee + late_checkout_fee`[cite: 53].
       - `service_charge_amount = ROUND(subtotal * 0.05)`[cite: 53].
       - `vat_amount = ROUND((subtotal + service_charge_amount) * 0.10)`[cite: 53].
       - `grand_total = subtotal + service_charge_amount + vat_amount`[cite: 53].
       - `deposit_deducted = bookings.deposit_amount`[cite: 53]:
         - **Nhánh Khách Online:** Đã cọc 30% $\rightarrow$ `balance_due` = **70% tiền phòng còn lại + 100% dịch vụ & phụ thu**[cite: 53].
         - **Nhánh Khách Walk-in:** Cọc `0đ` $\rightarrow$ `balance_due` = **100% toàn bộ `grand_total`**.
     - **Bước 3 (Thanh toán & Chuyển `DIRTY`):** Lễ tân chọn phương thức (`CASH`, `CARD`, `TRANSFER`) và bấm **[Xác nhận Hoàn tất Thanh toán]** $\rightarrow$ Trong `@Transactional` (có kiểm tra chống tạo trùng hóa đơn Idempotency): lưu bảng `invoices`, đổi `bookings.status = 'CHECKED_OUT'`, `actual_check_out = NOW()`, và đổi **`rooms.status = 'DIRTY'`** (màu cam)[cite: 28, 53].
     - **Luồng phụ (Xác nhận đã dọn sạch phòng):** Sau khi buồng phòng dọn xong, Lễ tân bấm nút **[Xác nhận đã dọn sạch]** trên thẻ phòng `DIRTY` $\rightarrow$ `rooms.status` về lại **`AVAILABLE`** (màu xanh lá)[cite: 53].
  3. **Sequence Diagram (`seq-uc05.png`):**
     - Lifelines: `Lễ tân` $\rightarrow$ `CheckOutView (WF-08)` $\rightarrow$ `StaffCheckOutController` $\rightarrow$ `CheckOutService` $\rightarrow$ `ServiceOrderRepository` / `InvoiceRepository` / `BookingRepository` / `RoomRepository`.

---

### 6. Hướng dẫn làm `UC06`: Ghi nhận Dịch vụ Lưu trú vào Room Folio
* **Người phụ trách:** **KIỆT** (Mục `4.2.6` — File `chuong-4-uc01-uc06-kiet.docx`)[cite: 36, 53].
* **Actor:** `Lễ tân (RECEPTIONIST)`[cite: 53].
* **Bảng CSDL liên quan:** `service_orders`, `services`, `bookings`[cite: 53].
* **Màn hình Wireframe:** `WF-07` (Modal / Màn hình Quản lý Room Folio 4 nhóm dịch vụ)[cite: 53].
* **Cách vẽ 3 sơ đồ của `UC06`:**
  1. **Sơ đồ UC chi tiết (`uc-06.png`):**
     - Bubble chính: `UC06: Ghi nhận sử dụng dịch vụ lưu trú (Room Folio)`.
     - `<<include>>`: `Chọn dịch vụ theo 4 nhóm (Minibar, Giặt ủi, F&B, Đền bù)`, `Đóng băng đơn giá lịch sử & Ghi sổ Room Folio`.
     - `<<extend>>`: `Xóa dòng dịch vụ ghi nhầm (khi phòng đang CHECKED_IN)`.
  2. **Bảng đặc tả & Activity Diagram (`act-uc06.png`):**
     - **Luồng chính:** Lễ tân chọn phòng đang `OCCUPIED` (booking ở trạng thái `CHECKED_IN`) $\rightarrow$ Mở Room Folio (`WF-07`) $\rightarrow$ Chọn tab nhóm dịch vụ (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`), chọn mặt hàng và nhập `quantity >= 1` $\rightarrow$ Hệ thống sao chép `services.unit_price` hiện hành vào `service_orders.unit_price` (đóng băng giá lịch sử), lưu `created_by` là lễ tân thực hiện và cập nhật tổng nợ Room Folio ngay lập tức[cite: 29, 53].
     - **Luồng phụ:** Xóa dòng dịch vụ thêm nhầm khi booking vẫn đang `CHECKED_IN`[cite: 29, 30].
     - **Luồng ngoại lệ:** Cố tình thêm dịch vụ cho đơn chưa Check-in hoặc đã Check-out, hoặc nhập số lượng `< 1`[cite: 29, 30].
  3. **Sequence Diagram (`seq-uc06.png`):**
     - Lifelines: `Lễ tân` $\rightarrow$ `RoomFolioView (WF-07)` $\rightarrow$ `ServiceOrderController` $\rightarrow$ `ServiceOrderService` $\rightarrow$ `BookingRepository` / `ServiceRepository` / `ServiceOrderRepository`[cite: 29, 53].

---

### 7. Hướng dẫn làm `UC07`: Báo cáo Thống kê Doanh thu & Công suất phòng (⭐ Trọng tâm 4)
* **Người phụ trách:** **KHOA** (Mục `4.2.7` — File `chuong-4-uc07-khoa.docx`)[cite: 35, 53].
* **Actor:** `Quản trị viên (ADMIN)`[cite: 53].
* **Bảng CSDL liên quan:** `invoices`, `bookings`, `rooms`, `room_types`, `service_orders`[cite: 53].
* **Màn hình Wireframe:** `WF-10` (`/admin/dashboard` — Báo cáo doanh thu đa chiều & Biểu đồ Occupancy Rate)[cite: 53].
* **Cách vẽ 3 sơ đồ của `UC07`:**
  1. **Sơ đồ UC chi tiết (`uc-07.png`):**
     - Bubble chính: `UC07: Báo cáo thống kê doanh thu và Công suất phòng`.
     - `<<include>>`: `Thống kê cơ cấu doanh thu đa chiều (Phòng, 4 nhóm DV, SVC 5%, VAT 10%)`, `Tính tỷ lệ lấp đầy (Occupancy Rate), ADR và RevPAR`.
     - `<<extend>>`: `Lọc báo cáo theo khoảng ngày / Tháng / Quý / Năm`.
  2. **Bảng đặc tả & Activity Diagram (`act-uc07.png`):**
     - Admin chọn khoảng thời gian báo cáo $\rightarrow$ Hệ thống truy vấn bảng `invoices` và `service_orders` để bóc tách doanh thu tiền phòng, doanh thu 4 nhóm dịch vụ, phụ thu, phí phục vụ SVC 5% và thuế VAT 10%[cite: 53].
     - Hệ thống tính tổng số phòng có thể kinh doanh $R_{\text{sellable}} = \text{COUNT}(\text{rooms WHERE status} \in \{\text{'AVAILABLE'}, \text{'OCCUPIED'}, \text{'DIRTY'}\})$ (loại trừ phòng `MAINTENANCE`) và tính bộ 3 chỉ số quản trị khách sạn: **Occupancy Rate (%)**, **ADR** và **RevPAR**[cite: 53].
  3. **Sequence Diagram (`seq-uc07.png`):**
     - Lifelines: `Admin` $\rightarrow$ `AdminReportView (WF-10)` $\rightarrow$ `AdminReportController` $\rightarrow$ `ReportService` $\rightarrow$ `InvoiceRepository` / `RoomRepository` / `BookingRepository`.

---

## PHẦN 4. DANH SÁCH FILE GIAO NỘP THEO TỪNG THÀNH VIÊN

| Thành viên | Danh sách File Word nộp cho Hoàng | Danh sách File Sơ đồ / Thiết kế nộp kèm (`.drawio` + `.png`) |
|---|---|---|
| **PHONG** | 1. `chuong-1-2-phong.docx` *(Chương 1 & Chương 2)*<br>2. `chuong-4-uc04-uc05-phong.docx` *(Mục 4.2.4 & 4.2.5)*<br>*(Ngoài ra gửi cả nhóm: `demo-script.docx`, `kich-ban-thuyet-trinh.docx`, `cau-hoi-du-kien.docx`)* | **6 sơ đồ UML:**<br>`uc-04`, `act-uc04`, `seq-uc04`<br>`uc-05`, `act-uc05`, `seq-uc05` |
| **KIỆT** | 1. `chuong-4-uc01-uc06-kiet.docx` *(Mục 4.2.1 & 4.2.6)*<br>2. `chuong-6-giao-dien-kiet.docx` *(Chương 6)*<br>*(Ngoài ra làm full bộ Slide: `presentation-en.pptx` / `.pdf`)* | **6 sơ đồ UML:** `uc-01`, `act-uc01`, `seq-uc01`, `uc-06`, `act-uc06`, `seq-uc06`<br>**+ 11 file giao diện:** `wf-01` $\rightarrow$ `wf-10` & `dieu-huong` |
| **KHOA** | 1. `chuong-4-uc07-khoa.docx` *(Mục 4.2.7, 4.3, 4.4)*<br>2. `chuong-5-csdl-khoa.docx` *(Chương 5)*<br>*(Ngoài ra bàn giao: Source Code GitHub, `README.md`, `schema.sql`, `sample-data.sql`, `demo-du-phong.mp4`)* | **5 sơ đồ UML:**<br>`erd`, `class-diagram`<br>`uc-07`, `act-uc07`, `seq-uc07` |
| **HOÀNG** | 1. `bao-cao-khung.docx` & `phan-dau-hoang.docx`<br>2. `chuong-3-hoang.docx` *(Chương 3)*<br>3. `chuong-4-uc02-uc03-hoang.docx` *(Mục 4.1, 4.2.2, 4.2.3)*<br>4. `chuong-7-8-hoang.docx` *(Chương 7, Chương 8, Tài liệu tham khảo)*<br>5. **Bản gộp cuối cùng:** `bao-cao.docx` & `bao-cao.pdf` | **7 sơ đồ UML:**<br>`usecase-tong-quat`<br>`uc-02`, `act-uc02`, `seq-uc02`<br>`uc-03`, `act-uc03`, `seq-uc03` |