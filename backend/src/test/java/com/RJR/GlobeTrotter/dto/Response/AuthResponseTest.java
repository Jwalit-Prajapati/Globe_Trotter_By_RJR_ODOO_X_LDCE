package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthResponseTest {

    @Test
    void builderStoresTokenAndUser() {
        UserResponse user = UserResponse.builder().id(1L).email("alex@example.com").build();
        AuthResponse response = AuthResponse.builder().token("token").tokenType("Bearer").user(user).build();

        assertEquals("token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(user, response.getUser());
    }
}
