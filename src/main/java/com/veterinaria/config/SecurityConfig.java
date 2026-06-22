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
@org.springframework.context.annotation.Profile("!test")
// Desactiva seguridad en pruebas unitarias
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // Maneja respuestas no autenticadas personalizadas
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    // Maneja respuestas de accesos prohibidos
    private final CustomAccessDeniedHandler accessDeniedHandler;
    @org.springframework.beans.factory.annotation.Value("${jwt.secret}")
    // Clave secreta cargada desde propiedades
    private String jwtSecret;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
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
                // Habilita configuracion de filtros cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura manejadores personalizados de errores
                .exceptionHandling(exceptions -> exceptions
                        // Asigna manejador para no autenticados
                        .authenticationEntryPoint(authenticationEntryPoint)
                        // Asigna manejador para accesos prohibidos
                        .accessDeniedHandler(accessDeniedHandler))
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

                        .anyRequest().authenticated())
                // Configura servidor de recursos oauth2
                .oauth2ResourceServer(oauth2 -> oauth2
                        // Habilita decodificacion y mapeo jwt
                        .jwt(jwt -> jwt
                                // Asigna decodificador de tokens local
                                .decoder(jwtDecoder())
                                // Asigna conversor de roles local
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    // Define configuracion global de cors
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        // Instancia configurador de rutas cors
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        // Instancia objeto de configuracion cors
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        // Habilita envio credenciales de usuario
        config.setAllowCredentials(true);
        // Agrega origenes permitidos para peticiones
        config.addAllowedOriginPattern("*");
        // Agrega cabeceras permitidas en peticiones
        config.addAllowedHeader("*");
        // Agrega metodos http permitidos peticiones
        config.addAllowedMethod("*");
        // Registra configuracion para toda ruta
        source.registerCorsConfiguration("/**", config);
        // Retorna fuente de configuracion configurada
        return source;
    }

    @Bean
    // Provee decodificador jwt local simetrico
    public org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder() {
        // Genera clave secreta desde propiedades
        javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(
                jwtSecret.getBytes(), "HmacSHA256");
        // Retorna decodificador con clave configurada
        return org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    // Convierte claims jwt en GrantedAuthority
    public org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Instancia conversor de autoridades jwt
        org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter();
        // Asigna claim de rol para mapear
        grantedAuthoritiesConverter.setAuthoritiesClaimName("rol");
        // Asigna prefijo de roles spring
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        // Instancia conversor principal de jwt
        org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationConverter = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter();
        // Registra conversor de autoridades personalizado
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        // Retorna conversor de jwt configurado
        return jwtAuthenticationConverter;
    }

    @Bean
    // Desactiva el registro automatico del filtro
    public org.springframework.boot.web.servlet.FilterRegistrationBean<JwtAuthenticationFilter> registration(
            JwtAuthenticationFilter filter) {
        // Instancia el bean de registro filtro
        org.springframework.boot.web.servlet.FilterRegistrationBean<JwtAuthenticationFilter> registration = new org.springframework.boot.web.servlet.FilterRegistrationBean<>(
                filter);
        // Deshabilita ejecucion en servlet global
        registration.setEnabled(false);
        // Retorna registro de filtro configurado
        return registration;
    }

}
