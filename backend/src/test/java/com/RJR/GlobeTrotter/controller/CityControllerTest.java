package com.RJR.GlobeTrotter.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.RJR.GlobeTrotter.dto.response.CityResponse;
import com.RJR.GlobeTrotter.dto.request.CityRequest;
import com.RJR.GlobeTrotter.service.CityService;

class CityControllerTest {

    @Test
    void getPopularCitiesReturnsOkResponse() {
        CityService service = mock(CityService.class);
        CityController controller = new CityController(service);
        List<CityResponse> expected = List.of(CityResponse.builder().id(1L).name("Paris").build());
        when(service.getPopularCities()).thenReturn(expected);

        ResponseEntity<List<CityResponse>> response = controller.getPopularCities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getPopularCities();
    }

    @Test
    void deleteCityReturnsNoContent() {
        CityService service = mock(CityService.class);
        CityController controller = new CityController(service);

        ResponseEntity<Void> response = controller.deleteCity(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteCity(1L);
    }

    @Test
    void searchCitiesReturnsOkResponse() {
        CityService service = mock(CityService.class);
        CityController controller = new CityController(service);
        List<CityResponse> expected = List.of(CityResponse.builder().id(1L).name("Paris").build());
        when(service.searchCities("Paris", "France")).thenReturn(expected);

        ResponseEntity<List<CityResponse>> response = controller.searchCities("Paris", "France");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).searchCities("Paris", "France");
    }

    @Test
    void getCityByIdReturnsOkResponse() {
        CityService service = mock(CityService.class);
        CityController controller = new CityController(service);
        CityResponse expected = CityResponse.builder().id(1L).name("Paris").build();
        when(service.getCityById(1L)).thenReturn(expected);

        ResponseEntity<CityResponse> response = controller.getCityById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).getCityById(1L);
    }

    @Test
    void createCityReturnsCreatedResponse() {
        CityService service = mock(CityService.class);
        CityController controller = new CityController(service);
        CityRequest request = CityRequest.builder().name("Paris").build();
        CityResponse expected = CityResponse.builder().id(1L).name("Paris").build();
        when(service.createCity(request)).thenReturn(expected);

        ResponseEntity<CityResponse> response = controller.createCity(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).createCity(request);
    }

    @Test
    void updateCityReturnsOkResponse() {
        CityService service = mock(CityService.class);
        CityController controller = new CityController(service);
        CityRequest request = CityRequest.builder().name("Updated Paris").build();
        CityResponse expected = CityResponse.builder().id(1L).name("Updated Paris").build();
        when(service.updateCity(1L, request)).thenReturn(expected);

        ResponseEntity<CityResponse> response = controller.updateCity(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(service).updateCity(1L, request);
    }
}
