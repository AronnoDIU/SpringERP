package com.springerp.config;

import com.springerp.entity.User;
import com.springerp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Seeds the database with default admin users on application startup.
 * Safe to run multiple times — checks for existence before inserting.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedAdminUser();
    }

    private void seedAdminUser() {
        String adminEmail = "admin@springerp.com";
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user already exists, skipping seed.");
            return;
        }

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setRole("ADMIN");
        admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
        admin.setPhoneNumber("0123456789");
        admin.setAddress("123 Main Street, Springfield");

        userRepository.save(admin);
        log.info("✅ Default admin user created: {} / Admin@123", adminEmail);
    }
}

