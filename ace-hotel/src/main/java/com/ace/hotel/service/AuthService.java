package com.ace.hotel.service;

import com.ace.hotel.dto.RegisterFormDto;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.Role;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.exception.MessageConstants;
import com.ace.hotel.repository.CustomerRepository;
import com.ace.hotel.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       CustomerRepository customerRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Customer registerCustomer(RegisterFormDto form) {
        // 1. Kiểm tra so khớp mật khẩu
        if (form.getPassword() == null || !form.getPassword().equals(form.getConfirmPassword())) {
            throw new BusinessException("Mật khẩu và xác nhận mật khẩu không trùng khớp.");
        }

        // 1b. Kiểm tra 4 tiêu chí bảo mật mật khẩu (>=8 ký tự, 1 chữ hoa, 1 số, 1 ký tự đặc biệt, không chứa dấu cách)
        String pwd = form.getPassword();
        if (pwd.contains(" ")) {
            throw new BusinessException("Mật khẩu không được chứa khoảng trắng (dấu cách).");
        }
        if (pwd.length() < 8 || !pwd.matches(".*[A-Z].*") || !pwd.matches(".*[0-9].*") || !pwd.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`].*")) {
            throw new BusinessException("Mật khẩu phải có ít nhất 8 ký tự, bao gồm ít nhất 1 chữ in hoa, 1 chữ số và 1 ký tự đặc biệt (không tính dấu cách).");
        }

        // 2. Ràng buộc độ tuổi tối thiểu từ đủ 18 tuổi
        if (form.getDateOfBirth() != null) {
            java.time.LocalDate minAdultDate = java.time.LocalDate.now().minusYears(18);
            if (form.getDateOfBirth().isAfter(minAdultDate)) {
                throw new BusinessException("Quý khách phải từ đủ 18 tuổi trở lên để đăng ký tài khoản thành viên.");
            }
        }

        // 3. Kiểm tra tính duy nhất của Tên đăng nhập
        String username = form.getUsername().trim();
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(MessageConstants.MSG_04);
        }

        // 4. Kiểm tra tính duy nhất của Số điện thoại
        String phone = form.getPhone().trim();
        if (customerRepository.existsByPhone(phone)) {
            throw new BusinessException("Số điện thoại này đã được đăng ký tài khoản thành viên.");
        }

        // 5. Kiểm tra tính duy nhất của CCCD (nếu có nhập)
        if (form.getIdNumber() != null && !form.getIdNumber().isBlank()) {
            String idNumber = form.getIdNumber().trim();
            if (customerRepository.existsByIdNumber(idNumber)) {
                throw new BusinessException("Số định danh cá nhân (CCCD) này đã được liên kết với một tài khoản khác.");
            }
        }

        // 6. Tạo tài khoản User với quyền CUSTOMER
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.setFullName(form.getFullName().trim());
        user.setRole(Role.CUSTOMER);
        user = userRepository.save(user);

        // 7. Tạo hồ sơ Customer gắn với user_id
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFullName(form.getFullName().trim());
        customer.setDateOfBirth(form.getDateOfBirth());
        customer.setPhone(phone);
        customer.setIdNumber(form.getIdNumber() != null && !form.getIdNumber().isBlank() ? form.getIdNumber().trim() : null);
        customer.setEmail(form.getEmail() != null && !form.getEmail().isBlank() ? form.getEmail().trim() : null);
        return customerRepository.save(customer);
    }
}
