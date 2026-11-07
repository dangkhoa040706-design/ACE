package com.ace.hotel.util;

import com.ace.hotel.exception.BusinessException;
import java.time.LocalDate;

/**
 * Tiện ích kiểm tra tính hợp lệ dữ liệu (Validation Rules) chuẩn hóa toàn hệ thống.
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    /**
     * BR-AUTH: Kiểm tra độ phức tạp của mật khẩu chuẩn 5 sao
     * - Tối thiểu 8 ký tự
     * - Ít nhất 1 chữ cái in hoa [A-Z]
     * - Ít nhất 1 chữ số [0-9]
     * - Ít nhất 1 ký tự đặc biệt
     * - Không chứa khoảng trắng (dấu cách)
     */
    public static void validatePasswordStrength(String password, String fieldLabel) {
        if (password == null || password.isEmpty()) {
            throw new BusinessException(fieldLabel + " không được để trống.");
        }
        if (password.contains(" ")) {
            throw new BusinessException(fieldLabel + " không được chứa khoảng trắng (dấu cách).");
        }
        if (password.length() < 8 
                || !password.matches(".*[A-Z].*") 
                || !password.matches(".*[0-9].*") 
                || !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`].*")) {
            throw new BusinessException(fieldLabel + " phải có ít nhất 8 ký tự, bao gồm ít nhất 1 chữ in hoa, 1 chữ số và 1 ký tự đặc biệt (không tính dấu cách).");
        }
    }

    /**
     * BR-AGE: Ràng buộc độ tuổi tối thiểu từ đủ 18 tuổi trở lên
     */
    public static void validateMinimumAge(LocalDate dateOfBirth, String errorMessage) {
        if (dateOfBirth != null) {
            LocalDate minAdultDate = LocalDate.now().minusYears(18);
            if (dateOfBirth.isAfter(minAdultDate)) {
                throw new BusinessException(errorMessage);
            }
        }
    }
}
