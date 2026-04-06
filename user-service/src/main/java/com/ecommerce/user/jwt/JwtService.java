package com.ecommerce.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private  String JWT_SECRET;

    @Value("${jwt.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refresh-token}")
    private long REFRESH_TOKEN_EXPIRATION;

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long userId, String email, String role) {
        return buildToken(userId, email, role, ACCESS_TOKEN_EXPIRATION, "access");
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = extractClaims(token);
        Object role = claims.get("role");

        if (role instanceof String roleStr) {
            return List.of(roleStr);
        }

        if (role instanceof List<?> list) {
            return list.stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }

    public String generateRefreshToken(Long userId, String email, String role) {
        return buildToken(userId, email, role, REFRESH_TOKEN_EXPIRATION, "refresh");
    }

    private String buildToken(Long userId, String email, String role, long expiration, String type) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .claim("typ", type)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
}
