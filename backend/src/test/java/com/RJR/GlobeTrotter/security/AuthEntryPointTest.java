package com.RJR.GlobeTrotter.security;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AuthEntryPointTest {

    @Test
    void writesUnauthorizedResponseBody() throws Exception {
        AuthEntryPoint entryPoint = new AuthEntryPoint();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        when(request.getRequestURI()).thenReturn("/api/trips");
        when(response.getOutputStream()).thenReturn(new ByteArrayServletOutputStream(output));

        entryPoint.commence(request, response, new BadCredentialsException("Bad credentials"));

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        String body = output.toString();
        assertTrue(body.contains("\"status\":401"));
        assertTrue(body.contains("\"path\":\"/api/trips\""));
        assertTrue(body.contains("\"message\":\"Bad credentials\""));
    }

    private static final class ByteArrayServletOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream output;

        private ByteArrayServletOutputStream(ByteArrayOutputStream output) {
            this.output = output;
        }

        @Override
        public void write(int value) throws IOException {
            output.write(value);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(jakarta.servlet.WriteListener listener) {
        }
    }
}
