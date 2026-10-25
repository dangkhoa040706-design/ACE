package com.ace.hotel.controller;

import com.ace.hotel.dto.RegisterFormDto;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public AuthController(AuthService authService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    private String getRedirectForAuth(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() &&
            !"anonymousUser".equals(authentication.getPrincipal())) {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                String role = auth.getAuthority();
                if ("ROLE_ADMIN".equals(role)) return "redirect:/admin/dashboard";
                if ("ROLE_RECEPTIONIST".equals(role)) return "redirect:/staff/dashboard";
                if ("ROLE_CUSTOMER".equals(role)) return "redirect:/customer/dashboard";
            }
        }
        return null;
    }

    @GetMapping("/login")
    public String loginPage(Authentication authentication,
                            @RequestParam(value = "redirect", required = false) String redirectUrl,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            jakarta.servlet.http.HttpServletRequest request,
                            Model model) {
        if (redirectUrl != null && !redirectUrl.isBlank()) {
            request.getSession().setAttribute("REDIRECT_URL", redirectUrl);
        }

        String redirect = getRedirectForAuth(authentication);
        if (redirect != null) return redirect;

        if (error != null) {
            model.addAttribute("errorMessage", MessageConstants.MSG_01);
        }
        if (logout != null) {
            model.addAttribute("successMessage", MessageConstants.MSG_02);
        }
        if (registered != null && !model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", MessageConstants.MSG_03);
        }

        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterFormDto());
        }
        if (!model.containsAttribute("activeTab")) {
            model.addAttribute("activeTab", "login");
        }
        model.addAttribute("pageTitle", "Đăng Nhập & Đăng Ký");
        return "public/login-register";
    }

    @GetMapping("/register")
    public String registerPage(Authentication authentication, Model model) {
        String redirect = getRedirectForAuth(authentication);
        if (redirect != null) return redirect;

        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterFormDto());
        }
        model.addAttribute("activeTab", "register");
        model.addAttribute("pageTitle", "Đăng Ký Tài Khoản Khách Hàng");
        return "public/login-register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("registerForm") RegisterFormDto form,
                                 BindingResult bindingResult,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeTab", "register");
            model.addAttribute("pageTitle", "Đăng Ký Tài Khoản Khách Hàng");
            return "public/login-register";
        }

        try {
            Customer customer = authService.registerCustomer(form);

            // Tự động đăng nhập luôn (Auto-login) cho khách hàng
            UserDetails userDetails = userDetailsService.loadUserByUsername(customer.getUser().getUsername());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Chào mừng Quý khách " + customer.getFullName() + " đã gia nhập Hội viên ACE Hotel 5★!");

            String redirectUrl = (String) request.getSession().getAttribute("REDIRECT_URL");
            if (redirectUrl != null && !redirectUrl.isBlank()) {
                request.getSession().removeAttribute("REDIRECT_URL");
                return "redirect:" + redirectUrl;
            }
            return "redirect:/customer/dashboard";
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("activeTab", "register");
            model.addAttribute("pageTitle", "Đăng Ký Tài Khoản Khách Hàng");
            return "public/login-register";
        }
    }
}
