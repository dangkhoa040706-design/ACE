package com.ace.hotel;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.ace.hotel.dto.DashboardView;
import com.ace.hotel.repository.BookingRepository;
import com.ace.hotel.repository.RoomRepository;
import com.ace.hotel.service.DashboardService;

/**
 * Kiểm tra nhanh: phân quyền (UC01) và các con số dashboard trên bộ dữ liệu mẫu.
 * Dùng H2 trong bộ nhớ nên không đụng tới ./data/ace. Dữ liệu mẫu được DemoDataLoader nạp
 * khi khởi động (bảng users trống). Chạy: mvn test
 */
@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:acetest;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class SecurityAndDashboardTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    DashboardService dashboardService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BookingRepository bookingRepository;

    // ------------------------------------------------------------- công khai / chưa đăng nhập

    @Test
    void landingPageIsPublic() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ACE")));
    }

    @Test
    void loginPageIsPublic() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void anonymousIsRedirectedToLogin() throws Exception {
        mvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        mvc.perform(get("/bookings"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // ------------------------------------------------------------- đăng nhập

    @Test
    void loginWithSeededAccount() throws Exception {
        mvc.perform(formLogin().user("letan").password("Ace@123"))
                .andExpect(authenticated().withUsername("letan"))
                .andExpect(redirectedUrl("/dashboard"));
        mvc.perform(formLogin().user("quanly").password("Ace@123"))
                .andExpect(authenticated().withUsername("quanly"));
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        mvc.perform(formLogin().user("letan").password("sai-mat-khau"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    // ------------------------------------------------------------- phân quyền theo vai trò

    @Test
    @WithUserDetails("letan")
    void receptionistSeesDashboardButNotManagerPages() throws Exception {
        mvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Tình trạng phòng")));
        mvc.perform(get("/reports")).andExpect(status().isForbidden());
        mvc.perform(get("/room-types")).andExpect(status().isForbidden());
        mvc.perform(get("/services")).andExpect(status().isForbidden());
        mvc.perform(get("/rooms/new")).andExpect(status().isForbidden());
        // lễ tân vẫn xem được danh sách phòng
        mvc.perform(get("/rooms")).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("quanly")
    void managerCanOpenManagerPages() throws Exception {
        mvc.perform(get("/reports")).andExpect(status().isOk());
        mvc.perform(get("/room-types")).andExpect(status().isOk());
        mvc.perform(get("/rooms/new")).andExpect(status().isOk());
    }

    // ------------------------------------------------------------- dữ liệu mẫu và dashboard

    @Test
    void sampleDataIsLoaded() {
        assertEquals(10, roomRepository.count());
        assertEquals(12, bookingRepository.count());
    }

    @Test
    void dashboardNumbersMatchSampleData() {
        DashboardView view = dashboardService.build();
        assertEquals(6, view.freeRooms(), "phòng trống");
        assertEquals(3, view.checkInToday(), "check-in hôm nay");
        assertEquals(1, view.checkOutToday(), "check-out hôm nay");
        assertEquals(2, view.servicesToday(), "dịch vụ hôm nay");
        assertEquals(10, view.rooms().size(), "số thẻ phòng");
    }
}
