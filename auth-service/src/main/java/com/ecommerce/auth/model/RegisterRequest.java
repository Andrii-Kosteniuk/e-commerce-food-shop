package com.ecommerce.auth.model;


public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String role
) {
}