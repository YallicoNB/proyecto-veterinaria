package com.veterinaria.dto.request;

import com.veterinaria.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String email;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}
