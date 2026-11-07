package com.ace.hotel.service;

import com.ace.hotel.dto.CustomerDashboardDto;
import com.ace.hotel.dto.CustomerProfileDto;
import com.ace.hotel.entity.Booking;
import com.ace.hotel.entity.Customer;
import com.ace.hotel.entity.User;
import com.ace.hotel.entity.enums.BookingStatus;
import com.ace.hotel.exception.BusinessException;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.CustomerRepository;
import com.ace.hotel.repository.UserRepository;
import com.ace.hotel.util.ValidationUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    Customer getOrCreateCustomerForUser(User user) {
        return customerRepository.findByUser_UserId(user.getUserId())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setUser(user);
                    c.setFullName(user.getFullName());
                    c.setPhone("0900000000");
                    return customerRepository.save(c);
                });
    }

    public CustomerDashboardDto getCustomerDashboardData(User user) {
        Customer customer = getOrCreateCustomerForUser(user);
        List<Booking> allBookings = bookingRepository.findByCustomer_User_UserIdOrderByCreatedAtDesc(user.getUserId());

        List<Booking> upcoming = new ArrayList<>();
        List<Booking> inHouse = new ArrayList<>();
        List<Booking> completed = new ArrayList<>();
        List<Booking> cancelled = new ArrayList<>();

        long totalNights = 0L;
        long totalDeposit = 0L;

        for (Booking b : allBookings) {
            if (b.getDepositAmount() != null) {
                totalDeposit += b.getDepositAmount();
            }
            if (b.getStatus() == BookingStatus.RESERVED) {
                upcoming.add(b);
            } else if (b.getStatus() == BookingStatus.CHECKED_IN) {
                inHouse.add(b);
                totalNights += b.getNights();
            } else if (b.getStatus() == BookingStatus.CHECKED_OUT) {
                completed.add(b);
                totalNights += b.getNights();
            } else if (b.getStatus() == BookingStatus.CANCELLED) {
                cancelled.add(b);
            }
        }

        CustomerDashboardDto dto = new CustomerDashboardDto();
        dto.setCustomer(customer);
        dto.setUser(user);
        dto.setMemberCode(String.format("ACE-VIP-%04d", customer.getCustomerId()));
        dto.setTotalBookings(allBookings.size());
        dto.setUpcomingCount(upcoming.size());
        dto.setInHouseCount(inHouse.size());
        dto.setCompletedCount(completed.size());
        dto.setCancelledCount(cancelled.size());
        dto.setTotalNightsStayed(totalNights);
        dto.setTotalDepositPaid(totalDeposit);

        // Phân cấp Hội Viên (Loyalty Tier Gamification)
        if (totalNights >= 5 || completed.size() >= 3) {
            dto.setLoyaltyTier("Obsidian VIP Member");
            dto.setLoyaltyTierLevel(3);
        } else if (totalNights >= 2 || completed.size() >= 1) {
            dto.setLoyaltyTier("Gold Elite Member");
            dto.setLoyaltyTierLevel(2);
        } else {
            dto.setLoyaltyTier("Silver Club Member");
            dto.setLoyaltyTierLevel(1);
        }

        dto.setAllBookings(allBookings);
        dto.setUpcomingBookings(upcoming);
        dto.setInHouseBookings(inHouse);
        dto.setCompletedBookings(completed);
        dto.setCancelledBookings(cancelled);

        return dto;
    }

    @Transactional
    public Customer updateProfile(Long userId, CustomerProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy thông tin tài khoản người dùng: " + userId));

        Customer customer = getOrCreateCustomerForUser(user);

        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new BusinessException("Họ và tên không được để trống.");
        }
        if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
            throw new BusinessException("Số điện thoại không được để trống.");
        }

        // Kiểm tra độ tuổi tối thiểu 18
        ValidationUtils.validateMinimumAge(dto.getDateOfBirth(), "Quý khách phải từ đủ 18 tuổi trở lên.");

        // Kiểm tra trùng lặp SĐT
        String trimmedPhone = dto.getPhone().trim();
        customerRepository.findByPhone(trimmedPhone).ifPresent(other -> {
            if (!other.getCustomerId().equals(customer.getCustomerId())) {
                throw new BusinessException("Số điện thoại này đã được sử dụng bởi khách hàng khác.");
            }
        });

        // Kiểm tra trùng lặp CCCD (nếu có nhập)
        if (dto.getIdNumber() != null && !dto.getIdNumber().isBlank()) {
            String trimmedId = dto.getIdNumber().trim();
            customerRepository.findByIdNumber(trimmedId).ifPresent(other -> {
                if (!other.getCustomerId().equals(customer.getCustomerId())) {
                    throw new BusinessException("Số định danh cá nhân (CCCD) này đã được liên kết với một tài khoản khác.");
                }
            });
            customer.setIdNumber(trimmedId);
        } else {
            customer.setIdNumber(null);
        }

        customer.setFullName(dto.getFullName().trim());
        customer.setPhone(trimmedPhone);
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setEmail(dto.getEmail() != null && !dto.getEmail().isBlank() ? dto.getEmail().trim() : null);

        // Đồng bộ fullName sang bảng users
        user.setFullName(customer.getFullName());
        userRepository.save(user);

        return customerRepository.save(customer);
    }

    @Transactional
    public void changePassword(Long userId, CustomerProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy thông tin tài khoản người dùng: " + userId));

        if (dto.getCurrentPassword() == null || dto.getCurrentPassword().isEmpty()) {
            throw new BusinessException("Vui lòng nhập mật khẩu hiện tại.");
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new BusinessException("Mật khẩu hiện tại không chính xác.");
        }

        if (dto.getNewPassword() == null || !dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BusinessException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp.");
        }

        ValidationUtils.validatePasswordStrength(dto.getNewPassword(), "Mật khẩu mới");

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}

