# ACE Hotel — Bộ Plan Thi Công Kỹ Thuật (PMS & Online Booking Portal)

> **Học phần:** 502050 (Phân tích và Thiết kế Yêu cầu) — Đề tài 1: Hệ thống quản lý khách sạn[cite: 15, 38].  
> **Người thực hiện phần Web Java + CSDL + Demo:** **Khoa**[cite: 15, 35].  
> **Phiên bản:** Bản thiết kế thi công mới (v2.0) cập nhật ngày 14/10/2026 — Đồng bộ 100% với `docs/quy-trinh-nghiep-vu-moi.md` và cấu trúc báo cáo Chương 1 – Chương 8[cite: 53].

**ACE** = **A**ccommodation (Lưu trú), **C**heck-in (Nhận phòng & Gán phòng sạch), **E**xtras (Dịch vụ đi kèm Room Folio)[cite: 2, 5, 53].

---

## 1. Luồng Tổng thể của Hệ thống Mới (End-to-End Flow)

~~~text
[1. PHÂN HỆ PUBLIC (/)]
   ├─ Xem Landing Page Storytelling (WF-01) -> Tìm Hạng phòng trống theo ngày
   └─ Đăng ký (/register) hoặc Đăng nhập (/login - WF-02) -> Tự động điều hướng theo 3 Role:
         │
         ├──► Role = CUSTOMER     ──► [2. CUSTOMER PORTAL (/customer/dashboard - WF-03, WF-04)]
         │                              • Tìm Hạng phòng theo Tồn kho (Quota Check)
         │                              • Xem Pop-up biểu giá ++ & Thanh toán Cọc 30% (RESERVED, room_id = NULL)
         │                              • Nhận Phiếu xác nhận đặt phòng kèm Mã QR (BK-XXXX) / Hủy đơn RESERVED
         │
         ├──► Role = RECEPTIONIST ──► [3. STAFF PMS PORTAL (/staff/dashboard - WF-05..WF-08)]
         │                              • Sơ đồ lưới 12 phòng mã hóa 4 trạng thái (AVAILABLE, OCCUPIED, DIRTY, MAINTENANCE)
         │                              • Check-in Khách Online: Quét/nhập mã QR -> Gán phòng vật lý sạch (AVAILABLE -> OCCUPIED)
         │                              • Tiếp nhận Khách Walk-in: Chọn phòng sạch nhận ngay (deposit_amount = 0đ)
         │                              • Ghi nợ / Xóa dịch vụ 4 nhóm (MINIBAR, LAUNDRY, FB, DAMAGES) vào Room Folio
         │                              • Check-out Quyết toán ++ (SVC 5% + VAT 10%):
         │                                  - Khách Online: Thu 70% tiền phòng còn lại + 100% Dịch vụ & Phụ thu
         │                                  - Khách Walk-in: Thu 100% toàn bộ hóa đơn
         │                              • Xuất/In Hóa đơn VAT -> Phòng tự động chuyển sang DIRTY (Chờ dọn dẹp)
         │                              • Bấm 1-Click [✓ Xác nhận đã dọn sạch] chuyển phòng từ DIRTY -> AVAILABLE
         │
         └──► Role = ADMIN        ──► [4. ADMIN BACKOFFICE (/admin/dashboard - WF-09, WF-10)]
                                        • Cấu hình Hạng phòng & Giá động (Ngày thường T2–T5 / Cuối tuần T6–CN)
                                        • Quản lý Phòng vật lý (Thêm/Sửa/Bảo trì/Xóa có bảo toàn lịch sử) & 4 nhóm Dịch vụ
                                        • Báo cáo Doanh thu đa chiều ++ & Công suất phòng (Occupancy Rate, ADR, RevPAR)
~~~

---

## 2. Sáu Quyết định Kiến trúc & Nghiệp vụ Đã Chốt Hạ

