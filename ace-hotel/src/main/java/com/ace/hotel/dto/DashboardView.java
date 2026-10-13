package com.ace.hotel.dto;

import java.util.List;

/** Toàn bộ dữ liệu cho trang dashboard. */
public record DashboardView(long freeRooms,
                            long checkInToday,
                            long checkOutToday,
                            long servicesToday,
                            List<RoomCard> rooms,
                            List<TodayRow> rows) {
}
