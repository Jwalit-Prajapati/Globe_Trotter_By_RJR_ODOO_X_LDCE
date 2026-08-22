package com.RJR.GlobeTrotter.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RJR.GlobeTrotter.dto.request.StopActivityRequest;
import com.RJR.GlobeTrotter.dto.response.ActivityResponse;
import com.RJR.GlobeTrotter.dto.response.StopActivityResponse;
import com.RJR.GlobeTrotter.entity.Activity;
import com.RJR.GlobeTrotter.entity.Stop;
import com.RJR.GlobeTrotter.entity.StopActivity;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.ActivityRepository;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StopActivityService {

    private final StopActivityRepository stopActivityRepository;
    private final StopRepository stopRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public StopActivityResponse addActivityToStop(Long userId, StopActivityRequest request) {
        Stop stop = getOwnedStop(userId, request.getStopId());
        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity", request.getActivityId()));

        validateDayWithinStop(stop, request.getDayDate());

        if (stopActivityRepository.existsByStopIdAndActivityIdAndDayDate(
                stop.getId(), activity.getId(), request.getDayDate())) {
            throw new IllegalArgumentException(
                    "This activity is already scheduled for this stop on the given day");
        }

        StopActivity stopActivity = StopActivity.builder()
                .stop(stop)
                .activity(activity)
                .dayDate(request.getDayDate())
                .scheduledTime(request.getScheduledTime())
                .cost(request.getCost())
                .build();

        stopActivity = stopActivityRepository.save(stopActivity);

        return toStopActivityResponse(stopActivity);
    }

    @Transactional(readOnly = true)
    public List<StopActivityResponse> getActivitiesForStop(Long userId, Long stopId) {
        getOwnedStop(userId, stopId);

        return stopActivityRepository.findByStopIdOrderByDayDateAscScheduledTimeAsc(stopId).stream()
                .map(this::toStopActivityResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StopActivityResponse getStopActivityById(Long userId, Long stopActivityId) {
        StopActivity stopActivity = getOwnedStopActivity(userId, stopActivityId);
        return toStopActivityResponse(stopActivity);
    }

    @Transactional
    public StopActivityResponse updateStopActivity(Long userId, Long stopActivityId, StopActivityRequest request) {
        StopActivity stopActivity = getOwnedStopActivity(userId, stopActivityId);

        Stop stop = stopActivity.getStop();
        if (!stop.getId().equals(request.getStopId())) {
            stop = getOwnedStop(userId, request.getStopId());
        }

        Activity activity = stopActivity.getActivity();
        if (!activity.getId().equals(request.getActivityId())) {
            activity = activityRepository.findById(request.getActivityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Activity", request.getActivityId()));
        }

        validateDayWithinStop(stop, request.getDayDate());

        boolean identityChanged = !stop.getId().equals(stopActivity.getStop().getId())
                || !activity.getId().equals(stopActivity.getActivity().getId())
                || !request.getDayDate().equals(stopActivity.getDayDate());

        if (identityChanged && stopActivityRepository.existsByStopIdAndActivityIdAndDayDate(
                stop.getId(), activity.getId(), request.getDayDate())) {
            throw new IllegalArgumentException(
                    "This activity is already scheduled for this stop on the given day");
        }

        stopActivity.setStop(stop);
        stopActivity.setActivity(activity);
        stopActivity.setDayDate(request.getDayDate());
        stopActivity.setScheduledTime(request.getScheduledTime());
        stopActivity.setCost(request.getCost());

        stopActivity = stopActivityRepository.save(stopActivity);

        return toStopActivityResponse(stopActivity);
    }

    @Transactional
    public void removeActivityFromStop(Long userId, Long stopActivityId) {
        StopActivity stopActivity = getOwnedStopActivity(userId, stopActivityId);
        stopActivityRepository.delete(stopActivity);
    }

    private void validateDayWithinStop(Stop stop, LocalDate dayDate) {
        if (stop.getStartDate() != null && dayDate.isBefore(stop.getStartDate())) {
            throw new IllegalArgumentException("Day date cannot be before the stop's start date");
        }
        if (stop.getEndDate() != null && dayDate.isAfter(stop.getEndDate())) {
            throw new IllegalArgumentException("Day date cannot be after the stop's end date");
        }
    }

    private Stop getOwnedStop(Long userId, Long stopId) {
        Stop stop = stopRepository.findById(stopId)
                .orElseThrow(() -> new ResourceNotFoundException("Stop", stopId));

        if (!stop.getTrip().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Stop", stopId);
        }

        return stop;
    }

    private StopActivity getOwnedStopActivity(Long userId, Long stopActivityId) {
        StopActivity stopActivity = stopActivityRepository.findById(stopActivityId)
                .orElseThrow(() -> new ResourceNotFoundException("StopActivity", stopActivityId));

        if (!stopActivity.getStop().getTrip().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("StopActivity", stopActivityId);
        }

        return stopActivity;
    }

    private StopActivityResponse toStopActivityResponse(StopActivity stopActivity) {
        return StopActivityResponse.builder()
                .id(stopActivity.getId())
                .stopId(stopActivity.getStop().getId())
                .activity(toActivityResponse(stopActivity.getActivity()))
                .dayDate(stopActivity.getDayDate())
                .scheduledTime(stopActivity.getScheduledTime())
                .cost(stopActivity.getCost())
                .build();
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
