package com.RJR.GlobeTrotter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.RJR.GlobeTrotter.dto.request.LoginRequest;
import com.RJR.GlobeTrotter.dto.request.RegisterRequest;
import com.RJR.GlobeTrotter.dto.response.AuthResponse;
import com.RJR.GlobeTrotter.service.AuthService;

class AuthControllerTest {

    @Test
    void registerReturnsCreatedResponse() {
        AuthService service = mock(AuthService.class);
        AuthController controller = new AuthController(service);
        RegisterRequest request = new RegisterRequest("Alex", "alex@example.com", "password123");
        AuthResponse expected = AuthResponse.builder().token("token").build();
        when(service.register(request)).thenReturn(expected);

        ResponseEntity<AuthResponse> response = controller.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).register(request);
    }

    @Test
    void loginReturnsOkResponse() {
        AuthService service = mock(AuthService.class);
        AuthController controller = new AuthController(service);
        LoginRequest request = new LoginRequest("alex@example.com", "password123");
        AuthResponse expected = AuthResponse.builder().token("token").build();
        when(service.login(request)).thenReturn(expected);

        ResponseEntity<AuthResponse> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).login(request);
    }
}
