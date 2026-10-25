# 01. PROJECT BRIEF – HỆ THỐNG QUẢN LÝ KHÁCH SẠN & ĐẶT PHÒNG TRỰC TUYẾN (ACE HOTEL)

> **Học phần:** 502050 – Phân tích và Thiết kế Yêu cầu, Khoa CNTT – ĐH Tôn Đức Thắng.  
> **Trạng thái:** Bản chốt chính thức ngày 14/10/2026 (Đồng bộ với `quy-trinh-nghiep-vu-moi.md` và cấu trúc báo cáo Chương 1 – Chương 8, không Phụ lục).

---

## 1. Tổng quan Dự án
| Hạng mục | Nội dung chi tiết |
|---|---|
| **Tên dự án** | Hệ thống Quản lý Khách sạn & Đặt phòng Trực tuyến **ACE Hotel** (*Accommodation, Check-in & Extras*) |
| **Hình thức** | Báo cáo đồ án cuối kỳ theo nhóm 4 thành viên (Đề tài 1) |
| **Phân vai 4 trụ cột** | • **Khoa:** Code full ứng dụng Web Java + Thao tác thiết bị Demo + Chương 5 (CSDL) + Mục 4.2.7 (`UC07`), `ERD`, `Class Diagram`<br>• **Hoàng:** Phân tích yêu cầu + Chủ trì viết, gộp và hoàn thiện quyển báo cáo Word/PDF (Phần đầu, Chương 3, `UC02–03`, Chương 7, Chương 8, Tài liệu tham khảo)<br>• **Phong:** Thuyết trình full 12 slide (14 phút) + Kịch bản Demo + 20 Câu hỏi phản biện + Chương 1, Chương 2, `UC04–05`<br>• **Kiệt:** Thiết kế full bộ Slide tiếng Anh (12 slide) + 10 Wireframe & Sơ đồ điều hướng (Chương 6) + `UC01`, `UC06` |
| **Hạn nộp báo cáo** | **23:59 ngày 09/11/2026** (Nộp `.docx` và `.pdf` lên hệ thống E-learning) |
| **Sản phẩm nộp kèm** | Bộ Slide tiếng Anh (`.pptx`/`.pdf`), Source code Java (GitHub + `.zip`), Video demo dự phòng (`.mp4`) |
| **Thời lượng bảo vệ** | 12–15 phút trình bày & demo trực tiếp (Phong thuyết trình, Khoa thao tác máy); 5–8 phút hỏi đáp (cả 4 thành viên) |
| **Tỷ trọng điểm** | 50% tổng điểm học phần: **Cột 1 – Quyển báo cáo (30%)** và **Cột 2 – Báo cáo & Demo (70%)** |

---

## 2. Mục tiêu Hệ thống
1. **Chuẩn hóa quy trình vận hành khách sạn thực tế (PMS & Online Booking Portal):**
   - Khép kín luồng nghiệp vụ từ **Phân hệ Khách hàng trực tuyến (Public)** đến **Phân hệ Nội bộ tại quầy (PMS / Backoffice)**.
   - Khách đặt trực tuyến theo **Hạng phòng (Room Type Quota)**, thanh toán **cọc 30%** để giữ chỗ (`RESERVED`) và nhận **Phiếu xác nhận kèm mã QR**.
   - Khi khách đến quầy, Lễ tân quét/nhập mã QR và **Gán phòng vật lý cụ thể đang trống sạch (`AVAILABLE / Clean`)**, đồng thời hỗ trợ tiếp nhận **Khách vãng lai (Walk-in)** nhận phòng ngay (**0đ cọc lúc vào**, thanh toán 100% lúc trả phòng).
2. **Khép kín dòng tiền theo chuẩn biểu giá `++` & Vòng đời buồng phòng (`DIRTY`):**
   - Ghi nhận chi phí dịch vụ phát sinh theo 4 nhóm (`MINIBAR`, `LAUNDRY`, `FB`, `DAMAGES`) vào tài khoản phòng (**Room Folio**) với cơ chế đóng băng đơn giá lịch sử.
   - Tự động quyết toán hóa đơn theo công thức `++` gồm **Phí phục vụ 5% (SVC)** và **Thuế GTGT 10% (VAT)**, khấu trừ tiền đã cọc (Khách Online trả 70% tiền phòng còn lại + dịch vụ/phụ thu; Khách Walk-in trả 100% toàn bộ hóa đơn).
   - Tự động chuyển phòng vừa trả sang trạng thái **`DIRTY` (Chờ dọn dẹp)** để khóa không cho xếp nhầm khách mới vào phòng chưa vệ sinh.
