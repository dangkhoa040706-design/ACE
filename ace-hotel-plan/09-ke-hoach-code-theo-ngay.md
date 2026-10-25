# 09. Kế hoạch Code Chi tiết Theo Ngày (Phần của Khoa — Tuần 1 đến Tuần 4)

> **Mục tiêu:** Chia nhỏ khối lượng công việc của riêng **Khoa** theo từng ngày cụ thể từ **12/10 đến 08/11/2026** (hạn nộp báo cáo **23:59 ngày 09/11/2026**) để đảm bảo một mình Khoa code trọn vẹn 4 phân hệ giao diện + 7 Use Case mà không bị quá tải[cite: 12, 25, 35].

---

## 1. Tuần 1 (12/10 đến 18/10): Chốt Quy trình PMS Mới, CSDL 8 Bảng & 5 Sơ đồ UML của Khoa

- [x] **Thứ Hai – Thứ Tư (12–14/10):**
  - Chốt `quy-trinh-nghiep-vu-moi.md`, bộ 4 ảnh phân công mới (`.png`), mục lục Chương 1–8 (bỏ Phụ lục) và nâng cấp toàn bộ thư mục `docs/` + `ace-hotel-plan/`.
- [ ] **Thứ Năm – Thứ Sáu (15–16/10):**
  - Viết hoàn chỉnh `database/schema.sql` và `database/sample-data.sql` (theo File `01` của bộ plan này)[cite: 35].
  - Vẽ 2 sơ đồ cấp hệ thống bằng draw.io: **Mục 4.3 – `class-diagram.drawio` / `.png` (8 lớp)** và **Mục 4.4 – `erd.drawio` / `.png` (8 bảng)**[cite: 35].
- [ ] **Thứ Bảy – Chủ Nhật (17–18/10):**
  - Viết file Word **`chuong-5-csdl-khoa.docx`** (Lược đồ quan hệ, Từ điển dữ liệu 8 bảng, Ràng buộc & SQL trọng tâm — lấy trực tiếp từ File `docs/06`)[cite: 24, 35].
  - Viết file Word **`chuong-4-uc07-khoa.docx`** (Bảng đặc tả 9 dòng của `UC07`) và vẽ bộ 3 sơ đồ của `UC07`: **`uc-07.png`**, **`act-uc07.png`**, **`seq-uc07.png`** (lấy trực tiếp từ File `docs/11`)[cite: 29, 35].
  - **Bàn giao cuối Tuần 1 (18/10):** Gửi `erd.png`, `class-diagram.png`, `uc-07.png`, `schema.sql`, `sample-data.sql` cho Hoàng, Phong, Kiệt; nhận 10 Wireframe từ Kiệt và đặc tả `UC01–UC06` từ 3 bạn[cite: 35].

---

## 2. Tuần 2 (19/10 đến 25/10): Lịch Code 7 Ngày (Mỗi Ngày 1 Cụm Chức năng + 1 Commit GitHub)

