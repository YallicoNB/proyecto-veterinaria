package com.veterinaria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El username es obligatorio")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "El password es obligatorio")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un correo válido")
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private Boolean activo = true;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Usuario(String username, String password, String email, Rol rol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
        this.activo = true;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return this.activo != null && this.activo;
    }
}