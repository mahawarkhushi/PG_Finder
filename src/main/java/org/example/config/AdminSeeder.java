package org.example.config;

import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if an admin already exists to prevent duplicate accounts
            if (!userRepository.existsByEmail("admin@pgfinder.com")) {
                User admin = User.builder()
                        .fullName("Super Admin")
                        .email("admin@pgfinder.com")
                        // BCrypt encrypts the password before saving to PostgreSQL!
                        .password(passwordEncoder.encode("adminpassword123"))
                        .phoneNumber("9999999999")
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(admin);
                System.out.println(">>> SUCCESS: Default Admin account created (admin@pgfinder.com) <<<");
            }
        };
    }
}