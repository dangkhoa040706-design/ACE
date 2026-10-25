package com.ace.hotel.entity;

import com.ace.hotel.entity.enums.BookingStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_bookings_quota", columnList = "room_type_id, status, check_in_date, check_out_date")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "booking_code", nullable = false, unique = true, length = 20)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "guest_count", nullable = false)
    private Integer guestCount = 1;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "estimated_check_in_time")
    private LocalTime estimatedCheckInTime = LocalTime.of(14, 0);

    @Column(name = "estimated_check_out_time")
    private LocalTime estimatedCheckOutTime = LocalTime.of(12, 0);

    @Column(name = "actual_check_in")
    private LocalDateTime actualCheckIn;

    @Column(name = "actual_check_out")
    private LocalDateTime actualCheckOut;

    @Column(name = "room_charge", nullable = false)
    private Long roomCharge = 0L;

    @Column(name = "deposit_amount", nullable = false)
    private Long depositAmount = 0L;

    @Column(name = "early_checkin_fee", nullable = false)
    private Long earlyCheckinFee = 0L;

    @Column(name = "late_checkout_fee", nullable = false)
    private Long lateCheckoutFee = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.RESERVED;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Booking() {}

    public long getNights() {
        if (checkInDate == null || checkOutDate == null) return 1;
        return Math.max(1, ChronoUnit.DAYS.between(checkInDate, checkOutDate));
    }

    public boolean isOverdue() {
        return status == BookingStatus.CHECKED_IN
            && checkOutDate != null
            && checkOutDate.isBefore(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")));
    }

    public boolean isWalkIn() {
        return depositAmount != null && depositAmount == 0L;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalTime getEstimatedCheckInTime() {
        return estimatedCheckInTime;
    }

    public void setEstimatedCheckInTime(LocalTime estimatedCheckInTime) {
        this.estimatedCheckInTime = estimatedCheckInTime;
    }

    public LocalTime getEstimatedCheckOutTime() {
        return estimatedCheckOutTime;
    }

    public void setEstimatedCheckOutTime(LocalTime estimatedCheckOutTime) {
        this.estimatedCheckOutTime = estimatedCheckOutTime;
    }

    public LocalDateTime getActualCheckIn() {
        return actualCheckIn;
    }

    public void setActualCheckIn(LocalDateTime actualCheckIn) {
        this.actualCheckIn = actualCheckIn;
    }

    public LocalDateTime getActualCheckOut() {
        return actualCheckOut;
    }

    public void setActualCheckOut(LocalDateTime actualCheckOut) {
        this.actualCheckOut = actualCheckOut;
    }

    public Long getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(Long roomCharge) {
        this.roomCharge = roomCharge;
    }

    public Long getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Long depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Long getEarlyCheckinFee() {
        return earlyCheckinFee;
    }

    public void setEarlyCheckinFee(Long earlyCheckinFee) {
        this.earlyCheckinFee = earlyCheckinFee;
    }

    public Long getLateCheckoutFee() {
        return lateCheckoutFee;
    }

    public void setLateCheckoutFee(Long lateCheckoutFee) {
        this.lateCheckoutFee = lateCheckoutFee;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
