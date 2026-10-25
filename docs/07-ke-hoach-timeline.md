# 07. KẾ HOẠCH THỰC HIỆN & LỊCH BÀN GIAO 4 TUẦN (CHƯƠNG 1–8 & 24 SƠ ĐỒ UML)

> **Đồng bộ 100% với bộ 4 ảnh phân công mới:** `ke-hoach-4-tuan.png`, `pc-1-chot-chung-ban-giao.png`, `pc-2-viec-khoa-hoang.png`, `pc-3-viec-phong-kiet.png`.  
> **Hạn cứng nộp báo cáo cuối kỳ:** **23:59 ngày 09/11/2026** trên hệ thống E-learning.

---

## 1. Bốn Trụ cột Phụ trách Chính của Nhóm
1. **KHOA:** **Code full** ứng dụng Web Java (4 phân hệ giao diện + 7 Use Case) + **Thao tác thiết bị khi Demo** + Viết **Chương 5 (CSDL)**, **Mục 4.2.7 (`UC07`)**, **Mục 4.3 (`Class Diagram`)**, **Mục 4.4 (`ERD`)** (Tổng: 5 sơ đồ UML).
2. **HOÀNG:** **Chủ trì quyển Báo cáo Word (`bao-cao.docx` / `bao-cao.pdf`):** Làm khung Word chuẩn Chương 1–8 (bỏ Phụ lục), viết **Phần đầu báo cáo**, **Chương 3**, **Mục 4.1 (`UC Tổng quát`)**, **Mục 4.2.2 (`UC02`)**, **Mục 4.2.3 (`UC03`)**, **Chương 7**, **Chương 8**, **Tài liệu tham khảo**, gộp toàn bộ bài và xuất PDF bản cuối để nộp (Tổng: 7 sơ đồ UML).
3. **PHONG:** **Thuyết trình full 12 slide (14 phút)** + Dẫn lời kịch bản Demo + Soạn 20 câu hỏi phản biện + Viết **Chương 1**, **Chương 2**, **Mục 4.2.4 (`UC04`)**, **Mục 4.2.5 (`UC05`)** (Tổng: 6 sơ đồ UML).
4. **KIỆT:** **Thiết kế full bộ Slide tiếng Anh (12 slide)** + Vẽ **10 Wireframe (`WF-01`..`WF-10`) & Sơ đồ điều hướng** + Viết **Chương 6**, **Mục 4.2.1 (`UC01`)**, **Mục 4.2.6 (`UC06`)** (Tổng: 6 sơ đồ UML + 11 hình giao diện).

---

## 2. Các Mốc Quan trọng (Milestones `M0` – `M6`)
| Mốc | Thời điểm | Nội dung bàn giao & Kiểm tra |
|:---:|---|---|
| **M0** | Đầu Tuần 1 (12–14/10) | Khoa gửi `quy-trinh-nghiep-vu-moi.md`; Hoàng gửi `bao-cao-khung.docx` (chia sẵn Chương 1 $\rightarrow$ Chương 8, không Phụ lục). |
| **M1** | Cuối Tuần 1 (18/10) | • **Kiệt $\rightarrow$ Khoa:** 10 Wireframe (`wf-01`..`wf-10`) + Đặc tả & Sơ đồ UC chi tiết `UC01`, `UC06`.<br>• **Hoàng $\rightarrow$ Khoa:** Đặc tả & Sơ đồ UC chi tiết `UC02`, `UC03` + `usecase-tong-quat.png`.<br>• **Phong $\rightarrow$ Khoa:** Đặc tả & Sơ đồ UC chi tiết `UC04`, `UC05`.<br>• **Khoa $\rightarrow$ Cả nhóm:** `erd.png`, `class-diagram.png`, `uc-07.png`, `schema.sql`, `sample-data.sql`. |
| **M2** | Cuối Tuần 2 (25/10) | • **Khoa:** Code xong các luồng cốt lõi của 4 phân hệ trên Spring Boot.<br>• **Hoàng, Phong, Kiệt:** Vẽ xong toàn bộ **7 Activity Diagrams + 7 Sequence Diagrams**.<br>• **Phong & Kiệt $\rightarrow$ Hoàng:** Bàn giao toàn bộ file Word phần mình (`Chương 1, 2, 4, 6`) để từ Tuần 3 Phong tập trung Thuyết trình, Kiệt tập trung Slide tiếng Anh. |
| **M3** | Giữa Tuần 3 (~29/10) | **Khoa bàn giao Code trên GitHub + `README.md` + Tài khoản Demo 3 Role**; đồng thời gửi `chuong-4-uc07-khoa.docx`, `chuong-5-csdl-khoa.docx` và 5 sơ đồ của Khoa cho Hoàng. |
| **M4** | Cuối Tuần 3 (01/11) | • **Hoàng:** Chụp ảnh app hoàn thiện **Chương 7, Chương 8** và **gộp xong toàn bộ `bao-cao.docx` (Chương 1 $\rightarrow$ Chương 8)** gửi cả nhóm xem.<br>• **Kiệt:** Hoàn thiện **12 Slide tiếng Anh (`presentation-en.pptx`)**.<br>• **Phong:** Chốt `demo-script.docx`, `kich-ban-thuyet-trinh.docx` và `cau-hoi-du-kien.docx` (20 câu). |
| **M5** | Đầu Tuần 4 (03/11) | **Khoa** gửi `demo-du-phong.mp4` cho **Kiệt** nhúng vào Slide 10; Phong và Khoa bấm giờ tập thuyết trình + demo 3 lần (14 phút). |
| **M6** | **09/11/2026 (23:59)** | **Hoàng** xuất bản cuối `bao-cao.docx` & `bao-cao.pdf`; nộp đầy đủ Báo cáo, Slide tiếng Anh và Source Code lên E-learning. |

