package com.ecommerce.user.controller;

import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.commonexception.exception.ResourceNotFoundException;
import com.ecommerce.user.model.User;
import com.ecommerce.user.model.UserMapper;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody RegisterRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }


    @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail(@Valid @RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("User by ID '%d' not found", id)));
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }

}
