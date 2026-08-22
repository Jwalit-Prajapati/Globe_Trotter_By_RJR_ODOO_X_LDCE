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

import com.RJR.GlobeTrotter.dto.response.StopResponse;
import com.RJR.GlobeTrotter.dto.request.StopRequest;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.StopService;

class StopControllerTest {

    @Test
    void getStopsForTripReturnsOkResponse() {
        StopService service = mock(StopService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopController controller = new StopController(service, users);
        List<StopResponse> expected = List.of(StopResponse.builder().id(1L).build());
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.getStopsForTrip(5L, 10L)).thenReturn(expected);

        ResponseEntity<List<StopResponse>> response = controller.getStopsForTrip(authentication, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getStopsForTrip(5L, 10L);
    }

    @Test
    void deleteStopReturnsNoContent() {
        StopService service = mock(StopService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopController controller = new StopController(service, users);
        when(users.getUserId(authentication)).thenReturn(5L);

        ResponseEntity<Void> response = controller.deleteStop(authentication, 10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteStop(5L, 10L);
    }

    @Test
    void createStopReturnsCreatedResponse() {
        StopService service = mock(StopService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopController controller = new StopController(service, users);
        StopRequest request = StopRequest.builder().tripId(10L).cityId(20L).build();
        StopResponse expected = StopResponse.builder().id(30L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.createStop(5L, request)).thenReturn(expected);

        ResponseEntity<StopResponse> response = controller.createStop(authentication, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).createStop(5L, request);
    }

    @Test
    void getStopByIdReturnsOkResponse() {
        StopService service = mock(StopService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopController controller = new StopController(service, users);
        StopResponse expected = StopResponse.builder().id(30L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.getStopById(5L, 30L)).thenReturn(expected);

        ResponseEntity<StopResponse> response = controller.getStopById(authentication, 30L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getStopById(5L, 30L);
    }

    @Test
    void updateStopReturnsOkResponse() {
        StopService service = mock(StopService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        StopController controller = new StopController(service, users);
        StopRequest request = StopRequest.builder().tripId(10L).cityId(20L).build();
        StopResponse expected = StopResponse.builder().id(30L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.updateStop(5L, 30L, request)).thenReturn(expected);

        ResponseEntity<StopResponse> response = controller.updateStop(authentication, 30L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).updateStop(5L, 30L, request);
    }
}
