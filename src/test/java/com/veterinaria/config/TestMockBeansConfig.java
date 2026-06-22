package com.veterinaria.config;

// Importa clase para simular servicios
import org.mockito.Mockito;
// Importa anotacion de configuracion spring
import org.springframework.context.annotation.Bean;
// Importa anotacion para definir beans
import org.springframework.context.annotation.Configuration;
// Importa servicio de tokens jwt
import com.veterinaria.jwt.JwtService;
// Importa servicio de detalles usuario
import com.veterinaria.service.CustomUserDetailsService;
// Importa filtro de autenticacion jwt
import com.veterinaria.jwt.JwtAuthenticationFilter;

@Configuration
// Configura beans simulados para pruebas
public class TestMockBeansConfig {

    @Bean
    // Provee servicio jwt simulado tests
    public JwtService jwtService() {
        return Mockito.mock(JwtService.class);
    }

    @Bean
    // Provee cargador usuario simulado tests
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }

    @Bean
    // Provee filtro jwt simulado tests
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return Mockito.mock(JwtAuthenticationFilter.class);
    }
}
