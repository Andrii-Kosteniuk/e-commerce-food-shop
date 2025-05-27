package com.ecommerce.user.controller;

import com.commondto.auth.RegisterRequest;
import com.commondto.user.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register-user")
    public User createNewUser(@RequestBody RegisterRequest request) {
        return userService.createUser(request);
    }


    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserForLogin(@PathVariable String email) {
        return ResponseEntity.ok(userService.getRegisteredUser(email));
    }

}