3. **Đảm bảo tính nhất quán & khả năng truy vết 100% (Chương 1 $\rightarrow$ Chương 8):**
   - Xây dựng đầy đủ **24 sơ đồ UML** trong Chương 4 (1 Use Case tổng quát + 7 Use Case chi tiết + 7 Activity Diagrams + 7 Sequence Diagrams + 1 Class Diagram + 1 ERD) khớp tuyệt đối với 8 bảng CSDL (Chương 5), 10 Wireframe (Chương 6) và ứng dụng Spring Boot chạy thật (Chương 7).

---

## 3. Đối tượng Người dùng & Các bên liên quan (Actors & Stakeholders)
| Đối tượng | Phân loại | Vai trò & Quyền hạn trên hệ thống |
|---|---|---|
| **Khách hàng (`CUSTOMER`)** | Tác nhân trực tiếp (Public Portal) | Đăng ký/đăng nhập tài khoản; tìm kiếm hạng phòng trống theo ngày; đặt phòng theo hạng & thanh toán cọc 30%; nhận Phiếu xác nhận QR; quản lý lịch sử đặt phòng & hủy đơn `RESERVED` trên `/customer/dashboard`. |
| **Lễ tân (`RECEPTIONIST`)** | Tác nhân trực tiếp (PMS Quầy) | Tác nghiệp trên `/staff/dashboard`: xem sơ đồ phòng 4 trạng thái; quét/nhập mã QR Check-in & gán phòng sạch; tiếp nhận khách Walk-in (0đ cọc); ghi nợ dịch vụ vào Room Folio; Check-out quyết toán `++` & in hóa đơn VAT; bấm xác nhận phòng đã dọn sạch (`DIRTY` $\rightarrow$ `AVAILABLE`). |
| **Quản trị viên (`ADMIN`)** | Tác nhân trực tiếp (Backoffice) | Tác nghiệp trên `/admin/dashboard`: cấu hình hạng phòng, giá ngày thường (T2–T5) & cuối tuần (T6–CN), quản lý phòng vật lý (Thêm/Sửa/Bảo trì/Xóa), quản lý 4 nhóm dịch vụ; xem báo cáo doanh thu đa chiều và chỉ số `Occupancy Rate`, `ADR`, `RevPAR`. |
| **Buồng phòng & Kế toán** | Bên liên quan (Ngoài hệ thống) | Nhân viên buồng phòng dọn vệ sinh và báo lễ tân bấm xác nhận dọn sạch trên PMS; Kế toán nhận số liệu hóa đơn VAT và báo cáo doanh thu từ Admin. |

---

## 4. Phạm vi Hệ thống (System Scope)

### 4.1. Những chức năng hệ thống LÀM (7 Use Case & 4 Phân hệ Giao diện):
1. **4 Phân hệ Giao diện riêng biệt:** (1) Landing Page Storytelling (`/`), (2) Dashboard Khách hàng (`/customer/dashboard`), (3) Dashboard Lễ tân (`/staff/dashboard`), (4) Dashboard Admin (`/admin/dashboard`).
2. **7 Use Case cốt lõi (Cài đặt Java 100%):**
   - `UC01`: Đăng ký tài khoản khách hàng, Đăng nhập xác thực `BCrypt` và Tự động điều hướng Dashboard theo 3 Role.
   - `UC02`: Quản lý Danh mục Hạng phòng (giá ngày thường/cuối tuần), Phòng vật lý và 4 nhóm Dịch vụ.
   - `UC03`: Tìm kiếm theo tồn kho Hạng phòng (Quota), Đặt phòng trực tuyến, Thanh toán cọc 30% và Xuất Phiếu xác nhận QR (*Trọng tâm 1*).
   - `UC04`: Quản lý Check-in quét QR, Gán phòng vật lý sạch (`AVAILABLE`) và Tiếp nhận khách Walk-in 0đ cọc (*Trọng tâm 2*).
   - `UC05`: Check-out, Quyết toán biểu giá `++` (SVC 5%, VAT 10%, trừ cọc 30% cho Online / thu 100% cho Walk-in) và Chuyển trạng thái dọn phòng `DIRTY` (*Trọng tâm 3*).
   - `UC06`: Ghi nhận sử dụng dịch vụ lưu trú (Minibar, Giặt ủi, F&B, Đền bù) vào Room Folio.
   - `UC07`: Báo cáo thống kê doanh thu đa chiều và Công suất khai thác phòng (`Occupancy Rate`, `ADR`, `RevPAR`) (*Trọng tâm 4*).

