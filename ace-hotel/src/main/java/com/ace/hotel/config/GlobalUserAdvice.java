package com.ace.hotel.config;

import com.ace.hotel.entity.User;
import com.ace.hotel.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserAdvice {

    private final UserRepository userRepository;

    public GlobalUserAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("currentActor")
    public User currentActor(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return userRepository.findByUsername(authentication.getName()).orElse(null);
        }
        return null;
    }
}
