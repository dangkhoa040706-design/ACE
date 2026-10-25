# 14. MA TRẬN TRUY VẾT YÊU CẦU (RTM – MỤC 7.4), 30 TEST CASES NGHIỆP VỤ & 20 CÂU HỎI ĐÁP BẢO VỆ

> **Ánh xạ Báo cáo:**  
> - **Chương 7 (Mục 7.4):** Ma trận truy vết yêu cầu hai chiều (RTM) và Kết quả kiểm thử nghiệp vụ *(Phụ trách: Hoàng – đưa trực tiếp vào Chương 7, không tách Phụ lục theo yêu cầu của giảng viên)*.  
> - **Ôn tập Bảo vệ (Tuần 4):** Bộ 20 câu hỏi phản biện chuyên sâu về quy trình PMS mới dành cho cả 4 thành viên *(Phụ trách điều phối: Phong & Khoa)*.

---

## 1. Ma trận Truy vết Yêu cầu Hai Chiều (RTM – Chương 7, Mục 7.4)

| Mã FR | Tên yêu cầu chức năng | MoSCoW | Mã UC | Quy tắc (`BR`) | Bộ Sơ đồ UML (Chương 4) | Bảng CSDL (Chương 5) | Màn hình (Chương 6) | Controller (Java) | Test Case | Bước Demo |
|:---:|---|:---:|:---:|:---:|---|---|---|---|---|:---:|
| **FR-01** | Đăng ký tài khoản khách hàng mới (5 trường) | Must | `UC01` | — | `uc-01`, `act-uc01`, `seq-uc01` | `users`, `customers` | `WF-02`, `WF-03` | `AuthController` | `TC-01`, `TC-02` | Bước 1 |
| **FR-02** | Đăng nhập `BCrypt` & Tự động điều hướng 3 Role | Must | `UC01` | — | `uc-01`, `act-uc01`, `seq-uc01` | `users` | `WF-02`, `WF-03`, `WF-05`, `WF-10` | `AuthController`, `SecurityConfig` | `TC-03`..`TC-06` | Bước 1, 3, 6 |
| **FR-03** | Quản lý Hạng phòng & Giá ngày thường / cuối tuần | Must | `UC02` | `BR3`, `BR7` | `uc-02`, `act-uc02`, `seq-uc02` | `room_types`, `rooms`, `bookings` | `WF-09` | `AdminCatalogController` | `TC-07`, `TC-08` | Bước 6 |
| **FR-04** | Quản lý Phòng vật lý, Bảo trì & Chặn xóa lịch sử | Must | `UC02` | `BR2`, `BR7` | `uc-02`, `act-uc02`, `seq-uc02` | `rooms`, `bookings` | `WF-09` | `AdminCatalogController` | `TC-09`..`TC-11` | Bước 6 |
| **FR-05** | Quản lý Danh mục 4 Nhóm Dịch vụ | Must | `UC02` | `BR5`, `BR7` | `uc-02`, `act-uc02`, `seq-uc02` | `services`, `service_orders` | `WF-09` | `AdminCatalogController` | `TC-12` | Bước 6 |
| **FR-06** | Tìm kiếm Hạng phòng theo Tồn kho (Quota) & Giá động | Must | `UC03` | `BR1`, `BR3` | `uc-03`, `act-uc03`, `seq-uc03` | `room_types`, `rooms`, `bookings` | `WF-01`, `WF-04` | `CustomerBookingController` | `TC-13`..`TC-15` | Bước 2 |
| **FR-07** | Xác nhận Pop-up, Cọc 30% & Giữ chỗ (`RESERVED`) | Must | `UC03` | `BR1`, `BR4`, `BR6` | `uc-03`, `act-uc03`, `seq-uc03` | `bookings`, `customers` | `WF-04` | `CustomerBookingController` | `TC-16`, `TC-17` | Bước 2 |
| **FR-08** | Xuất Phiếu xác nhận kèm Mã QR & Lịch sử đặt | Must | `UC03` | `BR4` | `uc-03`, `act-uc03`, `seq-uc03` | `bookings`, `customers` | `WF-03` | `CustomerBookingController` | `TC-18` | Bước 2 |
| **FR-09** | Hủy đơn đặt phòng giữ chỗ (`RESERVED`) | Should | `UC03` | `BR7` | `uc-03`, `act-uc03`, `seq-uc03` | `bookings` | `WF-03` | `CustomerBookingController` | `TC-19` | Bước 2 |
| **FR-10** | Check-in quét QR & Gán phòng vật lý sạch (`AVAILABLE`) | Must | `UC04` | `BR2`, `BR4` | `uc-04`, `act-uc04`, `seq-uc04` | `bookings`, `rooms`, `customers` | `WF-05`, `WF-06` | `StaffCheckInController` | `TC-20`..`TC-22` | Bước 3 |
| **FR-11** | Tiếp nhận Khách vãng lai (`Walk-in` – 0đ cọc lúc vào) | Must | `UC04` | `BR2`, `BR4` | `uc-04`, `act-uc04`, `seq-uc04` | `customers`, `bookings`, `rooms` | `WF-05`, `WF-06` | `StaffCheckInController` | `TC-23` | Bước 3 |
| **FR-12** | Ghi nhận Dịch vụ vào Room Folio & Đóng băng đơn giá | Must | `UC06` | `BR5` | `uc-06`, `act-uc06`, `seq-uc06` | `service_orders`, `services`, `bookings` | `WF-07` | `StaffFolioController` | `TC-24`, `TC-25` | Bước 4 |
| **FR-13** | Quyết toán Biểu giá `++` (`SVC 5%`, `VAT 10%`) & Trừ cọc | Must | `UC05` | `BR4`, `BR6` | `uc-05`, `act-uc05`, `seq-uc05` | `bookings`, `service_orders`, `invoices` | `WF-08` | `StaffCheckOutController` | `TC-26`, `TC-27` | Bước 5 |
| **FR-14** | Lập/In Hóa đơn VAT & Chuyển phòng sang `DIRTY` | Must | `UC05` | `BR6`, `BR7` | `uc-05`, `act-uc05`, `seq-uc05` | `invoices`, `bookings`, `rooms` | `WF-08`, `WF-05` | `StaffCheckOutController` | `TC-28` | Bước 5 |
| **FR-15** | Khóa phòng `DIRTY` & Xác nhận đã dọn sạch (`AVAILABLE`) | Must | `UC05` | `BR2`, `BR7` | `uc-05`, `act-uc05`, `seq-uc05` | `rooms` | `WF-05` | `StaffCheckOutController` | `TC-29` | Bước 5 |
| **FR-16** | Sơ đồ Lưới Phòng Trực quan 4 Trạng thái | Should | `UC04`, `UC05` | `BR2`, `BR7` | `erd`, `class-diagram` | `rooms`, `bookings` | `WF-05` | `StaffDashboardController` | `TC-20`, `TC-29` | Bước 3, 5 |
| **FR-17** | Báo cáo Cơ cấu Doanh thu Đa chiều (`++`) | Should | `UC07` | `BR6` | `uc-07`, `act-uc07`, `seq-uc07` | `invoices`, `service_orders` | `WF-10` | `AdminReportController` | `TC-30` | Bước 6 |
| **FR-18** | Đo lường `Occupancy Rate` (loại trừ `MAINTENANCE`), `ADR`, `RevPAR` | Could | `UC07` | — | `uc-07`, `act-uc07`, `seq-uc07` | `rooms`, `bookings`, `invoices` | `WF-10` | `AdminReportController` | `TC-30` | Bước 6 |

