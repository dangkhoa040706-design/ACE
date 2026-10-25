# HỆ THỐNG HỒ SƠ PHÂN TÍCH & THIẾT KẾ DỰ ÁN ACE HOTEL (MÔN 502050)

> **Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: `502050`) – Khoa Công nghệ Thông tin, Trường Đại học Tôn Đức Thắng.  
> **Đề tài 1:** Hệ thống Quản lý Khách sạn & Đặt phòng Trực tuyến **ACE Hotel** (*Accommodation, Check-in & Extras*).  
> **Trạng thái:** Đã đồng bộ 100% theo **Quy trình Nghiệp vụ Mới (PMS & Online Booking Portal)** và cấu trúc báo cáo **Chương 1 – Chương 8 (Không Phụ lục)**.

---

## 1. Bản đồ Ánh xạ Tài liệu trong `docs/` vào 8 Chương Quyển Báo cáo (`bao-cao.docx`)

### A. Tài liệu Định hướng Quy trình, Mục lục & Kế hoạch Nhóm
| File trong `docs/` | Nội dung cốt lõi | Người phụ trách |
|---|---|:---:|
| [`quy-trinh-nghiep-vu-moi.md`](./quy-trinh-nghiep-vu-moi.md) | **Tài liệu gốc quy trình mới:** 3 Actor, 4 Phân hệ Giao diện, 7 Use Case, Đặt phòng theo Quota hạng phòng, Cọc Online 30/70 vs Walk-in 0/100, Room Folio 4 nhóm, Biểu giá `++`, Vòng đời phòng `DIRTY` và DDL 8 bảng CSDL | Khoa + Cả nhóm |
| [`00-muc-luc-va-huong-dan-7-uc.md`](./00-muc-luc-va-huong-dan-7-uc.md) | **Mục lục chi tiết Chương 1 $\rightarrow$ Chương 8 (bỏ Phụ lục)** và Cẩm nang hướng dẫn vẽ bộ **24 sơ đồ UML** cho từng thành viên | Khoa + Hoàng |
| [`chot-pham-vi-actor-usecase-bang.md`](./chot-pham-vi-actor-usecase-bang.md) | Văn bản chốt Phạm vi, 3 Actor, 7 Use Case, 24 Sơ đồ UML, 8 Bảng CSDL và 7 Quy tắc nghiệp vụ (`BR1–BR7`) | Cả nhóm |
| [`01-project-brief.md`](./01-project-brief.md) | Tổng quan dự án, phân vai 4 trụ cột (Khoa, Hoàng, Phong, Kiệt), ràng buộc kỹ thuật và tiêu chí Rubric | Cả nhóm |
| [`07-ke-hoach-timeline.md`](./07-ke-hoach-timeline.md) | Kế hoạch thực hiện 4 tuần (`M0–M6`), lịch bàn giao các Chương và bộ 24 sơ đồ UML | Cả nhóm |
| [`08-bao-cao-tai-lieu-con-thieu.md`](./08-bao-cao-tai-lieu-con-thieu.md) | Bảng chốt 22 quyết định thiết kế (`D1–D22`), bảng đồng bộ trạng thái và checklist đầu vào để Khoa code (Hoàn tất 100%) | Cả nhóm |

---

