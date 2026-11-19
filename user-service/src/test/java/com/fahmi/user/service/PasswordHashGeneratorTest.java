package com.fahmi.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordHashGeneratorTest {

    @Test
    void generateHash() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String hash = encoder.encode(rawPassword);
        System.out.println("BCrypt Hash: " + hash);
    }
}