---

## 2. Bộ 30 Kịch bản Kiểm thử Nghiệp vụ (`TC-01` – `TC-30`)

| Mã TC | Mục tiêu kiểm thử | UC / BR | Thao tác & Dữ liệu đầu vào | Kết quả kỳ vọng (Pass Criteria) |
|:---:|---|:---:|---|---|
| **TC-01** | Đăng ký khách hàng mới thành công | `UC01` | Nhập đủ 5 trường tại `/register` (`nguyenvana`, `Ace@123`) | Tạo bản ghi `users` (`CUSTOMER`, BCrypt) & `customers`, tự động vào `/customer/dashboard` (`MSG-03`). |
| **TC-02** | Chặn đăng ký trùng `username` | `UC01` | Nhập `username = 'khach'` đã tồn tại | Chặn tạo mới, báo lỗi `MSG-04`. |
| **TC-03** | Đăng nhập `CUSTOMER` tự động điều hướng | `UC01` | Đăng nhập `khach` / `khach` | Tự động chuyển hướng tới `/customer/dashboard` (`WF-03`). |
| **TC-04** | Đăng nhập `RECEPTIONIST` tự động điều hướng | `UC01` | Đăng nhập `nhanvien` / `nhanvien` | Tự động chuyển hướng tới `/staff/dashboard` (`WF-05`). |
| **TC-05** | Đăng nhập `ADMIN` tự động điều hướng | `UC01` | Đăng nhập `admin` / `admin` | Tự động chuyển hướng tới `/admin/dashboard` (`WF-10`). |
| **TC-06** | Chặn truy cập chéo phân hệ (`Server-side RBAC`) | `UC01`, `NFR-03` | Đăng nhập `khach` hoặc `nhanvien`, gõ URL `/admin/dashboard` | Bị chặn phía Server với mã `403 Forbidden` (`MSG-05`). |
| **TC-07** | Cấu hình giá ngày thường & giá cuối tuần | `UC02`, `BR3` | Admin cập nhật hạng Deluxe: Giá T2–T5 `1.000.000đ`, T6–CN `1.250.000đ` | Lưu thành công (`MSG-06`), hiển thị đúng trên Landing Page và luồng tìm phòng. |
| **TC-08** | Chặn xóa Hạng phòng đang có phòng vật lý | `UC02`, `BR7` | Admin bấm Xóa hạng phòng `Standard` | Hệ thống từ chối xóa và hiện lỗi `MSG-14`. |
| **TC-09** | Chặn tạo phòng trùng `room_number` | `UC02` | Admin thêm phòng mới có số phòng `301` | Hệ thống chặn lưu và báo lỗi `MSG-12`. |
| **TC-10** | Chặn xóa Phòng đã có lịch sử đặt phòng | `UC02`, `BR7` | Admin bấm Xóa phòng `301` hoặc `401` | Hệ thống từ chối xóa và hiện lỗi `MSG-15`. |
| **TC-11** | Bật/Tắt bảo trì phòng trống & Chặn bảo trì phòng đang ở | `UC02` | Bật bảo trì phòng `305` (`AVAILABLE`) vs phòng `301` (`OCCUPIED`) | Phòng `305` sang `MAINTENANCE` thành công; phòng `301` bị chặn và báo lỗi `MSG-16`. |
| **TC-12** | Chặn xóa Dịch vụ đã phát sinh trong Room Folio | `UC02`, `BR7` | Admin xóa `Bữa sáng Buffet` vs xóa `Đền bù mất thẻ từ` (chưa có đơn) | `Bữa sáng Buffet` bị chặn xóa (`MSG-17`); `Đền bù mất thẻ từ` xóa thành công (`MSG-11`). |
| **TC-13** | Kiểm tra `BR3`: Ngày trả $\le$ Ngày nhận | `UC03`, `BR3` | Khách chọn Nhận `25/10`, Trả `25/10` | Hệ thống chặn tìm kiếm và báo lỗi `MSG-20`. |
| **TC-14** | Kiểm tra tồn kho Hạng phòng (`Quota Check`) | `UC03`, `BR1` | Hạng Deluxe có 3 phòng kinh doanh (`201`, `301`, `302`), đang có 2 đơn trùng ngày | Hệ thống tính `available_quota = 3 - 2 = 1 >= 1`, vẫn hiển thị hạng Deluxe cho khách đặt. |
| **TC-15** | Tính giá động kết hợp đêm Ngày thường & Cuối tuần | `UC03`, `BR3` | Đặt hạng Deluxe từ Thứ 5 đến Thứ 7 (1 đêm thường `1.000k` + 1 đêm cuối tuần `1.250k`) | Tiền phòng gốc tính đúng `2.250.000đ`. |
| **TC-16** | Kiểm tra Pop-up xác nhận biểu giá `++` & Cọc 30% | `UC03`, `BR4`, `BR6` | Đặt 2 đêm thường hạng Deluxe (`room_charge = 2.000.000đ`) | Pop-up hiện: `SVC 5% = 100.000đ`, `VAT 10% = 210.000đ`, Tổng `2.310.000đ`, **Cọc 30% = `693.000đ`**, Còn lại 70% = `1.617.000đ`. |
| **TC-17** | Thanh toán cọc 30% & Giữ chỗ (`room_id = NULL`) | `UC03`, `BR1` | Bấm xác nhận thanh toán cọc 30% trên Pop-up | Tạo bản ghi `bookings` với `status = 'RESERVED'`, `room_id = NULL`, `deposit_amount = 693000`. |
| **TC-18** | Sinh Phiếu xác nhận đặt phòng kèm Mã QR | `UC03` | Sau khi hoàn tất `TC-17` | Hiển thị thẻ Phiếu xác nhận có mã `BK-XXXX` và hình QR Code trên `/customer/dashboard`. |
| **TC-19** | Hủy đơn đặt phòng ở trạng thái `RESERVED` | `UC03`, `BR7` | Khách bấm **[Hủy đơn]** trên `BK-2404` (`RESERVED`) | Đơn chuyển sang `CANCELLED` (`MSG-19`), tồn kho `available_quota` tăng lại 1. |
| **TC-20** | Khóa phòng `DIRTY` / `OCCUPIED` / `MAINTENANCE` khi gán phòng | `UC04`, `BR2` | Lễ tân quét mã `BK-2402` (Hạng Deluxe) để gán phòng vật lý | Danh sách chọn chỉ hiện phòng `201` và `302` (`AVAILABLE`), tuyệt đối không hiện phòng `301` (`OCCUPIED`) hay phòng `DIRTY`/`MAINTENANCE`. |
| **TC-21** | Gán phòng vật lý sạch & Check-in khách đặt trước | `UC04` | Lễ tân chọn gán phòng `201` cho đơn `BK-2402` và bấm Xác nhận | `BK-2402` cập nhật `room_id = 4 (P.201)`, `status = 'CHECKED_IN'`; phòng `201` sang `OCCUPIED` (`MSG-24`). |
| **TC-22** | Ghi nhận phụ thu nhận phòng sớm (`Early Check-in`) | `UC04` | Khách nhận phòng lúc 08:00 sáng, Lễ tân chọn mức phụ thu 50% | Lưu đúng `early_checkin_fee` vào `bookings` để cộng vào `subtotal` lúc Check-out. |
| **TC-23** | Tiếp nhận Khách vãng lai (`Walk-in` – 0đ cọc lúc vào) | `UC04`, `BR4` | Lễ tân chọn phòng `101` (`AVAILABLE`), nhập thông tin khách Walk-in & CCCD | Tạo `customers` (`user_id = NULL`), tạo `bookings` với **`deposit_amount = 0`**, `status = 'CHECKED_IN'`, phòng `101` sang `OCCUPIED` (`MSG-25`). |
| **TC-24** | Ghi nợ dịch vụ vào Room Folio & Đóng băng đơn giá | `UC06`, `BR5` | Thêm `2 lon Bia Tiger (35.000đ)` vào phòng `301`; sau đó Admin tăng giá Bia lên `45.000đ` | Dòng dịch vụ trong Room Folio của phòng `301` vẫn giữ nguyên `unit_price = 35.000đ` (`70.000đ`). |
| **TC-25** | Xóa dòng dịch vụ ghi nhầm khi phòng đang `CHECKED_IN` | `UC06`, `BR5` | Lễ tân bấm **[Xóa]** một dòng dịch vụ vừa thêm nhầm trong Room Folio phòng `301` | Dòng dịch vụ bị xóa, tổng tiền dịch vụ tạm tính giảm tương ứng (`MSG-33`). |
| **TC-26** | Quyết toán Check-out Khách Online (Trừ cọc 30%, thu 70% + DV) | `UC05`, `BR4`, `BR6` | Mở Check-out `BK-2401` (Phòng `2.000k` + Folio `300k` = `2.300k`; đã cọc `693k`) | Hệ thống tính: `SVC 5% = 115k`, `VAT 10% = 241.5k`, `Grand Total = 2.656.500đ`, trừ cọc `693.000đ` $\rightarrow$ **`Balance Due = 1.963.500đ`**. |
| **TC-27** | Quyết toán Check-out Khách Walk-in (Cọc 0đ, thu đủ 100%) | `UC05`, `BR4`, `BR6` | Mở Check-out `BK-2405` (Khách Walk-in phòng `202`, `room_charge = 950.000đ`, `deposit = 0đ`) | Hệ thống tính: `SVC 5% = 47.500đ`, `VAT 10% = 99.750đ`, `Grand Total = 1.097.250đ`, trừ cọc `0đ` $\rightarrow$ **`Balance Due = 1.097.250đ` (100%)**. |
| **TC-28** | Hoàn tất Check-out, Chống trùng Hóa đơn & Chuyển phòng `DIRTY` | `UC05`, `BR7` | Bấm xác nhận thanh toán cho `BK-2401` (`P.301`) | Tạo đúng 1 bản ghi `invoices`; `BK-2401` sang `CHECKED_OUT`; **Phòng `301` tự động chuyển sang `🟠 DIRTY`**. |
| **TC-29** | Lễ tân bấm `[✓ Xác nhận đã dọn sạch]` mở khóa phòng `DIRTY` | `UC05`, `BR2`, `BR7` | Bấm nút `[✓ Xác nhận đã dọn sạch]` trên thẻ phòng `102` (hoặc `301`) đang `DIRTY` | Trạng thái phòng chuyển từ `DIRTY` về `🟢 AVAILABLE` (`MSG-30`), xuất hiện lại trong danh sách gán phòng. |
| **TC-30** | Báo cáo Doanh thu đa chiều `++` & Công suất `Occupancy Rate` | `UC07` | Mở `/admin/dashboard` (`WF-10`) đối chiếu số liệu | Mẫu số $R_{\text{sellable}}$ bằng `11` (loại trừ phòng `103` đang `MAINTENANCE`); `OR%`, `ADR`, `RevPAR` và tổng doanh thu `++` khớp tuyệt đối với bảng `invoices`. |

