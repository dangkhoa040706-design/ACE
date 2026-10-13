package com.ace.hotel.entity;

/** Hình thức thanh toán (invoices.payment_method). */
public enum PaymentMethod {
    CASH("Tiền mặt"),
    CARD("Thẻ"),
    TRANSFER("Chuyển khoản");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
