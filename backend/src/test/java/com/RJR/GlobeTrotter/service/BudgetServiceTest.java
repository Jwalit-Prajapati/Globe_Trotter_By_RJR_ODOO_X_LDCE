package com.RJR.GlobeTrotter.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;

class BudgetServiceTest {

    @Test
    void rejectsBudgetLookupForMissingTrip() {
        TripRepository trips = mock(TripRepository.class);
        when(trips.findById(99L)).thenReturn(Optional.empty());
        BudgetService service = new BudgetService(trips, mock(StopRepository.class), mock(StopActivityRepository.class));

        assertThrows(ResourceNotFoundException.class, () -> service.getTripBudget(1L, 99L));
    }
}
