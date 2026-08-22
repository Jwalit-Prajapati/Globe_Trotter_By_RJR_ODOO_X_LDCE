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

import com.RJR.GlobeTrotter.dto.request.StopActivityRequest;
import com.RJR.GlobeTrotter.dto.response.StopActivityResponse;
import com.RJR.GlobeTrotter.security.CurrentUserProvider;
import com.RJR.GlobeTrotter.service.StopActivityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stop-activities")
@RequiredArgsConstructor
public class StopActivityController {

    private final StopActivityService stopActivityService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<StopActivityResponse> addActivityToStop(
            Authentication authentication,
            @Valid @RequestBody StopActivityRequest request) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(stopActivityService.addActivityToStop(userId, request));
    }

    @GetMapping("/stop/{stopId}")
    public ResponseEntity<List<StopActivityResponse>> getActivitiesForStop(
            Authentication authentication, @PathVariable Long stopId) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(stopActivityService.getActivitiesForStop(userId, stopId));
    }

    @GetMapping("/{stopActivityId}")
    public ResponseEntity<StopActivityResponse> getStopActivityById(
            Authentication authentication, @PathVariable Long stopActivityId) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(stopActivityService.getStopActivityById(userId, stopActivityId));
    }

    @PutMapping("/{stopActivityId}")
    public ResponseEntity<StopActivityResponse> updateStopActivity(
            Authentication authentication,
            @PathVariable Long stopActivityId,
            @Valid @RequestBody StopActivityRequest request) {
        Long userId = currentUserProvider.getUserId(authentication);
        return ResponseEntity.ok(stopActivityService.updateStopActivity(userId, stopActivityId, request));
    }

    @DeleteMapping("/{stopActivityId}")
    public ResponseEntity<Void> removeActivityFromStop(
            Authentication authentication, @PathVariable Long stopActivityId) {
        Long userId = currentUserProvider.getUserId(authentication);
        stopActivityService.removeActivityFromStop(userId, stopActivityId);
        return ResponseEntity.noContent().build();
    }
}
