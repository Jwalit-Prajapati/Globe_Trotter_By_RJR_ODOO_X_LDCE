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

import com.RJR.GlobeTrotter.dto.request.StopRequest;
import com.RJR.GlobeTrotter.dto.response.StopResponse;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.StopService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stops")
@RequiredArgsConstructor
public class StopController {

    private final StopService stopService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<StopResponse> createStop(
            Authentication authentication,
            @Valid @RequestBody StopRequest request) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(stopService.createStop(userId, request));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<StopResponse>> getStopsForTrip(Authentication authentication, @PathVariable Long tripId) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(stopService.getStopsForTrip(userId, tripId));
    }

    @GetMapping("/{stopId}")
    public ResponseEntity<StopResponse> getStopById(Authentication authentication, @PathVariable Long stopId) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(stopService.getStopById(userId, stopId));
    }

    @PutMapping("/{stopId}")
    public ResponseEntity<StopResponse> updateStop(
            Authentication authentication,
            @PathVariable Long stopId,
            @Valid @RequestBody StopRequest request) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(stopService.updateStop(userId, stopId, request));
    }

    @DeleteMapping("/{stopId}")
    public ResponseEntity<Void> deleteStop(Authentication authentication, @PathVariable Long stopId) {
        Long userId = currentUserProvider.getUserId(authentication);
        stopService.deleteStop(userId, stopId);
        return ResponseEntity.noContent().build();
    }
}
