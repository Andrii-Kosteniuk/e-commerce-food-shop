package com.ecommerce.commondto.user;

public record UserResponse (

        Long id,
        String firstName,
        String lastName,
        String email,
        String password,
        String role
) {

}