### B. Tài liệu Nội dung Từng Chương của Quyển Báo cáo (Chương 1 $\rightarrow$ Chương 8)
| Chương trong Báo cáo | File Nguồn trong `docs/` | Nội dung chi tiết | Phụ trách viết Word |
|---|---|---|:---:|
| **Chương 1 & Chương 2**<br>*(Giới thiệu, Phạm vi & Khảo sát hiện trạng)* | • [`01-project-brief.md`](./01-project-brief.md)<br>• [`09-dac-ta-use-case-he-thong-va-phong.md`](./09-dac-ta-use-case-he-thong-va-phong.md) *(Mục 1)* | Lý do chọn đề tài, mục tiêu, phạm vi 2 phân hệ, stakeholders, quy trình hiện tại và 5 bài toán cần giải quyết | **Phong**<br>(`chuong-1-2-phong.docx`) |
| **Chương 3**<br>*(Xác định & Đặc tả Yêu cầu)* | • [`yeu-cau-fr-nfr-moscow.md`](./yeu-cau-fr-nfr-moscow.md)<br>• [`02-feature-list-backlog.md`](./02-feature-list-backlog.md)<br>• [`03-user-story-acceptance.md`](./03-user-story-acceptance.md) | 7 quy tắc nghiệp vụ (`BR1–BR7`), 18 yêu cầu chức năng (`FR-01`..`FR-18`), 9 yêu cầu phi chức năng (`NFR-01`..`NFR-09`) và bảng MoSCoW | **Hoàng**<br>(`chuong-3-hoang.docx`) |
| **Chương 4**<br>*(Mô hình hóa Yêu cầu bằng UML – 24 Sơ đồ)* | • [`09-dac-ta-use-case-he-thong-va-phong.md`](./09-dac-ta-use-case-he-thong-va-phong.md) *(UC tổng quát, UC01, UC02)*<br>• [`10-dac-ta-use-case-dat-phong-checkin-out.md`](./10-dac-ta-use-case-dat-phong-checkin-out.md) *(UC03, UC04, UC05)*<br>• [`11-dac-ta-use-case-dich-vu-va-thong-ke.md`](./11-dac-ta-use-case-dich-vu-va-thong-ke.md) *(UC06, UC07)*<br>• [`06-tai-lieu-ky-thuat.md`](./06-tai-lieu-ky-thuat.md) *(Class Diagram, ERD)* | • **Mục 4.1:** Biểu đồ Use Case tổng quát (Hoàng)<br>• **Mục 4.2.1 – 4.2.7:** 7 Use Case chính, mỗi UC gồm Sơ đồ UC chi tiết + Bảng đặc tả 9 dòng + Activity Diagram + Sequence Diagram (Kiệt, Hoàng, Phong, Khoa)<br>• **Mục 4.3 & 4.4:** Class Diagram & ERD 8 bảng (Khoa) | **Cả 4 thành viên**<br>(Gửi về Hoàng gộp) |
| **Chương 5**<br>*(Thiết kế Cơ sở dữ liệu)* | • [`06-tai-lieu-ky-thuat.md`](./06-tai-lieu-ky-thuat.md) *(Mục 5 & Mục 6)* | Lược đồ quan hệ 8 bảng, Từ điển dữ liệu chi tiết (`Data Dictionary`), ràng buộc toàn vẹn và các câu truy vấn SQL trọng tâm | **Khoa**<br>(`chuong-5-csdl-khoa.docx`) |
| **Chương 6**<br>*(Thiết kế Giao diện Người dùng)* | • [`04-sitemap-wireframe.md`](./04-sitemap-wireframe.md)<br>• [`05-content-brief-landing.md`](./05-content-brief-landing.md)<br>• [`12-phan-quyen-rbac-trang-thai-thong-bao.md`](./12-phan-quyen-rbac-trang-thai-thong-bao.md) | Hệ thống màu `ACE Color System` (Dark/Light Mode), Sơ đồ điều hướng 4 phân hệ (`dieu-huong.png`), Ma trận phân quyền RBAC 3 Role và 10 Wireframe (`WF-01`..`WF-10`) | **Kiệt**<br>(`chuong-6-giao-dien-kiet.docx`) |
| **Chương 7 & Chương 8**<br>*(Kết quả Prototype, Cài đặt Java, RTM & Kết luận)* | • [`13-kich-ban-demo-va-du-lieu-mau.md`](./13-kich-ban-demo-va-du-lieu-mau.md)<br>• [`14-kiem-thu-nghiep-vu-va-ma-tran-truy-vet.md`](./14-kiem-thu-nghiep-vu-va-ma-tran-truy-vet.md) | Kiến trúc kỹ thuật Spring Boot, bộ dữ liệu mẫu (`sample-data.sql`), kịch bản Demo 14 phút, ảnh chụp màn hình thực tế, **Ma trận truy vết 2 chiều RTM (Mục 7.4)**, 30 Test Cases và Kết luận (Chương 8) | **Hoàng**<br>(`chuong-7-8-hoang.docx` & Gộp toàn quyển `bao-cao.docx`) |

---

## 2. Danh mục Tài sản Thiết kế, Đề thi & Bảng Phân công (`docs/assets/`)

* [`01 FinalExam - 502050 - YeuCau.pdf`](./assets/01%20FinalExam%20-%20502050%20-%20YeuCau.pdf): Đề thi báo cáo cuối kỳ chính thức và bảng Rubric chấm điểm.
* [`ACE_Dark_Mode.png`](./assets/ACE_Dark_Mode.png) & [`ACE_Light_Mode.png`](./assets/ACE_Light_Mode.png): Bảng màu & Component UI chuẩn của hệ thống ACE Hotel.
* [`phieu-xac-nhan-dat-phong-mau.png`](./assets/phieu-xac-nhan-dat-phong-mau.png): Mẫu Phiếu xác nhận đặt phòng trực tuyến kèm mã QR Code.
* [`ke-hoach-4-tuan.png`](./assets/ke-hoach-4-tuan.png): Bảng tổng quan kế hoạch 4 tuần (Chương 1–8 & 24 sơ đồ UML).
* **Bộ ảnh phân công chi tiết (`docs/assets/phan-cong/`):**
  - [`pc-1-chot-chung-ban-giao.png`](./assets/phan-cong/pc-1-chot-chung-ban-giao.png): Quy ước chung, danh sách 24 sơ đồ UML, lịch bàn giao theo Chương và kịch bản 12 slide.
  - [`pc-2-viec-khoa-hoang.png`](./assets/phan-cong/pc-2-viec-khoa-hoang.png): Phân công chi tiết từng tuần của **Khoa** và **Hoàng**.
  - [`pc-3-viec-phong-kiet.png`](./assets/phan-cong/pc-3-viec-phong-kiet.png): Phân công chi tiết từng tuần của **Phong** và **Kiệt**.