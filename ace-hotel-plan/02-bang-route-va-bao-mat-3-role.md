# 02. Bảng Tổng hợp Route, Bảo mật 3 Role & Tự động Điều hướng (`UC01`)

> **Mục tiêu:** Đặc tả chi tiết toàn bộ đường dẫn URL của 4 phân hệ giao diện (`WF-01` đến `WF-10`), cấu hình `SecurityConfig`, bộ điều hướng tự động `RoleBasedAuthSuccessHandler` và luồng Đăng ký khách hàng 5 trường (`UC01`)[cite: 6, 10, 53].

---

## 1. Bảng Tổng hợp Route URL Toàn Hệ thống

| Phân hệ | HTTP Method | Đường dẫn Route | Quyền truy cập (`Role`) | Màn hình (`WF`) | Controller xử lý |
|---|:---:|---|---|:---:|---|
| **1. Public** | `GET` | `/` | Công khai (`permitAll`) | `WF-01` | `PublicController` |
| **1. Public** | `GET`, `POST` | `/login` | Công khai (`permitAll`) | `WF-02` | `AuthController` / Spring Security |
| **1. Public** | `GET`, `POST` | `/register` | Công khai (`permitAll`) | `WF-02` | `AuthController` |
| **1. Public** | `POST` | `/logout` | Đã đăng nhập | — | Spring Security (`redirect:/login?logout`) |
| **2. Customer** | `GET` | `/customer/dashboard` | `CUSTOMER`, `ADMIN` | `WF-03` | `CustomerPortalController` |
| **2. Customer** | `GET` | `/booking/search` | Công khai (Xem) / `CUSTOMER` | `WF-04` | `CustomerPortalController` |
| **2. Customer** | `POST` | `/booking/deposit` | `CUSTOMER`, `ADMIN` | `WF-04` $\rightarrow$ `WF-03` | `CustomerPortalController` |
| **2. Customer** | `POST` | `/customer/bookings/{id}/cancel` | `CUSTOMER`, `RECEPTIONIST`, `ADMIN` | `WF-03` | `CustomerPortalController` |
| **3. Staff PMS** | `GET` | `/staff/dashboard` | `RECEPTIONIST`, `ADMIN` | `WF-05` | `StaffDashboardController` |
| **3. Staff PMS** | `POST` | `/staff/rooms/{id}/clean` | `RECEPTIONIST`, `ADMIN` | `WF-05` | `StaffDashboardController` |
| **3. Staff PMS** | `GET`, `POST` | `/staff/check-in` | `RECEPTIONIST`, `ADMIN` | `WF-06` | `StaffCheckInController` |
| **3. Staff PMS** | `GET`, `POST` | `/staff/walk-in` | `RECEPTIONIST`, `ADMIN` | `WF-06` | `StaffCheckInController` |
| **3. Staff PMS** | `GET`, `POST` | `/staff/bookings/{id}/folio` | `RECEPTIONIST`, `ADMIN` | `WF-07` | `StaffFolioController` |
| **3. Staff PMS** | `POST` | `/staff/bookings/{id}/folio/{orderId}/delete` | `RECEPTIONIST`, `ADMIN` | `WF-07` | `StaffFolioController` |
| **3. Staff PMS** | `GET`, `POST` | `/staff/bookings/{id}/check-out` | `RECEPTIONIST`, `ADMIN` | `WF-08` | `StaffCheckOutController` |
| **3. Staff PMS** | `GET` | `/staff/invoices/{id}` | `RECEPTIONIST`, `ADMIN` | `WF-08` (Bản in) | `StaffCheckOutController` |
| **4. Admin** | `GET` | `/admin/dashboard` | **Chỉ `ADMIN`** | `WF-10` | `AdminReportController` |
| **4. Admin** | `GET` | `/admin/catalog` | `ADMIN` (CRUD), `RECEPTIONIST` (Chỉ xem) | `WF-09` | `AdminCatalogController` |
| **4. Admin** | `POST` | `/admin/room-types/**`, `/admin/rooms/**`, `/admin/services/**` | **Chỉ `ADMIN`** | `WF-09` | `AdminCatalogController` |

---

## 2. Cấu hình `SecurityConfig.java` & Tự động Điều hướng 3 Role

### 2.1. Class `RoleBasedAuthSuccessHandler.java`
Ngay sau khi đăng nhập thành công tại `/login`, hệ thống kiểm tra `Role` của tài khoản để chuyển hướng tự động về đúng 1 trong 3 Dashboard[cite: 53]:

~~~java
package com.ace.hotel.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }
            if ("ROLE_RECEPTIONIST".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
                return;
            }
            if ("ROLE_CUSTOMER".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/customer/dashboard");
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}
~~~

---