---

## 3. Bộ 20 Câu hỏi & Gợi ý Trả lời Phản biện Bảo vệ Đồ án Cuối kỳ

### Nhóm A: Câu hỏi về Quy trình Nghiệp vụ PMS Mới, Đặt cọc & Gán phòng (Trọng tâm: Phong, Hoàng)

1. **Tại sao khi khách đặt phòng trực tuyến (`UC03`), hệ thống chỉ lưu Hạng phòng (`room_type_id`) và để `room_id = NULL`, đến lúc Check-in tại quầy (`UC04`) mới gán số phòng vật lý cụ thể?**
   * *Trả lời:* Đây là chuẩn vận hành thực tế của các hệ thống PMS khách sạn hiện đại. Khi khách đặt trước từ xa (có thể trước vài tuần), khách sạn không thể biết chính xác phòng `P.201` hay `P.202` vào thời điểm 14:00 ngày khách đến đã được khách trước trả và buồng phòng dọn sạch (`AVAILABLE`) hay chưa. Việc giữ chỗ theo **Tồn kho Hạng phòng (Room Type Quota)** giúp chống Overbooking chính xác, còn việc **gán phòng vật lý đang trống sạch lúc Check-in** giúp lễ tân điều phối linh hoạt và đảm bảo 100% khách luôn nhận được phòng đã vệ sinh hoàn tất.
