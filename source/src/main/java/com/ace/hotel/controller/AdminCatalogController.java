package com.ace.hotel.controller;

import com.ace.hotel.entity.Room;
import com.ace.hotel.entity.RoomType;
import com.ace.hotel.entity.ServiceItem;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.service.CatalogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminCatalogController {

    private final CatalogService catalogService;

    public AdminCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping({"/catalog", "/settings"})
    public String catalogPage(@RequestParam(value = "tab", required = false, defaultValue = "room-types") String tab,
                              Model model) {
        model.addAttribute("roomTypes", catalogService.getAllRoomTypes());
        model.addAttribute("rooms", catalogService.getAllRooms());
        model.addAttribute("services", catalogService.getAllServices());
        model.addAttribute("activeTab", tab);
        model.addAttribute("pageTitle", "Quản Lý Danh Mục & Biểu Giá");
        return "admin/catalog";
    }

    // --- 1. Hạng Phòng & Giá ---
    @PostMapping("/room-types/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveRoomType(@ModelAttribute RoomType roomType, RedirectAttributes redirectAttributes) {
        try {
            catalogService.saveRoomType(roomType);
            redirectAttributes.addFlashAttribute("successMessage", MessageConstants.MSG_06);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/catalog?tab=room-types";
    }

    @PostMapping("/room-types/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRoomType(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteRoomType(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageConstants.MSG_07);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/catalog?tab=room-types";
    }

    // --- 2. Phòng Vật Lý & Bảo Trì ---
    @PostMapping("/rooms/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveRoom(@ModelAttribute Room room,
                           @RequestParam("roomTypeId") Long roomTypeId,
                           RedirectAttributes redirectAttributes) {
        try {
            catalogService.saveRoom(room, roomTypeId);
            redirectAttributes.addFlashAttribute("successMessage", MessageConstants.MSG_08);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/catalog?tab=rooms";
    }

    @PostMapping("/rooms/{id}/toggle-maintenance")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleMaintenance(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogService.toggleMaintenance(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái bảo trì phòng thành công.");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/catalog?tab=rooms";
    }

    @PostMapping("/rooms/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRoom(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageConstants.MSG_09);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/catalog?tab=rooms";
    }

    // --- 3. Dịch Vụ ---
    @PostMapping("/services/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveService(@ModelAttribute ServiceItem service, RedirectAttributes redirectAttributes) {
        try {
            catalogService.saveService(service);
            redirectAttributes.addFlashAttribute("successMessage", MessageConstants.MSG_10);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/catalog?tab=services";
    }

    @PostMapping("/services/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteService(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogService.deleteService(id);
            redirectAttributes.addFlashAttribute("successMessage", MessageConstants.MSG_11);
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/catalog?tab=services";
    }
}
