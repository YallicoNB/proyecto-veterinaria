package com.veterinaria.config;

// Importa excepciones de autenticacion spring
import org.springframework.security.core.AuthenticationException;
// Importa punto de entrada autenticacion
import org.springframework.security.web.AuthenticationEntryPoint;
// Importa anotacion de componente spring
import org.springframework.stereotype.Component;
// Importa objeto para mapear json
import com.fasterxml.jackson.databind.ObjectMapper;
// Importa clases para servlet request
import jakarta.servlet.http.HttpServletRequest;
// Importa clases para servlet response
import jakarta.servlet.http.HttpServletResponse;
// Importa clase para tiempo actual
import java.time.LocalDateTime;
// Importa clase para mapear datos
import java.util.HashMap;
// Importa interfaz de mapa utilitario
import java.util.Map;

@Component
// Maneja errores de credenciales no validas
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    // Intercepta errores de peticion no autenticada
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws java.io.IOException {
        // Define tipo contenido de respuesta json
        response.setContentType("application/json");
        // Define codigo de respuesta no autorizado
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Crea mapa para almacenar respuesta json
        Map<String, Object> body = new HashMap<>();
        // Guarda marca de tiempo actual local
        body.put("timestamp", LocalDateTime.now().toString());
        // Guarda codigo de estado http error
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        // Guarda mensaje explicativo del error ocurrido
        body.put("error", "No autenticado: " + authException.getMessage());

        // Serializa respuesta java a formato json
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
