package com.project.daybart.demo.Authentication;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.project.daybart.demo.Users.Users;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public final AuthenticacionRepository authRepo;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private long expirationTime;

    public String login(Users user) {
        try {
            Optional<Users> existingUser = authRepo.findByEmailAndPassword(user.getEmail(), user.getPassword());

            if (existingUser.isPresent()) {
                return generateAccessToken(existingUser.get());
            } else {
                throw new Exception("Email o contraseña incorrectos.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al autenticar: " + e.getMessage(), e);
        }
    }

    private String generateAccessToken(Users user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("rol", user.getRole())
                .claim("name", user.getName())
                .claim("lastname", user.getLastName())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}