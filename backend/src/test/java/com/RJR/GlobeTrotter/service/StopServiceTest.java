package com.RJR.GlobeTrotter.service;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

=======
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.RJR.GlobeTrotter.dto.request.StopRequest;
import com.RJR.GlobeTrotter.dto.response.StopResponse;
import com.RJR.GlobeTrotter.entity.City;
import com.RJR.GlobeTrotter.entity.Stop;
import com.RJR.GlobeTrotter.entity.Trip;
import com.RJR.GlobeTrotter.entity.User;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
<<<<<<< HEAD
=======
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
=======
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
import com.RJR.GlobeTrotter.repository.CityRepository;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;
<<<<<<< HEAD
<<<<<<< HEAD
=======
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
=======
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94

class StopServiceTest {

    private final StopRepository stopRepository = mock(StopRepository.class);
    private final TripRepository tripRepository = mock(TripRepository.class);
    private final CityRepository cityRepository = mock(CityRepository.class);
    private final StopActivityRepository stopActivityRepository = mock(StopActivityRepository.class);

    private final StopService stopService = new StopService(stopRepository, tripRepository, cityRepository,
            stopActivityRepository);

    private StopRequest sampleRequest(Long tripId, Long cityId, Integer orderIndex) {
        return StopRequest.builder()
                .tripId(tripId)
                .cityId(cityId)
                .startDate(LocalDate.of(2026, 6, 1))
                .endDate(LocalDate.of(2026, 6, 5))
                .orderIndex(orderIndex)
                .transportCost(new BigDecimal("50.00"))
                .stayCost(new BigDecimal("300.00"))
                .mealCost(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void createStopSavesStopWithGivenOrderIndex() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).name("Paris").build();
        when(tripRepository.findById(5L)).thenReturn(Optional.of(trip));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));
        when(stopRepository.existsByTripIdAndOrderIndex(5L, 0)).thenReturn(false);
        when(stopRepository.save(any(Stop.class))).thenAnswer(invocation -> {
            Stop stop = invocation.getArgument(0);
            stop.setId(20L);
            return stop;
        });
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(20L)).thenReturn(List.of());

        StopResponse response = stopService.createStop(1L, sampleRequest(5L, 2L, 0));

        assertEquals(20L, response.getId());
        assertEquals(5L, response.getTripId());
        assertEquals(0, response.getOrderIndex());
        assertEquals("Paris", response.getCity().getName());
    }

    @Test
    void createStopAssignsNextOrderIndexWhenNotProvided() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        when(tripRepository.findById(5L)).thenReturn(Optional.of(trip));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));
        Stop existingStop = Stop.builder().id(21L).orderIndex(3).build();
        when(stopRepository.findByTripIdOrderByOrderIndexAsc(5L)).thenReturn(List.of(existingStop));
        when(stopRepository.save(any(Stop.class))).thenAnswer(invocation -> {
            Stop stop = invocation.getArgument(0);
            stop.setId(22L);
            return stop;
        });
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(22L)).thenReturn(List.of());

        StopResponse response = stopService.createStop(1L, sampleRequest(5L, 2L, null));

        assertEquals(4, response.getOrderIndex());
        verify(stopRepository, never()).existsByTripIdAndOrderIndex(any(), any());
    }

    @Test
    void createStopThrowsWhenTripNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(5L).user(owner).build();
        when(tripRepository.findById(5L)).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> stopService.createStop(1L, sampleRequest(5L, 2L, 0)));

        verify(stopRepository, never()).save(any(Stop.class));
    }

    @Test
    void createStopThrowsWhenCityMissing() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        when(tripRepository.findById(5L)).thenReturn(Optional.of(trip));
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stopService.createStop(1L, sampleRequest(5L, 2L, 0)));
    }

    @Test
    void createStopThrowsWhenOrderIndexAlreadyExists() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        when(tripRepository.findById(5L)).thenReturn(Optional.of(trip));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));
        when(stopRepository.existsByTripIdAndOrderIndex(5L, 0)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> stopService.createStop(1L, sampleRequest(5L, 2L, 0)));

        verify(stopRepository, never()).save(any(Stop.class));
    }

    @Test
    void getStopsForTripReturnsOrderedStops() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(city).orderIndex(0).build();
        when(tripRepository.findById(5L)).thenReturn(Optional.of(trip));
        when(stopRepository.findByTripIdOrderByOrderIndexAsc(5L)).thenReturn(List.of(stop));
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(20L)).thenReturn(List.of());

        List<StopResponse> responses = stopService.getStopsForTrip(1L, 5L);

        assertEquals(1, responses.size());
        assertEquals(20L, responses.get(0).getId());
    }

    @Test
    void getStopsForTripThrowsWhenTripNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(5L).user(owner).build();
        when(tripRepository.findById(5L)).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> stopService.getStopsForTrip(1L, 5L));
    }

    @Test
    void getStopByIdReturnsStopForOwner() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(city).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(20L)).thenReturn(List.of());

        StopResponse response = stopService.getStopById(1L, 20L);

        assertEquals(20L, response.getId());
    }

    @Test
    void getStopByIdThrowsWhenStopMissing() {
        when(stopRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stopService.getStopById(1L, 20L));
    }

    @Test
    void getStopByIdThrowsWhenNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(5L).user(owner).build();
        Stop stop = Stop.builder().id(20L).trip(trip).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));

        assertThrows(ResourceNotFoundException.class, () -> stopService.getStopById(1L, 20L));
    }

    @Test
    void updateStopAppliesChangesWithoutCityLookupWhenCityUnchanged() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(city).orderIndex(0).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(stopRepository.save(any(Stop.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(20L)).thenReturn(List.of());

        StopResponse response = stopService.updateStop(1L, 20L, sampleRequest(5L, 2L, 0));

        assertEquals(new BigDecimal("50.00"), response.getTransportCost());
        verify(cityRepository, never()).findById(any());
    }

    @Test
    void updateStopThrowsWhenTripIdMismatch() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(city).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));

        assertThrows(IllegalArgumentException.class,
                () -> stopService.updateStop(1L, 20L, sampleRequest(999L, 2L, 0)));

        verify(stopRepository, never()).save(any(Stop.class));
    }

    @Test
    void updateStopLooksUpNewCityWhenChanged() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City oldCity = City.builder().id(2L).build();
        City newCity = City.builder().id(3L).name("Rome").build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(oldCity).orderIndex(0).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(cityRepository.findById(3L)).thenReturn(Optional.of(newCity));
        when(stopRepository.save(any(Stop.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(20L)).thenReturn(List.of());

        StopResponse response = stopService.updateStop(1L, 20L, sampleRequest(5L, 3L, 0));

        assertEquals("Rome", response.getCity().getName());
    }

    @Test
    void updateStopThrowsWhenNewCityMissing() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City oldCity = City.builder().id(2L).build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(oldCity).orderIndex(0).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(cityRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> stopService.updateStop(1L, 20L, sampleRequest(5L, 3L, 0)));

        verify(stopRepository, never()).save(any(Stop.class));
    }

    @Test
    void updateStopThrowsWhenNewOrderIndexAlreadyTaken() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(city).orderIndex(0).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(stopRepository.existsByTripIdAndOrderIndex(5L, 1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> stopService.updateStop(1L, 20L, sampleRequest(5L, 2L, 1)));

        verify(stopRepository, never()).save(any(Stop.class));
    }

    @Test
    void updateStopSkipsOrderIndexCheckWhenUnchanged() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        Stop stop = Stop.builder().id(20L).trip(trip).city(city).orderIndex(0).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(stopRepository.save(any(Stop.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(20L)).thenReturn(List.of());

        stopService.updateStop(1L, 20L, sampleRequest(5L, 2L, 0));

        verify(stopRepository, never()).existsByTripIdAndOrderIndex(any(), any());
    }

    @Test
    void deleteStopRemovesStopAndItsActivities() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        Stop stop = Stop.builder().id(20L).trip(trip).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));

        stopService.deleteStop(1L, 20L);

        verify(stopActivityRepository).deleteByStopId(20L);
        verify(stopRepository).delete(stop);
    }

    @Test
    void deleteStopThrowsWhenNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(5L).user(owner).build();
        Stop stop = Stop.builder().id(20L).trip(trip).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));

        assertThrows(ResourceNotFoundException.class, () -> stopService.deleteStop(1L, 20L));

        verify(stopRepository, never()).delete(any(Stop.class));
<<<<<<< HEAD
=======
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
>>>>>>> 7c5ebc2d69ca10790429b917c02ef1f6cbf77eb8
=======
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
    }
}
