# 08. Kế hoạch theo tuần (phần của Khoa)

Khung thời gian và phân công lấy từ hai hình kế hoạch của nhóm [CHỐT]; các mục có ghi **(thêm)** là phần mình bổ sung cho landing page, dashboard và design system [ĐỀ XUẤT].

Tuần tính từ thứ Hai: T1 12/10 đến 18/10, T2 19/10 đến 25/10, T3 26/10 đến 01/11, T4 02/11 đến 08/11. Hạn nộp 09/11 23g59.

---

## Tuần 1 (12 đến 18/10): Thu thập yêu cầu, làm UML, nhập vào doc

**Đầu tuần**
- [ ] Cả nhóm chốt: 3 actor, 7 use case, 5 chức năng sẽ code, tên các bảng chính, và 4 điểm ở mục 6 của file chốt (khách không tự đặt online; một đặt phòng một phòng; không phụ thu, giảm giá, thuế; tên bảng tiếng Anh).
- [ ] **Bàn giao:** tên các bảng chính → cả nhóm.
- [ ] (thêm) Nhờ Kiệt bổ sung wireframe **landing page** và **dashboard** vào mục 2.7 (gửi kèm `01` và `03`).
- [ ] (thêm) Chốt cùng nhóm các câu hỏi mở 1, 2, 3 trong `10-cau-hoi-mo.md` (cách tính số đêm, trùng lịch, trạng thái hiển thị).

**Trong tuần**
- [ ] ERD / Class Diagram: người dùng, loại phòng, phòng, khách hàng, đặt phòng, hóa đơn, dịch vụ, chi tiết dịch vụ.
- [ ] Mục 2.6: lược đồ quan hệ, khóa chính / khóa ngoại, ràng buộc, từ điển dữ liệu.
- [ ] Viết `schema.sql` và `sample-data.sql` (khoảng 10 phòng, 5 loại phòng, 10 khách, vài dịch vụ), dùng ngày tương đối.
- [ ] Đặc tả UC07 (thống kê doanh thu, công suất phòng).
- [ ] Dựng khung Spring Boot, kết nối H2 (nếu quá tải thì dời sang Tuần 2).
- [ ] (thêm, nếu còn thời gian) `tokens.css`, layout nền, nút đổi theme.
- [ ] Nhập mọi thứ vào doc.

**Cuối tuần**
- [ ] **Bàn giao:** ERD + từ điển dữ liệu trong doc → cả nhóm.
- [ ] **Nhận:** wireframe các màn hình + mục 2.7 (Kiệt); đặc tả UC của mình (Hoàng, Phong, Kiệt) để Khoa đọc và code.

Điểm liên quan: mục 2.5, 2.6, tài liệu 20%, demo Java 10%.

---

## Tuần 2 (19 đến 25/10): Chỉ Khoa code

Code theo wireframe của Kiệt và đặc tả use case. Chạy thử từng chức năng ngay khi xong, commit GitHub mỗi khi xong một phần.

Thứ tự làm (đi từ nền tảng đến phần ăn điểm demo):
- [ ] **1. Nền tảng:** dự án chạy, `schema.sql` và dữ liệu mẫu nạp được, entity + repository, layout, `tokens.css`, theme.
- [ ] **2. UC01:** đăng nhập, phân quyền, 403, tài khoản demo (`02`).
- [ ] **3. Dashboard:** khung và menu theo vai trò (`03`).
- [ ] **4. UC02:** phòng và loại phòng (S02 đến S04).
- [ ] **5. UC03:** tìm phòng trống, khách hàng, đặt phòng, danh sách, hủy (S05 đến S08).
- [ ] **6. UC04 + UC05:** check-in; check-out, thanh toán, hóa đơn (S09, S11).
- [ ] **7. UC06:** dịch vụ (S10).
- [ ] **8. Dashboard đầy đủ:** KPI, thẻ phòng, bảng hôm nay.
- [ ] **9. Landing page** (P1, timebox nửa đến một ngày, xem `01`). Nếu chưa kịp thì sang đầu Tuần 3.

