package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class UserResponseTest {

    @Test
    void builderStoresUserDetails() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 8, 22, 10, 30);
        UserResponse response = UserResponse.builder().id(1L).name("Alex").email("alex@example.com")
                .createdAt(createdAt).build();

        assertEquals(1L, response.getId());
        assertEquals("Alex", response.getName());
        assertEquals("alex@example.com", response.getEmail());
        assertEquals(createdAt, response.getCreatedAt());
    }
}
