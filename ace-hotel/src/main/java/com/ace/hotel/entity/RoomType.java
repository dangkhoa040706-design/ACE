package com.ace.hotel.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room_types")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Long roomTypeId;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "price_per_night", nullable = false)
    private Long pricePerNight;

    @Column(name = "weekend_price_per_night", nullable = false)
    private Long weekendPricePerNight;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "bed_type", length = 100)
    private String bedType;

    @Column(length = 500)
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    public RoomType() {}

    public RoomType(Long roomTypeId, String name, Long pricePerNight, Long weekendPricePerNight,
                    Integer capacity, String description, String imageUrl) {
        this.roomTypeId = roomTypeId;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.weekendPricePerNight = weekendPricePerNight;
        this.capacity = capacity;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public RoomType(Long roomTypeId, String name, Long pricePerNight, Long weekendPricePerNight,
                    Integer capacity, String bedType, String description, String imageUrl) {
        this.roomTypeId = roomTypeId;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.weekendPricePerNight = weekendPricePerNight;
        this.capacity = capacity;
        this.bedType = bedType;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Long pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Long getWeekendPricePerNight() {
        return weekendPricePerNight;
    }

    public void setWeekendPricePerNight(Long weekendPricePerNight) {
        this.weekendPricePerNight = weekendPricePerNight;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getBedType() {
        if (bedType != null && !bedType.isBlank()) {
            return bedType;
        }
        if (name != null) {
            return switch (name.trim().toLowerCase()) {
                case "standard" -> "1 Giường đôi Queen (1m6) hoặc 2 Giường đơn";
                case "superior" -> "1 Giường đôi King (1m8) hoặc 2 Giường đơn";
                case "deluxe" -> "1 Giường King lớn (2m × 2m2) ban công";
                case "family" -> "2 Giường đôi Queen lớn (1m6 × 2m)";
                case "suite" -> "1 Giường King Master + 1 Sofa Bed cao cấp";
                default -> capacity != null && capacity >= 4 ? "2 Giường lớn" : "1 Giường đôi tiêu chuẩn";
            };
        }
        return "1 Giường đôi tiêu chuẩn";
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
