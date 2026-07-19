package com.ecommerce.auth.service;

import com.ecommerce.auth.feign.UserServiceClient;
import com.ecommerce.commondto.auth.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceTest {

    @MockitoBean
    private UserServiceClient userServiceClient;

    @Autowired
    AuthService authService;

    @Test
    void testRegisterUser() {

        // ARRANGE
        var registerRequest = new RegisterRequest("Andrii", "Kosteniuk", "andrii@gmail.com", "andrii777", "ADMIN");
        var authenticationResponse = new AuthenticationResponse("access-token", "refresh-token");
        when(userServiceClient.registerUser(registerRequest)).thenReturn(authenticationResponse);

        // ACT
        var response = authService.registerUser(registerRequest);

        // ASSERT
        assertNotNull(response);
        assertEquals("access-token", response.token());
        assertEquals("refresh-token", response.refreshToken());

        verify(userServiceClient).registerUser(registerRequest);

    }

    @Test
    void testAuthenticateUser() {

        // ARRANGE
        var authenticationRequest = new AuthenticationRequest("andrii@gmail.com", "andrii777");
        var authenticationResponse = new AuthenticationResponse("access-token", "refresh-token");
        when(userServiceClient.loginUser(authenticationRequest)).thenReturn(authenticationResponse);

        // ACT
        var response = authService.authenticate(authenticationRequest);

        // ASSERT
        assertNotNull(response);
        assertEquals("access-token", response.token());
        assertEquals("refresh-token", response.refreshToken());

        verify(userServiceClient).loginUser(authenticationRequest);

    }

    @Test
    void testRefreshToken() {

        // ARRANGE
        var refreshTokenRequest = new RefreshTokenRequest("refresh-token");
        var authenticationResponse = new AuthenticationResponse("access-token", "refresh-token");
        when(userServiceClient.refreshToken(refreshTokenRequest)).thenReturn(authenticationResponse);

        // ACT
        var response = authService.refreshToken(refreshTokenRequest);

        // ASSERT
        assertNotNull(response);
        assertEquals("access-token", response.token());
        assertEquals("refresh-token", response.refreshToken());

        verify(userServiceClient).refreshToken(refreshTokenRequest);

    }

    @Test
    void testLogout() {

        // ARRANGE
        var logOutRequest = new LogOutRequest("access-token", "refresh-token");
        doNothing().when(userServiceClient).logout(logOutRequest);

        // ACT
        authService.logout(logOutRequest);
        // ASSERT

        verify(userServiceClient).logout(logOutRequest);

    }
}
