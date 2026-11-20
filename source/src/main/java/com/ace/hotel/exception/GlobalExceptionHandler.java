package com.ace.hotel.exception;

import com.ace.hotel.config.RoleBasedAuthSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String extractSafeInternalPath(String referer, HttpServletRequest request) {
        if (referer == null || referer.isBlank()) return null;
        try {
            URI uri = URI.create(referer.trim());
            if (uri.getHost() != null && !uri.getHost().equalsIgnoreCase(request.getServerName())) {
                return null;
            }
            String path = uri.getRawPath();
            if (path == null || path.isBlank()) return null;
            String candidate = uri.getRawQuery() != null ? path + "?" + uri.getRawQuery() : path;
            return RoleBasedAuthSuccessHandler.isSafeRedirectUrl(candidate) ? candidate : null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
        log.warn("Business exception caught: {}", ex.getMessage());
        if (response.isCommitted()) {
            return null;
        }
        String safeReferer = extractSafeInternalPath(request.getHeader("Referer"), request);
        if (safeReferer != null && !request.getMethod().equalsIgnoreCase("GET")) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:" + safeReferer;
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("pageTitle", "Lỗi Xử Lý Nghiệp Vụ");
        return "error/500";
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public String handleNoResourceFoundException(org.springframework.web.servlet.resource.NoResourceFoundException ex, HttpServletResponse response, Model model) {
        log.warn("Resource/Route not found: {}", ex.getResourcePath());
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        model.addAttribute("errorMessage", "Không tìm thấy trang hoặc tài nguyên yêu cầu. Vui lòng kiểm tra lại đường dẫn.");
        model.addAttribute("pageTitle", "404 - Không Tìm Thấy");
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, HttpServletResponse response, Model model) {
        if (response.isCommitted()) {
            log.warn("Response already committed, skipping error view rendering: {}", ex.getMessage());
            return null;
        }
        log.error("Unhandled exception caught: {}", ex.getMessage(), ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorMessage", "Đã xảy ra sự cố hệ thống. Vui lòng liên hệ ban quản trị ACE Hotel.");
        model.addAttribute("pageTitle", "Lỗi Hệ Thống");
        return "error/500";
    }
}
