package com.ace.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ace.hotel.repository.RoomTypeRepository;
import com.ace.hotel.repository.ServiceItemRepository;

/**
 * Landing page công khai. Chỉ giới thiệu, không đặt phòng. Chỉ đọc loại phòng và danh mục dịch vụ;
 * template chỉ in các cột công khai (tên, giá, sức chứa, mô tả, đơn vị).
 */
@Controller
public class LandingController {

    private final RoomTypeRepository roomTypeRepository;
    private final ServiceItemRepository serviceItemRepository;

    public LandingController(RoomTypeRepository roomTypeRepository, ServiceItemRepository serviceItemRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("roomTypes", roomTypeRepository.findAllByOrderByPricePerNightAsc());
        model.addAttribute("services", serviceItemRepository.findAllByOrderByNameAsc());
        return "landing";
    }
}
