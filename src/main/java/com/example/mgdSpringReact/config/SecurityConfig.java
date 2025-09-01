package com.example.mgdSpringReact.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // For REST APIs, CSRF can be disabled or ignored for API paths
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                // Allow health and static resources without authentication
                .requestMatchers("/actuator/health", "/", "/index.html", "/assets/**", "/static/**", "/favicon.ico").permitAll()
                // Require auth for API endpoints
                .requestMatchers("/api", "/api/**").authenticated()
                // Everything else can be public (e.g., SPA client-side routes)
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:5173", // Vite default
                "http://localhost:3000"  // CRA default
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
