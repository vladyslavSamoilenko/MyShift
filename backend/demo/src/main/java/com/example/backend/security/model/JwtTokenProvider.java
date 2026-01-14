package com.example.backend.security.model;

import com.example.backend.model.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final Long jwtValidityInMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration:3600000}") Long jwtValidityInMilliseconds) {
        this.secretKey = getKey(secret);
        this.jwtValidityInMilliseconds = jwtValidityInMilliseconds;
    }

    private SecretKey getKey(String secretKey64) {
        byte[] decode64 = Decoders.BASE64.decode(secretKey64);
        return Keys.hmacShaKeyFor(decode64);
    }

    public String generateToken(@NonNull User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(AuthenticationConstants.USER_ID, user.getId());
        claims.put(AuthenticationConstants.EMAIL, user.getEmail());
        claims.put(AuthenticationConstants.UPDATED_AT, LocalDateTime.now().toString());
        claims.put(AuthenticationConstants.ROLE, user.getRole());

        return createToken(claims, user.getEmail());
    }

    public String refreshToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return createToken(claims, claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getRole(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(AuthenticationConstants.ROLE, String.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtValidityInMilliseconds))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