| Ngày | Cụm chức năng cần hoàn thành | Các File Code chính (`ace-hotel/src`) | Tiêu chí Xong trong ngày (Commit GitHub) |
|---|---|---|---|
| **Ngày 1**<br>*(Thứ Hai 19/10)* | **Nền tảng CSDL, Entities, Repositories & Design System Layout** | `schema.sql`, `sample-data.sql`, `DemoDataSeeder`, 5 `Enum`, 8 `Entity`, 8 `Repository`, `tokens.css`, `theme.js`, `public-layout.html`, `pms-layout.html` | Chạy `mvn spring-boot:run` lên cổng `8080`, nạp thành công 12 phòng & 6 booking mẫu vào H2; nút đổi Dark/Light Mode hoạt động mượt[cite: 12, 35]. |
| **Ngày 2**<br>*(Thứ Ba 20/10)* | **`UC01`: Đăng nhập, Đăng ký 5 trường & Tự động điều hướng 3 Role (`WF-02`)** | `SecurityConfig`, `RoleBasedAuthSuccessHandler`, `CustomUserDetailsService`, `AuthService`, `AuthController`, `login-register.html`, `403.html` | Đăng nhập `admin`, `nhanvien`, `khach` tự động nhảy về đúng 3 Dashboard; đăng ký khách mới tạo đủ `users` + `customers`; gõ chéo URL bị chặn `403`[cite: 6, 12, 53]. |
| **Ngày 3**<br>*(Thứ Tư 21/10)* | **`UC02`: Quản trị Danh mục Hạng phòng (Giá T2–T5 / T6–CN), Phòng & 4 Nhóm Dịch vụ (`WF-09`)** | `CatalogService`, `AdminCatalogController`, `admin/catalog.html` | Admin thêm/sửa được hạng phòng, phòng vật lý, dịch vụ; bật/tắt bảo trì phòng `305`; xóa phòng `301` bị chặn đúng thông báo `MSG-15`[cite: 8, 12, 21]. |
| **Ngày 4**<br>*(Thứ Năm 22/10)* | **`UC03` (⭐ Trọng tâm 1): Landing Storytelling (`WF-01`), Tìm Quota, Pop-up Cọc 30% (`WF-04`) & Phiếu QR (`WF-03`)** | `PricingService`, `BookingService`, `PublicController`, `CustomerPortalController`, `landing.html`, `search-deposit.html`, `customer/dashboard.html`, `qr-generator.js` | Khách tìm phòng theo ngày thấy đúng số suất `availableQuota`; mở Pop-up thấy đúng `SVC 5%`, `VAT 10%`, `Cọc 30%`; bấm xác nhận sinh đơn `RESERVED` (`room_id = NULL`) kèm **Phiếu xác nhận có Mã QR**; hủy được đơn `RESERVED`[cite: 12, 47, 53]. |
| **Ngày 5**<br>*(Thứ Sáu 23/10)* | **`UC04` (⭐ Trọng tâm 2): Dashboard Lễ tân (`WF-05`), Quét QR Gán phòng sạch & Walk-in 0đ cọc (`WF-06`)** | `CheckInService`, `StaffDashboardController`, `StaffCheckInController`, `staff/dashboard.html`, `staff/check-in.html` | Sơ đồ 12 phòng hiện đúng 4 màu; nhập mã `BK-2402` chỉ lọc phòng Deluxe đang `AVAILABLE` (`P.201`, `P.302`), gán `P.201` chuyển sang `OCCUPIED`; tạo khách Walk-in nhận phòng `P.101` ngay với `deposit_amount = 0`[cite: 12, 21, 53]. |
| **Ngày 6**<br>*(Thứ Bảy 24/10)* | **`UC06` & `UC05` (⭐ Trọng tâm 3): Room Folio 4 nhóm (`WF-07`), Check-out Quyết toán `++` (`WF-08`) & Dọn phòng `DIRTY`** | `FolioService`, `CheckOutService`, `StaffFolioController`, `StaffCheckOutController`, `staff/folio.html`, `staff/check-out.html`, `staff/invoice-print.html` | Thêm/xóa dịch vụ Room Folio cho `P.301`; mở Check-out `BK-2401` ra đúng `Grand Total = 2.656.500đ`, trừ cọc `693.000đ` còn thực thu **`1.963.500đ`** (và `BK-2405` Walk-in thu 100%); thanh toán xong phòng `P.301` chuyển sang **`🟠 DIRTY`**; bấm nút `[✓ Xác nhận đã dọn sạch]` phòng về lại **`🟢 AVAILABLE`**[cite: 12, 21, 53]. |
| **Ngày 7**<br>*(Chủ Nhật 25/10)* | **`UC07` (⭐ Trọng tâm 4): Dashboard Admin – Báo cáo Doanh thu `++` & Công suất `Occupancy Rate` (`WF-10`)** | `ReportService`, `AdminReportController`, `admin/dashboard-report.html` | Tính đúng $R_{\text{sellable}} = 11$ (loại trừ `P.103` đang `MAINTENANCE`), hiển thị đúng `Occupancy Rate (%)`, `ADR`, `RevPAR` và bảng cơ cấu doanh thu đa chiều `++`[cite: 12, 21, 53]. |

---

## 3. Tuần 3 (26/10 đến 01/11): Kiểm thử, Viết `README.md` & Bàn giao Code Giữa Tuần

- [ ] **Đầu tuần (26–28/10):**
  - Chạy rà soát toàn bộ 30 Test Cases (trong File `10` bên dưới), kiểm tra kỹ giao diện ở cả Dark Mode và Light Mode.
  - Viết `ace-hotel/README.md` (hướng dẫn cài JDK 21, lệnh chạy, danh sách 4 tài khoản demo, lệnh nạp lại dữ liệu mẫu sạch `--ace.seed=true`)[cite: 10, 12, 35].
- [ ] **Giữa tuần (~29/10 – Mốc bàn giao M3):**
  - **Bàn giao:** Gửi link GitHub repo + `README.md` cho **Hoàng, Phong, Kiệt** để Hoàng chạy chụp ảnh màn hình viết Chương 7, Kiệt chụp ảnh làm Slide 9–12 và Phong khớp kịch bản Demo[cite: 12, 25, 35].
  - **Gửi file báo cáo của Khoa cho Hoàng:** Gửi `chuong-4-uc07-khoa.docx`, `chuong-5-csdl-khoa.docx` và 5 file ảnh sơ đồ (`erd.png`, `class-diagram.png`, `uc-07.png`, `act-uc07.png`, `seq-uc07.png`) cho **Hoàng** gộp vào `bao-cao.docx`[cite: 35].
- [ ] **Cuối tuần (30/10–01/11):**
  - Sửa nhanh các lỗi nhỏ (nếu có) khi 3 bạn chạy thử; đóng gói bản `.zip` dự phòng[cite: 12, 35].

---

## 4. Tuần 4 (02/11 đến 08/11): Quay Video Dự phòng, Tập Bấm Máy Demo & Bảo vệ

- [ ] **Đầu tuần (02–03/11):**
  - Nạp lại dữ liệu mẫu sạch (`--ace.seed=true`), quay video **`demo-du-phong.mp4`** đi trọn 6 bước Demo và gửi cho **Kiệt** chèn vào Slide 10[cite: 12, 35].
  - Điền đáp án chi tiết cho **8 câu hỏi kỹ thuật & CSDL** trong file `cau-hoi-du-kien.docx` của Phong[cite: 35].
- [ ] **Giữa – Cuối tuần (04–08/11):**
  - Cùng **Phong** tập khớp lời thuyết trình và thao tác bấm máy trên thiết bị demo **3 lần**, bấm giờ phần Demo gọn trong **5.5 – 6 phút** (tổng bài trình bày 14 phút)[cite: 12, 35, 36].
  - Cùng cả nhóm ôn tập toàn bộ 20 câu hỏi phản biện[cite: 35, 36].