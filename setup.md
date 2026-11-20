# Cách chạy ACE Hotel trên máy local

## 1. Chuẩn bị môi trường

- Cài **JDK 21**.
- Có Internet ở lần chạy đầu để tải Maven và thư viện. Dự án có Maven Wrapper nên không cần cài Maven riêng.
- Giải nén đầy đủ dự án, giữ nguyên thư mục `source/.mvn/`.
- Không cần cài cơ sở dữ liệu riêng; ứng dụng sử dụng H2 tích hợp.

Mở PowerShell và kiểm tra:

```powershell
java -version
javac -version
```

Cả hai lệnh cần hiển thị phiên bản 21. Nếu máy chưa nhận đúng JDK, thay đường dẫn dưới đây bằng thư mục JDK 21 đã cài trên máy:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

## 2. Chạy ứng dụng

Mở PowerShell tại thư mục gốc dự án, nơi có thư mục `source`, rồi chạy:

```powershell
cd .\source
.\mvnw.cmd spring-boot:run
```

Nếu đã mở PowerShell trong `source` (thư mục chứa `pom.xml` và `mvnw.cmd`), chỉ chạy lệnh thứ hai.

Đợi xuất hiện thông báo `Started AceHotelApplication`. Lần chạy đầu có thể lâu hơn do tải thư viện và khởi tạo dữ liệu.

## 3. Mở web local

Mở trình duyệt và truy cập:

**http://localhost:8088**

Giữ cửa sổ PowerShell đang chạy khi sử dụng web. Nhấn **Ctrl+C** để dừng ứng dụng.

Nếu cổng 8088 đang được sử dụng, chạy bằng cổng 8089:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8089"
```

Sau đó mở **http://localhost:8089**.
