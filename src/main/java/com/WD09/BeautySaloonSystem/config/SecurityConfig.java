package com.WD09.BeautySaloonSystem.config;



import com.WD09.BeautySaloonSystem.entities.AdminUser;
import com.WD09.BeautySaloonSystem.entities.User;
import com.WD09.BeautySaloonSystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            com.WD09.BeautySaloonSystem.entities.User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRoleName()) // Returns "CUSTOMER" or "ADMIN"
                    .build();
        };
    }

    // Dynamic Redirect Handler using OOP Polymorphism
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler(UserRepository userRepository) {
        return (request, response, authentication) -> {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            // Executes getDashboardRedirectUrl() dynamically based on instance
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
                        .requestMatchers("/", "/register", "/login", "/style.css", "/script.js", "/*.jpg", "/*.jpeg", "/*.png", "/*.svg", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
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