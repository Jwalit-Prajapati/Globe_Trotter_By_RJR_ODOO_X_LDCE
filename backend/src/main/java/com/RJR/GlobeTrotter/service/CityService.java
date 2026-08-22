package com.RJR.GlobeTrotter.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RJR.GlobeTrotter.dto.request.CityRequest;
import com.RJR.GlobeTrotter.dto.response.CityResponse;
import com.RJR.GlobeTrotter.entity.City;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.CityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    @Transactional(readOnly = true)
    public List<CityResponse> searchCities(String name, String country) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasCountry = country != null && !country.isBlank();

        List<City> cities;
        if (hasName && hasCountry) {
            cities = cityRepository.findByNameContainingIgnoreCaseAndCountryIgnoreCase(name, country);
        } else if (hasName) {
            cities = cityRepository.findByNameContainingIgnoreCase(name);
        } else if (hasCountry) {
            cities = cityRepository.findByCountryIgnoreCase(country);
        } else {
            cities = cityRepository.findAll();
        }

        return cities.stream().map(this::toCityResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CityResponse> getPopularCities() {
        return cityRepository.findTop10ByOrderByPopularityDesc().stream()
                .map(this::toCityResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CityResponse getCityById(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City", cityId));

        return toCityResponse(city);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CityResponse createCity(CityRequest request) {
        City city = City.builder()
                .name(request.getName())
                .country(request.getCountry())
                .region(request.getRegion())
                .costIndex(request.getCostIndex())
                .popularity(request.getPopularity())
                .imageUrl(request.getImageUrl())
                .build();

        city = cityRepository.save(city);

        return toCityResponse(city);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CityResponse updateCity(Long cityId, CityRequest request) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City", cityId));

        city.setName(request.getName());
        city.setCountry(request.getCountry());
        city.setRegion(request.getRegion());
        city.setCostIndex(request.getCostIndex());
        city.setPopularity(request.getPopularity());
        city.setImageUrl(request.getImageUrl());

        city = cityRepository.save(city);

        return toCityResponse(city);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteCity(Long cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new ResourceNotFoundException("City", cityId);
        }
        cityRepository.deleteById(cityId);
    }

    private CityResponse toCityResponse(City city) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .country(city.getCountry())
                .region(city.getRegion())
                .costIndex(city.getCostIndex())
                .popularity(city.getPopularity())
                .imageUrl(city.getImageUrl())
                .build();
    }
}
