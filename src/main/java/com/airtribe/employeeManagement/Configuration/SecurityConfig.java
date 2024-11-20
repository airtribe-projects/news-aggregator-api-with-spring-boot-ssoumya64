package com.airtribe.employeeManagement.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception {
           http.csrf().disable()
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/employees").hasAnyRole("ADMIN", "MANAGER")
                       .requestMatchers("/employees/{id}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                       .requestMatchers("/employees/search").hasAnyRole("ADMIN", "MANAGER")
                       .requestMatchers("/login/**", "/oauth2/**").permitAll()
                       .anyRequest().authenticated()
               ).oauth2Login(Customizer.withDefaults());
     return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
