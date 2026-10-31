package com.teja.job_portal_api.config;

import com.teja.job_portal_api.security.CustomUserDetailsService;
import com.teja.job_portal_api.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler((req, res, ex) -> res.setStatus(HttpStatus.FORBIDDEN.value())))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/auth/**", "/api/users/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/**", "/api/companies/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/jobs/*/apply").hasRole("CANDIDATE")
                .requestMatchers(HttpMethod.POST, "/api/jobs/**", "/api/companies/**").hasRole("EMPLOYER")
                .requestMatchers(HttpMethod.PUT, "/api/jobs/**", "/api/companies/**").hasRole("EMPLOYER")
                .requestMatchers(HttpMethod.DELETE, "/api/companies/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasAnyRole("EMPLOYER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/applications/*/status").hasRole("EMPLOYER")
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}