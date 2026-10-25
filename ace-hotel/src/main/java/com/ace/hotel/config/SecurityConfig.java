package com.ace.hotel.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
                .requestMatchers("/", "/login", "/register", "/booking/search", "/error",
                                 "/css/**", "/js/**", "/images/**", "/videos/**", "/fonts/**").permitAll()

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
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            );

        // H2 Console in iframe during admin debugging & CSRF ignore for console/testing
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        // Eagerly resolve deferred CSRF token to prevent lazy session creation during template rendering
        http.addFilterAfter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrfToken != null) {
                    csrfToken.getToken();
                }
                filterChain.doFilter(request, response);
            }
        }, CsrfFilter.class);

        return http.build();
    }
}

