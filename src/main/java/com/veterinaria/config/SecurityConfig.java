package com.veterinaria.config;

import com.veterinaria.jwt.JwtAuthenticationFilter;
import com.veterinaria.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/adopcion/disponibles").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/tienda/productos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tienda/productos/{id}").permitAll()

                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                .requestMatchers("/api/veterinaria/**").hasAnyRole("ADMIN", "VETERINARIO")

                .requestMatchers("/api/lavanderia/**").hasAnyRole("ADMIN", "EMPLEADO_LAVANDERIA")

                .requestMatchers(HttpMethod.POST, "/api/tienda/ventas").hasAnyRole("ADMIN", "CLIENTE_TIENDA")
                .requestMatchers(HttpMethod.POST, "/api/tienda/productos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tienda/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tienda/productos/**").hasRole("ADMIN")
                .requestMatchers("/api/tienda/**").hasAnyRole("ADMIN", "CLIENTE_TIENDA")

                .requestMatchers(HttpMethod.POST, "/api/adopcion/solicitudes").hasAnyRole("ADMIN", "ADOPTANTE")
                .requestMatchers(HttpMethod.PATCH, "/api/adopcion/solicitudes/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}