2. **Khi `room_id = NULL` đối với các đơn đặt trước (`RESERVED`), hệ thống làm sao để kiểm tra còn phòng trống hay không?**
   * *Trả lời:* Theo quy tắc `BR1`, hệ thống kiểm tra theo công thức **Tồn kho Hạng phòng (Quota)** trong khoảng ngày nửa mở `[newIn, newOut)`: lấy tổng số phòng kinh doanh của hạng phòng đó ($\text{status} \neq \text{'MAINTENANCE'}$) trừ đi tổng số đơn đặt thuộc hạng phòng đó đang ở trạng thái `RESERVED` hoặc `CHECKED_IN` có khoảng ngày giao nhau (`existing.in < new.out AND existing.out > new.in`). Nếu kết quả $\ge 1$ thì hạng phòng đó còn suất trống.
3. **Quy tắc thu tiền giữa Khách đặt Online và Khách vãng lai (Walk-in) khác nhau như thế nào và được xử lý thống nhất ra sao khi Check-out?**
   * *Trả lời:* Theo quy tắc `BR4`, **Khách đặt Online** đặt từ xa nên cần thanh toán **cọc 30%** tổng gói phòng ước tính (`++`) để giữ chỗ (`deposit_amount = 30%`) và nhận Phiếu xác nhận QR; trong khi **Khách Walk-in** đến nhận phòng trực tiếp tại quầy (đã xuất trình CCCD) nên nhận phòng ngay mà **không cần thu cọc trước (`deposit_amount = 0`)**. Khi Check-out (`UC05`), cả hai luồng đều dùng chung một công thức duy nhất: `balance_due = grand_total - deposit_amount`. Nhờ đó, khách Online chỉ trả **70% tiền phòng còn lại + 100% dịch vụ/phụ thu**, còn khách Walk-in trả **100% toàn bộ hóa đơn**.
