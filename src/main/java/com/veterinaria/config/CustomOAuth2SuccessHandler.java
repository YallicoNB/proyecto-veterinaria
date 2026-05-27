package com.veterinaria.config;

// Importa servicio de tokens jwt
import com.veterinaria.jwt.JwtService;
// Importa clase del modelo usuario
import com.veterinaria.model.Usuario;
// Importa enums de roles usuario
import com.veterinaria.model.Rol;
// Importa servicio de negocio usuario
import com.veterinaria.service.UsuarioService;
// Importa mapeador de objetos json
import com.fasterxml.jackson.databind.ObjectMapper;
// Importa clases para servlet request
import jakarta.servlet.http.HttpServletRequest;
// Importa clases para servlet response
import jakarta.servlet.http.HttpServletResponse;
// Importa interfaz de autenticacion spring
import org.springframework.security.core.Authentication;
// Importa usuario principal de oauth2
import org.springframework.security.oauth2.core.user.OAuth2User;
// Importa manejador de exito autenticacion
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// Importa anotacion de componente spring
import org.springframework.stereotype.Component;
// Importa clase para mapear datos
import java.util.HashMap;
// Importa interfaz de mapa utilitario
import java.util.Map;
// Importa clase de utilidad opcional
import java.util.Optional;

@Component
// Gestiona exito de autenticaciones oauth2
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    // Inicializa dependencias del manejador exito
    public CustomOAuth2SuccessHandler(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @Override
    // Procesa flujo tras autenticacion exitosa
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws java.io.IOException {
        // Obtiene el usuario principal autenticado
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        // Extrae correo del perfil social
        String email = oauth2User.getAttribute("email");
        // Extrae nombre del perfil social
        String name = oauth2User.getAttribute("name");

        // Busca si existe el usuario
        Optional<Usuario> optUsuario = usuarioService.buscarPorUsername(email);
        Usuario usuario;

        if (optUsuario.isEmpty()) {
            // Crea nuevo usuario por defecto
            usuario = new Usuario();
            // Asigna correo como nombre usuario
            usuario.setUsername(email);
            // Asigna contraseña temporal por seguridad
            usuario.setPassword("GoogleOAuth2SecurePass2026Temp");
            // Asigna correo electronico al usuario
            usuario.setEmail(email);
            // Asigna rol cliente por defecto
            usuario.setRol(Rol.CLIENTE_TIENDA);
            // Activa cuenta de nuevo usuario
            usuario.setActivo(true);
            // Guarda usuario en base datos
            usuario = usuarioService.guardar(usuario);
        } else {
            // Obtiene usuario existente de base
            usuario = optUsuario.get();
        }

        // Genera token jwt para usuario
        String token = jwtService.generarToken(usuario);

        // Define tipo contenido de respuesta
        response.setContentType("application/json");
        // Define codigo de estado exitoso
        response.setStatus(HttpServletResponse.SC_OK);

        // Crea mapa para respuesta json
        Map<String, Object> body = new HashMap<>();
        // Guarda token generado en respuesta
        body.put("token", token);
        // Guarda username del usuario autenticado
        body.put("username", usuario.getUsername());
        // Guarda rol del usuario autenticado
        body.put("rol", usuario.getRol().name());

        // Serializa respuesta a formato json
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
