package com.RJR.GlobeTrotter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RJR.GlobeTrotter.dto.request.TripRequest;
import com.RJR.GlobeTrotter.dto.response.ShareResponse;
import com.RJR.GlobeTrotter.dto.response.TripResponse;
import com.RJR.GlobeTrotter.dto.response.TripSummaryResponse;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.TripService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/public/{publicSlug}")
    public ResponseEntity<TripResponse> getPublicTrip(@PathVariable String publicSlug) {
        return ResponseEntity.ok(tripService.getPublicTrip(publicSlug));
    }

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(
            Authentication authentication,
            @Valid @RequestBody TripRequest request) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<TripSummaryResponse>> getUserTrips(Authentication authentication) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(tripService.getUserTrips(userId));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponse> getTripById(Authentication authentication, @PathVariable Long tripId) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(tripService.getTripById(userId, tripId));
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<TripResponse> updateTrip(
            Authentication authentication,
            @PathVariable Long tripId,
            @Valid @RequestBody TripRequest request) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(tripService.updateTrip(userId, tripId, request));
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(Authentication authentication, @PathVariable Long tripId) {
        Long userId = currentUserProvider.getUserId(authentication);
        tripService.deleteTrip(userId, tripId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tripId}/share")
    public ResponseEntity<ShareResponse> shareTrip(Authentication authentication, @PathVariable Long tripId) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(tripService.shareTrip(userId, tripId));
    }
}