---

## 3. Phân công Công việc Chi tiết theo Thành viên và Tuần

| Thành viên | Tuần 1 (12–18/10) | Tuần 2 (19–25/10) | Tuần 3 (26/10–01/11) | Tuần 4 (02–08/11) |
|---|---|---|---|---|
| **KHOA**<br>*(Code full, Demo, Chương 5, UC07, ERD, Class)* | • Chốt `quy-trinh-nghiep-vu-moi.md`<br>• Vẽ **Mục 4.3 (`Class Diagram`)** & **Mục 4.4 (`ERD`)**<br>• Viết **Chương 5 (CSDL 8 bảng)**<br>• Viết `schema.sql`, `sample-data.sql`<br>• Viết **Mục 4.2.7 (`UC07`)** + vẽ `uc-07`, `act-uc07`, `seq-uc07` | • Dựng project Spring Boot + Security 3 Role<br>• **Code full** 4 phân hệ giao diện & 7 Use Case (`UC01` $\rightarrow$ `UC07`); commit GitHub mỗi ngày | • Hoàn thiện, test kỹ Quota, công thức `++` & `DIRTY`<br>• **Bàn giao code + `README.md` giữa tuần**<br>• Gửi file doc Chương 4, Chương 5 & 5 sơ đồ cho Hoàng<br>• Sửa lỗi theo phản hồi | • Chuẩn bị máy demo, nạp dữ liệu mẫu sạch<br>• Quay `demo-du-phong.mp4` gửi Kiệt<br>• Điền 8 đáp án kỹ thuật<br>• **Tập thao tác thiết bị demo cùng Phong (3 lần)** |
| **HOÀNG**<br>*(Chủ trì Doc: Phần đầu, Chương 3, UC02–03, Chương 7–8, Gộp bài)* | • Tạo `bao-cao-khung.docx` (Chương 1–8, không Phụ lục)<br>• Viết **Chương 3 (BR1–7, 18 FR, 9 NFR, MoSCoW)**<br>• Vẽ **Mục 4.1 (`UC Tổng quát`)**<br>• Viết **Mục 4.2.2 (`UC02`)** & **4.2.3 (`UC03`)** + vẽ `uc-02`, `uc-03` | • Vẽ 4 sơ đồ: `act-uc02`, `seq-uc02`, `act-uc03`, `seq-uc03`<br>• Làm **Phần đầu báo cáo** (Bìa, Mục lục, Danh mục) & **Tài liệu tham khảo**<br>• Soạn trước **Mục 7.1, 7.2, 7.4 (RTM)** | • Chạy app chụp ảnh hoàn thiện **Chương 7 (`7.1`–`7.4`)**<br>• Viết trọn vẹn **Chương 8 – Kết luận (`8.1`–`8.3`)**<br>• **Gộp toàn bộ Chương 1–8 và 24 sơ đồ UML thành `bao-cao.docx`** | • **Rà soát `bao-cao.docx` theo 6 tiêu chí, cập nhật mục lục và xuất `bao-cao.pdf` bản cuối để nộp E-learning**<br>• Ôn tập 20 câu hỏi đáp |
| **PHONG**<br>*(Thuyết trình full 12 slide, Chương 1–2, UC04–05, Kịch bản)* | • Viết **Chương 1 (Giới thiệu & Phạm vi)**<br>• Viết **Chương 2 (Khảo sát hiện trạng)**<br>• Viết **Mục 4.2.4 (`UC04`)** & **4.2.5 (`UC05`)** + vẽ `uc-04`, `uc-05` | • Vẽ 4 sơ đồ: `act-uc04`, `seq-uc04`, `act-uc05`, `seq-uc05`<br>• **Gửi toàn bộ Chương 1, 2, UC04, UC05 & 6 sơ đồ cho Hoàng**<br>• Viết kịch bản thuyết trình slide 1–8 & demo script bản 1 | • Chạy thử app, chốt `demo-script.docx` bản cuối<br>• Hoàn chỉnh `kich-ban-thuyet-trinh.docx` (12 slide, 14 phút)<br>• Soạn `cau-hoi-du-kien.docx` (20 câu hỏi & đáp án) | • **Một mình thuyết trình toàn bộ 12 slide (14 phút), tập khớp demo với Khoa 3 lần**<br>• Điều phối cả nhóm ôn 20 câu hỏi đáp |
| **KIỆT**<br>*(Slide tiếng Anh full, Chương 6, 10 Wireframe, UC01 & UC06)* | • Vẽ **10 Wireframe (`WF-01`..`WF-10`)**<br>• Viết **Mục 4.2.1 (`UC01`)** & **Mục 4.2.6 (`UC06`)** + vẽ `uc-01`, `uc-06` | • Viết **Chương 6 (`6.1`–`6.3`)** + vẽ `dieu-huong.png`<br>• Vẽ 4 sơ đồ: `act-uc01`, `seq-uc01`, `act-uc06`, `seq-uc06`<br>• **Gửi toàn bộ UC01, UC06, Chương 6, 10 WF & 6 sơ đồ cho Hoàng**<br>• Làm Slide tiếng Anh 1–8 | • Chạy app chụp ảnh giao diện thực tế<br>• **Làm tiếp Slide 9–12, hoàn thiện đủ 12 Slide tiếng Anh (`presentation-en.pptx`)** | • Chèn `demo-du-phong.mp4` vào Slide 10<br>• **Check tiếng Anh & xuất `presentation-en.pptx` / `.pdf` bản cuối**<br>• Ôn tập 20 câu hỏi đáp |