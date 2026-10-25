package com.ace.hotel.config;

import com.ace.hotel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DemoDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final DataSource dataSource;
    private final UserRepository userRepository;

    @Value("${ace.seed:false}")
    private boolean forceSeed;

    public DemoDataSeeder(DataSource dataSource, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        boolean shouldSeed = forceSeed || userRepository.count() == 0;
        if (shouldSeed) {
            log.info("========== [ACE HOTEL] SEEDING SAMPLE DATA (forceSeed={}, userCount={}) ==========",
                    forceSeed, userRepository.count());
            try {
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                populator.addScript(new ClassPathResource("sample-data.sql"));
                populator.setContinueOnError(false);
                populator.execute(dataSource);
                log.info("========== [ACE HOTEL] SEED COMPLETED SUCCESSFULLY! Users in DB: {} ==========",
                        userRepository.count());
            } catch (Exception e) {
                log.error("Failed to execute sample-data.sql: {}", e.getMessage(), e);
            }
        } else {
            log.info("[ACE HOTEL] Database already populated ({} users). Use --ace.seed=true to re-seed.",
                    userRepository.count());
        }

        // Tự động đồng bộ cột bed_type và URL ảnh phòng sang định dạng .png mới
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            // Đảm bảo cột bed_type tồn tại trên CSDL H2 bền vững
            try {
                stmt.executeUpdate("ALTER TABLE room_types ADD COLUMN IF NOT EXISTS bed_type VARCHAR(100)");
                stmt.executeUpdate("UPDATE room_types SET bed_type = '1 Giường đôi Queen (1m6) hoặc 2 Giường đơn' WHERE name = 'Standard' AND (bed_type IS NULL OR bed_type = '')");
                stmt.executeUpdate("UPDATE room_types SET bed_type = '1 Giường đôi King (1m8) hoặc 2 Giường đơn' WHERE name = 'Superior' AND (bed_type IS NULL OR bed_type = '')");
                stmt.executeUpdate("UPDATE room_types SET bed_type = '1 Giường King lớn (2m × 2m2) ban công' WHERE name = 'Deluxe' AND (bed_type IS NULL OR bed_type = '')");
                stmt.executeUpdate("UPDATE room_types SET bed_type = '2 Giường đôi Queen lớn (1m6 × 2m)' WHERE name = 'Family' AND (bed_type IS NULL OR bed_type = '')");
                stmt.executeUpdate("UPDATE room_types SET bed_type = '1 Giường King Master + 1 Sofa Bed cao cấp' WHERE name = 'Suite' AND (bed_type IS NULL OR bed_type = '')");
            } catch (Exception ex) {
                log.debug("[ACE HOTEL] Di trú bed_type bỏ qua: {}", ex.getMessage());
            }

            int updatedJpg = stmt.executeUpdate("UPDATE room_types SET image_url = REPLACE(image_url, '.jpg', '.png') WHERE image_url LIKE '%.jpg'");
            int updatedSvg = stmt.executeUpdate("UPDATE room_types SET image_url = REPLACE(image_url, '.svg', '.png') WHERE image_url LIKE '%.svg'");
            if (updatedJpg > 0 || updatedSvg > 0) {
                log.info("[ACE HOTEL] Đã cập nhật {} đường dẫn ảnh hạng phòng sang định dạng .png mới!", (updatedJpg + updatedSvg));
            }
        } catch (Exception e) {
            log.warn("[ACE HOTEL] Lưu ý khi cập nhật ảnh phòng hoặc bed_type trong DB: {}", e.getMessage());
        }
    }
}
