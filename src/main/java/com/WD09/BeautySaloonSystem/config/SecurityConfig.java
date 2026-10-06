package com.WD09.BeautySaloonSystem.config;

import com.WD09.BeautySaloonSystem.entities.AdminUser;
import com.WD09.BeautySaloonSystem.entities.User;
import com.WD09.BeautySaloonSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {

    // Serves uploaded staff photos from the "uploads" folder at /uploads/**
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadRoot = Paths.get("uploads").toAbsolutePath();
        try {
            // The folder must exist at startup, otherwise the location has no trailing "/"
            Files.createDirectories(uploadRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload folder: " + uploadRoot, e);
        }
        String location = uploadRoot.toUri().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRoleName()) // "CUSTOMER" or "ADMIN"
                    .build();
        };
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler(UserRepository userRepository) {
        return (request, response, authentication) -> {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            response.sendRedirect(user.getDashboardRedirectUrl().replace("redirect:", ""));
        };
    }

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@salon.com")) {
                AdminUser admin = new AdminUser();
                admin.setFullName("System Admin");
                admin.setEmail("admin@salon.com");
                admin.setPhone("0761234567");
                admin.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(admin);
                System.out.println("Admin account created successfully!");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/style.css", "/script.js",
                                "/*.jpg", "/*.jpeg", "/*.png", "/*.svg", "/css/**", "/js/**").permitAll()
                        // Staff photos: any logged-in customer or admin may view them
                        .requestMatchers("/uploads/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/customer/**", "/appointments/book", "/appointments/my-bookings").hasRole("CUSTOMER")
                        .requestMatchers("/admin/**", "/appointments/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // CSRF stays ON (default). The admin staff form sends its token both as a
                // hidden field and in the URL, so photo uploads (multipart) are accepted.
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}