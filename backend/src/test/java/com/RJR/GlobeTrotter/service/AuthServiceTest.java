package com.RJR.GlobeTrotter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
<<<<<<< HEAD
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
=======
import static org.mockito.Mockito.mock;
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
<<<<<<< HEAD
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
=======
import org.springframework.security.authentication.AuthenticationManager;
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
import org.springframework.security.crypto.password.PasswordEncoder;

import com.RJR.GlobeTrotter.dto.request.LoginRequest;
import com.RJR.GlobeTrotter.dto.request.RegisterRequest;
import com.RJR.GlobeTrotter.dto.response.AuthResponse;
import com.RJR.GlobeTrotter.entity.User;
import com.RJR.GlobeTrotter.exception.EmailAlreadyInUseException;
import com.RJR.GlobeTrotter.exception.InvalidCredentialsException;
import com.RJR.GlobeTrotter.repository.UserRepository;
import com.RJR.GlobeTrotter.security.JwtService;

class AuthServiceTest {

<<<<<<< HEAD
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    private final AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService,
            authenticationManager);

    @Test
    void registerCreatesUserAndReturnsToken() {
        RegisterRequest request = new RegisterRequest("Alex", "alex@example.com", "password123");
        when(userRepository.existsByEmail("alex@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateToken(1L, "alex@example.com")).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertEquals("token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("Alex", response.getUser().getName());
        assertEquals("alex@example.com", response.getUser().getEmail());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("Alex", userCaptor.getValue().getName());
        assertEquals("alex@example.com", userCaptor.getValue().getEmail());
        assertEquals("encoded-password", userCaptor.getValue().getPasswordHash());
    }

    @Test
    void registerThrowsWhenEmailAlreadyInUse() {
        RegisterRequest request = new RegisterRequest("Alex", "alex@example.com", "password123");
        when(userRepository.existsByEmail("alex@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> authService.register(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginAuthenticatesAndReturnsToken() {
        LoginRequest request = new LoginRequest("alex@example.com", "password123");
        User user = User.builder().id(1L).name("Alex").email("alex@example.com")
                .passwordHash("encoded-password").build();
        when(userRepository.findByEmail("alex@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(1L, "alex@example.com")).thenReturn("token");

        AuthResponse response = authService.login(request);
=======
    @Test
    void registerCreatesUserAndReturnsToken() {
        UserRepository users = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        JwtService jwt = mock(JwtService.class);
        AuthenticationManager authentication = mock(AuthenticationManager.class);
        AuthService service = new AuthService(users, encoder, jwt, authentication);
        RegisterRequest request = new RegisterRequest("Alex", "alex@example.com", "password123");
        User saved = User.builder().id(1L).name("Alex").email("alex@example.com")
                .passwordHash("encoded").build();

        when(users.existsByEmail(request.getEmail())).thenReturn(false);
        when(encoder.encode(request.getPassword())).thenReturn("encoded");
        when(users.save(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(saved);
        when(jwt.generateToken(1L, "alex@example.com")).thenReturn("token");

        AuthResponse response = service.register(request);
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8

        assertEquals("token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("alex@example.com", response.getUser().getEmail());
<<<<<<< HEAD
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("alex@example.com", "password123"));
    }

    @Test
    void loginThrowsInvalidCredentialsOnBadCredentials() {
        LoginRequest request = new LoginRequest("alex@example.com", "wrong-password");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad credentials"));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void loginThrowsInvalidCredentialsWhenUserMissingAfterAuthentication() {
        LoginRequest request = new LoginRequest("missing@example.com", "password123");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
=======
        verify(users).save(org.mockito.ArgumentMatchers.any(User.class));
    }

    @Test
    void registerRejectsDuplicateEmail() {
        UserRepository users = mock(UserRepository.class);
        AuthService service = new AuthService(users, mock(PasswordEncoder.class), mock(JwtService.class),
                mock(AuthenticationManager.class));
        RegisterRequest request = new RegisterRequest("Alex", "alex@example.com", "password123");
        when(users.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> service.register(request));
    }

    @Test
    void loginRejectsMissingUser() {
        UserRepository users = mock(UserRepository.class);
        AuthenticationManager authentication = mock(AuthenticationManager.class);
        AuthService service = new AuthService(users, mock(PasswordEncoder.class), mock(JwtService.class), authentication);
        LoginRequest request = new LoginRequest("missing@example.com", "password123");
        when(users.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> service.login(request));

        verify(authentication).authenticate(org.mockito.ArgumentMatchers.any());
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
    }
}
