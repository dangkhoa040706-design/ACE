package com.ace.hotel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
        log.warn("Business exception caught: {}", ex.getMessage());
        if (response.isCommitted()) {
            return null;
        }
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank() && !request.getMethod().equalsIgnoreCase("GET")) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:" + referer;
        }
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("pageTitle", "Lỗi Xử Lý Nghiệp Vụ");
        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, HttpServletResponse response, Model model) {
        if (response.isCommitted()) {
            log.warn("Response already committed, skipping error view rendering: {}", ex.getMessage());
            return null;
        }
        log.error("Unhandled exception caught: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "Đã xảy ra sự cố hệ thống. Vui lòng liên hệ ban quản trị ACE Hotel.");
        model.addAttribute("pageTitle", "Lỗi Hệ Thống");
        return "error/500";
    }
}