**Mốc cắt giảm** (kế hoạch nhóm): nếu trễ thì bỏ dịch vụ, còn 4 chức năng (đề yêu cầu 3 đến 5).
(thêm) Thứ tự cắt trước đó, từ ít ảnh hưởng điểm nhất: nút Light mode → dữ liệu động của landing → landing page → KPI và thẻ phòng đẹp → in hóa đơn → **rồi mới** đến UC06. Lý do: xem `10-cau-hoi-mo.md` mục 5 về ngưỡng 80% chức năng.

Điểm liên quan: demo Java 10%, demo giao diện 10%.

---

## Tuần 3 (26/10 đến 01/11): Bàn giao code giữa tuần, làm báo cáo và slide

**Đầu tuần**
- [ ] Hoàn thiện chức năng còn lại; đối chiếu từng màn hình với wireframe; test các luồng chính; sửa lỗi.
- [ ] Landing page nếu chưa xong (P1).
- [ ] Viết README: cài JDK 21 và IntelliJ, cách chạy, tài khoản demo, nạp dữ liệu mẫu, nguồn tham khảo (`06`, mục 4).

**Giữa tuần**
- [ ] **Bàn giao:** code trên GitHub + README + tài khoản demo → Hoàng, Phong, Kiệt.

**Nửa sau tuần**
- [ ] Sửa lỗi theo phản hồi của 3 bạn khi chạy thử; đóng gói source trên GitHub (kèm file zip dự phòng).
- [ ] (thêm) Trả lời nhanh các lỗi khi ba bạn chụp ảnh màn hình và chạy minh họa cho mục 2.8, 2.9, 2.10.

**Cuối tuần**
- [ ] **Nhận:** doc ghép hoàn chỉnh (.docx + PDF) từ Kiệt để Khoa check; slide hoàn chỉnh để Khoa tập.

Điểm liên quan: demo Java 10%, demo giao diện 10%.

---

## Tuần 4 (02 đến 08/11): Ôn tập

- [ ] Chuẩn bị máy sẽ dùng khi báo cáo: cài JDK, chép app, **nạp lại dữ liệu mẫu sạch**.
- [ ] Tập demo trực tiếp theo demo script của Phong (`09`).
- [ ] Quay **video demo dự phòng** (phòng khi app lỗi lúc báo cáo).
- [ ] **Bàn giao:** video demo dự phòng → Kiệt (trong tuần) để bỏ vào slide.
- [ ] Tập hỏi đáp phần code và CSDL (danh sách câu hỏi gợi ý ở `09`).
- [ ] Cuối tuần: kiểm tra lại link repo / file nén sẵn sàng nộp.

Điểm liên quan: demo 20%, hỏi đáp 20%.

**Lưu ý:** ngày báo cáo thực tế chưa có. Nếu cô xếp sớm hơn 09/11 thì dời Tuần 4 lên tương ứng.

---

## Bảng bàn giao tổng hợp

| Khi nào | Ai → ai | Gửi gì |
|---|---|---|
| Đầu tuần 1 | Khoa → cả nhóm | Tên các bảng chính |
| Đầu tuần 1 | Khoa → Kiệt | (thêm) Yêu cầu wireframe landing và dashboard |
| Cuối tuần 1 | Kiệt → Khoa | Wireframe các màn hình + mục 2.7 |
| Cuối tuần 1 | Hoàng, Phong, Kiệt → Khoa | Đặc tả use case của mình trong doc |
| Cuối tuần 1 | Khoa → cả nhóm | ERD + từ điển dữ liệu trong doc |
| Giữa tuần 3 | Khoa → Hoàng, Phong, Kiệt | Code GitHub + README + tài khoản demo |
| Cuối tuần 3 | Kiệt → Hoàng, Phong, Khoa | Doc ghép hoàn chỉnh (.docx + PDF) |
| Trong tuần 4 | Khoa → Kiệt | Video demo dự phòng |