### 4.2. Những nội dung hệ thống KHÔNG LÀM (Out of Scope / Won't have):
- Không kết nối đồng bộ kênh OTA bên ngoài (Booking.com, Agoda, Traveloka).
- Không quản lý nhân sự, chấm công, tính lương hay kế toán tổng hợp/kho nguyên liệu bếp.
- Không quản lý chuỗi nhiều chi nhánh khách sạn hoặc ứng dụng di động native (iOS/Android).
- Không gộp nhiều phòng khác nhau trong cùng một mã booking đơn lẻ (mỗi mã `BK-XXXX` đại diện cho 1 suất phòng để kiểm soát chính xác thời điểm nhận/trả và Room Folio của từng phòng).

---

## 5. Ràng buộc Kỹ thuật & Thể thức Báo cáo
- **Công nghệ cài đặt:** Java **JDK 21**, **Spring Boot 3.5** (Spring Web, Spring Data JPA, Validation, Spring Security + BCrypt), **Thymeleaf**, **H2 Database** (chế độ lưu file `./data/ace_hotel_db`).
- **Hệ thống màu & Giao diện:** Bám sát bộ **ACE Color System** (Dark Mode mặc định, có nút chuyển sang Light Mode; màu thương hiệu `#FDBF2D`; font chữ **Inter** nhúng nội bộ).
- **Cấu trúc & Định dạng quyển báo cáo:** Gồm **8 Chương (Chương 1 $\rightarrow$ Chương 8)** và **Danh mục Tài liệu tham khảo**, **không sử dụng Phụ lục**. Định dạng Word: Times New Roman 13pt, giãn dòng 1.5, lề trên 3.5 cm / dưới 3 cm / trái 3.5 cm / phải 2 cm, số trang đánh ở giữa phía trên đầu trang.

---

## 6. Tiêu chí Đánh giá theo Rubric Học phần (502050)
| Tiêu chí đánh giá | Cột điểm | Trọng số | Mục tiêu chất lượng của nhóm |
|---|:---:|:---:|---|
| **Report Formatting** | Cột 1 | 10% | Trình bày chuẩn thể thức Chương 1–8, không lỗi chính tả, mục lục/danh mục tự động. |
| **Tài liệu Phân tích & Thiết kế** | Cột 1 | 20% | Đầy đủ khảo sát, 18 FR, 9 NFR, MoSCoW, 24 sơ đồ UML, 8 bảng CSDL, 10 Wireframe, RTM. |
| **Demo Giao diện / Prototype** | Cột 2 | 10% | 4 giao diện thực tế đúng bảng màu ACE Dark/Light, luồng điều hướng mượt mà. |
| **Demo Cài đặt bằng Java** | Cột 2 | 10% | Chạy thật 100% cả 7 Use Case trên trình duyệt, đúng công thức Quota, `++` và `DIRTY`. |
| **Presentation Skills** | Cột 2 | 10% | Slide tiếng Anh chuyên nghiệp (+0.25đ), Phong thuyết trình tự tin đúng 14 phút. |
| **Response to Questions** | Cột 2 | 20% | Nắm vững 20 câu hỏi phản biện về nghiệp vụ PMS, CSDL và kiến trúc code. |
| **Team Participation** | Cột 2 | 20% | Phân chia rõ ràng 4 trụ cột, đóng góp đồng đều 25% mỗi thành viên. |