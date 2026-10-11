package com.prathmesh.expense.controller;

import com.prathmesh.expense.dto.LoginRequest;
import com.prathmesh.expense.dto.RegisterRequest;
import com.prathmesh.expense.dto.UserResponse;
import com.prathmesh.expense.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "User APIs",
        description = "user registration, login and profile APIs"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(
            @Valid @RequestBody RegisterRequest request) {

        return userService.createUser(request);
    }

    @PostMapping("/login")
    public String login(
            @Valid @RequestBody LoginRequest request) {

        return userService.login(
                request.getEmail(),
                request.getPassword());
    }
    @GetMapping("/me")
    public String getCurrentUser(Authentication authentication) {

        return "logged in as: " + authentication.getName();
    }
}