package com.RJR.GlobeTrotter.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RJR.GlobeTrotter.dto.request.ActivityRequest;
import com.RJR.GlobeTrotter.dto.response.ActivityResponse;
import com.RJR.GlobeTrotter.entity.Activity;
import com.RJR.GlobeTrotter.entity.City;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.ActivityRepository;
import com.RJR.GlobeTrotter.repository.CityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CityRepository cityRepository;

    @Transactional(readOnly = true)
    public List<ActivityResponse> searchActivities(Long cityId, String category) {
        boolean hasCity = cityId != null;
        boolean hasCategory = category != null && !category.isBlank();

        List<Activity> activities;
        if (hasCity && hasCategory) {
            activities = activityRepository.findByCityIdAndCategoryIgnoreCase(cityId, category);
        } else if (hasCity) {
            activities = activityRepository.findByCityId(cityId);
        } else if (hasCategory) {
            activities = activityRepository.findByCategoryIgnoreCase(category);
        } else {
            activities = activityRepository.findAll();
        }

        return activities.stream().map(this::toActivityResponse).toList();
    }

    @Transactional(readOnly = true)
    public ActivityResponse getActivityById(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));

        return toActivityResponse(activity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ActivityResponse createActivity(ActivityRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City", request.getCityId()));

        Activity activity = Activity.builder()
                .city(city)
                .name(request.getName())
                .category(request.getCategory())
                .durationMinutes(request.getDurationMinutes())
                .estimatedCost(request.getEstimatedCost())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        activity = activityRepository.save(activity);

        return toActivityResponse(activity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ActivityResponse updateActivity(Long activityId, ActivityRequest request) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));

        if (!activity.getCity().getId().equals(request.getCityId())) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City", request.getCityId()));
            activity.setCity(city);
        }

        activity.setName(request.getName());
        activity.setCategory(request.getCategory());
        activity.setDurationMinutes(request.getDurationMinutes());
        activity.setEstimatedCost(request.getEstimatedCost());
        activity.setDescription(request.getDescription());
        activity.setImageUrl(request.getImageUrl());

        activity = activityRepository.save(activity);

        return toActivityResponse(activity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteActivity(Long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new ResourceNotFoundException("Activity", activityId);
        }
        activityRepository.deleteById(activityId);
    }

    private ActivityResponse toActivityResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .cityId(activity.getCity().getId())
                .name(activity.getName())
                .category(activity.getCategory())
                .durationMinutes(activity.getDurationMinutes())
                .estimatedCost(activity.getEstimatedCost())
                .description(activity.getDescription())
                .imageUrl(activity.getImageUrl())
                .build();
    }
}
