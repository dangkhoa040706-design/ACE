package com.ace.hotel.controller;

import com.ace.hotel.entity.RoomType;
import com.ace.hotel.entity.ServiceItem;
import com.ace.hotel.entity.enums.ServiceCategory;
import com.ace.hotel.repository.RoomTypeRepository;
import com.ace.hotel.repository.ServiceItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PublicController {

    private final RoomTypeRepository roomTypeRepository;
    private final ServiceItemRepository serviceItemRepository;

    public PublicController(RoomTypeRepository roomTypeRepository,
                            ServiceItemRepository serviceItemRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    @GetMapping("/")
    public String landingPage(Model model) {
        List<RoomType> roomTypes = roomTypeRepository.findAllByOrderByPricePerNightAsc();
        List<ServiceItem> allServices = serviceItemRepository.findAllByOrderByCategoryAscNameAsc();

        Map<ServiceCategory, List<ServiceItem>> servicesByCategory = allServices.stream()
                .collect(Collectors.groupingBy(ServiceItem::getCategory));

        LocalDate defaultCheckIn = LocalDate.now();
        LocalDate defaultCheckOut = defaultCheckIn.plusDays(1);

        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("servicesByCategory", servicesByCategory);
        model.addAttribute("defaultCheckIn", defaultCheckIn);
        model.addAttribute("defaultCheckOut", defaultCheckOut);
        model.addAttribute("defaultGuests", 2);
        model.addAttribute("pageTitle", "ACE Hotel - Accommodation, Check-in & Extras");

        return "public/landing";
    }
}