| # | Hạng mục | Quy chuẩn áp dụng trong Code (`ace-hotel/src`) |
|:---:|---|---|
| **1** | **Kiến trúc Giao diện** | **4 phân hệ độc lập** (`WF-01` đến `WF-10`), mặc định **Dark Mode**, có nút chuyển đổi **Light Mode** lưu vào `localStorage ('ace-theme')`[cite: 11, 15, 53]. |
| **2** | **Đặt phòng & Gán phòng** | Đặt Online lưu theo **Hạng phòng (`room_type_id`, `room_id = NULL`)** dựa trên công thức Quota; khi Check-in tại quầy Lễ tân mới chọn **gán phòng vật lý đang trống sạch (`AVAILABLE`)**[cite: 53]. |
| **3** | **Đặt cọc & Quyết toán** | • **Khách Online:** Cọc trước **30%** lúc đặt (`deposit_amount > 0`) $\rightarrow$ Lúc Check-out trả **70% tiền phòng còn lại + 100% dịch vụ & phụ thu**[cite: 53].<br>• **Khách Walk-in:** Nhận phòng ngay **0đ cọc** (`deposit_amount = 0`) $\rightarrow$ Lúc Check-out trả **100% toàn bộ hóa đơn**. |
| **4** | **Công thức Biểu giá `++`** | Tính tuần tự số nguyên `BIGINT` (VNĐ): `subtotal` $\rightarrow$ `SVC (5%) = ROUND(subtotal * 0.05)` $\rightarrow$ `VAT (10%) = ROUND((subtotal + SVC) * 0.10)` $\rightarrow$ `grand_total = subtotal + SVC + VAT` $\rightarrow$ `balance_due = grand_total - deposit_deducted`[cite: 53]. |
| **5** | **Vòng đời Phòng `DIRTY`** | Check-out xong phòng tự động sang **`DIRTY` (màu cam `#F59E0B`)**, khóa không cho gán khách mới; Lễ tân bấm **[✓ Xác nhận đã dọn sạch]** mới về lại **`AVAILABLE` (màu xanh `#22C55E`)**[cite: 53]. |
| **6** | **Công suất `Occupancy Rate`** | Mẫu số phòng kinh doanh $R_{\text{sellable}} = \text{AVAILABLE} + \text{OCCUPIED} + \text{DIRTY}$ (**bắt buộc loại trừ phòng `MAINTENANCE`**)[cite: 53]. |

---

## 3. Mục lục 12 File trong `ace-hotel-plan/`

| STT | Tên File | Nội dung chi tiết |
|:---:|---|---|
| **1** | `README.md` | Bản đồ tổng quan luồng hệ thống mới, 6 quyết định cốt lõi và mục lục tra cứu |
| **2** | `00-tong-quan-va-kien-truc.md` | Phạm vi 7 UC, 7 quy tắc `BR1–BR7`, Tech Stack, `pom.xml`, `application.properties` & Cây thư mục `src/` |
| **3** | `01-csdl-schema-va-seed-data.md` | Toàn bộ DDL `schema.sql` (8 bảng), kịch bản `sample-data.sql` hoàn chỉnh & Thiết kế 8 JPA Entities |
| **4** | `02-bang-route-va-bao-mat-3-role.md` | Bảng tổng hợp Route URL toàn hệ thống, `SecurityConfig`, Đăng ký khách hàng & Auto-Redirect 3 Role |
| **5** | `03-design-system-va-layout.md` | Bộ biến màu `tokens.css` (kèm màu `DIRTY`), `theme.js`, cách gắn 4 file Logo & 2 Layout Thymeleaf |
| **6** | `04-phan-he-public-va-customer-portal.md` | Đặc tả code `WF-01`, `WF-03`, `WF-04`: Landing Storytelling, Tìm Quota, Pop-up Cọc 30% & Phiếu QR |
| **7** | `05-phan-he-staff-pms-checkin-walkin.md` | Đặc tả code `WF-05`, `WF-06`: Sơ đồ lưới 12 phòng, Quét QR gán phòng sạch & Tiếp nhận Walk-in 0đ |
| **8** | `06-phan-he-staff-pms-folio-va-checkout.md` | Đặc tả code `WF-07`, `WF-08`: Room Folio 4 nhóm, Check-out `++` (70% vs 100%), Hóa đơn VAT & `DIRTY` |
| **9** | `07-phan-he-admin-catalog-va-report.md` | Đặc tả code `WF-09`, `WF-10`: CRUD Hạng phòng (Giá T2–T5 / T6–CN), Phòng, Dịch vụ & Báo cáo `OR%` |
| **10** | `08-cong-thuc-service-va-sql-trong-tam.md` | Code mẫu Java Service & `@Query` cho Quota, Giá động, Làm tròn `BIGINT ++`, Idempotency & 35 `MSG` |
| **11** | `09-ke-hoach-code-theo-ngay.md` | Lịch trình code từng ngày của Khoa trong Tuần 2–3, thứ tự commit GitHub & mốc kiểm soát tiến độ |
| **12** | `10-kiem-thu-kich-ban-demo-va-hoi-dap.md` | Checklist tự kiểm thử 30 Test Cases, Kịch bản bấm máy Demo 6 bước & 8 Câu trả lời kỹ thuật của Khoa |