package com.ace.hotel.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Nhãn ngày tiếng Việt, ví dụ "Thứ Hai, 12/10/2026". */
public final class DateLabels {

    private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DateLabels() {
    }

    public static String vietnameseDay(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Thứ Hai";
            case TUESDAY -> "Thứ Ba";
            case WEDNESDAY -> "Thứ Tư";
            case THURSDAY -> "Thứ Năm";
            case FRIDAY -> "Thứ Sáu";
            case SATURDAY -> "Thứ Bảy";
            case SUNDAY -> "Chủ Nhật";
        };
    }

    public static String label(LocalDate date) {
        return vietnameseDay(date.getDayOfWeek()) + ", " + date.format(DMY);
    }

    public static String today() {
        return label(LocalDate.now());
    }
}
