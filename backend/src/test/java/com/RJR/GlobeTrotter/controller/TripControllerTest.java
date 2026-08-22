package com.RJR.GlobeTrotter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.RJR.GlobeTrotter.dto.response.ShareResponse;
import com.RJR.GlobeTrotter.dto.response.TripResponse;
import com.RJR.GlobeTrotter.dto.response.TripSummaryResponse;
import com.RJR.GlobeTrotter.dto.request.TripRequest;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.TripService;

class TripControllerTest {

    @Test
    void getPublicTripReturnsOkResponse() {
        TripService service = mock(TripService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        TripController controller = new TripController(service, users);
        TripResponse expected = TripResponse.builder().id(10L).name("Europe").build();
        when(service.getPublicTrip("public-trip")).thenReturn(expected);

        ResponseEntity<TripResponse> response = controller.getPublicTrip("public-trip");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getPublicTrip("public-trip");
    }

    @Test
    void shareTripReturnsOkResponseForCurrentUser() {
        TripService service = mock(TripService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        TripController controller = new TripController(service, users);
        ShareResponse expected = ShareResponse.builder().tripId(10L).publicSlug("trip").build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.shareTrip(5L, 10L)).thenReturn(expected);

        ResponseEntity<ShareResponse> response = controller.shareTrip(authentication, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).shareTrip(5L, 10L);
    }

    @Test
    void createTripReturnsCreatedResponse() {
        TripService service = mock(TripService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        TripController controller = new TripController(service, users);
        TripRequest request = TripRequest.builder().name("Europe").build();
        TripResponse expected = TripResponse.builder().id(10L).name("Europe").build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.createTrip(5L, request)).thenReturn(expected);

        ResponseEntity<TripResponse> response = controller.createTrip(authentication, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).createTrip(5L, request);
    }

    @Test
    void getUserTripsReturnsOkResponse() {
        TripService service = mock(TripService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        TripController controller = new TripController(service, users);
        java.util.List<TripSummaryResponse> expected = java.util.List.of(
                TripSummaryResponse.builder().id(10L).name("Europe").build());
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.getUserTrips(5L)).thenReturn(expected);

        ResponseEntity<java.util.List<TripSummaryResponse>> response = controller.getUserTrips(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getUserTrips(5L);
    }

    @Test
    void getTripByIdReturnsOkResponse() {
        TripService service = mock(TripService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        TripController controller = new TripController(service, users);
        TripResponse expected = TripResponse.builder().id(10L).build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.getTripById(5L, 10L)).thenReturn(expected);

        ResponseEntity<TripResponse> response = controller.getTripById(authentication, 10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getTripById(5L, 10L);
    }

    @Test
    void updateTripReturnsOkResponse() {
        TripService service = mock(TripService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        TripController controller = new TripController(service, users);
        TripRequest request = TripRequest.builder().name("Updated Europe").build();
        TripResponse expected = TripResponse.builder().id(10L).name("Updated Europe").build();
        when(users.getUserId(authentication)).thenReturn(5L);
        when(service.updateTrip(5L, 10L, request)).thenReturn(expected);

        ResponseEntity<TripResponse> response = controller.updateTrip(authentication, 10L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).updateTrip(5L, 10L, request);
    }

    @Test
    void deleteTripReturnsNoContent() {
        TripService service = mock(TripService.class);
        CurrentUserProvider users = mock(CurrentUserProvider.class);
        Authentication authentication = mock(Authentication.class);
        TripController controller = new TripController(service, users);
        when(users.getUserId(authentication)).thenReturn(5L);

        ResponseEntity<Void> response = controller.deleteTrip(authentication, 10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteTrip(5L, 10L);
    }
}
