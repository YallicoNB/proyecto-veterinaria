package com.veterinaria.jwt;

import com.veterinaria.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private static final String SECRET =
            "miClaveSuperSecretaVeterinaria2026JWTSegura123";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generarToken(Usuario usuario) {

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("rol", usuario.getRol().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }
public String extraerUsername(String token) {
    return Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
}

public boolean validarToken(String token) {
    try {
        Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return true;
    } catch (Exception e) {
        return false;
    }
}
}