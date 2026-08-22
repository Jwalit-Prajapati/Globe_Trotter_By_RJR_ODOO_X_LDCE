package com.RJR.GlobeTrotter.service;

<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

=======
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
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
import java.util.Optional;

import org.junit.jupiter.api.Test;

<<<<<<< HEAD
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
=======
import com.RJR.GlobeTrotter.dto.request.StopActivityRequest;
import com.RJR.GlobeTrotter.dto.response.StopActivityResponse;
import com.RJR.GlobeTrotter.entity.Activity;
import com.RJR.GlobeTrotter.entity.City;
import com.RJR.GlobeTrotter.entity.Stop;
import com.RJR.GlobeTrotter.entity.StopActivity;
import com.RJR.GlobeTrotter.entity.Trip;
import com.RJR.GlobeTrotter.entity.User;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.ActivityRepository;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;

class StopActivityServiceTest {

    private final StopActivityRepository stopActivityRepository = mock(StopActivityRepository.class);
    private final StopRepository stopRepository = mock(StopRepository.class);
    private final ActivityRepository activityRepository = mock(ActivityRepository.class);

    private final StopActivityService stopActivityService = new StopActivityService(stopActivityRepository,
            stopRepository, activityRepository);

    private Stop ownedStop(Long stopId, Long userId) {
        User user = User.builder().id(userId).build();
        Trip trip = Trip.builder().id(5L).user(user).build();
        City city = City.builder().id(2L).build();
        return Stop.builder().id(stopId).trip(trip).city(city)
                .startDate(LocalDate.of(2026, 6, 1)).endDate(LocalDate.of(2026, 6, 5)).build();
    }

    private Activity sampleActivity(Long activityId) {
        City city = City.builder().id(2L).build();
        return Activity.builder().id(activityId).city(city).name("Louvre Museum Tour").build();
    }

    private StopActivityRequest sampleRequest(Long stopId, Long activityId, LocalDate dayDate) {
        return StopActivityRequest.builder()
                .stopId(stopId)
                .activityId(activityId)
                .dayDate(dayDate)
                .cost(new BigDecimal("25.00"))
                .build();
    }

    @Test
    void addActivityToStopSavesAndReturnsResponse() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(activityRepository.findById(30L)).thenReturn(Optional.of(activity));
        when(stopActivityRepository.existsByStopIdAndActivityIdAndDayDate(20L, 30L, LocalDate.of(2026, 6, 2)))
                .thenReturn(false);
        when(stopActivityRepository.save(any(StopActivity.class))).thenAnswer(invocation -> {
            StopActivity stopActivity = invocation.getArgument(0);
            stopActivity.setId(40L);
            return stopActivity;
        });

        StopActivityResponse response = stopActivityService.addActivityToStop(1L,
                sampleRequest(20L, 30L, LocalDate.of(2026, 6, 2)));

