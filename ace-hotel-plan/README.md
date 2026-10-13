# ACE Hotel — Bộ plan làm demo từ đầu đến cuối

Môn 502050 (Phân tích và Thiết kế Yêu cầu), Đề tài 1: Hệ thống quản lý khách sạn.
Người thực hiện phần web + demo: **Khoa**. Bản plan lập ngày 13/10/2026.

ACE = **A**ccommodation, **C**heck-in & **E**xtras.

---

## Luồng tổng thể của demo

```
Landing page (/)  →  Đăng nhập (/login)  →  Dashboard (/dashboard)  →  Chức năng nghiệp vụ
  công khai             UC01                  một dashboard,            UC02 đến UC06 (và UC07
  chỉ giới thiệu                              menu theo vai trò         nếu còn thời gian)
```

## Quyết định đã chốt khi lập plan

| # | Nội dung | Quyết định |
|---|---|---|
| 1 | Booking trên landing page | **Chỉ giới thiệu.** Không có form đặt phòng; khách liên hệ và đặt qua lễ tân (đúng phạm vi đã chốt của nhóm) |
| 2 | Dashboard sau đăng nhập | **Một dashboard**, menu và khối hiển thị theo vai trò (lễ tân / quản lý) |
| 3 | Chế độ màu | **Dark mặc định**, có nút chuyển sang Light (bộ ACE Color System) |
| 4 | Dạng bộ plan | **Markdown theo module** + checklist theo Tuần 1 đến 4 |

## Mục lục

| File | Nội dung |
|---|---|
| `00-tong-quan.md` | Mục tiêu, phạm vi, actor, stack, ràng buộc của đề, bản đồ truy vết |
| `01-landing-page.md` | Landing page: các khối, nội dung, nguồn dữ liệu, tiêu chí xong |
| `02-dang-nhap-phan-quyen.md` | UC01: đăng nhập, phân quyền, ma trận quyền |
| `03-dashboard.md` | Dashboard: bố cục, menu theo vai trò, KPI, bảng hôm nay |
| `04-man-hinh-nghiep-vu.md` | Từng màn hình UC02 đến UC07: trường, kiểm tra hợp lệ, quy tắc |
| `05-csdl.md` | 8 bảng, DDL nháp, trạng thái, kế hoạch dữ liệu mẫu |
| `06-kien-truc-route.md` | Kiến trúc Spring Boot, package, bảng route, cấu trúc repo |
| `07-giao-dien-design-system.md` | Màu, component, đổi theme, cách dùng logo |
| `08-ke-hoach-theo-tuan.md` | Checklist Tuần 1 đến 4, bàn giao, mốc cắt giảm |
| `09-demo-va-kiem-thu.md` | Kịch bản demo, checklist kiểm thử, máy demo, rủi ro |
| `10-cau-hoi-mo.md` | Các điểm còn lệch hoặc chưa chốt, kèm đề xuất mặc định |
| `assets/` | 4 file logo ACE đã xuất (nền trong suốt và có tên) |

## Ký hiệu dùng trong các file

- **[CHỐT]**: đã có trong đề hoặc trong file chốt của nhóm (`chot-pham-vi-actor-usecase-bang.md`, `yeu-cau-fr-nfr-moscow.md`).
- **[ĐỀ XUẤT]**: mình đề xuất để làm được ngay; bạn đổi được.
- **[CẦN XÁC NHẬN]**: cần hỏi nhóm hoặc cô trước khi coi là cố định (xem `10-cau-hoi-mo.md`).
- **P0**: bắt buộc cho demo và cho đề. **P1**: nên có. **P2**: chỉ làm nếu còn thời gian.

## Mốc quan trọng

- 12/10: bắt đầu Tuần 1. 18/10 cuối Tuần 1: wireframe và đặc tả use case của 3 bạn phải xong để Khoa đọc và code.
- Giữa Tuần 3 (26/10 đến 01/11): Khoa **bàn giao code** (GitHub, README, tài khoản demo) cho Hoàng, Phong, Kiệt.
- Tuần 4 (02/11 đến 08/11): ôn tập, video demo dự phòng.
- **09/11/2026, 23g59**: hạn nộp quyển báo cáo (.docx và/hoặc PDF) trên Elearning. Ngày báo cáo và demo thực tế chưa có.
