package com.fahmi.user.service.service;

import com.fahmi.base.ApiResponse;
import com.fahmi.base.ResponseMessage;
import com.fahmi.user.service.dto.LoginRequest;
import com.fahmi.user.service.dto.LoginResponse;
import com.fahmi.user.service.entity.RefreshToken;
import com.fahmi.user.service.entity.Role;
import com.fahmi.user.service.entity.User;
import com.fahmi.user.service.repository.RefreshTokenRepository;
import com.fahmi.user.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final long REFRESH_TOKEN_DAYS = 7;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request) {
        String normalizedEmail = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String maskedEmail = maskEmail(normalizedEmail);

        log.info("Login attempt started for email={}", maskedEmail);

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(normalizedEmail);
        if (!userOpt.isPresent()) {
            log.warn("Login failed (401): user not found for email={}", maskedEmail);
            return ResponseEntity.ok(ApiResponse.error(ResponseMessage.INVALID_CREDENTIALS));
        }

        User user = userOpt.get();

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            log.warn("Login failed (403): inactive user status={} for publicId={}", user.getStatus(), user.getPublicId());
            return ResponseEntity.ok(
                    ApiResponse.error(ResponseMessage.ACCOUNT_INACTIVE + ": " + user.getStatus())
            );
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Login failed (401): password mismatch for publicId={}", user.getPublicId());
            return ResponseEntity.ok(ApiResponse.error(ResponseMessage.INVALID_CREDENTIALS));
        }

        Instant now = Instant.now();
        user.setLastLoginAt(now);
        userRepository.save(user);
        log.debug("Updated lastLoginAt={} for publicId={}", now, user.getPublicId());

        String rawRefreshToken = UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
        Instant expiresAt = now.plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setTokenHash(sha256Hex(rawRefreshToken));
        refreshTokenEntity.setExpiresAt(expiresAt);
        refreshTokenRepository.save(refreshTokenEntity);

        List<String> roleCodes = user.getRoles().stream()
                .map(Role::getCode)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        log.info("Login succeeded for publicId={}, roles={}, refreshTokenExpiresAt={}",
                user.getPublicId(), roleCodes, expiresAt);

        LoginResponse loginResponse = new LoginResponse(
                user.getPublicId(),
                user.getEmail(),
                user.getFullName(),
                user.getStatus(),
                roleCodes,
                rawRefreshToken,
                expiresAt
        );

        return ResponseEntity.ok(ApiResponse.success(ResponseMessage.LOGIN_SUCCESS, loginResponse));
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    private String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hashed.length * 2);
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available", e);
            throw new IllegalStateException("SHA-256 tidak tersedia", e);
        }
    }
}