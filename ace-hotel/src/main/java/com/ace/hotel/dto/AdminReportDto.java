package com.ace.hotel.dto;

import com.ace.hotel.entity.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AdminReportDto {

    private LocalDate fromDate;
    private LocalDate toDate;

    // Chỉ số Công suất phòng (Operational KPIs)
    private long totalRooms;           // Tổng số phòng vật lý (12)
    private long maintenanceRooms;     // Số phòng đang bảo trì (1)
    private long sellableRooms;        // R_sellable = 11 (loại trừ bảo trì)
    private long occupiedRooms;        // Số phòng đang OCCUPIED
    private double occupancyRate;      // OR% = occupiedRooms / R_sellable * 100%
    private long totalInvoicesCount;   // Số lượng hóa đơn quyết toán
    private long totalNightsPaid;      // Tổng số đêm phòng đã thanh toán
    private long adr;                  // Average Daily Rate (VNĐ)
    private long revPar;               // Revenue Per Available Room (VNĐ)

    // Cơ cấu Doanh thu Đa chiều (Financial Revenue Breakdown ++)
    private long totalRoomRevenue;     // 1. Tiền phòng gốc
    private long revenueMinibar;       // 2a. Dịch vụ Minibar
    private long revenueLaundry;       // 2b. Dịch vụ Giặt ủi
    private long revenueFb;            // 2c. Dịch vụ Ẩm thực & Bữa sáng
    private long revenueDamages;       // 2d. Đền bù tài sản
    private long totalServiceRevenue;  // Tổng 4 nhóm dịch vụ
    private long totalSurchargeRevenue;// 3. Phụ thu sớm & trễ
    private long totalSubtotal;        // Tổng doanh thu gốc (Base)
    private long totalSvcRevenue;      // 4. Phí phục vụ (SVC 5%)
    private long totalVatRevenue;      // 5. Thuế GTGT (VAT 10%)
    private long totalGrandRevenue;    // 6. Tổng cộng doanh thu (Grand Total ++)
    private long totalDepositDeducted; // Cọc đã khấu trừ
    private long totalBalanceDue;      // Thực thu tại quầy

    // Thống kê theo phương thức thanh toán
    private Map<String, Long> paymentMethodTotals;

    // Danh sách hóa đơn trong kỳ lọc
    private List<Invoice> invoices;

    public AdminReportDto() {}

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getMaintenanceRooms() {
        return maintenanceRooms;
    }

    public void setMaintenanceRooms(long maintenanceRooms) {
        this.maintenanceRooms = maintenanceRooms;
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

    public double getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(double occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public long getTotalInvoicesCount() {
        return totalInvoicesCount;
    }

    public void setTotalInvoicesCount(long totalInvoicesCount) {
        this.totalInvoicesCount = totalInvoicesCount;
    }

    public long getTotalNightsPaid() {
        return totalNightsPaid;
    }

    public void setTotalNightsPaid(long totalNightsPaid) {
        this.totalNightsPaid = totalNightsPaid;
    }

    public long getAdr() {
        return adr;
    }

    public void setAdr(long adr) {
        this.adr = adr;
    }

    public long getRevPar() {
        return revPar;
    }

    public void setRevPar(long revPar) {
        this.revPar = revPar;
    }

    public long getTotalRoomRevenue() {
        return totalRoomRevenue;
    }

    public void setTotalRoomRevenue(long totalRoomRevenue) {
        this.totalRoomRevenue = totalRoomRevenue;
    }

    public long getRevenueMinibar() {
        return revenueMinibar;
    }

    public void setRevenueMinibar(long revenueMinibar) {
        this.revenueMinibar = revenueMinibar;
    }

    public long getRevenueLaundry() {
        return revenueLaundry;
    }

    public void setRevenueLaundry(long revenueLaundry) {
        this.revenueLaundry = revenueLaundry;
    }

    public long getRevenueFb() {
        return revenueFb;
    }

    public void setRevenueFb(long revenueFb) {
        this.revenueFb = revenueFb;
    }

    public long getRevenueDamages() {
        return revenueDamages;
    }

    public void setRevenueDamages(long revenueDamages) {
        this.revenueDamages = revenueDamages;
    }

    public long getTotalServiceRevenue() {
        return totalServiceRevenue;
    }

    public void setTotalServiceRevenue(long totalServiceRevenue) {
        this.totalServiceRevenue = totalServiceRevenue;
    }

    public long getTotalSurchargeRevenue() {
        return totalSurchargeRevenue;
    }

    public void setTotalSurchargeRevenue(long totalSurchargeRevenue) {
        this.totalSurchargeRevenue = totalSurchargeRevenue;
    }

    public long getTotalSubtotal() {
        return totalSubtotal;
    }

    public void setTotalSubtotal(long totalSubtotal) {
        this.totalSubtotal = totalSubtotal;
    }

    public long getTotalSvcRevenue() {
        return totalSvcRevenue;
    }

    public void setTotalSvcRevenue(long totalSvcRevenue) {
        this.totalSvcRevenue = totalSvcRevenue;
    }

    public long getTotalVatRevenue() {
        return totalVatRevenue;
    }

    public void setTotalVatRevenue(long totalVatRevenue) {
        this.totalVatRevenue = totalVatRevenue;
    }

    public long getTotalGrandRevenue() {
        return totalGrandRevenue;
    }

    public void setTotalGrandRevenue(long totalGrandRevenue) {
        this.totalGrandRevenue = totalGrandRevenue;
    }

    public long getTotalDepositDeducted() {
        return totalDepositDeducted;
    }

    public void setTotalDepositDeducted(long totalDepositDeducted) {
        this.totalDepositDeducted = totalDepositDeducted;
    }

    public long getTotalBalanceDue() {
        return totalBalanceDue;
    }

    public void setTotalBalanceDue(long totalBalanceDue) {
        this.totalBalanceDue = totalBalanceDue;
    }

    public Map<String, Long> getPaymentMethodTotals() {
        return paymentMethodTotals;
    }

    public void setPaymentMethodTotals(Map<String, Long> paymentMethodTotals) {
        this.paymentMethodTotals = paymentMethodTotals;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
