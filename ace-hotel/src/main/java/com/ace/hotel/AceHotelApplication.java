package com.ace.hotel;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AceHotelApplication {

    public static void main(String[] args) {
        // Cố định múi giờ Việt Nam để CURRENT_DATE trong H2 và LocalDate.now() luôn khớp ngày demo,
        // dù máy demo đặt múi giờ khác.
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SpringApplication.run(AceHotelApplication.class, args);
    }
}
