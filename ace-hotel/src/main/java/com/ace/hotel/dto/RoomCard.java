package com.ace.hotel.dto;

import java.util.List;

/** Thẻ phòng trên dashboard. */
public record RoomCard(String roomNumber,
                       String typeName,
                       int capacity,
                       long pricePerNight,
                       DisplayState state,
                       List<String> chips,
                       List<ActionLink> actions) {
}
