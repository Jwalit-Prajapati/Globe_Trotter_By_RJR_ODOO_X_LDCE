package com.RJR.GlobeTrotter.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;
import com.RJR.GlobeTrotter.repository.UserRepository;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;

class TripServiceTest {

    @Test
    void rejectsTripCreationForMissingUser() {
        UserRepository users = mock(UserRepository.class);
        when(users.findById(99L)).thenReturn(Optional.empty());
        TripService service = new TripService(mock(TripRepository.class), users, mock(StopRepository.class));
        com.RJR.GlobeTrotter.dto.request.TripRequest request =
                com.RJR.GlobeTrotter.dto.request.TripRequest.builder().name("Trip").build();

        assertThrows(ResourceNotFoundException.class, () -> service.createTrip(99L, request));
    }
}
