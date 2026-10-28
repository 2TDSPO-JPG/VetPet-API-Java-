package com.challenger.sprint.javaspg.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MobileTokenFilter tokens) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.ignoringRequestMatchers("/tutores/**", "/pets/**", "/perguntas/**", "/veterinarios/**", "/exames/**", "/prontuarios/**", "/auth/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/login", "/error", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/tutores", "/veterinarios", "/tutores/login", "/veterinarios/login", "/auth/admin/login", "/tutores/recuperar-senha", "/tutores/confirmar-recuperacao").permitAll()
                .requestMatchers(HttpMethod.GET, "/tutores", "/exames").hasRole("ADMIN")
                .anyRequest().authenticated())
            .addFilterBefore(tokens, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(errors -> errors.defaultAuthenticationEntryPointFor((request, response, error) -> {
                response.setStatus(401); response.setContentType("application/json"); response.getWriter().write("{\"message\":\"Entre novamente no aplicativo para continuar.\"}");
            }, request -> request.getRequestURI().matches("^/(tutores|pets|perguntas|veterinarios|exames|prontuarios|auth|cuidados)(/.*)?$")))
            .formLogin(Customizer.withDefaults());
        return http.build();
    }
    @Bean public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.stream(System.getenv().getOrDefault("APP_CORS_ORIGINS", "http://localhost:8081,http://127.0.0.1:8081").split(",")).map(String::trim).toList());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**", config); return source;
    }
}
