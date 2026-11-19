package com.fahmi.user.service.controller;

import com.fahmi.base.ApiResponse;
import com.fahmi.base.ResponseMessage;
import com.fahmi.user.service.dto.LoginRequest;
import com.fahmi.user.service.dto.LoginResponse;
import com.fahmi.user.service.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        log.info("Received login request for email={}", maskEmail(email));

        ResponseEntity<ApiResponse<LoginResponse>> loginResponse = authService.login(request);
        assert loginResponse.getBody() != null;
        log.info("Login request processed successfully for publicId={}", loginResponse.getBody().getData().getPublicId());

        return loginResponse;
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(atIndex);
    }
}