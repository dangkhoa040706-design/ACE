package com.ace.hotel.service;

import com.ace.hotel.entity.RoomType;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class PricingService {

    /**
     * BR3: Tính tổng tiền phòng gốc theo từng đêm lưu trú [checkIn, checkOut)
     * - Thứ 2 đến Thứ 5: áp dụng pricePerNight
     * - Thứ 6, Thứ 7, Chủ Nhật: áp dụng weekendPricePerNight
     */
    public long calculateRoomCharge(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        if (roomType == null || checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return roomType != null && roomType.getPricePerNight() != null ? roomType.getPricePerNight() : 0L;
        }
        long total = 0L;
        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
            DayOfWeek dow = date.getDayOfWeek();
            if (dow == DayOfWeek.FRIDAY || dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                total += roomType.getWeekendPricePerNight();
            } else {
                total += roomType.getPricePerNight();
            }
        }
        return total;
    }

    /**
     * BR6: Tính Phí phục vụ 5% (SVC) làm tròn số nguyên BIGINT
     */
    public long calculateSvc5Percent(long subtotal) {
        return Math.round(subtotal * 0.05);
    }

    /**
     * BR6: Tính Thuế GTGT 10% (VAT) trên tổng (Subtotal + SVC 5%)
     */
    public long calculateVat10Percent(long subtotal, long svcAmount) {
        return Math.round((subtotal + svcAmount) * 0.10);
    }

    /**
     * BR4: Tính Tiền cọc bắt buộc 30% cho Khách đặt trực tuyến (Online)
     */
    public long calculateOnlineDeposit30Percent(long grandTotalEstimate) {
        return Math.round(grandTotalEstimate * 0.30);
    }

    /**
     * Phụ thu nhận phòng sớm (Early Check-in): 0%, 30%, 50%, 100% của giá 1 đêm ngày thường
     */
    public long calculateEarlyCheckinFee(RoomType roomType, int percent) {
        if (roomType == null || percent <= 0) return 0L;
        long basePrice = roomType.getPricePerNight();
        if (percent >= 100) return basePrice;
        return Math.round(basePrice * (percent / 100.0));
    }

    /**
     * Phụ thu trả phòng trễ (Late Check-out): 0%, 30%, 50%, 100% của giá 1 đêm ngày thường
     */
    public long calculateLateCheckoutFee(RoomType roomType, int percent) {
        if (roomType == null || percent <= 0) return 0L;
        long basePrice = roomType.getPricePerNight();
        if (percent >= 100) return basePrice;
        return Math.round(basePrice * (percent / 100.0));
    }
}

