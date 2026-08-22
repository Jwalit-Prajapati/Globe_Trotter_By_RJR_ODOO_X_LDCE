package com.RJR.GlobeTrotter.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.RJR.GlobeTrotter.security.AuthEntryPoint;
import com.RJR.GlobeTrotter.security.CustomUserDetailsService;
import com.RJR.GlobeTrotter.security.JwtAuthenticationFilter;

class SecurityConfigTest {

    @Test
    void createsPasswordEncoder() {
        SecurityConfig config = new SecurityConfig(mock(CustomUserDetailsService.class),
                mock(AuthEntryPoint.class), mock(JwtAuthenticationFilter.class));

        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder.matches("password", encoder.encode("password")));
    }
}
