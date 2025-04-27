package com.ecommerce.auth.advise;

import com.ecommerce.auth.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error");

        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    private ResponseEntity<ApiError> buildErrorResponse(String message, HttpStatus status, String path) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path);
        return new ResponseEntity<>(apiError, status);
    }
}