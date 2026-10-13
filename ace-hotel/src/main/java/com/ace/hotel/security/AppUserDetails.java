package com.ace.hotel.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/** Người dùng đã đăng nhập: thêm họ tên và nhãn vai trò để giao diện hiển thị. */
public class AppUserDetails extends User {

    private final String fullName;
    private final String roleLabel;

    public AppUserDetails(String username, String passwordHash,
                          Collection<? extends GrantedAuthority> authorities,
                          String fullName, String roleLabel) {
        super(username, passwordHash, authorities);
        this.fullName = fullName;
        this.roleLabel = roleLabel;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRoleLabel() {
        return roleLabel;
    }
}
