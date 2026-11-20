package com.ace.hotel.dto;

import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.entity.User;

import java.util.List;

public class CustomerDashboardDto {

    private Customer customer;
    private User user;
    private String memberCode;
    private String loyaltyTier;
    private int loyaltyTierLevel; // 1: Member, 2: Gold, 3: Obsidian VIP

    private int totalBookings;
    private int upcomingCount;
    private int inHouseCount;
    private int completedCount;
    private int cancelledCount;
    private long totalNightsStayed;
    private long totalDepositPaid;

    private List<Booking> allBookings;
    private List<Booking> upcomingBookings;
    private List<Booking> inHouseBookings;
    private List<Booking> completedBookings;
    private List<Booking> cancelledBookings;

    public CustomerDashboardDto() {}

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(String loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }

    public int getLoyaltyTierLevel() {
        return loyaltyTierLevel;
    }

    public void setLoyaltyTierLevel(int loyaltyTierLevel) {
        this.loyaltyTierLevel = loyaltyTierLevel;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public int getUpcomingCount() {
        return upcomingCount;
    }

    public void setUpcomingCount(int upcomingCount) {
        this.upcomingCount = upcomingCount;
    }

    public int getInHouseCount() {
        return inHouseCount;
    }

    public void setInHouseCount(int inHouseCount) {
        this.inHouseCount = inHouseCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getCancelledCount() {
        return cancelledCount;
    }

    public void setCancelledCount(int cancelledCount) {
        this.cancelledCount = cancelledCount;
    }

    public long getTotalNightsStayed() {
        return totalNightsStayed;
    }

    public void setTotalNightsStayed(long totalNightsStayed) {
        this.totalNightsStayed = totalNightsStayed;
    }

    public long getTotalDepositPaid() {
        return totalDepositPaid;
    }

    public void setTotalDepositPaid(long totalDepositPaid) {
        this.totalDepositPaid = totalDepositPaid;
    }

    public List<Booking> getAllBookings() {
        return allBookings;
    }

    public void setAllBookings(List<Booking> allBookings) {
        this.allBookings = allBookings;
    }

    public List<Booking> getUpcomingBookings() {
        return upcomingBookings;
    }

    public void setUpcomingBookings(List<Booking> upcomingBookings) {
        this.upcomingBookings = upcomingBookings;
    }

    public List<Booking> getInHouseBookings() {
        return inHouseBookings;
    }

    public void setInHouseBookings(List<Booking> inHouseBookings) {
        this.inHouseBookings = inHouseBookings;
    }

    public List<Booking> getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(List<Booking> completedBookings) {
        this.completedBookings = completedBookings;
    }

    public List<Booking> getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(List<Booking> cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }
}

