package com.ecommerce.user.jwt;


import com.ecommerce.user.service.TokenBlocklistService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTests {


    @Mock
    JwtService jwtService;

    @Mock
    TokenBlocklistService tokenBlocklistService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claims claims;

    @InjectMocks
    JwtAuthenticationFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void shouldPassThroughUnauthenticatedWithoutAuthorizationHeader() throws ServletException, IOException {

        // ARRANGE
        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, tokenBlocklistService);

        // ACT
        // ASSERT
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldPassThroughUnauthenticatedWithValidAuthorizationHeader() throws ServletException, IOException {

        // ARRANGE
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer access-token");

        when(jwtService.validateToken("access-token")).thenReturn(true);
        when(jwtService.extractClaims("access-token")).thenReturn(claims);

        when(claims.getSubject()).thenReturn("andrii@gmail.com");
        when(claims.get("tokenId", String.class)).thenReturn("tokenId-1");

        when(tokenBlocklistService.isRevoked("tokenId-1")).thenReturn(false);
        when(jwtService.getUserRoleFromToken("access-token")).thenReturn("USER");

        // ACT
        filter.doFilter(request, response, filterChain);

        // ASSERT
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        verify(filterChain).doFilter(request, response);

    }


    @Test
    void shouldNotPassThroughFilterWithInvalidToken() throws ServletException, IOException {

        // ARRANGE
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer invalid-token");
        when(jwtService.validateToken("invalid-token")).thenReturn(false);

        // ACT
        filter.doFilter(request, response, filterChain);

        // ASSERT
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(jwtService).validateToken("invalid-token");
        verifyNoInteractions(tokenBlocklistService);
        verify(filterChain, never()).doFilter(any(), any());
    }
    @Test
    void shouldReturnUnauthorizedWhenTokenIsRevoked() throws ServletException, IOException {

        // ARRANGE
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer revoked-token");

        when(jwtService.validateToken("revoked-token")).thenReturn(true);
        when(jwtService.extractClaims("revoked-token")).thenReturn(claims);
        when(claims.get("tokenId", String.class)).thenReturn("tokenId-1");
        when(tokenBlocklistService.isRevoked("tokenId-1")).thenReturn(true);

        // ACT
        filter.doFilter(request, response, filterChain);

        // ASSERT
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(jwtService).validateToken("revoked-token");
        verify(jwtService).extractClaims("revoked-token");
        verify(tokenBlocklistService).isRevoked("tokenId-1");
        verify(filterChain, never()).doFilter(any(), any());
    }



}
