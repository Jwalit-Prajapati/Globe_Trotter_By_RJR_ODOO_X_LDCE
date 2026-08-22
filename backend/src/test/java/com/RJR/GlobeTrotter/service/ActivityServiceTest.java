package com.RJR.GlobeTrotter.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.RJR.GlobeTrotter.repository.ActivityRepository;
import com.RJR.GlobeTrotter.repository.CityRepository;

class ActivityServiceTest {

    @Test
    void searchWithoutFiltersReturnsAllActivities() {
        ActivityRepository activities = mock(ActivityRepository.class);
        when(activities.findAll()).thenReturn(List.of());
        ActivityService service = new ActivityService(activities, mock(CityRepository.class));

        assertTrue(service.searchActivities(null, null).isEmpty());
    }
}
