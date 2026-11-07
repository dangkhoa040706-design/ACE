package com.ace.hotel.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    public static boolean isSafeRedirectUrl(String url) {
        if (url == null || url.isBlank()) return false;
        String trimmed = url.trim();
        if (!trimmed.startsWith("/") || trimmed.startsWith("//") || trimmed.startsWith("/\\")) {
            return false;
        }
        int queryIdx = trimmed.indexOf('?');
        String pathPart = queryIdx >= 0 ? trimmed.substring(0, queryIdx) : trimmed;
        if (pathPart.contains(":") || pathPart.contains("\\")) {
            return false;
        }
        return !trimmed.contains("://");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 1. Kiểm tra URL chuyển hướng ưu tiên lưu trong Session (chỉ chấp nhận relative URL an toàn nội bộ)
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object sessionRedirect = session.getAttribute("REDIRECT_URL");
            if (sessionRedirect instanceof String redirectUrl && !redirectUrl.isBlank()) {
                session.removeAttribute("REDIRECT_URL");
                if (isSafeRedirectUrl(redirectUrl)) {
                    response.sendRedirect(request.getContextPath() + redirectUrl);
                    return;
                }
            }
        }

        // 2. Kiểm tra SavedRequest của Spring Security (nếu bị intercept từ trang có bảo vệ)
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null && savedRequest.getRedirectUrl() != null && !savedRequest.getRedirectUrl().contains("/login")) {
            response.sendRedirect(savedRequest.getRedirectUrl());
            return;
        }

        // 3. Phân quyền chuyển hướng mặc định theo Role
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
