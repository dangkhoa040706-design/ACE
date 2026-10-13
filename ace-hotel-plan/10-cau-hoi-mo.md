# 10. Câu hỏi mở và điểm cần chốt

Danh sách các chỗ còn lệch hoặc chưa rõ giữa đề, file chốt, hai hình kế hoạch, hình ACE Color System và plan này. Mỗi mục có **đề xuất mặc định**: nếu chưa ai phản đối thì cứ làm theo đề xuất.

| # | Vấn đề | Đề xuất mặc định | Ai quyết |
|---|---|---|---|
| 1 | **Số đêm tính theo ngày đã đặt hay ngày trả thực tế?** FR-13 chỉ ghi "số đêm × giá". | Theo ngày đã đặt (`check_out_date − check_in_date`, tối thiểu 1); trả sớm vẫn tính đủ. Đơn giản và không cần phụ thu hay hoàn tiền | Nhóm; Hoàng kiểm lại FR-13 |
| 2 | **Thế nào là trùng lịch?** Rule 2 nói "chưa hủy". | Khoảng nửa mở `[nhận, trả)`: trả ngày 15 thì đặt nhận ngày 15 được. Đặt phòng đã **CHECKED_OUT** không tính là trùng (để phòng trả sớm được bán lại) | Nhóm |
| 3 | **Trạng thái hiển thị khác CSDL.** Hình màu có Đã đặt, Quá hạn, Đang dọn; CSDL chỉ có AVAILABLE, OCCUPIED, MAINTENANCE. | Suy ra Đã đặt và Quá hạn từ `bookings`; đổi "Đang dọn / bảo trì" thành "Bảo trì". Nếu muốn có "Đang dọn" thật thì thêm giá trị vào `rooms.status` và cập nhật ERD, từ điển dữ liệu | Khoa + nhóm (trước khi chốt ERD) |
| 4 | **"Late check-out" trong biểu mẫu mẫu.** File chốt loại trừ phụ thu check-out trễ. | Bỏ khỏi hình hoặc đổi nhãn thành thứ không ảnh hưởng tiền. Muốn giữ thì phải chốt lại phạm vi | Khoa + nhóm |
| 5 | **Ngưỡng "≥ 80% chức năng chính" tính trên mẫu số nào?** Đề không nói rõ. | Hỏi cô. Nếu mẫu số là 7 use case: code UC01 đến UC06 = 6/7 ≈ 86% (Xuất sắc); cắt UC06 còn 5/7 ≈ 71% (Tốt). Vì vậy cắt landing và phần trang trí **trước** UC06 | Hỏi cô |
| 6 | **Phần trình bày của từng người.** Đề bắt buộc mỗi thành viên trình bày ít nhất một phần; kế hoạch hiện giao Phong toàn bộ thuyết trình, Khoa demo, còn Hoàng và Kiệt chưa có phần nói. | Chia ví dụ: Phong (mở đầu, khảo sát, phạm vi) · Hoàng (yêu cầu, use case, UML) · Khoa (CSDL + demo) · Kiệt (giao diện, kết luận, hướng phát triển) | Nhóm |
| 7 | **README trước hay sau bàn giao?** Hình phân công chi tiết xếp README trong gói bàn giao giữa Tuần 3; hình kế hoạch 4 tuần xếp README ở nửa sau tuần 3. | Viết **trước hoặc cùng lúc** bàn giao (ba bạn cần nó để chạy được); cập nhật thêm sau nếu có phản hồi | Khoa |
| 8 | **Wireframe cho landing page và dashboard.** Danh sách wireframe hiện tại của Kiệt không có hai màn hình này, trong khi đề yêu cầu demo đúng thiết kế trong báo cáo. | Nhờ Kiệt thêm vào mục 2.7 ngay đầu Tuần 1, dựa trên `01` và `03` | Khoa → Kiệt |
| 9 | **Ai quản lý danh mục dịch vụ (FR-12)?** Nháp chưa nêu. | Quản lý | Nhóm; Hoàng ghi vào FR |
| 10 | **Check-in sớm hoặc khác ngày dự kiến.** | Cho phép, kèm cảnh báo | Nhóm |
| 11 | **Số giấy tờ khách có bắt buộc, duy nhất không?** | Không bắt buộc; nếu nhập thì duy nhất | Nhóm |
| 12 | **Ô KPI "Dịch vụ đi kèm" đếm gì?** | Số dòng dịch vụ ghi nhận trong hôm nay | Khoa |
| 13 | **Landing page và dashboard xuất hiện thế nào trong báo cáo?** Không thuộc 7 use case. | Mô tả là màn hình phụ ở 2.7 và 2.8; không thêm use case, không đổi số 7 | Hoàng, Kiệt |
| 14 | **Ảnh và font trên landing page.** Đề bắt buộc khai báo nguồn. | Ưu tiên ảnh tự tạo; mọi thứ bên ngoài đưa vào tài liệu tham khảo | Khoa |
| 15 | **Ngày báo cáo thực tế** chưa có. | Giữ lịch hiện tại; dời Tuần 4 nếu cô xếp sớm | Hỏi cô |
