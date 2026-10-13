package com.ace.hotel.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ace.hotel.util.DateLabels;

/** Thuộc tính dùng chung cho mọi trang (thanh trên hiển thị ngày hôm nay). */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("todayLabel")
    public String todayLabel() {
        return DateLabels.today();
    }
}
