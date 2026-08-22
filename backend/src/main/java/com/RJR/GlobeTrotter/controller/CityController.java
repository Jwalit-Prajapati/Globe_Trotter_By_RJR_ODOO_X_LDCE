package com.RJR.GlobeTrotter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RJR.GlobeTrotter.dto.request.CityRequest;
import com.RJR.GlobeTrotter.dto.response.CityResponse;
import com.RJR.GlobeTrotter.service.CityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public ResponseEntity<List<CityResponse>> searchCities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String country) {
        return ResponseEntity.ok(cityService.searchCities(name, country));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<CityResponse>> getPopularCities() {
        return ResponseEntity.ok(cityService.getPopularCities());
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long cityId) {
        return ResponseEntity.ok(cityService.getCityById(cityId));
    }

    @PostMapping
    public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(request));
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<CityResponse> updateCity(
            @PathVariable Long cityId,
            @Valid @RequestBody CityRequest request) {
        return ResponseEntity.ok(cityService.updateCity(cityId, request));
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long cityId) {
        cityService.deleteCity(cityId);
        return ResponseEntity.noContent().build();
    }
}
