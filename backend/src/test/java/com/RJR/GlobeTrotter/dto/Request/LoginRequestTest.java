package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    void constructorsAndGettersSettersWork() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        
        LoginRequest emptyRequest = new LoginRequest();
        emptyRequest.setEmail("new@example.com");
        emptyRequest.setPassword("newpass");
        
        assertEquals("new@example.com", emptyRequest.getEmail());
        assertEquals("newpass", emptyRequest.getPassword());
    }
}
