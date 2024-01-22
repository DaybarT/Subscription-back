package com.project.daybart.demo.Authentication;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.project.daybart.demo.Users.Users;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Claims;
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

    public ResponseEntity<String> login(Users user) {
        try {
            Optional<Users> existingUser = authRepo.findByEmailAndPassword(user.getEmail(), user.getPassword());

            if (existingUser.isPresent()) {
                String accessToken = generateAccessToken(existingUser.get());
                return ResponseEntity.ok(accessToken);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email o contraseña incorrectos.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al autenticar: " + e.getMessage());
        }
    }

    private ResponseEntity<Object> verify(String token) { //hay que usarlo
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Claims jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Object[] payload = { jws.getSubject(), jws };

            Date expirationDate = jws.getExpiration();
            Date now = new Date();

            if (expirationDate != null && expirationDate.before(now)) {
                return new ResponseEntity<>("Token expired", HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(payload, HttpStatus.OK);
            }
        } catch (Exception e) {
            // El token no es válido
            return new ResponseEntity<>("Token invalid", HttpStatus.NOT_ACCEPTABLE);
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