4. **Tại sao sau khi khách trả phòng (`Check-out`), trạng thái phòng không về thẳng `AVAILABLE` mà phải qua trạng thái `DIRTY` (Chờ dọn dẹp)? Vậy có cần làm thêm phân hệ đăng nhập cho nhân viên Buồng phòng không?**
   * *Trả lời:* Nếu trả phòng xong mà chuyển thẳng về `AVAILABLE`, lễ tân ca trực rất dễ xếp ngay một khách mới vào căn phòng vừa trả chưa kịp thay chăn ga và vệ sinh. Do đó, hệ thống tự động chuyển phòng sang `DIRTY` (màu cam) và khóa không cho xuất hiện trong danh sách gán phòng (`BR2`, `BR7`). Để giữ phạm vi hệ thống tinh gọn đúng quy mô khách sạn vừa và nhỏ, khi nhân viên buồng phòng dọn xong và báo qua bộ đàm, Lễ tân chỉ cần bấm 1 click vào nút **[✓ Xác nhận đã dọn sạch]** ngay trên thẻ phòng ở `/staff/dashboard` để đưa phòng về `AVAILABLE`.
5. **Biểu giá `++` trong khách sạn là gì và được hệ thống tính toán như thế nào?**
   * *Trả lời:* Trong ngành khách sạn, ký hiệu `++` nghĩa là giá niêm yết sẽ cộng thêm **5% Phí phục vụ (Service Charge – SVC)** và **10% Thuế GTGT (VAT)** tính trên tổng đã gồm phí phục vụ:
     $$\text{SVC} = \text{ROUND}(\text{Subtotal} \times 5\%), \qquad \text{VAT} = \text{ROUND}((\text{Subtotal} + \text{SVC}) \times 10\%), \qquad \text{Grand Total} = \text{Subtotal} + \text{SVC} + \text{VAT}$$

