package org.example.config;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Password Hasher Bean: Cryptographically hashes and verifies passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. UserDetailsService Bean: Connects Spring Security to our PostgreSQL database
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name()) // e.g. "ADMIN" becomes "ROLE_ADMIN"
                    .build();
        };
    }

    // 3. Security Filter Chain Bean: The URL Permission Rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST APIs (allows tools like Postman to make POST calls)
                .csrf(csrf -> csrf.disable())

                // URL Permissions
                .authorizeHttpRequests(auth -> auth
                        // RULE 1: Anyone can search and view Bangalore areas (100% Public)
                        .requestMatchers(HttpMethod.GET, "/api/v1/areas/**").permitAll()

                        // RULE 2: ONLY users with role "ADMIN" can create a new area!
                        .requestMatchers(HttpMethod.POST, "/api/v1/areas/**").hasRole("ADMIN")

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // Enables HTTP Basic Authentication (send email & password in the request)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}