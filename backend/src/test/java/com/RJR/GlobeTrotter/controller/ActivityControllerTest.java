package com.RJR.GlobeTrotter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.RJR.GlobeTrotter.dto.response.ActivityResponse;
import com.RJR.GlobeTrotter.dto.request.ActivityRequest;
import com.RJR.GlobeTrotter.service.ActivityService;

class ActivityControllerTest {

    @Test
    void searchActivitiesReturnsOkResponse() {
        ActivityService service = mock(ActivityService.class);
        ActivityController controller = new ActivityController(service);
        List<ActivityResponse> expected = List.of(ActivityResponse.builder().id(1L).name("Museum").build());
        when(service.searchActivities(2L, "Culture")).thenReturn(expected);

        ResponseEntity<List<ActivityResponse>> response = controller.searchActivities(2L, "Culture");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).searchActivities(2L, "Culture");
    }

    @Test
    void deleteActivityReturnsNoContent() {
        ActivityService service = mock(ActivityService.class);
        ActivityController controller = new ActivityController(service);

        ResponseEntity<Void> response = controller.deleteActivity(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteActivity(1L);
    }

    @Test
    void getActivityByIdReturnsOkResponse() {
        ActivityService service = mock(ActivityService.class);
        ActivityController controller = new ActivityController(service);
        ActivityResponse expected = ActivityResponse.builder().id(1L).build();
        when(service.getActivityById(1L)).thenReturn(expected);

        ResponseEntity<ActivityResponse> response = controller.getActivityById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getActivityById(1L);
    }

    @Test
    void createActivityReturnsCreatedResponse() {
        ActivityService service = mock(ActivityService.class);
        ActivityController controller = new ActivityController(service);
        ActivityRequest request = ActivityRequest.builder().cityId(2L).name("Museum").build();
        ActivityResponse expected = ActivityResponse.builder().id(1L).build();
        when(service.createActivity(request)).thenReturn(expected);

        ResponseEntity<ActivityResponse> response = controller.createActivity(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).createActivity(request);
    }

    @Test
    void updateActivityReturnsOkResponse() {
        ActivityService service = mock(ActivityService.class);
        ActivityController controller = new ActivityController(service);
        ActivityRequest request = ActivityRequest.builder().cityId(2L).name("Updated Museum").build();
        ActivityResponse expected = ActivityResponse.builder().id(1L).name("Updated Museum").build();
        when(service.updateActivity(1L, request)).thenReturn(expected);

        ResponseEntity<ActivityResponse> response = controller.updateActivity(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).updateActivity(1L, request);
    }
}
