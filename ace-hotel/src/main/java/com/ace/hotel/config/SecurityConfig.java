package com.ace.hotel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * UC01 - Đăng nhập và phân quyền (FR-01, FR-02, NFR-02, NFR-03).
 * Quyền được kiểm tra ở máy chủ; ẩn nút trên giao diện chỉ để gọn.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Công khai: landing page, đăng nhập, tài nguyên tĩnh, trang lỗi
                .requestMatchers("/", "/login", "/error",
                        "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                // Chỉ quản lý
                .requestMatchers("/room-types/**", "/services/**", "/reports/**").hasRole("MANAGER")
                .requestMatchers("/rooms/new", "/rooms/*/edit", "/rooms/*/delete").hasRole("MANAGER")
                // Còn lại: phải đăng nhập (lễ tân hoặc quản lý)
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"))
            .exceptionHandling(ex -> ex.accessDeniedPage("/403"));
        return http.build();
    }

    /** Mật khẩu lưu dạng băm BCrypt, không lưu mật khẩu thô (NFR-02). */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
