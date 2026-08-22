package com.RJR.GlobeTrotter.service;

<<<<<<< HEAD
=======
<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.RJR.GlobeTrotter.dto.request.ActivityRequest;
import com.RJR.GlobeTrotter.dto.response.ActivityResponse;
import com.RJR.GlobeTrotter.entity.Activity;
import com.RJR.GlobeTrotter.entity.City;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
<<<<<<< HEAD
=======
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
import com.RJR.GlobeTrotter.repository.ActivityRepository;
import com.RJR.GlobeTrotter.repository.CityRepository;

class ActivityServiceTest {

<<<<<<< HEAD
=======
<<<<<<< HEAD
    @Test
    void searchWithoutFiltersReturnsAllActivities() {
        ActivityRepository activities = mock(ActivityRepository.class);
        when(activities.findAll()).thenReturn(List.of());
        ActivityService service = new ActivityService(activities, mock(CityRepository.class));

        assertTrue(service.searchActivities(null, null).isEmpty());
=======
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
    private final ActivityRepository activityRepository = mock(ActivityRepository.class);
    private final CityRepository cityRepository = mock(CityRepository.class);

    private final ActivityService activityService = new ActivityService(activityRepository, cityRepository);

    private ActivityRequest sampleRequest(Long cityId) {
        return ActivityRequest.builder()
                .cityId(cityId)
                .name("Louvre Museum Tour")
                .category("Culture")
                .durationMinutes(120)
                .estimatedCost(new BigDecimal("25.00"))
                .description("Guided tour of the Louvre")
                .imageUrl("https://example.com/louvre.jpg")
                .build();
    }

    @Test
    void searchActivitiesFiltersByCityAndCategoryWhenBothProvided() {
        City city = City.builder().id(1L).build();
        Activity activity = Activity.builder().id(10L).city(city).name("Louvre Museum Tour").build();
        when(activityRepository.findByCityIdAndCategoryIgnoreCase(1L, "culture")).thenReturn(List.of(activity));

        List<ActivityResponse> results = activityService.searchActivities(1L, "culture");

        assertEquals(1, results.size());
        assertEquals("Louvre Museum Tour", results.get(0).getName());
        verify(activityRepository, never()).findAll();
    }

    @Test
    void searchActivitiesFiltersByCityOnlyWhenCategoryBlank() {
        City city = City.builder().id(1L).build();
        Activity activity = Activity.builder().id(10L).city(city).build();
        when(activityRepository.findByCityId(1L)).thenReturn(List.of(activity));

        List<ActivityResponse> results = activityService.searchActivities(1L, "  ");

        assertEquals(1, results.size());
        verify(activityRepository).findByCityId(1L);
    }

    @Test
    void searchActivitiesFiltersByCategoryOnlyWhenCityMissing() {
        City city = City.builder().id(1L).build();
        Activity activity = Activity.builder().id(10L).city(city).build();
        when(activityRepository.findByCategoryIgnoreCase("culture")).thenReturn(List.of(activity));

        List<ActivityResponse> results = activityService.searchActivities(null, "culture");

        assertEquals(1, results.size());
        verify(activityRepository).findByCategoryIgnoreCase("culture");
    }

    @Test
    void searchActivitiesReturnsAllWhenNoFiltersProvided() {
        City city = City.builder().id(1L).build();
        Activity activity = Activity.builder().id(10L).city(city).build();
        when(activityRepository.findAll()).thenReturn(List.of(activity));

        List<ActivityResponse> results = activityService.searchActivities(null, "");

        assertEquals(1, results.size());
        verify(activityRepository).findAll();
    }

    @Test
    void getActivityByIdReturnsActivity() {
        City city = City.builder().id(1L).build();
        Activity activity = Activity.builder().id(10L).city(city).name("Louvre Museum Tour").build();
        when(activityRepository.findById(10L)).thenReturn(Optional.of(activity));

        ActivityResponse response = activityService.getActivityById(10L);

        assertEquals(10L, response.getId());
        assertEquals(1L, response.getCityId());
        assertEquals("Louvre Museum Tour", response.getName());
    }

    @Test
    void getActivityByIdThrowsWhenMissing() {
        when(activityRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.getActivityById(10L));
    }

    @Test
    void createActivitySavesAndReturnsActivity() {
        City city = City.builder().id(1L).build();
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> {
            Activity activity = invocation.getArgument(0);
            activity.setId(10L);
            return activity;
        });

        ActivityResponse response = activityService.createActivity(sampleRequest(1L));

        assertEquals(10L, response.getId());
        assertEquals(1L, response.getCityId());
        assertEquals("Louvre Museum Tour", response.getName());

        ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository).save(activityCaptor.capture());
        assertEquals(city, activityCaptor.getValue().getCity());
        assertEquals(new BigDecimal("25.00"), activityCaptor.getValue().getEstimatedCost());
    }

    @Test
    void createActivityThrowsWhenCityMissing() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.createActivity(sampleRequest(1L)));

        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void updateActivityKeepsSameCityWithoutLookup() {
        City city = City.builder().id(1L).build();
        Activity activity = Activity.builder().id(10L).city(city).name("Old Name").build();
        when(activityRepository.findById(10L)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ActivityResponse response = activityService.updateActivity(10L, sampleRequest(1L));

        assertEquals("Louvre Museum Tour", response.getName());
        assertEquals(1L, response.getCityId());
        verify(cityRepository, never()).findById(any());
        verify(activityRepository).save(activity);
    }

    @Test
    void updateActivityLooksUpNewCityWhenChanged() {
        City oldCity = City.builder().id(1L).build();
        City newCity = City.builder().id(2L).build();
        Activity activity = Activity.builder().id(10L).city(oldCity).name("Old Name").build();
        when(activityRepository.findById(10L)).thenReturn(Optional.of(activity));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(newCity));
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ActivityResponse response = activityService.updateActivity(10L, sampleRequest(2L));

        assertEquals(2L, response.getCityId());
        verify(cityRepository).findById(2L);
    }

    @Test
    void updateActivityThrowsWhenActivityMissing() {
        when(activityRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> activityService.updateActivity(10L, sampleRequest(1L)));

        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void updateActivityThrowsWhenNewCityMissing() {
        City oldCity = City.builder().id(1L).build();
        Activity activity = Activity.builder().id(10L).city(oldCity).build();
        when(activityRepository.findById(10L)).thenReturn(Optional.of(activity));
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> activityService.updateActivity(10L, sampleRequest(2L)));

        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void deleteActivityRemovesExistingActivity() {
        when(activityRepository.existsById(10L)).thenReturn(true);

        activityService.deleteActivity(10L);

        verify(activityRepository).deleteById(10L);
    }

    @Test
    void deleteActivityThrowsWhenMissing() {
        when(activityRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> activityService.deleteActivity(10L));

        verify(activityRepository, never()).deleteById(any());
<<<<<<< HEAD
=======
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
>>>>>>> 060f2bb8a5e05d5070723012c61cebafa95a2b94
    }
}
