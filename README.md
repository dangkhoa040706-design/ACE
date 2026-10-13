# ACE Hotel — Accommodation, Check-in & Extras

Hệ thống quản lý khách sạn vừa và nhỏ (Môn học `502050` — Đề tài 1).

---

## Cấu trúc thư mục dự án

```text
D:\ACE\
├── ace-hotel\          # Mã nguồn Spring Boot 3.5 + Thymeleaf + H2 Database
│   ├── src\            # Mã nguồn Java, templates Thymeleaf, static CSS/JS/fonts
│   ├── pom.xml         # Quản lý thư viện Maven (Java 21)
│   └── README.md       # Hướng dẫn chạy và danh sách tài khoản demo
│
├── ace-hotel-plan\     # Kế hoạch chi tiết 11 phần (kiến trúc, CSDL, giao diện, route)
│   ├── 00-tong-quan.md .. 10-cau-hoi-mo.md
│   └── assets\         # Bộ nhận diện thương hiệu và logo ACE
│
└── docs\               # Toàn bộ hồ sơ phân tích thiết kế môn học 502050
    ├── 01-project-brief.md .. 14-kiem-thu-nghiep-vu-va-ma-tran-truy-vet.md
    └── assets\         # Đề thi, bảng màu Design System, sơ đồ kế hoạch và phân công
        └── phan-cong\  # Sơ đồ phân công chi tiết theo thành viên
```

---

## Hướng dẫn nhanh

1. **Xem tài liệu phân tích thiết kế**: Mở thư mục [`docs/`](file:///D:/ACE/docs/README.md) để đọc đầy đủ đặc tả Use Case, ERD, Wireframe, RBAC và kịch bản demo.
2. **Xem kế hoạch phát triển**: Mở thư mục [`ace-hotel-plan/`](file:///D:/ACE/ace-hotel-plan/README.md).
3. **Chạy ứng dụng Java**:
   - Mở thư mục [`ace-hotel/`](file:///D:/ACE/ace-hotel) bằng **IntelliJ IDEA** (yêu cầu **JDK 21**).
   - Chạy class `AceHotelApplication` hoặc lệnh `mvn spring-boot:run`.
   - Truy cập: `http://localhost:8080`.
   - Tài khoản đăng nhập demo:
     - Lễ tân: `letan` / `Ace@123`
     - Quản lý: `quanly` / `Ace@123`
