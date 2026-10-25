package com.ace.hotel.dto;

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
    long remainingAtCounter
) {}
