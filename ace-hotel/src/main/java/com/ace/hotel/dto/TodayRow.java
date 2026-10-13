package com.ace.hotel.dto;

import java.util.List;

/** Một dòng của bảng "Check-in / Check-out hôm nay". */
public record TodayRow(String code,
                       String customerName,
                       String roomLabel,
                       String checkIn,
                       String checkOut,
                       DisplayState state,
                       boolean highlight,
                       List<ActionLink> actions) {
}
