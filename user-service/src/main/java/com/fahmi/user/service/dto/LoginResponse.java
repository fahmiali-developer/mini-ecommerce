package com.fahmi.user.service.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class LoginResponse {

    private UUID publicId;
    private String email;
    private String fullName;
    private String status;
    private List<String> roles;
    private String refreshToken;
    private Instant refreshTokenExpiresAt;

    public LoginResponse(UUID publicId, String email, String fullName, String status,
                         List<String> roles, String refreshToken, Instant refreshTokenExpiresAt) {
        this.publicId = publicId;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.roles = roles;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public  LoginResponse() {}

    public UUID getPublicId() {
        return publicId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }
}