package com.ace.hotel.entity;

/** Trạng thái đặt phòng (bookings.status). */
public enum BookingStatus {
    BOOKED("Đã đặt"),
    CHECKED_IN("Đang ở"),
    CHECKED_OUT("Đã trả"),
    CANCELLED("Đã hủy");

    private final String label;

    BookingStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
