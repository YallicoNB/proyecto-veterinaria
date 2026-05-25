package com.veterinaria.dto.response;

import com.veterinaria.model.Rol;
import com.veterinaria.model.Usuario;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponse {

    private Long id;
    private String username;
    private String email;
    private Rol rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        dto.setActivo(usuario.getActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        return dto;
    }
}
