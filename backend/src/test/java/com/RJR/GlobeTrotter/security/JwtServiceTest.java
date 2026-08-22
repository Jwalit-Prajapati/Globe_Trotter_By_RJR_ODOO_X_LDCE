package com.RJR.GlobeTrotter.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        String secret = Base64.getEncoder().encodeToString(
                "test-secret-key-with-at-least-32-bytes".getBytes(StandardCharsets.UTF_8));
        jwtService = new JwtService(secret, 60_000);
    }

    @Test
    void tokenContainsEmailAndUserId() {
        String token = jwtService.generateToken(42L, "alex@example.com");

        assertEquals("alex@example.com", jwtService.extractEmail(token));
        assertEquals(42L, jwtService.extractUserId(token));
    }

    @Test
    void validatesMatchingUserAndRejectsDifferentUser() {
        String token = jwtService.generateToken(42L, "alex@example.com");
        UserDetails matchingUser = User.withUsername("alex@example.com").password("password")
                .authorities(Collections.emptyList()).build();
        UserDetails differentUser = User.withUsername("other@example.com").password("password")
                .authorities(Collections.emptyList()).build();

        assertTrue(jwtService.isTokenValid(token, matchingUser));
        assertFalse(jwtService.isTokenValid(token, differentUser));
    }
}
