package com.ace.hotel.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class CustomerProfileDto {

    private String fullName;
    private String phone;
    private String idNumber;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    private String email;

    // Đổi mật khẩu
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    public CustomerProfileDto() {}

    public CustomerProfileDto(String fullName, String phone, String idNumber, LocalDate dateOfBirth, String email) {
        this.fullName = fullName;
        this.phone = phone;
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}

