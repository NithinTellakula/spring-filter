package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Allow static frontend files and custom endpoints
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/images/**",
                                "/login", "/student/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("http://localhost:8080/index.html?login=success", true)
                        .failureUrl("http://localhost:8080/index.html?login=failure")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                );
        return http.build();
    }
}
