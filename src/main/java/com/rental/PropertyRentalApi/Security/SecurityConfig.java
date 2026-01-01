package com.rental.PropertyRentalApi.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
// @SuppressWarnings("unused")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // DEV: disable CSRF (API testing)
                // PROD: still disabled if using JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // ============================
                // CORS CONFIG
                // ============================
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    // ============================
                    // DEV ORIGINS
                    // ============================
                    config.addAllowedOrigin("http://localhost:3000");
                    config.addAllowedOrigin("http://localhost:5173");

                    // ============================
                    // PROD ORIGINS (UNCOMMENT)
                    // ============================
                    // config.addAllowedOrigin("https://yourdomain.com");
                    // config.addAllowedOrigin("https://www.yourdomain.com");

                    // Allowed HTTP methods
                    String[] allowedMethods = {
                            "GET", "POST", "PUT", "DELETE", "OPTIONS"
                    };
                    for (String method : allowedMethods) {
                        config.addAllowedMethod(method);
                    }

                    // DEV: allow all headers
                    config.addAllowedHeader("*");
                    config.addExposedHeader("*");

                    // PROD: restrict headers
                    // config.addAllowedHeader("Authorization");
                    // config.addAllowedHeader("Content-Type");
                    // config.addExposedHeader("Authorization");

                    // Allow cookies / Authorization header
                    config.setAllowCredentials(true);

                    return config;
                }))

                // ============================
                // AUTHORIZATION RULES
                // ============================
                .authorizeHttpRequests(auth -> auth
                                // Public endpoints (login, register)
                                .requestMatchers("/api/auth/**").permitAll()

                                // DEV: allow all endpoints
                                .anyRequest().permitAll()

                        // ============================
                        // PROD (UNCOMMENT)
                        // ============================
                        // .anyRequest().authenticated()
                )

                // Stateless session (JWT-ready)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // PROD: add JWT filter here
        // http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is safe for both DEV and PROD
        return new BCryptPasswordEncoder();
    }

    // ========================
    // COMMAND OUT FOR NOW
    // ========================
    // EXPLAIN: UserDetailsService
    // ========================
    // This is a Spring Security interface that tells Spring how to load a user given a login identifier (usually username or email).
    // In your example, it fetches a UserEntity by email and throws an error if not found.
    // Once you use Spring Security’s authentication flow (like AuthenticationManager), this bean will be used automatically when someone logs in.
    // ========================
    // @Bean
    // public UserDetailsService userDetailsService(UserRepository userRepository) {
    //  return username ->
    //          userRepository.findByEmail(username)
    //                  .orElseThrow(() -> notFound("Username not found."));
    // }

    // EXPLAIN: AuthenticationManager
    // ========================
    // This is Spring Security’s engine for authenticating users.
    // It checks:
    // Is the user in the database?
    // Does the password match?
    // Is the account enabled/locked/expired?
    // Normally, instead of manually checking passwords like our current login code does, you can call authenticationManager.authenticate(...) and it does all the checks for you.
    // ========================
    // @Bean
    // public AuthenticationManager authenticationManager(
    //        AuthenticationConfiguration config
    // ) throws Exception {
    //    return config.getAuthenticationManager();
    // }
}
