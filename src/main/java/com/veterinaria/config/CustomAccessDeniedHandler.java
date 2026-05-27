package com.veterinaria.config;

// Importa excepciones de acceso denegado spring
import org.springframework.security.access.AccessDeniedException;
// Importa manejador de acceso denegado
import org.springframework.security.web.access.AccessDeniedHandler;
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
// Maneja errores de accesos no autorizados
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    // Intercepta peticiones con roles insuficientes
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws java.io.IOException {
        // Define tipo contenido de respuesta json
        response.setContentType("application/json");
        // Define codigo de respuesta prohibida
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Crea mapa para almacenar respuesta json
        Map<String, Object> body = new HashMap<>();
        // Guarda marca de tiempo actual local
        body.put("timestamp", LocalDateTime.now().toString());
        // Guarda codigo de estado http error
        body.put("status", HttpServletResponse.SC_FORBIDDEN);
        // Guarda mensaje explicativo del error ocurrido
        body.put("error", "Acceso denegado: no tienes permisos para esta operación");

        // Serializa respuesta java a formato json
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
