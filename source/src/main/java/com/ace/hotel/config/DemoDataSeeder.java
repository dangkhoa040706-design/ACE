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
    }
}
