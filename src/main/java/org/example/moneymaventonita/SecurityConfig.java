package org.example.moneymaventonita;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // CORS wird erlaubt (weiter unten definiert)
                .and()
                .csrf(csrf -> csrf.disable()) // CSRF für REST-APIs ausschalten
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/registration",
                                "/login",
                                "/user-profile",
                                "/update-profile",
                                "/expenses",
                                "/expenses/**",
                                "/financial-profile",
                                "/dashboard/",
                                "/dashboard/**",
                                "/dashboard/impulse-vs-necessity",
                                "/dashboard/monthly-expenses",
                                "/dashboard/daily-expenses",
                                "/dashboard/dashboard/daily-expenses",
                                "/financial-goal",
                                "/goal-history"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ✅ CORS-Konfiguration (für localhost:5173)
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Cookie/JWT Support
        config.setAllowedOriginPatterns(List.of("https://moneymavenfrontend-4.onrender.com"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}