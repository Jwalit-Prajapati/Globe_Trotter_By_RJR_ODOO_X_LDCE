package com.RJR.GlobeTrotter.service;

<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.RJR.GlobeTrotter.dto.request.CityRequest;
import com.RJR.GlobeTrotter.dto.response.CityResponse;
import com.RJR.GlobeTrotter.entity.City;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
import com.RJR.GlobeTrotter.repository.CityRepository;

class CityServiceTest {

<<<<<<< HEAD
    @Test
    void searchWithoutFiltersReturnsAllCities() {
        CityRepository cities = mock(CityRepository.class);
        when(cities.findAll()).thenReturn(List.of());
        CityService service = new CityService(cities);

        assertTrue(service.searchCities(null, null).isEmpty());
    }

    @Test
    void popularCitiesReturnsEmptyResultWhenRepositoryHasNoCities() {
        CityRepository cities = mock(CityRepository.class);
        when(cities.findTop10ByOrderByPopularityDesc()).thenReturn(List.of());
        CityService service = new CityService(cities);

        assertTrue(service.getPopularCities().isEmpty());
=======
    private final CityRepository cityRepository = mock(CityRepository.class);

    private final CityService cityService = new CityService(cityRepository);

    private CityRequest sampleRequest() {
        return CityRequest.builder()
                .name("Paris")
                .country("France")
                .region("Ile-de-France")
                .costIndex(80)
                .popularity(95)
                .imageUrl("https://example.com/paris.jpg")
                .build();
    }

    @Test
    void searchCitiesFiltersByNameAndCountryWhenBothProvided() {
        City city = City.builder().id(1L).name("Paris").country("France").build();
        when(cityRepository.findByNameContainingIgnoreCaseAndCountryIgnoreCase("par", "france"))
                .thenReturn(List.of(city));

        List<CityResponse> results = cityService.searchCities("par", "france");

        assertEquals(1, results.size());
        assertEquals("Paris", results.get(0).getName());
        verify(cityRepository, never()).findAll();
    }

    @Test
    void searchCitiesFiltersByNameOnlyWhenCountryBlank() {
        City city = City.builder().id(1L).name("Paris").build();
        when(cityRepository.findByNameContainingIgnoreCase("par")).thenReturn(List.of(city));

        List<CityResponse> results = cityService.searchCities("par", "  ");

        assertEquals(1, results.size());
        verify(cityRepository).findByNameContainingIgnoreCase("par");
    }

    @Test
    void searchCitiesFiltersByCountryOnlyWhenNameBlank() {
        City city = City.builder().id(1L).country("France").build();
        when(cityRepository.findByCountryIgnoreCase("france")).thenReturn(List.of(city));

        List<CityResponse> results = cityService.searchCities(null, "france");

        assertEquals(1, results.size());
        verify(cityRepository).findByCountryIgnoreCase("france");
    }

    @Test
    void searchCitiesReturnsAllWhenNoFiltersProvided() {
        City city = City.builder().id(1L).name("Paris").build();
        when(cityRepository.findAll()).thenReturn(List.of(city));

        List<CityResponse> results = cityService.searchCities(null, "");

        assertEquals(1, results.size());
        verify(cityRepository).findAll();
    }

    @Test
    void getPopularCitiesReturnsTopCities() {
        City city = City.builder().id(1L).name("Paris").popularity(95).build();
        when(cityRepository.findTop10ByOrderByPopularityDesc()).thenReturn(List.of(city));

        List<CityResponse> results = cityService.getPopularCities();

        assertEquals(1, results.size());
        assertEquals(95, results.get(0).getPopularity());
    }

    @Test
    void getCityByIdReturnsCity() {
        City city = City.builder().id(1L).name("Paris").country("France").build();
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        CityResponse response = cityService.getCityById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Paris", response.getName());
    }

    @Test
    void getCityByIdThrowsWhenMissing() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.getCityById(1L));
    }

    @Test
    void createCitySavesAndReturnsCity() {
        when(cityRepository.save(any(City.class))).thenAnswer(invocation -> {
            City city = invocation.getArgument(0);
            city.setId(1L);
            return city;
        });

        CityResponse response = cityService.createCity(sampleRequest());

        assertEquals(1L, response.getId());
        assertEquals("Paris", response.getName());
        assertEquals("France", response.getCountry());

        ArgumentCaptor<City> cityCaptor = ArgumentCaptor.forClass(City.class);
        verify(cityRepository).save(cityCaptor.capture());
        assertEquals(80, cityCaptor.getValue().getCostIndex());
        assertEquals(95, cityCaptor.getValue().getPopularity());
    }

    @Test
    void updateCityModifiesExistingCity() {
        City city = City.builder().id(1L).name("Old Name").build();
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(cityRepository.save(any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CityResponse response = cityService.updateCity(1L, sampleRequest());

        assertEquals("Paris", response.getName());
        assertEquals("France", response.getCountry());
        verify(cityRepository).save(city);
    }

    @Test
    void updateCityThrowsWhenMissing() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(1L, sampleRequest()));

        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void deleteCityRemovesExistingCity() {
        when(cityRepository.existsById(1L)).thenReturn(true);

        cityService.deleteCity(1L);

        verify(cityRepository).deleteById(1L);
    }

    @Test
    void deleteCityThrowsWhenMissing() {
        when(cityRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(1L));

        verify(cityRepository, never()).deleteById(any());
>>>>>>> e9080cdf5de3cc86242a07b3f4737bbde0690d1b
    }
}
