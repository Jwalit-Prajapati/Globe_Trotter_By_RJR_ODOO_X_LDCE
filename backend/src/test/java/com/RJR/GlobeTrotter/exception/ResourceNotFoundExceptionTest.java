package com.RJR.GlobeTrotter.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void formatsLongIdentifierMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Trip", 42L);

        assertEquals("Trip not found with id: 42", exception.getMessage());
    }

    @Test
    void formatsStringIdentifierMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "alex@example.com");

        assertEquals("User not found with id: alex@example.com", exception.getMessage());
    }
}
