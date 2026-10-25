package com.ace.hotel.dto;

import com.ace.hotel.entity.ServiceOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CheckOutSummaryDto {

    private Long bookingId;
    private String bookingCode;
    private String customerName;
    private String customerPhone;
    private String roomNumber;
    private String roomTypeName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime actualCheckIn;
    private LocalDateTime actualCheckOut;
    private long nights;

    // Chi tiết chi phí gốc (Base)
    private long roomCharge;
    private long serviceChargeTotal;
    private long earlyCheckinFee;
    private long suggestedLateFee;
    private long lateCheckoutFee;
    private long subtotal;

    // Biểu giá ++ (SVC 5% và VAT 10%)
    private long serviceChargeAmount;
    private long vatAmount;
    private long grandTotal;

    // Khấu trừ cọc & Thực thu tại quầy
    private long depositDeducted;
    private long balanceDue;

    private List<ServiceOrder> serviceOrders;

    public CheckOutSummaryDto() {}

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
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

    public LocalDateTime getActualCheckIn() {
        return actualCheckIn;
    }

    public void setActualCheckIn(LocalDateTime actualCheckIn) {
        this.actualCheckIn = actualCheckIn;
    }

    public LocalDateTime getActualCheckOut() {
        return actualCheckOut;
    }

    public void setActualCheckOut(LocalDateTime actualCheckOut) {
        this.actualCheckOut = actualCheckOut;
    }

    public long getNights() {
        return nights;
    }

    public void setNights(long nights) {
        this.nights = nights;
    }

    public long getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(long roomCharge) {
        this.roomCharge = roomCharge;
    }

    public long getServiceChargeTotal() {
        return serviceChargeTotal;
    }

    public void setServiceChargeTotal(long serviceChargeTotal) {
        this.serviceChargeTotal = serviceChargeTotal;
    }

    public long getEarlyCheckinFee() {
        return earlyCheckinFee;
    }

    public void setEarlyCheckinFee(long earlyCheckinFee) {
        this.earlyCheckinFee = earlyCheckinFee;
    }

    public long getSuggestedLateFee() {
        return suggestedLateFee;
    }

    public void setSuggestedLateFee(long suggestedLateFee) {
        this.suggestedLateFee = suggestedLateFee;
    }

    public long getLateCheckoutFee() {
        return lateCheckoutFee;
    }

    public void setLateCheckoutFee(long lateCheckoutFee) {
        this.lateCheckoutFee = lateCheckoutFee;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(long subtotal) {
        this.subtotal = subtotal;
    }

    public long getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(long serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public long getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(long vatAmount) {
        this.vatAmount = vatAmount;
    }

    public long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public long getDepositDeducted() {
        return depositDeducted;
    }

    public void setDepositDeducted(long depositDeducted) {
        this.depositDeducted = depositDeducted;
    }

    public long getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(long balanceDue) {
        this.balanceDue = balanceDue;
    }

    public List<ServiceOrder> getServiceOrders() {
        return serviceOrders;
    }

    public void setServiceOrders(List<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }
}