---

### Nhóm B: Câu hỏi về Mô hình UML, Thiết kế CSDL & Kỹ thuật Java (Trọng tâm: Khoa, Hoàng)

6. **Trong bảng `customers`, tại sao cột `user_id` và `date_of_birth` lại cho phép `NULL` (`Nullable`)?**
   * *Trả lời:* Để bảng `customers` phục vụ chung cho cả **Khách đặt Online** lẫn **Khách vãng lai (Walk-in)**. Khách đặt Online tự đăng ký tài khoản tại `/register` nên có `user_id` nối với bảng `users` và có nhập `date_of_birth`. Ngược lại, khách Walk-in đến quầy chỉ đưa CCCD cho lễ tân nhập nhanh (`full_name`, `phone`, `id_number`) mà không cần tạo tài khoản đăng nhập, nên `user_id` và `date_of_birth` được phép `NULL`.
7. **Tại sao trong bảng `service_orders` (Room Folio) lại có cột `unit_price` trong khi bảng `services` đã có `unit_price`? Có bị dư thừa dữ liệu không?**
   * *Trả lời:* Đây là mẫu thiết kế **Price Snapshot (Đóng băng lịch sử giá – `BR5`)**. `services.unit_price` là giá niêm yết hiện hành; còn `service_orders.unit_price` lưu lại mức giá tại đúng thời điểm khách sử dụng dịch vụ. Nếu ngày mai Admin tăng giá *Bữa sáng* từ `150.000đ` lên `200.000đ`, các hóa đơn cũ và các suất ăn khách đã gọi hôm qua không bị nhảy giá sai lệch.
8. **Với kiểu dữ liệu số nguyên `BIGINT` cho tiền tệ và các ràng buộc `CHECK` trong bảng `invoices`, làm sao tránh lỗi lệch 1 đồng do làm tròn số thập phân?**
   * *Trả lời:* Trong `CheckOutService`, hệ thống không nhân tắt `Subtotal × 1.155`, mà tính tuần tự từng biến nguyên: tính `service_charge_amount = Math.round(subtotal * 0.05)`, sau đó tính `vat_amount = Math.round((subtotal + service_charge_amount) * 0.10)`, và lấy `grand_total = subtotal + service_charge_amount + vat_amount`. Nhờ cộng trực tiếp từ các thành phần đã làm tròn, biểu thức `CHECK (grand_total = subtotal + service_charge_amount + vat_amount)` luôn đúng tuyệt đối 100%.
