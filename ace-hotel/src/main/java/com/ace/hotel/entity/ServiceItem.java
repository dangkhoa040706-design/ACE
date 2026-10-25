package com.ace.hotel.entity;

import com.ace.hotel.entity.enums.ServiceCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ServiceCategory category;

    @Column(name = "unit_price", nullable = false)
    private Long unitPrice;

    @Column(nullable = false, length = 30)
    private String unit;

    public ServiceItem() {}

    public ServiceItem(Long serviceId, String name, ServiceCategory category, Long unitPrice, String unit) {
        this.serviceId = serviceId;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.unit = unit;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
