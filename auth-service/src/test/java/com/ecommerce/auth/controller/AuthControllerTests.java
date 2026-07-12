package com.ecommerce.auth.controller;

import com.ecommerce.auth.service.AuthService;
import com.ecommerce.commondto.auth.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;

    @Test
    void shouldRegisterUser() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "Andrii",
                "Kosteniuk",
                "andrii@gmail.com",
                "andrii777",
                "ADMIN"
        );

        AuthenticationResponse response =
                new AuthenticationResponse(
                        "access-token",
                        "refresh-token"
                );

        when(authService.registerUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token")
                        .value("access-token"))
                .andExpect(jsonPath("$.refreshToken")
                        .value("refresh-token"));

        verify(authService).registerUser(request);

    }

    @Test
    void shouldLoginUser() throws Exception {

        AuthenticationRequest request = new AuthenticationRequest(
                "andrii@gmail.com",
                "andrii777"

        );

        AuthenticationResponse response =
                new AuthenticationResponse(
                        "access-token",
                        "refresh-token"
                );

        when(authService.authenticate(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("access-token"))
                .andExpect(jsonPath("$.refreshToken")
                        .value("refresh-token"));

        verify(authService).authenticate(request);

    }

    @Test
    void shouldRefreshToken() throws Exception {

        RefreshTokenRequest request = new RefreshTokenRequest("refresh-token");

        AuthenticationResponse response =
                new AuthenticationResponse(
                        "access-token",
                        "refresh-token"
                );

        when(authService.refreshToken(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("access-token"))
                .andExpect(jsonPath("$.refreshToken")
                        .value("refresh-token"));

        verify(authService).refreshToken(request);

    }

    @Test
    void shouldLogoutUser() throws Exception {

        LogOutRequest request = new LogOutRequest(
                "access-token",
                "refresh-token");


        doNothing().when(authService).logout(request);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(authService).logout(request);

    }
}
