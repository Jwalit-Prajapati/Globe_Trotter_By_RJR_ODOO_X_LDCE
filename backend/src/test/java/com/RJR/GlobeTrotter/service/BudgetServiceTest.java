package com.RJR.GlobeTrotter.service;

<<<<<<< HEAD
<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
=======
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
=======
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

<<<<<<< HEAD
<<<<<<< HEAD
import java.math.BigDecimal;
import java.util.List;
=======
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
=======
=======
import java.math.BigDecimal;
import java.util.List;
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
import java.util.Optional;

import org.junit.jupiter.api.Test;

<<<<<<< HEAD
<<<<<<< HEAD
=======
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
=======
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
import com.RJR.GlobeTrotter.dto.response.BudgetResponse;
import com.RJR.GlobeTrotter.entity.Stop;
import com.RJR.GlobeTrotter.entity.StopActivity;
import com.RJR.GlobeTrotter.entity.Trip;
import com.RJR.GlobeTrotter.entity.User;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;

class BudgetServiceTest {

    private final TripRepository tripRepository = mock(TripRepository.class);
    private final StopRepository stopRepository = mock(StopRepository.class);
    private final StopActivityRepository stopActivityRepository = mock(StopActivityRepository.class);

    private final BudgetService budgetService = new BudgetService(tripRepository, stopRepository,
            stopActivityRepository);

    @Test
    void getTripBudgetSumsCostsAndComputesRemainingBudget() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(10L).user(user).budgetLimit(new BigDecimal("1000.00")).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

        Stop stop1 = Stop.builder().id(100L).transportCost(new BigDecimal("50.00"))
                .stayCost(new BigDecimal("200.00")).mealCost(new BigDecimal("30.00")).build();
        Stop stop2 = Stop.builder().id(101L).transportCost(new BigDecimal("20.00"))
                .stayCost(null).mealCost(new BigDecimal("10.00")).build();
        when(stopRepository.findByTripId(10L)).thenReturn(List.of(stop1, stop2));

        StopActivity activity1 = StopActivity.builder().cost(new BigDecimal("40.00")).build();
        StopActivity activity2 = StopActivity.builder().cost(null).build();
        when(stopActivityRepository.findByStopIdIn(List.of(100L, 101L))).thenReturn(List.of(activity1, activity2));

        BudgetResponse response = budgetService.getTripBudget(1L, 10L);

        assertEquals(10L, response.getTripId());
        assertEquals(new BigDecimal("1000.00"), response.getBudgetLimit());
        assertEquals(new BigDecimal("70.00"), response.getTransportCost());
        assertEquals(new BigDecimal("200.00"), response.getStayCost());
        assertEquals(new BigDecimal("40.00"), response.getMealCost());
        assertEquals(new BigDecimal("40.00"), response.getActivityCost());
        assertEquals(new BigDecimal("350.00"), response.getTotalCost());
        assertEquals(new BigDecimal("650.00"), response.getRemainingBudget());
    }

    @Test
    void getTripBudgetReturnsNullRemainingBudgetWhenNoBudgetLimit() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(10L).user(user).budgetLimit(null).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
        when(stopRepository.findByTripId(10L)).thenReturn(List.of());
        when(stopActivityRepository.findByStopIdIn(List.of())).thenReturn(List.of());

        BudgetResponse response = budgetService.getTripBudget(1L, 10L);

        assertNull(response.getBudgetLimit());
        assertNull(response.getRemainingBudget());
        assertEquals(BigDecimal.ZERO, response.getTotalCost());
    }

    @Test
    void getTripBudgetThrowsWhenTripMissing() {
        when(tripRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> budgetService.getTripBudget(1L, 10L));
    }

    @Test
    void getTripBudgetThrowsWhenNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(10L).user(owner).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> budgetService.getTripBudget(1L, 10L));
<<<<<<< HEAD
=======
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
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
=======
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
    }
}
