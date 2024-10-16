package com.beauty_saloon_backend.config;

import com.beauty_saloon_backend.filter.JwtRequestFilter;
import com.beauty_saloon_backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtRequestFilter jwtRequestFilter){
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/authenticate", "/api/users/register").permitAll()
                        .requestMatchers("/api/contact/**").permitAll()
                        .requestMatchers("/api/bookings/service-provider/*").permitAll()
                        .requestMatchers("/api/bookings/all-booking").permitAll()
                        .requestMatchers("/api/bookings/create").permitAll()
                        .requestMatchers("/api/bookings/service-provider/*/bookings-on-date").permitAll()
                        .requestMatchers("/api/bookings/user/*").permitAll()
                        .requestMatchers("/api/serviceProviders/all-serviceprovider").permitAll()
                        .requestMatchers("/api/serviceProviders/user/*").permitAll()
                        .requestMatchers("/api/openingTimes/*/opening-times").permitAll()
                        .requestMatchers("/api/users/me").permitAll()
                        .requestMatchers("/api/bookings/*").permitAll()
                        .requestMatchers("/api/bookings/update/*").permitAll()
                        .requestMatchers("/api/serviceProviders/user/*").permitAll()
                        .requestMatchers("/api/services/all-service-with-names").permitAll()
                        .requestMatchers("/api/users/update/*").permitAll()
                        .requestMatchers("/", "/index.html", "/Static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/home", "/services", "/prices", "/about", "/contact").permitAll()

                        .requestMatchers("/api/users/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/serviceProviders/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/serviceLengths/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/openingTimes/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/bookings/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/bookings/update/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/bookings/create").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/services/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/statistics/*").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .requestMatchers("/api/bookings/service-provider/**").hasAnyAuthority("Admin", "Szolgaltato", "Recepcios")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // Frontend URL-je
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }


}
