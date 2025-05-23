package com.ecommerce.user.controller;

import com.commondto.auth.RegisterRequest;
import com.commondto.user.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.service.UserService;
import com.ecommerce.user.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register-user")
    public UserResponse createNewUser(@RequestBody RegisterRequest request) {
        return userMapper.userToUserResponse(userService.createUser(request));
    }


    @GetMapping("/login-user")
    public UserResponse getUserForLogin(String email) {
        User user = userService.getRegisteredUser(email);
        return userMapper.userToUserResponse(user);
    }

    @GetMapping("/authenticated-user")
    public User getAuthenticatedUser() {
        return userService.getAuthenticatedUser();
    }

}
