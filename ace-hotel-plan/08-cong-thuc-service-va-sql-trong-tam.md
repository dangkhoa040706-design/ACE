# 08. Code Mẫu Tầng `Service`, Truy vấn `@Query` & Từ điển 35 Hằng số `MSG`

> **Mục tiêu:** Cung cấp sẵn toàn bộ các đoạn code Java xử lý thuật toán nghiệp vụ quan trọng nhất (Tính giá động ngày thường/cuối tuần, làm tròn số nguyên `BIGINT` cho biểu giá `++`, truy vấn tồn kho Quota hạng phòng, khóa dòng phòng chống Race Condition và class `MessageConstants.java` chứa đủ 35 thông báo tiếng Việt) để Khoa copy trực tiếp vào `ace-hotel/src/main/java/com/ace/hotel/`[cite: 10, 30, 53].

---

## 1. Class `PricingService.java` (Tính Giá Động T2–T5 / T6–CN & Biểu giá `++`)

~~~java
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
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return roomType.getPricePerNight();
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
}
~~~

---

## 2. Truy vấn Kiểm tra Tồn kho Hạng phòng (`BookingRepository.java` & `RoomRepository.java`)

### 2.1. Trong `RoomRepository.java`
~~~java
package com.ace.hotel.repository;

import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.enums.RoomStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // BR1: Đếm số phòng kinh doanh của 1 hạng phòng (loại trừ phòng đang MAINTENANCE)
    @Query("SELECT COUNT(r) FROM Room r WHERE r.roomType.roomTypeId = :typeId AND r.status <> 'MAINTENANCE'")
    int countSellableRoomsByType(@Param("typeId") Long typeId);

    // BR2: Lọc danh sách phòng vật lý đang trống sạch (AVAILABLE) để gán khi Check-in
    List<Room> findByRoomType_RoomTypeIdAndStatusOrderByRoomNumberAsc(Long roomTypeId, RoomStatus status);

    // Khóa dòng phòng khi thực hiện gán phòng Check-in để chống 2 lễ tân gán trùng
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Room r WHERE r.roomId = :roomId")
    Optional<Room> findByIdForUpdate(@Param("roomId") Long roomId);

    // UC07: Đếm tổng số phòng có thể kinh doanh toàn khách sạn (AVAILABLE + OCCUPIED + DIRTY)
    @Query("SELECT COUNT(r) FROM Room r WHERE r.status <> 'MAINTENANCE'")
    long countAllSellableRooms();

    long countByStatus(RoomStatus status);

    boolean existsByRoomType_RoomTypeId(Long roomTypeId);

    boolean existsByRoomNumberIgnoreCase(String roomNumber);
}
~~~

### 2.2. Trong `BookingRepository.java`
~~~java
package com.ace.hotel.repository;

import com.ace.hotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingCodeIgnoreCase(String bookingCode);

    List<Booking> findByCustomer_CustomerIdOrderByCreatedAtDesc(Long customerId);

    // BR1: Đếm số đơn đang giữ chỗ (RESERVED) hoặc đang ở (CHECKED_IN) giao khoảng ngày [checkIn, checkOut)
    @Query("""
        SELECT COUNT(b) FROM Booking b
        WHERE b.roomType.roomTypeId = :typeId
          AND b.status IN ('RESERVED', 'CHECKED_IN')
          AND b.checkInDate < :checkOut
          AND b.checkOutDate > :checkIn
    """)
    int countOverlappingActiveBookings(@Param("typeId") Long typeId,
                                       @Param("checkIn") LocalDate checkIn,
                                       @Param("checkOut") LocalDate checkOut);

    boolean existsByRoom_RoomId(Long roomId);

    boolean existsByRoomType_RoomTypeId(Long roomTypeId);
}
~~~

---

## 3. Hàm Tính Tồn kho Khả dụng (`BookingService.getAvailableQuota`)

~~~java
public int getAvailableQuota(Long roomTypeId, LocalDate checkIn, LocalDate checkOut) {
    int sellableRooms = roomRepository.countSellableRoomsByType(roomTypeId);
    int activeOverlapping = bookingRepository.countOverlappingActiveBookings(roomTypeId, checkIn, checkOut);
    return Math.max(0, sellableRooms - activeOverlapping);
}
~~~

---

## 4. Toàn văn Class `MessageConstants.java` (35 Thông báo Chuẩn Tiếng Việt `MSG-01`..`MSG-35`)

Đặt tại `com.ace.hotel.exception.MessageConstants.java` để dùng thống nhất trong toàn bộ `Service` và `Controller`[cite: 30]:

~~~java
package com.ace.hotel.exception;

public final class MessageConstants {

    private MessageConstants() {}

    // UC01: Đăng ký, Đăng nhập & Phân quyền 3 Role
    public static final String MSG_01 = "Sai tên đăng nhập hoặc mật khẩu.";
    public static final String MSG_02 = "Bạn đã đăng xuất khỏi hệ thống ACE Hotel.";
    public static final String MSG_03 = "Đăng ký tài khoản khách hàng thành công! Chào mừng bạn đến với ACE Hotel.";
    public static final String MSG_04 = "Tên đăng nhập này đã được sử dụng. Vui lòng chọn tên đăng nhập khác.";
    public static final String MSG_05 = "Bạn không có quyền truy cập vào phân hệ chức năng này.";