9. **Tại sao khi tính Tỷ lệ lấp đầy phòng (`Occupancy Rate`) ở `UC07`, mẫu số lại phải trừ đi các phòng đang ở trạng thái `MAINTENANCE`?**
   * *Trả lời:* Theo chuẩn mực quản trị khách sạn quốc tế (USALI), những phòng đang đóng cửa sửa chữa bảo trì (`MAINTENANCE` / *Out of Order*) không đủ điều kiện kỹ thuật để bán cho khách, nên tổng số phòng có thể khai thác kinh doanh thực tế là $R_{\text{sellable}} = \text{AVAILABLE} + \text{OCCUPIED} + \text{DIRTY}$.
10. **Làm sao hệ thống chống được lỗi Race Condition khi 2 khách hàng cùng bấm thanh toán cọc cho suất phòng cuối cùng của một hạng phòng, hoặc Lễ tân bấm đúp chuột vào nút Thanh toán Check-out?**
    * *Trả lời:* (1) Khi tạo booking hoặc gán phòng, phương thức Service chạy trong `@Transactional` và kiểm tra lại tồn kho Quota / khóa dòng phòng (`SELECT ... FOR UPDATE`) ngay trước lệnh `save()`; (2) Ở bảng `invoices`, cột `booking_id` có ràng buộc `UNIQUE` (quan hệ 1–1) kết hợp kiểm tra Idempotency `findByBookingId()` trước khi `INSERT`.
11. **Nếu chuyển hệ quản trị CSDL từ H2 sang MySQL hoặc PostgreSQL khi triển khai thực tế thì cần sửa những gì?**
    * *Trả lời:* Nhờ kiến trúc tách lớp sử dụng **Spring Data JPA**, toàn bộ code `Controller`, `Service`, `Repository`, `Entity` giữ nguyên 100%, chỉ cần đổi khai báo JDBC Driver và `spring.datasource.url` trong `application.properties`.
12. **Cơ chế tính giá phòng khi kỳ lưu trú của khách kéo dài qua cả ngày thường (Thứ 2–Thứ 5) và cuối tuần (Thứ 6–Chủ Nhật) hoạt động thế nào?**
    * *Trả lời:* Phương thức `calculateRoomCharge(checkInDate, checkOutDate)` duyệt vòng lặp `LocalDate` qua từng đêm lưu trú từ `checkInDate` đến `checkOutDate.minusDays(1)`: nếu `dayOfWeek` là `FRIDAY`, `SATURDAY` hoặc `SUNDAY` thì cộng `weekend_price_per_night`, ngược lại cộng `price_per_night`.

---

### Nhóm C: Câu hỏi về Giao diện, Phân quyền 3 Role, Sơ đồ UML & Làm việc Nhóm (Trọng tâm: Kiệt, Phong, Cả nhóm)

13. **Cơ chế tự động điều hướng về 3 Dashboard khác nhau sau khi đăng nhập (`UC01`) được cài đặt như thế nào?**
    * *Trả lời:* Hệ thống cài đặt lớp custom `AuthenticationSuccessHandler` trong Spring Security: ngay sau khi xác thực `BCrypt` thành công, handler kiểm tra `GrantedAuthority` của người dùng để redirect `ROLE_ADMIN` về `/admin/dashboard`, `ROLE_RECEPTIONIST` về `/staff/dashboard`, và `ROLE_CUSTOMER` về `/customer/dashboard`.
14. **Nếu một Khách hàng (`CUSTOMER`) đang đăng nhập mà gõ trực tiếp đường dẫn `/staff/dashboard` lên trình duyệt thì hệ thống xử lý ra sao?**
    * *Trả lời:* Bộ lọc phân quyền phía máy chủ (`SecurityFilterChain` – `NFR-03`) kiểm tra Role trên từng pattern URL (`/admin/**` chỉ cho `ADMIN`, `/staff/**` chỉ cho `RECEPTIONIST`/`ADMIN`, `/customer/**` cho `CUSTOMER`/`ADMIN`). Mọi truy cập trái quyền đều bị chặn ngay tại Server với mã `403 Forbidden` (`MSG-05`).
15. **Mối liên hệ giữa 24 sơ đồ UML ở Chương 4 với cấu trúc CSDL ở Chương 5 và code Java thực tế ở Chương 7 là gì?**
    * *Trả lời:* 8 lớp trong `Class Diagram` (Mục 4.3) ánh xạ 1–1 với 8 bảng trong `ERD` (Mục 4.4) và 8 `@Entity` trong package `com.ace.hotel.entity`. Đồng thời, các lifelines trong 7 `Sequence Diagrams` (`Controller` $\rightarrow$ `Service` $\rightarrow$ `Repository`) trùng khớp trực tiếp với tên class và phương thức trong source code Spring Boot.
