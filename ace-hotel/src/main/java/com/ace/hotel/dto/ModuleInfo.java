package com.ace.hotel.dto;

/** Thông tin hiển thị trên trang "đang xây dựng" của các màn hình chưa làm. */
public record ModuleInfo(String title, String useCase, String screens, String week, String active) {
}
