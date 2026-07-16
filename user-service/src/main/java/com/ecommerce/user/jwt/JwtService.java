package com.ecommerce.user.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private  String jwtSecret;

    @Value("${jwt.expiration}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh-token}")
    private long refreshTokenExpirationTime;

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
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long userId, String email, String role) {
        return buildToken(userId, email, role, accessTokenExpirationTime, "access");
    }

    public String getUserRoleFromToken(String token) {
        Claims claims = extractClaims(token);

        return claims.get("role", String.class);
    }

    public String generateRefreshToken(Long userId, String email, String role) {
        return buildToken(userId, email, role, refreshTokenExpirationTime, "refresh");
    }

    private String buildToken(Long userId, String email, String role, long expiration, String type) {
        return Jwts.builder()
                .subject(email)
                .claim("tokenId", UUID.randomUUID().toString())
                .claim("userId", userId)
                .claim("role", role)
                .claim("typ", type)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
}
