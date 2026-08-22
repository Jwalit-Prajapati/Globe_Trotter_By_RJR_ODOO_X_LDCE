package com.RJR.GlobeTrotter.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.RJR.GlobeTrotter.repository.ActivityRepository;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;

class StopActivityServiceTest {

    @Test
    void rejectsAddingActivityToMissingStop() {
        StopRepository stops = mock(StopRepository.class);
        when(stops.findById(99L)).thenReturn(Optional.empty());
        StopActivityService service = new StopActivityService(mock(StopActivityRepository.class), stops,
                mock(ActivityRepository.class));

        com.RJR.GlobeTrotter.dto.request.StopActivityRequest request =
                com.RJR.GlobeTrotter.dto.request.StopActivityRequest.builder()
                        .stopId(99L).activityId(1L).dayDate(java.time.LocalDate.now())
                        .cost(java.math.BigDecimal.ZERO).build();

        assertThrows(ResourceNotFoundException.class, () -> service.addActivityToStop(1L, request));
    }
}