    // UC02: Quản lý Danh mục Phòng, Biểu giá & Dịch vụ
    public static final String MSG_06 = "Lưu thông tin hạng phòng và biểu giá thành công.";
    public static final String MSG_07 = "Đã xóa hạng phòng thành công.";
    public static final String MSG_08 = "Lưu thông tin phòng vật lý thành công.";
    public static final String MSG_09 = "Đã xóa phòng vật lý thành công.";
    public static final String MSG_10 = "Lưu danh mục dịch vụ thành công.";
    public static final String MSG_11 = "Đã xóa dịch vụ khỏi danh mục.";
    public static final String MSG_12 = "Số phòng hoặc tên danh mục đã tồn tại. Vui lòng kiểm tra lại.";
    public static final String MSG_13 = "Đơn giá ngày thường, giá cuối tuần phải lớn hơn 0 VNĐ và sức chứa tối thiểu là 1 khách.";
    public static final String MSG_14 = "Không thể xóa hạng phòng này vì đang có phòng vật lý hoặc đơn đặt phòng tham chiếu.";
    public static final String MSG_15 = "Không thể xóa phòng đã có lịch sử đặt phòng. Vui lòng chuyển phòng sang trạng thái Bảo trì nếu ngưng khai thác.";
    public static final String MSG_16 = "Không thể chuyển sang trạng thái Bảo trì vì phòng đang có khách lưu trú (OCCUPIED).";
    public static final String MSG_17 = "Không thể xóa dịch vụ này vì đã phát sinh trong lịch sử Room Folio của khách hàng.";

    // UC03: Tìm kiếm & Đặt phòng trực tuyến (Quota, Cọc 30%, QR)
    public static final String MSG_18 = "Đặt phòng và thanh toán cọc 30%% thành công! Mã đặt phòng của quý khách là %s.";
    public static final String MSG_19 = "Đã hủy đơn đặt phòng giữ chỗ %s thành công.";
    public static final String MSG_20 = "Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 đêm.";
    public static final String MSG_21 = "Không còn hạng phòng trống phù hợp trong khoảng ngày và số lượng khách đã chọn.";
    public static final String MSG_22 = "Hạng phòng này vừa hết suất trống trong khoảng ngày bạn chọn. Vui lòng chọn hạng phòng khác.";
    public static final String MSG_23 = "Chỉ có thể hủy đơn đặt phòng khi đang ở trạng thái Đã xác nhận giữ chỗ (RESERVED).";

    // UC04: Quản lý Check-in, Gán phòng sạch & Tiếp nhận Walk-in 0đ cọc
    public static final String MSG_24 = "Check-in thành công! Đã gán Phòng %s (Sạch) cho đơn đặt %s và kích hoạt Room Folio.";
    public static final String MSG_25 = "Tiếp nhận khách vãng lai (Walk-in) thành công! Đã nhận Phòng %s (Tiền cọc: 0đ – Quyết toán 100%% khi trả phòng).";
    public static final String MSG_26 = "Không tìm thấy mã đặt phòng %s hoặc đơn không ở trạng thái chờ nhận phòng (RESERVED).";
    public static final String MSG_27 = "Hiện chưa có phòng vật lý nào thuộc hạng phòng này ở trạng thái Trống sạch (AVAILABLE). Vui lòng xác nhận dọn sạch phòng DIRTY trước khi gán!";
    public static final String MSG_28 = "Phòng %s vừa được gán cho khách khác hoặc không ở trạng thái sạch (AVAILABLE).";

    // UC05: Check-out, Quyết toán ++ & Dọn phòng DIRTY
    public static final String MSG_29 = "Quyết toán trả phòng thành công! Đã lập Hóa đơn VAT #%s và chuyển Phòng %s sang trạng thái Chờ dọn dẹp (DIRTY).";
    public static final String MSG_30 = "Đã xác nhận dọn vệ sinh hoàn tất! Phòng %s đã chuyển về trạng thái Trống sạch (AVAILABLE), sẵn sàng đón khách mới.";
    public static final String MSG_31 = "Chỉ có thể thực hiện quyết toán trả phòng cho đơn đang ở trạng thái Đang lưu trú (CHECKED_IN).";

    // UC06: Ghi nhận dịch vụ lưu trú vào Room Folio
    public static final String MSG_32 = "Đã ghi nhận dịch vụ vào Room Folio của Phòng %s thành công.";
    public static final String MSG_33 = "Đã xóa dòng dịch vụ khỏi Room Folio thành công.";
    public static final String MSG_34 = "Chỉ được phép thêm hoặc xóa dịch vụ trong Room Folio khi phòng đang có khách lưu trú (CHECKED_IN).";
    public static final String MSG_35 = "Số lượng dịch vụ sử dụng phải lớn hơn hoặc bằng 1.";
}
~~~