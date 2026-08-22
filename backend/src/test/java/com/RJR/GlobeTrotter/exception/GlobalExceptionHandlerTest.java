package com.RJR.GlobeTrotter.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsAccessDeniedToForbidden() {
        ResponseEntity<Map<String, Object>> response = handler
                .handleAccessDenied(new AccessDeniedException("denied"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(403, response.getBody().get("status"));
        assertEquals("Forbidden", response.getBody().get("error"));
        assertEquals("You do not have permission to perform this action", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void mapsDuplicateEmailToConflict() {
        ResponseEntity<Map<String, Object>> response = handler
                .handleEmailAlreadyInUse(new EmailAlreadyInUseException("alex@example.com"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertEquals("Email already in use: alex@example.com", response.getBody().get("message"));
    }

    @Test
    void mapsInvalidCredentialsToUnauthorized() {
        ResponseEntity<Map<String, Object>> response = handler
                .handleInvalidCredentials(new InvalidCredentialsException());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().get("status"));
        assertEquals("Invalid email or password", response.getBody().get("message"));
    }
}
