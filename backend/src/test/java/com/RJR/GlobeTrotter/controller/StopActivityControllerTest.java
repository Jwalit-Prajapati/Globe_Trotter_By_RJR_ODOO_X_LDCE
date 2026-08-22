package com.RJR.GlobeTrotter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.RJR.GlobeTrotter.dto.response.StopActivityResponse;
import com.RJR.GlobeTrotter.dto.request.StopActivityRequest;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.StopActivityService;

class StopActivityControllerTest {

    @Test
    void getActivitiesForStopReturnsOkResponse() {
        StopActivityService service = mock(StopActivityService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopActivityController controller = new StopActivityController(service, users);
        List<StopActivityResponse> expected = List.of(StopActivityResponse.builder().id(1L).build());
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.getActivitiesForStop(5L, 10L)).thenReturn(expected);

        ResponseEntity<List<StopActivityResponse>> response = controller.getActivitiesForStop(authentication, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getActivitiesForStop(5L, 10L);
    }

    @Test
    void removeActivityReturnsNoContent() {
        StopActivityService service = mock(StopActivityService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopActivityController controller = new StopActivityController(service, users);
        when(users.getUserId(authentication)).thenReturn(5L);

        ResponseEntity<Void> response = controller.removeActivityFromStop(authentication, 10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).removeActivityFromStop(5L, 10L);
    }

    @Test
    void addActivityToStopReturnsCreatedResponse() {
        StopActivityService service = mock(StopActivityService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopActivityController controller = new StopActivityController(service, users);
        StopActivityRequest request = StopActivityRequest.builder().stopId(10L).activityId(20L)
                .dayDate(java.time.LocalDate.of(2026, 9, 1)).cost(java.math.BigDecimal.ZERO).build();
        StopActivityResponse expected = StopActivityResponse.builder().id(30L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.addActivityToStop(5L, request)).thenReturn(expected);

        ResponseEntity<StopActivityResponse> response = controller.addActivityToStop(authentication, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).addActivityToStop(5L, request);
    }

    @Test
    void getStopActivityByIdReturnsOkResponse() {
        StopActivityService service = mock(StopActivityService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopActivityController controller = new StopActivityController(service, users);
        StopActivityResponse expected = StopActivityResponse.builder().id(30L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.getStopActivityById(5L, 30L)).thenReturn(expected);

        ResponseEntity<StopActivityResponse> response = controller.getStopActivityById(authentication, 30L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getStopActivityById(5L, 30L);
    }

    @Test
    void updateStopActivityReturnsOkResponse() {
        StopActivityService service = mock(StopActivityService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopActivityController controller = new StopActivityController(service, users);
        StopActivityRequest request = StopActivityRequest.builder().stopId(10L).activityId(20L)
                .dayDate(java.time.LocalDate.of(2026, 9, 1)).cost(java.math.BigDecimal.ZERO).build();
        StopActivityResponse expected = StopActivityResponse.builder().id(30L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.updateStopActivity(5L, 30L, request)).thenReturn(expected);

        ResponseEntity<StopActivityResponse> response = controller.updateStopActivity(authentication, 30L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).updateStopActivity(5L, 30L, request);
    }
}
