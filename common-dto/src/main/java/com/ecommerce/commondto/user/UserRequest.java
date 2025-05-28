package com.ecommerce.commondto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email

) {
}
