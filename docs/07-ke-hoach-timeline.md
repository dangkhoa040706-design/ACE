# 07. KẾ HOẠCH VÀ TIMELINE (4 TUẦN)

Tổng hợp từ [ke-hoach-4-tuan.png](file:///D:/ACE/docs/assets/ke-hoach-4-tuan.png) và [phan-cong-chi-tiet-ban-giao.png](file:///D:/ACE/docs/assets/phan-cong-chi-tiet-ban-giao.png). **Hạn nộp báo cáo: 23:59 09/11/2026.** Tuần 4 là tuần ôn tập; nếu cô xếp lịch báo cáo sớm hơn 09/11 thì dời Tuần 4 tương ứng.

## 1. Mốc quan trọng
| Mốc | Thời điểm | Nội dung |
|---|---|---|
| M0 | Đầu Tuần 1 (12/10) | Chốt actor, 7 UC, tên 8 bảng, Kiệt gửi khung Word |
| M1 | Cuối Tuần 1 (18/10) | Kiệt gửi wireframe + mục 2.7; Hoàng, Phong, Kiệt gửi đặc tả UC; Khoa gửi ERD + từ điển dữ liệu |
| M2 | Cuối Tuần 2 (25/10) | Mốc cắt giảm: trễ thì bỏ UC06, còn 4 chức năng (đề yêu cầu 3-5) |
| M3 | Giữa Tuần 3 (~29/10) | **Khoa bàn giao code + README + tài khoản demo** |
| M4 | Cuối Tuần 3 (01/11) | Hoàng, Phong gửi mục 2.8, 2.9, 2.10 + ma trận truy vết → Kiệt ghép doc; Kiệt gửi doc hoàn chỉnh (.docx + PDF) và slide |
| M5 | Trong Tuần 4 | Khoa gửi video demo dự phòng cho Kiệt |
| M6 | 09/11 23:59 | Nộp báo cáo (.docx/PDF), slide, source, link prototype |

## 2. Công việc theo người và tuần
| Người | Tuần 1 (12-18/10) | Tuần 2 (19-25/10) | Tuần 3 (26/10-01/11) | Tuần 4 (02-08/11) |
|---|---|---|---|---|
| **Khoa** (web + demo) | Mục 2.5, 2.6; ERD, từ điển dữ liệu; schema.sql, sample-data.sql; đặc tả UC07; dựng khung Spring Boot + H2 | **Chỉ Khoa code** 5 chức năng; commit mỗi ngày | Hoàn thiện, test; **bàn giao code giữa tuần**; sửa lỗi theo phản hồi; đóng gói | Chuẩn bị máy demo; quay video dự phòng; tập hỏi đáp code/CSDL |
| **Hoàng** (phân tích) | Mục 2.4; Use Case tổng quát; đặc tả UC02, UC03 + sequence/activity UC03 | Rà soát nhất quán mục 2.2-2.7; ma trận truy vết nháp; ~10 câu hỏi dự kiến | Chạy app, viết mục 2.8; hoàn chỉnh ma trận truy vết | Check toàn bộ doc (chính tả, định dạng, nhất quán) |
| **Phong** (thuyết trình) | Mục 2.2, 2.3; đặc tả UC04, UC05 + sequence/activity UC05 | Kịch bản thuyết trình nháp; demo script nháp theo wireframe | Chốt demo script; mục 2.9, 2.10 (phân công, MSSV, tỷ lệ, chữ ký); ~20 câu hỏi dự kiến | Ôn thuyết trình, bấm giờ 12-15 phút kèm demo |
| **Kiệt** (slide + định dạng) | Khung Word chuẩn; wireframe + mục 2.7; đặc tả UC01, UC06 + sequence/activity UC06 | Bìa, mục lục, danh mục hình/bảng, tài liệu tham khảo; slide tiếng Anh phần phân tích/thiết kế | Chèn ảnh demo, hoàn chỉnh slide; ghép doc, rà chính tả, xuất PDF | Check slide (nội dung, tiếng Anh, khớp demo) |

## 3. Bàn giao theo tuần
| Tuần | Ai → ai | Gửi gì | Khi nào |
|---|---|---|---|
| 1 | Kiệt → cả nhóm | Khung Word chuẩn | Ngày đầu |
| 1 | Khoa → cả nhóm | Tên bảng; ERD + từ điển dữ liệu | Đầu tuần; cuối tuần |
| 1 | Kiệt → Khoa | Wireframe + mục 2.7 | Cuối tuần |
| 1 | Hoàng, Phong, Kiệt → Khoa | Đặc tả UC01-UC06 | Cuối tuần |
| 3 | Khoa → Hoàng, Phong, Kiệt | Code GitHub + README + tài khoản demo | Giữa tuần |
| 3 | Hoàng, Phong → Kiệt | Mục 2.8 + ma trận (Hoàng); 2.9, 2.10 (Phong) | Cuối tuần |
| 3 | Kiệt → Hoàng, Phong, Khoa | Doc ghép (.docx + PDF); slide | Cuối tuần |
| 4 | Khoa → Kiệt | Video demo dự phòng | Trong tuần |

## 4. Phân bổ theo rubric
| Tiêu chí | Người chính |
|---|---|
| Report Formatting 10% | Kiệt (+ Hoàng check) |
| Tài liệu phân tích, thiết kế 20% | Hoàng, Phong, Kiệt, Khoa |
| Demo giao diện 10% / Demo Java 10% | Khoa (Phong dẫn kịch bản) |
| Presentation Skills 10% | Phong (cả nhóm đều phải trình bày một phần) |
| Response to Questions 20% | Cả nhóm (mỗi người trả lời được phần của mình **và** phần chung) |
| Team Participation 20% | Cả nhóm, đóng góp đều |

## 5. Lưu ý
- Đề yêu cầu **mọi thành viên đều trình bày một phần**: kế hoạch hiện tập trung thuyết trình ở Phong, cần chia phần nói cho Khoa, Hoàng, Kiệt.
- Trong Tuần 2 chỉ Khoa code, ba bạn còn lại nên dùng thời gian trống để rà soát và tập, tránh chỉnh yêu cầu muộn gây lệch với code.
