package com.RJR.GlobeTrotter.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.RJR.GlobeTrotter.repository.CityRepository;

class CityServiceTest {

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
    }
}
