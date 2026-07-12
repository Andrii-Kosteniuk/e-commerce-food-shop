package com.ecommerce.gateway;


import com.ecommerce.gatewaysecurity.jwt.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TestJwtUtil {

    JwtUtil jwtUtil;
    String token;

    private static final String SECRET = "VGhpc0lzQVN1cGVyU2VjcmV0S2V5VGhhdElzQXRMZWFzdDMyQnl0ZXNMb25n";
    private static final int USER_ID = 1;
    private static final String TOKEN_ID = "5L";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
        token = createToken();
    }

    @Test
    void testValidateToken() {
        // GIVEN


        //WHEN
        var actual = jwtUtil.isValidToken(token);

        //THEN
        assertThat(actual).isTrue();
    }

    @Test
    void testRejectInvalidToken() {
        // GIVEN

        //WHEN
        var actual = jwtUtil.isValidToken("invalid-token");

        //THEN
        assertThat(actual).isFalse();
    }


    @Test
    void testExtractAllClaims() {
        // GIVEN

        //WHEN
        var actual = jwtUtil.extractAllClaims(token);

        //THEN
        assertAll(
                () -> assertThat(actual).containsEntry("userId", USER_ID),
                () -> assertThat(actual).containsEntry("tokenId",TOKEN_ID),
                () -> assertThat(actual).containsEntry("role","ADMIN")
        );

    }

    @Test
    void testExtractEmail() {
        // GIVEN
        String email = "andrii-kosteniuk@gmail.com";

        //WHEN
        var actual = jwtUtil.extractEmail(token);

        //THEN
        assertThat(actual).isEqualTo(email);
    }

    @Test
    void testExtractRole() {
        // GIVEN

        //WHEN
        var actual = jwtUtil.extractRole(token);

        //THEN
        assertThat(actual).isEqualTo("ADMIN");
    }

    @Test
    void testExtractUserId() {
        // GIVEN

        //WHEN
        var actual = jwtUtil.extractUserId(token);

        //THEN
        assertThat(actual).isEqualTo(USER_ID);
    }

    @Test
    void testExtractTokenId() {
        // GIVEN

        //WHEN
        var actual = jwtUtil.extractTokenId(token);

        //THEN
        assertThat(actual).isEqualTo(TOKEN_ID);
    }


    private String createToken() {

        SecretKey key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(SECRET));

        return Jwts.builder()
                .subject("andrii-kosteniuk@gmail.com")
                .claim("role", "ADMIN")
                .claim("userId", USER_ID)
                .claim("tokenId", TOKEN_ID)
                .signWith(key)
                .compact();
    }

}
