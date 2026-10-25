package com.ace.hotel.dto;

import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Room;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminHomeDashboardDto {

    private String greetingTitle;
    private String greetingPeriod;
    private String greetingQuote;
    private String currentDateText;

    // KPI 1: Công suất phòng
    private double occupancyRate;
    private double targetOccupancyRate = 85.0;
    private long totalRooms;
    private long sellableRooms;
    private long occupiedRooms;
    private long availableRooms;
    private long dirtyRooms;
    private long maintenanceRooms;

    // KPI 2: Doanh thu thực tế
    private Long todayRevenue = 0L;
    private Long monthRevenue = 0L;
    private int todayInvoicesCount = 0;
    private int monthInvoicesCount = 0;

    // KPI 4: Lưu lượng khách hôm nay
    private long arrivalsCount = 0;
    private long departuresCount = 0;
    private long inHouseCount = 0;

    // Giám sát bổ trợ
    private List<Booking> recentBookings = new ArrayList<>();
    private List<Room> dirtyRoomsList = new ArrayList<>();
    private List<Room> allRoomsList = new ArrayList<>();
    private Map<Integer, List<Room>> roomsByFloor = new LinkedHashMap<>();

    public AdminHomeDashboardDto() {}

    public String getGreetingTitle() {
        return greetingTitle;
    }

    public void setGreetingTitle(String greetingTitle) {
        this.greetingTitle = greetingTitle;
    }

    public String getGreetingPeriod() {
        return greetingPeriod;
    }

    public void setGreetingPeriod(String greetingPeriod) {
        this.greetingPeriod = greetingPeriod;
    }

    public String getGreetingQuote() {
        return greetingQuote;
    }

    public void setGreetingQuote(String greetingQuote) {
        this.greetingQuote = greetingQuote;
    }

    public String getCurrentDateText() {
        return currentDateText;
    }

    public void setCurrentDateText(String currentDateText) {
        this.currentDateText = currentDateText;
    }

    public double getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public double getTargetOccupancyRate() {
        return targetOccupancyRate;
    }

    public void setTargetOccupancyRate(double targetOccupancyRate) {
        this.targetOccupancyRate = targetOccupancyRate;
    }

    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getSellableRooms() {
        return sellableRooms;
    }

    public void setSellableRooms(long sellableRooms) {
        this.sellableRooms = sellableRooms;
    }

    public long getOccupiedRooms() {
        return occupiedRooms;
    }

    public void setOccupiedRooms(long occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
    }

    public long getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(long availableRooms) {
        this.availableRooms = availableRooms;
    }

    public long getDirtyRooms() {
        return dirtyRooms;
    }

    public void setDirtyRooms(long dirtyRooms) {
        this.dirtyRooms = dirtyRooms;
    }

    public long getMaintenanceRooms() {
        return maintenanceRooms;
    }

    public void setMaintenanceRooms(long maintenanceRooms) {
        this.maintenanceRooms = maintenanceRooms;
    }

    public Long getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(Long todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public Long getMonthRevenue() {
        return monthRevenue;
    }

    public void setMonthRevenue(Long monthRevenue) {
        this.monthRevenue = monthRevenue;
    }

    public int getTodayInvoicesCount() {
        return todayInvoicesCount;
    }

    public void setTodayInvoicesCount(int todayInvoicesCount) {
        this.todayInvoicesCount = todayInvoicesCount;
    }

    public int getMonthInvoicesCount() {
        return monthInvoicesCount;
    }

    public void setMonthInvoicesCount(int monthInvoicesCount) {
        this.monthInvoicesCount = monthInvoicesCount;
    }

    public long getArrivalsCount() {
        return arrivalsCount;
    }

    public void setArrivalsCount(long arrivalsCount) {
        this.arrivalsCount = arrivalsCount;
    }

    public long getDeparturesCount() {
        return departuresCount;
    }

    public void setDeparturesCount(long departuresCount) {
        this.departuresCount = departuresCount;
    }

    public long getInHouseCount() {
        return inHouseCount;
    }

    public void setInHouseCount(long inHouseCount) {
        this.inHouseCount = inHouseCount;
    }

    public List<Booking> getRecentBookings() {
        return recentBookings;
    }

    public void setRecentBookings(List<Booking> recentBookings) {
        this.recentBookings = recentBookings;
    }

    public List<Room> getDirtyRoomsList() {
        return dirtyRoomsList;
    }

    public void setDirtyRoomsList(List<Room> dirtyRoomsList) {
        this.dirtyRoomsList = dirtyRoomsList;
    }

    public List<Room> getAllRoomsList() {
        return allRoomsList;
    }

    public void setAllRoomsList(List<Room> allRoomsList) {
        this.allRoomsList = allRoomsList;
    }

    public Map<Integer, List<Room>> getRoomsByFloor() {
        return roomsByFloor;
    }

    public void setRoomsByFloor(Map<Integer, List<Room>> roomsByFloor) {
        this.roomsByFloor = roomsByFloor;
    }
}
