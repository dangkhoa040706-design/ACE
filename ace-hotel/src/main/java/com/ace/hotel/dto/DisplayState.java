package com.ace.hotel.dto;

/**
 * Trạng thái HIỂN THỊ trên dashboard, suy ra từ rooms.status và bookings
 * (CSDL chỉ có AVAILABLE, OCCUPIED, MAINTENANCE). Xem plan 03-dashboard.md mục 4.
 */
public enum DisplayState {
    AVAILABLE("Phòng trống", "st-free"),
    RESERVED("Đã đặt", "st-reserved"),
    OCCUPIED("Đang ở", "st-occupied"),
    OVERDUE("Quá hạn", "st-overdue"),
    MAINTENANCE("Bảo trì", "st-maint"),
    DONE("Đã trả", "st-done");

    private final String label;
    private final String cssClass;

    DisplayState(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }

    public String getCssClass() {
        return cssClass;
    }
}
