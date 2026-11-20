package com.ace.hotel.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class OnlineBookingRequestDto {

    private Long roomTypeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime estimatedCheckInTime = LocalTime.of(14, 0);

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime estimatedCheckOutTime = LocalTime.of(12, 0);

    private Integer guestCount = 1;

    private String paymentMethod = "TRANSFER";

    private String fullName;
    private String phone;
    private String email;
    private String idNumber;
    private Long roomId;
    private String roomNumber;
    private String specialRequests;

    public OnlineBookingRequestDto() {}

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public LocalTime getEstimatedCheckInTime() {
        return estimatedCheckInTime;
    }

    public void setEstimatedCheckInTime(LocalTime estimatedCheckInTime) {
        this.estimatedCheckInTime = estimatedCheckInTime;
    }

    public LocalTime getEstimatedCheckOutTime() {
        return estimatedCheckOutTime;
    }

    public void setEstimatedCheckOutTime(LocalTime estimatedCheckOutTime) {
        this.estimatedCheckOutTime = estimatedCheckOutTime;
    }
}
