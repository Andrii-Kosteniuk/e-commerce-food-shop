package com.ecommerce.user.controller;

import com.ecommerce.commondto.auth.*;
import com.ecommerce.security.filter.InternalAuthenticationFilter;
import com.ecommerce.user.jwt.JwtService;
import com.ecommerce.user.service.TokenBlocklistService;
import com.ecommerce.user.service.UserAuthenticationService;
import com.ecommerce.user.service.UserRepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@TestPropertySource(properties = {"spring.cloud.config.enabled=false"})
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    private static final String BASE_URL = "/api/v1/internal/users";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    UserAuthenticationService userAuthenticationService;
    @MockitoBean
    UserRepositoryService userRepositoryService;
    @MockitoBean
    TokenBlocklistService  tokenBlocklistService;
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    InternalAuthenticationFilter internalAuthenticationFilter;

    @Test
    void shouldReturnAuthenticationResponseIfUserIsRegistered() throws Exception {

        // ARRANGE
        var request = new RegisterRequest("Andrii", "Kosteniuk", "andrii@gmail.com", "pass1234", "USER");
        var response = new AuthenticationResponse("access-token", "refresh-token");

        when(userAuthenticationService.registerUser(request)).thenReturn(response);

        // ACT
        // ASSERT
        mockMvc.perform(post(BASE_URL + "/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void shouldAuthenticateUserSuccessfully() throws Exception {

        // ARRANGE
        var request = new AuthenticationRequest("andrii@gmail.com", "pass1234");
        var response = new AuthenticationResponse("access-token", "refresh-token");

        when(userAuthenticationService.authenticate(request)).thenReturn(response);

        // ACT
        // ASSERT
        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void shouldRefreshTokenSuccessfully() throws Exception {

        // ARRANGE
        var request = new RefreshTokenRequest("refresh-token");
        var response = new AuthenticationResponse("access-token", "refresh-token");

        when(userAuthenticationService.refreshToken(request)).thenReturn(response);

        // ACT
        // ASSERT
        mockMvc.perform(post(BASE_URL + "/refresh-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void shouldLogoutUserSuccessfully() throws Exception {

        // ARRANGE
        var request = new LogOutRequest("access-token", "refresh-token");

        // ACT
        // ASSERT
        mockMvc.perform(post(BASE_URL + "/logout")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(userAuthenticationService).logout(request);
    }


}
