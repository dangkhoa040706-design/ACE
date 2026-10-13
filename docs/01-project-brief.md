# 01. PROJECT BRIEF – HỆ THỐNG QUẢN LÝ KHÁCH SẠN (ACE)

> Môn 502050 – Phân tích và Thiết kế Yêu cầu, ĐH Tôn Đức Thắng. Bản nháp, cả nhóm chốt ở đầu Tuần 1.

## 1. Tổng quan
| Mục | Nội dung |
|---|---|
| Tên dự án | Hệ thống quản lý khách sạn **ACE** (Accommodation, Check-in & Extras) |
| Loại | Bài thi báo cáo cuối kỳ, làm nhóm (Đề tài 1) |
| Nhóm | Khoa (web + demo), Hoàng (phân tích), Phong (thuyết trình), Kiệt (slide + định dạng) |
| Hạn nộp quyển báo cáo | **23:59 ngày 09/11/2026** (nộp .docx và/hoặc PDF lên E-learning) |
| Nộp kèm | Slide, source Java (zip hoặc link GitHub), link/file prototype |
| Báo cáo & demo | 12-15 phút trình bày + demo, 5-8 phút hỏi đáp |
| Tỷ trọng | 50% điểm học phần (Cột 1 báo cáo 30%, Cột 2 demo 70%) |

## 2. Mục tiêu
1. Số hóa nghiệp vụ lễ tân của một khách sạn vừa và nhỏ: đặt phòng, check-in, check-out, dịch vụ, thanh toán.
2. Chứng minh quy trình phân tích: khảo sát → yêu cầu → UML → CSDL → giao diện → cài đặt Java, có **truy vết** từ yêu cầu đến demo.
3. Cài đặt chạy thật tối thiểu 3-5 chức năng bằng Java (nhóm chọn **5 chức năng**).

## 3. Đối tượng người dùng
| Vai trò | Mô tả |
|---|---|
| Lễ tân | Thao tác hằng ngày tại quầy |
| Quản lý | Quyền lễ tân + quản lý phòng/loại phòng + thống kê |
| Khách hàng | Tác nhân ngoài, không thao tác trực tiếp trên hệ thống |

## 4. Phạm vi
**LÀM:** quản lý loại phòng/phòng; tìm phòng trống và đặt phòng; check-in/check-out; dịch vụ đi kèm; thanh toán và hóa đơn; đăng nhập, phân quyền; thống kê doanh thu và công suất (chỉ đặc tả).

**KHÔNG LÀM:** khách tự đặt online/thanh toán online; kết nối Booking, Agoda; nhân sự, lương, kế toán, thuế; kho nhà hàng; nhiều chi nhánh; ứng dụng di động; giảm giá, phụ thu; đặt nhiều phòng trong một đơn.

## 5. Ràng buộc
- Công nghệ: Java (Spring Boot + Thymeleaf + H2), JDK 21.
- Chỉ **Khoa** code (Tuần 2); bàn giao code giữa Tuần 3.
- Định dạng báo cáo: Times New Roman 12-13pt, giãn dòng 1.5, lề trên 3.5 / dưới 3 / trái 3.5 / phải 2 cm, số trang giữa đầu trang.
- Khai báo nguồn tham khảo (thư viện, mẫu thiết kế, hình ảnh bên ngoài).

## 6. Tiêu chí thành công (theo rubric)
| Tiêu chí | Trọng số |
|---|---|
| Report Formatting | 10% (cột 1) |
| Tài liệu phân tích và thiết kế | 20% (cột 1) |
| Demo giao diện/prototype | 10% (cột 2) |
| Demo Java (≥80% chức năng chính để đạt mức xuất sắc) | 10% (cột 2) |
| Presentation Skills | 10% |
| Response to Questions | 20% |
| Team Participation | 20% |

## 7. Rủi ro chính
| Rủi ro | Biện pháp |
|---|---|
| Khoa code một mình, trễ tiến độ | Mốc cắt giảm cuối Tuần 2: bỏ UC06, còn 4 chức năng |
| Doc, wireframe, code bị lệch tên | Chốt tên actor/UC/bảng ngày đầu Tuần 1 |
| App lỗi lúc demo | Quay video demo dự phòng (Tuần 4) |
| Hỏi đáp: không giải thích được phần người khác | Mỗi người đọc hết doc, tập bộ ~20 câu hỏi |

## 8. Xác nhận
- [x] Cả nhóm đồng ý phạm vi mục 4 và 4 điểm chốt trong `chot-pham-vi-actor-usecase-bang.md` cùng 22 quyết định chi tiết trong `12-phan-quyen-rbac-trang-thai-thong-bao.md`.
- [x] Đề tài số 1 (Hệ thống quản lý khách sạn ACE) nằm trong danh sách đề tài chuẩn của học phần 502050.
