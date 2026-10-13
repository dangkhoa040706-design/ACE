package com.ace.hotel.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** UC01: trang đăng nhập. Việc xác thực do Spring Security xử lý ở POST /login. */
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Authentication authentication) {
        boolean loggedIn = authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        return loggedIn ? "redirect:/dashboard" : "login";
    }
}
