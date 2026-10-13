# HỆ THỐNG TÀI LIỆU DỰ ÁN ACE (ACCOMMODATION, CHECK-IN & EXTRAS)

> **Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: `502050`) – Khoa Công nghệ Thông tin, Trường Đại học Tôn Đức Thắng.  
> **Đề tài:** Hệ thống Quản lý Khách sạn vừa và nhỏ (**ACE**).

---

## 1. Cấu trúc Thư mục Tài liệu (`docs/`)

Toàn bộ tài liệu phân tích, thiết kế, kế hoạch và kiểm thử của dự án được tổ chức thống nhất trong thư mục `docs/` theo thứ tự logic từ tổng quan đến chi tiết kỹ thuật:

### A. Tài liệu Nền tảng & Kế hoạch (`01`, `07`, `08`, Bản chốt)
| File | Nội dung chính | Phụ trách chính |
|---|---|---|
| [chot-pham-vi-actor-usecase-bang.md](file:///D:/ACE/docs/chot-pham-vi-actor-usecase-bang.md) | Văn bản chốt Phạm vi, 3 Actor, 7 Use Case, 8 bảng CSDL và 7 quy tắc nghiệp vụ (`BR1–BR7`) | Cả nhóm |
| [01-project-brief.md](file:///D:/ACE/docs/01-project-brief.md) | Tổng quan dự án, mục tiêu, phạm vi, ràng buộc, tiêu chí đánh giá theo Rubric và quản trị rủi ro | Cả nhóm |
| [07-ke-hoach-timeline.md](file:///D:/ACE/docs/07-ke-hoach-timeline.md) | Kế hoạch thực hiện 4 tuần (`M0–M6`), phân công chi tiết từng thành viên và lịch bàn giao | Cả nhóm |
| [08-bao-cao-tai-lieu-con-thieu.md](file:///D:/ACE/docs/08-bao-cao-tai-lieu-con-thieu.md) | Checklist 12 hạng mục đầu vào nghiệp vụ theo nguyên tắc *"Zero Guesswork"* (Đã hoàn thiện 100%) | Cả nhóm |

### B. Tài liệu Yêu cầu & Đặc tả Use Case / UML (`02`, `03`, `09`, `10`, `11`)
| File | Nội dung chính | Mục báo cáo |
|---|---|---|
| [02-feature-list-backlog.md](file:///D:/ACE/docs/02-feature-list-backlog.md) | Danh sách chức năng theo module (Product Backlog `FR-01`..`FR-18`) | Mục 2.2, 2.4 |
| [yeu-cau-fr-nfr-moscow.md](file:///D:/ACE/docs/yeu-cau-fr-nfr-moscow.md) | Danh sách 18 Yêu cầu chức năng (`FR`), 9 Yêu cầu phi chức năng (`NFR`) và bảng ưu tiên `MoSCoW` | Mục 2.4 |
| [03-user-story-acceptance.md](file:///D:/ACE/docs/03-user-story-acceptance.md) | Tóm tắt User Stories và Tiêu chí nghiệm thu (Acceptance Criteria) cho 7 Use Case | Mục 2.4, 2.5 |
| [09-dac-ta-use-case-he-thong-va-phong.md](file:///D:/ACE/docs/09-dac-ta-use-case-he-thong-va-phong.md) | Khảo sát hiện trạng, **Biểu đồ Use Case tổng quát**, **Biểu đồ Lớp (Class Diagram)**, Đặc tả chi tiết **`UC01`** & **`UC02`** | Mục 2.3, 2.5 |
| [10-dac-ta-use-case-dat-phong-checkin-out.md](file:///D:/ACE/docs/10-dac-ta-use-case-dat-phong-checkin-out.md) | Đặc tả chi tiết **`UC03` (Đặt phòng)**, **`UC04` (Check-in)**, **`UC05` (Check-out & Thanh toán)** kèm **Sequence & Activity Diagrams** | Mục 2.5 |
| [11-dac-ta-use-case-dich-vu-va-thong-ke.md](file:///D:/ACE/docs/11-dac-ta-use-case-dich-vu-va-thong-ke.md) | Đặc tả chi tiết **`UC06` (Dịch vụ đi kèm)** kèm **Sequence & Activity Diagrams** và **`UC07` (Thống kê doanh thu & công suất)** | Mục 2.5 |

### C. Tài liệu Thiết kế Giao diện, CSDL, Phân quyền & Kiểm thử (`04`, `05`, `06`, `12`, `13`, `14`)
| File | Nội dung chính | Mục báo cáo |
|---|---|---|
| [04-sitemap-wireframe.md](file:///D:/ACE/docs/04-sitemap-wireframe.md) | Sitemap điều hướng, Design Tokens và **Wireframe ASCII + Đặc tả trường/nút cho toàn bộ 15 màn hình (`S01`–`S15`)** | Mục 2.7 |
| [05-content-brief-landing.md](file:///D:/ACE/docs/05-content-brief-landing.md) | Nội dung chi tiết cho trang Landing giới thiệu nội bộ (`S01` – `/`) | Mục 2.7 |
| [06-tai-lieu-ky-thuat.md](file:///D:/ACE/docs/06-tai-lieu-ky-thuat.md) | Kiến trúc phân lớp Spring Boot, **Biểu đồ ERD 8 bảng**, **Từ điển dữ liệu (Data Dictionary)** và ràng buộc CSDL | Mục 2.5, 2.6 |
| [12-phan-quyen-rbac-trang-thai-thong-bao.md](file:///D:/ACE/docs/12-phan-quyen-rbac-trang-thai-thong-bao.md) | Bảng chốt 22 quyết định (`D1–D22`), **Bảng & Sơ đồ chuyển trạng thái**, Công thức Dashboard, **RBAC Matrix** và **35 Thông báo tiếng Việt** | Mục 2.4, 2.6, 2.7 |
| [13-kich-ban-demo-va-du-lieu-mau.md](file:///D:/ACE/docs/13-kich-ban-demo-va-du-lieu-mau.md) | Đặc tả bộ dữ liệu mẫu (`sample-data.sql`) và **Kịch bản thuyết trình & Demo trực tiếp 12–15 phút** cho 4 thành viên | Mục 2.8, 3.2, 4 |
| [14-kiem-thu-nghiep-vu-va-ma-tran-truy-vet.md](file:///D:/ACE/docs/14-kiem-thu-nghiep-vu-va-ma-tran-truy-vet.md) | **Ma trận truy vết 2 chiều (RTM)**, **30 Test Cases nghiệp vụ** và **20 Câu hỏi & Đáp án bảo vệ cuối kỳ** | Mục 2.8, 2.9 |

---

## 2. Thư mục Tài sản Thiết kế & Đề thi (`docs/assets/`)

* [01 FinalExam - 502050 - YeuCau.pdf](file:///D:/ACE/docs/assets/01%20FinalExam%20-%20502050%20-%20YeuCau.pdf): Đề thi báo cáo cuối kỳ chính thức và bảng Rubric chấm điểm.
* [ACE_Light_Mode.png](file:///D:/ACE/docs/assets/ACE_Light_Mode.png): Bảng màu & Component UI chuẩn của hệ thống ACE (Chế độ Sáng).
* [ACE_Dark_Mode.png](file:///D:/ACE/docs/assets/ACE_Dark_Mode.png): Bảng màu & Component UI chuẩn của hệ thống ACE (Chế độ Tối).
* [ke-hoach-4-tuan.png](file:///D:/ACE/docs/assets/ke-hoach-4-tuan.png): Sơ đồ tổng quan kế hoạch làm việc 4 tuần của nhóm.
* [phan-cong-chi-tiet-ban-giao.png](file:///D:/ACE/docs/assets/phan-cong-chi-tiet-ban-giao.png): Bảng phân công nhiệm vụ và lịch bàn giao chi tiết từng tuần.
* **Sơ đồ phân công chi tiết theo thành viên (`docs/assets/phan-cong/`)**:
  - [pc-1-chot-chung-ban-giao.png](file:///D:/ACE/docs/assets/phan-cong/pc-1-chot-chung-ban-giao.png): Kế hoạch chốt chung và mốc bàn giao.
  - [pc-2-viec-khoa-hoang.png](file:///D:/ACE/docs/assets/phan-cong/pc-2-viec-khoa-hoang.png): Phân công công việc của Khoa & Hoàng.
  - [pc-3-viec-phong-kiet.png](file:///D:/ACE/docs/assets/phan-cong/pc-3-viec-phong-kiet.png): Phân công công việc của Phong & Kiệt.
