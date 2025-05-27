package com.ecommerce.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (! hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getAccessToken(request);
        if (jwtUtils.validateToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(accessToken, request);
        log.info("Setting authentication context for user: {}", jwtUtils.getSubjectFromToken(accessToken));
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization header: {}", authorizationHeader);
        return ! ObjectUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer");
    }

    public String getAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authorizationHeader.substring(7);
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        String username = jwtUtils.getSubjectFromToken(token);
        log.info("Extracted username from token: {}", username);
        List<GrantedAuthority> authorities = jwtUtils.getRolesFromToken(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        log.info("Extracted authorities from token: {}", authorities);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.info("Extracted user details from token: {}", userDetails);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Set authentication context for user: {}", username);
    }

}
