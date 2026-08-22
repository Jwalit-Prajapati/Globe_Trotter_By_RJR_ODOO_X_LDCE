package com.RJR.GlobeTrotter.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.RJR.GlobeTrotter.entity.Role;
import com.RJR.GlobeTrotter.entity.User;
import com.RJR.GlobeTrotter.repository.UserRepository;

class CustomUserDetailsServiceTest {

    @Test
    void mapsUserAndRoleToUserDetails() {
        UserRepository repository = mock(UserRepository.class);
        User user = User.builder().email("alex@example.com").passwordHash("encoded-password")
                .role(Role.USER).build();
        when(repository.findByEmail("alex@example.com")).thenReturn(Optional.of(user));

        UserDetails details = new CustomUserDetailsService(repository).loadUserByUsername("alex@example.com");

        assertEquals("alex@example.com", details.getUsername());
        assertEquals("encoded-password", details.getPassword());
        assertEquals(1, details.getAuthorities().size());
        assertEquals("ROLE_USER", details.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void throwsWhenUserDoesNotExist() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> new CustomUserDetailsService(repository).loadUserByUsername("missing@example.com"));

        assertEquals("No user found with email: missing@example.com", exception.getMessage());
    }
}
