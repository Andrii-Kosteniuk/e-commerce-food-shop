package com.commondto.user;


public record UserResponse (

        String firstName,
        String lastName,
        String email,
        String password,
        String role
) {

}