16. **Tại sao nhóm sử dụng cả hai chế độ màu Dark Mode (mặc định) và Light Mode với màu vàng `#FDBF2D`? Làm sao để chữ màu vàng đọc rõ trên nền sáng?**
    * *Trả lời:* Tông Đen – Vàng Gold (`#FDBF2D`) ở Dark Mode mang lại vẻ sang trọng đặc trưng của khách sạn cao cấp, trong khi Light Mode hỗ trợ nhân viên lễ tân làm việc lâu dưới ánh sáng ban ngày. Trên nền sáng, chữ trên nút vàng dùng màu đen `#0A0A0A`, còn chữ nhấn/liên kết dùng token `--primary-text: #B45309` (vàng nâu đậm) để đạt chuẩn tương phản WCAG.
17. **Tính năng quét mã QR khi Check-in (`UC04`) vận hành như thế nào khi demo trên laptop?**
    * *Trả lời:* Trên Phiếu xác nhận đặt phòng của khách (`WF-03`), hệ thống tự động sinh mã QR chứa chuỗi `booking_code` (ví dụ `BK-8K4P9X2M`). Tại màn hình Lễ tân (`WF-05`/`WF-06`), ô tra cứu hỗ trợ cả đầu đọc súng bắn mã QR cầm tay (tự động điền chuỗi mã và nhấn Enter) lẫn gõ/dán mã trực tiếp, đảm bảo tốc độ thao tác dưới 2 giây và không phụ thuộc vào điều kiện ánh sáng webcam.
18. **Tại sao hệ thống chặn không cho Admin xóa một Phòng vật lý đã từng có khách ở, mà chỉ cho chuyển sang trạng thái `MAINTENANCE`?**
    * *Trả lời:* Theo quy tắc bảo toàn lịch sử `BR7` và `NFR-04`, các bản ghi `bookings` và `invoices` cũ đều tham chiếu khóa ngoại tới `room_id`. Nếu xóa vật lý phòng đó sẽ làm mất dữ liệu đối soát doanh thu và hóa đơn lịch sử.
19. **Nếu trong tương lai khách sạn muốn mở rộng cho phép đặt theo đoàn nhiều phòng (Group Booking) trong 1 hóa đơn thì thiết kế hiện tại nâng cấp thế nào?**
    * *Trả lời:* Chỉ cần tách mối quan hệ giữa `bookings` và `rooms` thành bảng chi tiết `booking_items(item_id, booking_id, room_type_id, room_id, price_per_night, actual_check_in, actual_check_out)`, trong đó mỗi dòng `booking_items` giữ một Room Folio riêng và gom chung về `bookings` khi lập `invoices`.
20. **Mỗi thành viên hãy tóm tắt rõ phần đóng góp cá nhân của mình trong dự án này?**
    * *Trả lời:*
      - **Khoa:** Thiết kế quy trình PMS mới, vẽ `ERD`, `Class Diagram`, bộ 3 sơ đồ `UC07`, viết Chương 5 (CSDL 8 bảng), tạo `schema.sql` / `sample-data.sql`, code full ứng dụng Spring Boot (4 phân hệ) và thao tác máy khi demo.
      - **Hoàng:** Viết Chương 3 (`BR1–BR7`, `18 FR`, `9 NFR`, `MoSCoW`), vẽ `Use Case tổng quát`, đặc tả & vẽ 6 sơ đồ cho `UC02`, `UC03`, viết Chương 7 (kèm Ma trận truy vết RTM), Chương 8, Phần đầu báo cáo và gộp/hoàn thiện toàn bộ quyển báo cáo Word/PDF để nộp.
      - **Phong:** Viết Chương 1, Chương 2, đặc tả & vẽ 6 sơ đồ cho `UC04`, `UC05`, xây dựng kịch bản demo, bộ 20 câu hỏi phản biện và đảm nhận thuyết trình toàn bộ 12 slide (14 phút).
      - **Kiệt:** Vẽ 10 Wireframe (`WF-01`..`WF-10`) & Sơ đồ điều hướng, viết Chương 6, đặc tả & vẽ 6 sơ đồ cho `UC01`, `UC06` và thiết kế toàn bộ 12 Slide thuyết trình bằng tiếng Anh.