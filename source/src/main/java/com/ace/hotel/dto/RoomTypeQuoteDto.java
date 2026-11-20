package com.ace.hotel.dto;

import java.util.Collections;
import java.util.List;

public record RoomTypeQuoteDto(
    Long roomTypeId,
    String name,
    int capacity,
    String bedType,
    String description,
    String imageUrl,
    long pricePerNight,
    long weekendPricePerNight,
    int availableQuota,
    long nights,
    long roomCharge,
    long estSvc,
    long estVat,
    long estGrandTotal,
    long depositAmount,
    long remainingAtCounter,
    String suggestedRoomNumber,
    List<String> availableRoomNumbers
) {
    public RoomTypeQuoteDto(Long roomTypeId, String name, int capacity, String bedType, String description,
                            String imageUrl, long pricePerNight, long weekendPricePerNight, int availableQuota,
                            long nights, long roomCharge, long estSvc, long estVat, long estGrandTotal,
                            long depositAmount, long remainingAtCounter) {
        this(roomTypeId, name, capacity, bedType, description, imageUrl, pricePerNight, weekendPricePerNight,
             availableQuota, nights, roomCharge, estSvc, estVat, estGrandTotal, depositAmount, remainingAtCounter,
             "P." + (roomTypeId * 100 + 1) + " (Tầng " + roomTypeId + ")", Collections.emptyList());
    }
}
