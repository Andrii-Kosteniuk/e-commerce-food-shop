package com.ecommerce.user.controller;

import com.ecommerce.commondto.auth.RegisterRequest;
import com.ecommerce.commondto.user.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.model.UserMapper;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody RegisterRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }


    @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserForLogin(@Valid @RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }

}
