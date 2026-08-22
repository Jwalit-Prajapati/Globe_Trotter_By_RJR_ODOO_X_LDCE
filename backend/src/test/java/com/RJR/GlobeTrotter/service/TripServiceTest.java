package com.RJR.GlobeTrotter.service;

<<<<<<< HEAD
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
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.RJR.GlobeTrotter.dto.request.TripRequest;
import com.RJR.GlobeTrotter.dto.response.ShareResponse;
import com.RJR.GlobeTrotter.dto.response.TripResponse;
import com.RJR.GlobeTrotter.dto.response.TripSummaryResponse;
import com.RJR.GlobeTrotter.entity.Stop;
import com.RJR.GlobeTrotter.entity.Trip;
import com.RJR.GlobeTrotter.entity.User;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;
import com.RJR.GlobeTrotter.repository.UserRepository;

class TripServiceTest {

    private final TripRepository tripRepository = mock(TripRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final StopRepository stopRepository = mock(StopRepository.class);

    private final TripService tripService = new TripService(tripRepository, userRepository, stopRepository);

    private TripRequest sampleRequest() {
        return TripRequest.builder()
                .name("Euro Trip")
                .description("Backpacking across Europe")
                .startDate(LocalDate.of(2026, 6, 1))
                .endDate(LocalDate.of(2026, 6, 20))
                .budgetLimit(new BigDecimal("2000.00"))
                .isPublic(false)
                .build();
    }

    @Test
    void createTripSavesTripForExistingUser() {
        User user = User.builder().id(1L).name("Alex").email("alex@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip trip = invocation.getArgument(0);
            trip.setId(10L);
            return trip;
        });

        TripResponse response = tripService.createTrip(1L, sampleRequest());

        assertEquals(10L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("Euro Trip", response.getName());
        assertEquals(new BigDecimal("2000.00"), response.getBudgetLimit());

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(tripCaptor.capture());
        assertEquals(user, tripCaptor.getValue().getUser());
        assertEquals("Backpacking across Europe", tripCaptor.getValue().getDescription());
    }

    @Test
    void createTripThrowsWhenUserMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.createTrip(1L, sampleRequest()));

        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void getUserTripsReturnsSummariesWithStopCount() {
        Trip trip = Trip.builder().id(10L).name("Euro Trip").isPublic(false).build();
        when(tripRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(trip));
        when(stopRepository.findByTripId(10L)).thenReturn(List.of(mock(Stop.class), mock(Stop.class)));

        List<TripSummaryResponse> summaries = tripService.getUserTrips(1L);

        assertEquals(1, summaries.size());
        assertEquals("Euro Trip", summaries.get(0).getName());
        assertEquals(2, summaries.get(0).getStopCount());
    }

    @Test
    void getTripByIdReturnsTripForOwner() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(10L).user(user).name("Euro Trip").build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

        TripResponse response = tripService.getTripById(1L, 10L);

        assertEquals(10L, response.getId());
        assertEquals("Euro Trip", response.getName());
    }

    @Test
    void getTripByIdThrowsWhenTripMissing() {
        when(tripRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.getTripById(1L, 10L));
    }

    @Test
    void getTripByIdThrowsWhenNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(10L).user(owner).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> tripService.getTripById(1L, 10L));
    }

    @Test
    void updateTripModifiesOwnedTrip() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(10L).user(user).name("Old Name").build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripResponse response = tripService.updateTrip(1L, 10L, sampleRequest());

        assertEquals("Euro Trip", response.getName());
        assertEquals("Backpacking across Europe", response.getDescription());
        verify(tripRepository).save(trip);
    }

    @Test
    void updateTripThrowsWhenNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(10L).user(owner).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> tripService.updateTrip(1L, 10L, sampleRequest()));

        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void deleteTripRemovesOwnedTrip() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(10L).user(user).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

        tripService.deleteTrip(1L, 10L);

        verify(tripRepository).delete(trip);
    }

    @Test
    void deleteTripThrowsWhenNotOwnedByUser() {
        User owner = User.builder().id(2L).build();
        Trip trip = Trip.builder().id(10L).user(owner).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> tripService.deleteTrip(1L, 10L));

        verify(tripRepository, never()).delete(any(Trip.class));
    }

    @Test
    void shareTripGeneratesSlugWhenMissing() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(10L).user(user).isPublic(false).build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
        when(tripRepository.existsByPublicSlug(any())).thenReturn(false);
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShareResponse response = tripService.shareTrip(1L, 10L);

        assertEquals(10L, response.getTripId());
        assertNotNull(response.getPublicSlug());
        assertEquals(10, response.getPublicSlug().length());
        assertEquals("/public/" + response.getPublicSlug(), response.getShareUrl());
        assertTrue(response.getIsPublic());
    }

    @Test
    void shareTripKeepsExistingSlug() {
        User user = User.builder().id(1L).build();
        Trip trip = Trip.builder().id(10L).user(user).isPublic(false).publicSlug("existing12").build();
        when(tripRepository.findById(10L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShareResponse response = tripService.shareTrip(1L, 10L);

        assertEquals("existing12", response.getPublicSlug());
        verify(tripRepository, never()).existsByPublicSlug(any());
    }

    @Test
    void getPublicTripReturnsTripWhenPublic() {
        Trip trip = Trip.builder().id(10L).user(User.builder().id(1L).build())
                .name("Euro Trip").isPublic(true).publicSlug("abc1234567").build();
        when(tripRepository.findByPublicSlug("abc1234567")).thenReturn(Optional.of(trip));

        TripResponse response = tripService.getPublicTrip("abc1234567");

        assertEquals("Euro Trip", response.getName());
        assertEquals("abc1234567", response.getPublicSlug());
    }

    @Test
    void getPublicTripThrowsWhenTripNotPublic() {
        Trip trip = Trip.builder().id(10L).user(User.builder().id(1L).build())
                .isPublic(false).publicSlug("abc1234567").build();
        when(tripRepository.findByPublicSlug("abc1234567")).thenReturn(Optional.of(trip));

        assertThrows(ResourceNotFoundException.class, () -> tripService.getPublicTrip("abc1234567"));
    }

    @Test
    void getPublicTripThrowsWhenSlugMissing() {
        when(tripRepository.findByPublicSlug("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.getPublicTrip("missing"));
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
    }
}
