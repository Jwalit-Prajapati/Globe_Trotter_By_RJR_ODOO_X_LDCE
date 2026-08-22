package com.RJR.GlobeTrotter.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.RJR.GlobeTrotter.repository.CityRepository;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;

class StopServiceTest {

    @Test
    void rejectsStopCreationForMissingTrip() {
        TripRepository trips = mock(TripRepository.class);
        when(trips.findById(99L)).thenReturn(Optional.empty());
        StopService service = new StopService(mock(StopRepository.class), trips, mock(CityRepository.class),
                mock(StopActivityRepository.class));
        com.RJR.GlobeTrotter.dto.request.StopRequest request =
                com.RJR.GlobeTrotter.dto.request.StopRequest.builder().tripId(99L).cityId(1L).build();

        assertThrows(ResourceNotFoundException.class, () -> service.createStop(1L, request));
    }
}
