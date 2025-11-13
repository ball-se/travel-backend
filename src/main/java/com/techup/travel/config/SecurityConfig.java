package com.techup.travel.config;

import com.techup.travel.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

// ✅ CORS import
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// หมายเหตุ: @Configuration ระบุว่าคลาสนี้เป็น Spring Configuration class
@Configuration
// หมายเหตุ: @RequiredArgsConstructor ให้ Lombok สร้าง constructor สำหรับฟิลด์ final เพื่อฉีด dependency
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ เปิดให้ CORS
        .csrf(csrf -> csrf.disable()) // ปิด CSRF สำหรับ API (ใช้ JWT แทน)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll() // ✅ login/register เปิดได้
            // ✅ Public GET endpoints
            .requestMatchers(HttpMethod.GET, "/api/trips").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/trips/search").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/trips/**").permitAll()  // ✅ GET ทุก path ภายใต้ /api/trips
            // ✅ Protected endpoints
            .requestMatchers("/api/auth/me").authenticated()
            .requestMatchers("/api/trips/mine").authenticated()
            .requestMatchers("/api/files/upload").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/trips").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/trips/**").authenticated()  // ✅ PUT ทุก path
            .requestMatchers(HttpMethod.DELETE, "/api/trips/**").authenticated()  // ✅ DELETE ทุก path
            .anyRequest().authenticated()
        )
        .httpBasic(httpBasic -> httpBasic.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(
            org.springframework.security.config.http.SessionCreationPolicy.STATELESS
        ))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  // ✅ อนุญาตทุก origin ยิงได้หมด
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // 💡 ใครก็ยิงได้
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}