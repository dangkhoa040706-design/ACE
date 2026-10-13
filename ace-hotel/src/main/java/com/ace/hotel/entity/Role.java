package com.ace.hotel.entity;

/** Vai trò người dùng (users.role). */
public enum Role {
    RECEPTIONIST("Lễ tân"),
    MANAGER("Quản lý");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
