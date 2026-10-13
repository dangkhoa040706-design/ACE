package com.ace.hotel.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import com.ace.hotel.repository.UserRepository;

/**
 * Nạp sample-data.sql.
 * - Lần chạy đầu (bảng users trống): tự nạp, để có tài khoản đăng nhập ngay.
 * - Muốn nạp lại sạch trước buổi demo: chạy với tham số --ace.seed=true (xóa hết rồi nạp lại).
 */
@Component
public class DemoDataLoader implements ApplicationRunner {

    private final DataSource dataSource;
    private final UserRepository userRepository;

    @Value("${ace.seed:false}")
    private boolean forceSeed;

    @Value("${ace.seed-if-empty:true}")
    private boolean seedIfEmpty;

    public DemoDataLoader(DataSource dataSource, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean empty = userRepository.count() == 0;
        if (forceSeed || (seedIfEmpty && empty)) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("sample-data.sql"));
            populator.setSqlScriptEncoding("UTF-8");
            populator.execute(dataSource);
            System.out.println("[ACE] Đã nạp dữ liệu mẫu từ sample-data.sql");
        }
    }
}
