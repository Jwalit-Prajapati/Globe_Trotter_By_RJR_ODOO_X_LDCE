package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RegisterRequestTest {

    @Test
    void constructorsAndGettersSettersWork() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123");
        
        assertEquals("John Doe", request.getName());
        assertEquals("john@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        
        RegisterRequest emptyRequest = new RegisterRequest();
        emptyRequest.setName("Jane Doe");
        emptyRequest.setEmail("jane@example.com");
        emptyRequest.setPassword("newpass");
        
        assertEquals("Jane Doe", emptyRequest.getName());
        assertEquals("jane@example.com", emptyRequest.getEmail());
        assertEquals("newpass", emptyRequest.getPassword());
    }
}
