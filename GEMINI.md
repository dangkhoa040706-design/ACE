# QUY TẮC BẮT BUỘC DỰ ÁN ACE HOTEL (MANDATORY PROJECT RULES)

## 1. Nghiêm Cấm Chạy Toàn Bộ Test Suite Nặng (`AceHotelApplicationTests`)
- **Lý do**: Lớp `AceHotelApplicationTests` chứa 26 integration tests khởi động toàn bộ ứng dụng Spring Boot, nạp H2 DB và hàng chục MockMvc requests. Trên môi trường Windows, tiến trình này dễ bị đệm xuất (stdout buffering) hoặc treo vô tận trong task nền, làm gián đoạn người dùng.
- **Quy tắc bắt buộc**:
  - **TUYỆT ĐỐI KHÔNG** chạy `AceHotelApplicationTests` trong quá trình phát triển thường quy trừ khi người dùng yêu cầu rõ ràng.
  - Khi cần xác minh mã nguồn và logic sau khi sửa đổi, **CHỈ ĐƯỢC PHÉP** chạy:
    1. Biên dịch kiểm tra cú pháp & Thymeleaf: `mvn compile` (hoàn thành trong 2-3 giây).
    2. Bộ kiểm thử khói siêu tốc: `mvn test -Dtest=BugFixSmokeTest` (hoàn thành trong < 1 giây).
    3. Hoặc chạy kiểm thử đơn vị độc lập tương ứng với lớp vừa sửa đổi qua cờ `-Dtest=TênTest`.

## 2. Quản Lý Tiến Trình Nền (Background Tasks) & Tránh Treo Lệnh
- Tuyệt đối không để tiến trình Maven chạy nền lơ lửng. Khi chạy lệnh Maven qua terminal:
  - Phải đặt `WaitMsBeforeAsync` ở mức 8000 - 10000ms để chạy đồng bộ và kết thúc dứt điểm.
  - Nếu một tiến trình Maven chạy quá 15 giây mà không trả kết quả, phải lập tức kiểm tra và hủy (`kill`) tiến trình đó, không được để task nền chạy kéo dài làm người dùng phải chờ đợi.
  - Sau mỗi thao tác, kiểm tra danh sách task nền để đảm bảo 0 task bị kẹt.

## 3. Quy Chuẩn Lệnh Maven Trên Môi Trường Windows
- Luôn ưu tiên dùng Maven Wrapper của dự án: `mvnw.cmd` (hoặc `.\mvnw` trong PowerShell).
- Đảm bảo thiết lập `JAVA_HOME` trỏ đúng tới JDK 21:
  `cmd.exe /c "set JAVA_HOME=C:\Program Files\Android\openjdk\jdk-21.0.8&& cd d:\ACE\ace-hotel && mvnw.cmd ..."`
