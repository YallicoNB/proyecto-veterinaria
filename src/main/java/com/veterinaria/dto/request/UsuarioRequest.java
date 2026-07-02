package com.veterinaria.dto.request;

import com.veterinaria.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String email;

    private Rol rol;
}
