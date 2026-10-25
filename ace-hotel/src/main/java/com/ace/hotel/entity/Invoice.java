package com.ace.hotel.entity;

import com.ace.hotel.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(name = "room_charge", nullable = false)
    private Long roomCharge = 0L;

    @Column(name = "service_charge_total", nullable = false)
    private Long serviceChargeTotal = 0L;

    @Column(name = "early_checkin_fee", nullable = false)
    private Long earlyCheckinFee = 0L;

    @Column(name = "late_checkout_fee", nullable = false)
    private Long lateCheckoutFee = 0L;

    @Column(nullable = false)
    private Long subtotal = 0L;

    @Column(name = "service_charge_amount", nullable = false)
    private Long serviceChargeAmount = 0L;

    @Column(name = "vat_amount", nullable = false)
    private Long vatAmount = 0L;

    @Column(name = "grand_total", nullable = false)
    private Long grandTotal = 0L;

    @Column(name = "deposit_deducted", nullable = false)
    private Long depositDeducted = 0L;

    @Column(name = "balance_due", nullable = false)
    private Long balanceDue = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public Invoice() {}

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Long getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(Long roomCharge) {
        this.roomCharge = roomCharge;
    }

    public Long getServiceChargeTotal() {
        return serviceChargeTotal;
    }

    public void setServiceChargeTotal(Long serviceChargeTotal) {
        this.serviceChargeTotal = serviceChargeTotal;
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

    public Long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Long subtotal) {
        this.subtotal = subtotal;
    }

    public Long getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(Long serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public Long getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Long vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Long grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Long getDepositDeducted() {
        return depositDeducted;
    }

    public void setDepositDeducted(Long depositDeducted) {
        this.depositDeducted = depositDeducted;
    }

    public Long getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(Long balanceDue) {
        this.balanceDue = balanceDue;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