        assertEquals(40L, response.getId());
        assertEquals(20L, response.getStopId());
        assertEquals("Louvre Museum Tour", response.getActivity().getName());
        assertEquals(new BigDecimal("25.00"), response.getCost());
    }

    @Test
    void addActivityToStopThrowsWhenStopNotOwnedByUser() {
        Stop stop = ownedStop(20L, 2L);
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));

        assertThrows(ResourceNotFoundException.class,
                () -> stopActivityService.addActivityToStop(1L, sampleRequest(20L, 30L, LocalDate.of(2026, 6, 2))));

        verify(stopActivityRepository, never()).save(any(StopActivity.class));
    }

    @Test
    void addActivityToStopThrowsWhenActivityMissing() {
        Stop stop = ownedStop(20L, 1L);
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(activityRepository.findById(30L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> stopActivityService.addActivityToStop(1L, sampleRequest(20L, 30L, LocalDate.of(2026, 6, 2))));
    }

    @Test
    void addActivityToStopThrowsWhenDayBeforeStopStart() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(activityRepository.findById(30L)).thenReturn(Optional.of(activity));

        assertThrows(IllegalArgumentException.class, () -> stopActivityService.addActivityToStop(1L,
                sampleRequest(20L, 30L, LocalDate.of(2026, 5, 31))));

        verify(stopActivityRepository, never()).save(any(StopActivity.class));
    }

    @Test
    void addActivityToStopThrowsWhenDayAfterStopEnd() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(activityRepository.findById(30L)).thenReturn(Optional.of(activity));

        assertThrows(IllegalArgumentException.class, () -> stopActivityService.addActivityToStop(1L,
                sampleRequest(20L, 30L, LocalDate.of(2026, 6, 6))));
    }

    @Test
    void addActivityToStopThrowsWhenAlreadyScheduled() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(activityRepository.findById(30L)).thenReturn(Optional.of(activity));
        when(stopActivityRepository.existsByStopIdAndActivityIdAndDayDate(20L, 30L, LocalDate.of(2026, 6, 2)))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> stopActivityService.addActivityToStop(1L,
                sampleRequest(20L, 30L, LocalDate.of(2026, 6, 2))));

        verify(stopActivityRepository, never()).save(any(StopActivity.class));
    }

    @Test
    void getActivitiesForStopReturnsOrderedActivities() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(activity)
                .dayDate(LocalDate.of(2026, 6, 2)).cost(new BigDecimal("25.00")).build();
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));
        when(stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(20L))
                .thenReturn(List.of(stopActivity));

        List<StopActivityResponse> responses = stopActivityService.getActivitiesForStop(1L, 20L);

        assertEquals(1, responses.size());
        assertEquals(40L, responses.get(0).getId());
    }

    @Test
    void getActivitiesForStopThrowsWhenNotOwnedByUser() {
        Stop stop = ownedStop(20L, 2L);
        when(stopRepository.findById(20L)).thenReturn(Optional.of(stop));

        assertThrows(ResourceNotFoundException.class, () -> stopActivityService.getActivitiesForStop(1L, 20L));
    }

    @Test
    void getStopActivityByIdReturnsResponseForOwner() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(activity)
                .dayDate(LocalDate.of(2026, 6, 2)).cost(new BigDecimal("25.00")).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));

        StopActivityResponse response = stopActivityService.getStopActivityById(1L, 40L);

        assertEquals(40L, response.getId());
    }

    @Test
    void getStopActivityByIdThrowsWhenMissing() {
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stopActivityService.getStopActivityById(1L, 40L));
    }

    @Test
    void getStopActivityByIdThrowsWhenNotOwnedByUser() {
        Stop stop = ownedStop(20L, 2L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(activity).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));

        assertThrows(ResourceNotFoundException.class, () -> stopActivityService.getStopActivityById(1L, 40L));
    }

    @Test
    void updateStopActivityAppliesChangesWithoutLookupsWhenIdentityUnchanged() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(activity)
                .dayDate(LocalDate.of(2026, 6, 2)).cost(new BigDecimal("25.00")).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));
        when(stopActivityRepository.save(any(StopActivity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StopActivityResponse response = stopActivityService.updateStopActivity(1L, 40L,
                sampleRequest(20L, 30L, LocalDate.of(2026, 6, 2)));

        assertEquals(new BigDecimal("25.00"), response.getCost());
        verify(stopRepository, never()).findById(any());
        verify(activityRepository, never()).findById(any());
        verify(stopActivityRepository, never()).existsByStopIdAndActivityIdAndDayDate(any(), any(), any());
    }

    @Test
    void updateStopActivityLooksUpNewStopWhenChanged() {
        Stop oldStop = ownedStop(20L, 1L);
        Stop newStop = ownedStop(21L, 1L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(oldStop).activity(activity)
                .dayDate(LocalDate.of(2026, 6, 2)).cost(new BigDecimal("25.00")).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));
        when(stopRepository.findById(21L)).thenReturn(Optional.of(newStop));
        when(stopActivityRepository.existsByStopIdAndActivityIdAndDayDate(21L, 30L, LocalDate.of(2026, 6, 2)))
                .thenReturn(false);
        when(stopActivityRepository.save(any(StopActivity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StopActivityResponse response = stopActivityService.updateStopActivity(1L, 40L,
                sampleRequest(21L, 30L, LocalDate.of(2026, 6, 2)));

        assertEquals(21L, response.getStopId());
    }

    @Test
    void updateStopActivityThrowsWhenNewStopNotOwnedByUser() {
        Stop oldStop = ownedStop(20L, 1L);
        Stop otherUsersStop = ownedStop(21L, 2L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(oldStop).activity(activity)
                .dayDate(LocalDate.of(2026, 6, 2)).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));
        when(stopRepository.findById(21L)).thenReturn(Optional.of(otherUsersStop));

        assertThrows(ResourceNotFoundException.class, () -> stopActivityService.updateStopActivity(1L, 40L,
                sampleRequest(21L, 30L, LocalDate.of(2026, 6, 2))));

        verify(stopActivityRepository, never()).save(any(StopActivity.class));
    }

    @Test
    void updateStopActivityLooksUpNewActivityWhenChanged() {
        Stop stop = ownedStop(20L, 1L);
        Activity oldActivity = sampleActivity(30L);
        Activity newActivity = sampleActivity(31L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(oldActivity)
                .dayDate(LocalDate.of(2026, 6, 2)).cost(new BigDecimal("25.00")).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));
        when(activityRepository.findById(31L)).thenReturn(Optional.of(newActivity));
        when(stopActivityRepository.existsByStopIdAndActivityIdAndDayDate(20L, 31L, LocalDate.of(2026, 6, 2)))
                .thenReturn(false);
        when(stopActivityRepository.save(any(StopActivity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StopActivityResponse response = stopActivityService.updateStopActivity(1L, 40L,
                sampleRequest(20L, 31L, LocalDate.of(2026, 6, 2)));

        assertEquals(31L, response.getActivity().getId());
    }

    @Test
    void updateStopActivityThrowsWhenNewActivityMissing() {
        Stop stop = ownedStop(20L, 1L);
        Activity oldActivity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(oldActivity)
                .dayDate(LocalDate.of(2026, 6, 2)).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));
        when(activityRepository.findById(31L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stopActivityService.updateStopActivity(1L, 40L,
                sampleRequest(20L, 31L, LocalDate.of(2026, 6, 2))));

        verify(stopActivityRepository, never()).save(any(StopActivity.class));
    }

    @Test
    void updateStopActivityThrowsWhenNewDayOutsideStopRange() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(activity)
                .dayDate(LocalDate.of(2026, 6, 2)).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));

        assertThrows(IllegalArgumentException.class, () -> stopActivityService.updateStopActivity(1L, 40L,
                sampleRequest(20L, 30L, LocalDate.of(2026, 6, 10))));

        verify(stopActivityRepository, never()).save(any(StopActivity.class));
    }

    @Test
    void updateStopActivityThrowsWhenIdentityChangeCollides() {
        Stop stop = ownedStop(20L, 1L);
        Activity oldActivity = sampleActivity(30L);
        Activity newActivity = sampleActivity(31L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(oldActivity)
                .dayDate(LocalDate.of(2026, 6, 2)).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));
        when(activityRepository.findById(31L)).thenReturn(Optional.of(newActivity));
        when(stopActivityRepository.existsByStopIdAndActivityIdAndDayDate(20L, 31L, LocalDate.of(2026, 6, 2)))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> stopActivityService.updateStopActivity(1L, 40L,
                sampleRequest(20L, 31L, LocalDate.of(2026, 6, 2))));

        verify(stopActivityRepository, never()).save(any(StopActivity.class));
    }

    @Test
    void removeActivityFromStopDeletesOwnedActivity() {
        Stop stop = ownedStop(20L, 1L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(activity).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));

        stopActivityService.removeActivityFromStop(1L, 40L);

        verify(stopActivityRepository).delete(stopActivity);
    }

    @Test
    void removeActivityFromStopThrowsWhenNotOwnedByUser() {
        Stop stop = ownedStop(20L, 2L);
        Activity activity = sampleActivity(30L);
        StopActivity stopActivity = StopActivity.builder().id(40L).stop(stop).activity(activity).build();
        when(stopActivityRepository.findById(40L)).thenReturn(Optional.of(stopActivity));

        assertThrows(ResourceNotFoundException.class, () -> stopActivityService.removeActivityFromStop(1L, 40L));

        verify(stopActivityRepository, never()).delete(any(StopActivity.class));
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
    }
}
