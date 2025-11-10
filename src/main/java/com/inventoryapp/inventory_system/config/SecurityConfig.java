package com.inventoryapp.inventory_system.config;

import com.inventoryapp.inventory_system.service.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//CORS config import statements
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity //Enable Spring Security's web security support
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean //This bean will be used by spring security to hash(encode/validate) passwords
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return  authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // This exposes the AuthenticationManager as a bean, fixing the error in AuthController
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF(Cross-Site Request Forgery)- since we are not using Cookies for session tracking
                .csrf(csrf -> csrf.disable())

                .cors(withDefaults()) // Enable CORS with default settings

                // 2. Define authorization rules
                .authorizeHttpRequests(authz -> authz
                        // PUBLIC ENDPOINTS:
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/index.html", "/static/**", "/js/**", "/css/**", "/ws-inventory/**").permitAll()

                        // --- SECURE ENDPOINTS ---

                        // ADMIN-ONLY: Only admins can CREATE new products
                        .requestMatchers(HttpMethod.POST, "/api/inventory").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/inventory/{sku}/stock").hasAuthority("ROLE_ADMIN")

                        // USER & ADMIN: Can READ inventory and CREATE sales
                        .requestMatchers(HttpMethod.GET, "/api/inventory").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/inventory/sale").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // DEFAULT:
                        .anyRequest().authenticated()
                )

                // 3. Set Session Management to STATELESS
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Set our custom provider
                .authenticationProvider(authenticationProvider())

                // 5. Add our JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //1. Specify the allowed origin (my React app)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        //2. Specify the allowed Http methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        //3. Specify the allowed headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        //4. Allow credentials (cookies, authorization headers, etc.)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); // Apply CORS config to all /api/** endpoints
        return source;
    }
}
