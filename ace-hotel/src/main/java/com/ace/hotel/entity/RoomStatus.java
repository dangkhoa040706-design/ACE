package com.ace.hotel.entity;

/** Trạng thái phòng trong CSDL (rooms.status). */
public enum RoomStatus {
    AVAILABLE("Trống"),
    OCCUPIED("Đang ở"),
    MAINTENANCE("Bảo trì");

    private final String label;

    RoomStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
