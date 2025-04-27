package com.ecommerce.auth.model;

import java.time.LocalDateTime;

public record ApiError (
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