### 2.2. Class `SecurityConfig.java` (Kiểm tra quyền phía Máy chủ – `NFR-03`)
~~~java
package com.ace.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final RoleBasedAuthSuccessHandler successHandler;

    public SecurityConfig(RoleBasedAuthSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 1. Tài nguyên tĩnh & Trang công khai
                .requestMatchers("/", "/login", "/register", "/booking/search",
                                 "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()

                // 2. Phân hệ Quản trị (Chỉ ADMIN mới được POST/xem báo cáo; Lễ tân được GET xem bảng giá)
                .requestMatchers(HttpMethod.GET, "/admin/catalog").hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers("/admin/**", "/h2-console/**").hasRole("ADMIN")

                // 3. Phân hệ Nhân viên Lễ tân PMS (RECEPTIONIST & ADMIN)
                .requestMatchers("/staff/**").hasAnyRole("RECEPTIONIST", "ADMIN")

                // 4. Phân hệ Cổng Khách hàng (CUSTOMER & ADMIN)
                .requestMatchers("/customer/**", "/booking/deposit").hasAnyRole("CUSTOMER", "ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            );

        // Cho phép hiển thị H2 Console trong iframe khi debug với quyền ADMIN
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
~~~

---

## 3. Luồng Đăng ký Tài khoản Khách hàng Mới (`POST /register` – 5 Trường bắt buộc)

### 3.1. DTO `RegisterFormDto.java`
~~~java
package com.ace.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class RegisterFormDto {

    @NotBlank(message = "Vui lòng nhập họ và tên.")
    private String fullName;

    @NotNull(message = "Vui lòng nhập ngày sinh.")
    @Past(message = "Ngày sinh phải ở trong quá khứ.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Vui lòng nhập số điện thoại.")
    @Pattern(regexp = "^[0-9]{9,15}$", message = "Số điện thoại gồm 9 đến 15 chữ số.")
    private String phone;

    @NotBlank(message = "Vui lòng nhập tên đăng nhập.")
    @Size(min = 4, max = 50, message = "Tên đăng nhập từ 4 đến 50 ký tự.")
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu.")
    @Size(min = 6, max = 100, message = "Mật khẩu tối thiểu 6 ký tự.")
    private String password;

    // Getters & Setters
}
~~~

### 3.2. Xử lý trong `AuthService.registerCustomer(RegisterFormDto dto)`
1. Kiểm tra `userRepository.existsByUsernameIgnoreCase(dto.getUsername().trim())`: nếu đã tồn tại thì ném `BusinessException(MessageConstants.MSG_04)` (*"Tên đăng nhập này đã được sử dụng. Vui lòng chọn tên đăng nhập khác."*)[cite: 30, 53].
2. Trong một `@Transactional`:
   - Tạo thực thể `User`: gán `username`, `passwordHash = passwordEncoder.encode(dto.getPassword())`, `fullName = dto.getFullName()`, `role = Role.CUSTOMER` $\rightarrow$ `userRepository.save(user)`[cite: 6, 53].
   - Tạo thực thể `Customer`: gán `user = savedUser`, `fullName = dto.getFullName()`, `dateOfBirth = dto.getDateOfBirth()`, `phone = dto.getPhone()` $\rightarrow$ `customerRepository.save(customer)`[cite: 53].
3. Sau khi đăng ký xong, tự động nạp `Authentication` vào `SecurityContext` (hoặc chuyển về `/login?registered=true`) và đưa khách thẳng tới `/customer/dashboard` kèm thông báo `MSG-03`[cite: 53].

---

## 4. Hành vi khi Người dùng Đã Đăng nhập quay lại `/` hoặc `/login`

- Tại `GET /login`: Nếu `authentication != null && authentication.isAuthenticated()`, `AuthController` gọi thẳng `successHandler` để chuyển người dùng về đúng Dashboard của họ (`/admin/dashboard`, `/staff/dashboard` hoặc `/customer/dashboard`), không bắt nhập lại mật khẩu[cite: 6, 53].
- Tại `GET /` (Landing Page `WF-01`): Nút góc phải trên Header tự động đổi từ **[Đăng nhập / Đăng ký]** thành **[Vào Bảng điều khiển (`{fullName}`)]** trỏ về Dashboard tương ứng với `Role` hiện tại[cite: 5, 53].

---

## 5. Checklist Kiểm thử `UC01` (Đăng ký, Đăng nhập & Phân quyền 3 Role)

- [ ] Đăng nhập `admin` / `admin` $\rightarrow$ tự động vào `/admin/dashboard` (`WF-10`).
- [ ] Đăng nhập `nhanvien` / `nhanvien` $\rightarrow$ tự động vào `/staff/dashboard` (`WF-05`).
- [ ] Đăng nhập `khach` / `khach` $\rightarrow$ tự động vào `/customer/dashboard` (`WF-03`).
- [ ] Đăng ký khách hàng mới đủ 5 trường tại `/register` $\rightarrow$ tạo bản ghi ở cả `users` và `customers`, mật khẩu lưu dạng băm `$2a$10$...`[cite: 6, 53].
- [ ] Đăng nhập sai mật khẩu hoặc tên không tồn tại $\rightarrow$ hiển thị đúng thông báo chung `MSG-01`: *"Sai tên đăng nhập hoặc mật khẩu."*[cite: 6, 30].
- [ ] Đang đăng nhập `khach` mà gõ `/staff/dashboard` hoặc `/admin/dashboard` $\rightarrow$ bị chặn trang `403` tiếng Việt (`MSG-05`), có nút quay về Dashboard của mình[cite: 6, 30].
- [ ] Đang đăng nhập `nhanvien` mà gõ `/admin/dashboard` hoặc gửi POST xóa phòng $\rightarrow$ bị chặn `403`[cite: 6, 30].