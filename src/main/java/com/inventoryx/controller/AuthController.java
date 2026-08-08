package com.inventoryx.controller;

import com.inventoryx.dto.RegisterRequest;
import com.inventoryx.dto.RegisterResponse;
import com.inventoryx.entity.User;
import com.inventoryx.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        RegisterResponse response =
                new RegisterResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}