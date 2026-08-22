package com.RJR.GlobeTrotter.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RJR.GlobeTrotter.dto.request.StopRequest;
import com.RJR.GlobeTrotter.dto.response.ActivityResponse;
import com.RJR.GlobeTrotter.dto.response.CityResponse;
import com.RJR.GlobeTrotter.dto.response.StopActivityResponse;
import com.RJR.GlobeTrotter.dto.response.StopResponse;
import com.RJR.GlobeTrotter.entity.Activity;
import com.RJR.GlobeTrotter.entity.City;
import com.RJR.GlobeTrotter.entity.Stop;
import com.RJR.GlobeTrotter.entity.StopActivity;
import com.RJR.GlobeTrotter.entity.Trip;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.CityRepository;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StopService {

    private final StopRepository stopRepository;
    private final TripRepository tripRepository;
    private final CityRepository cityRepository;
    private final StopActivityRepository stopActivityRepository;

    @Transactional
    public StopResponse createStop(Long userId, StopRequest request) {
        Trip trip = getOwnedTrip(userId, request.getTripId());
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City", request.getCityId()));

        Integer orderIndex = request.getOrderIndex();
        if (orderIndex == null) {
            orderIndex = nextOrderIndex(trip.getId());
        } else if (stopRepository.existsByTripIdAndOrderIndex(trip.getId(), orderIndex)) {
            throw new IllegalArgumentException(
                    "A stop with order index " + orderIndex + " already exists for this trip");
        }

        Stop stop = Stop.builder()
                .trip(trip)
                .city(city)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .orderIndex(orderIndex)
                .transportCost(request.getTransportCost())
                .stayCost(request.getStayCost())
                .mealCost(request.getMealCost())
                .build();

        stop = stopRepository.save(stop);

        return toStopResponse(stop);
    }

    @Transactional(readOnly = true)
    public List<StopResponse> getStopsForTrip(Long userId, Long tripId) {
        getOwnedTrip(userId, tripId);

        return listStopsForTrip(tripId);
    }

    /**
     * Returns stops for a trip without an ownership check. Callers (e.g. TripService) must
     * have already verified the caller is allowed to see this trip.
     */
    List<StopResponse> listStopsForTrip(Long tripId) {
        return stopRepository.findByTripIdOrderByOrderIndexAsc(tripId).stream()
                .map(this::toStopResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StopResponse getStopById(Long userId, Long stopId) {
        Stop stop = getOwnedStop(userId, stopId);
        return toStopResponse(stop);
    }

    @Transactional
    public StopResponse updateStop(Long userId, Long stopId, StopRequest request) {
        Stop stop = getOwnedStop(userId, stopId);

        if (!stop.getTrip().getId().equals(request.getTripId())) {
            throw new IllegalArgumentException("Stop does not belong to the specified trip");
        }

        if (!stop.getCity().getId().equals(request.getCityId())) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException("City", request.getCityId()));
            stop.setCity(city);
        }

        Integer orderIndex = request.getOrderIndex();
        if (orderIndex != null && !orderIndex.equals(stop.getOrderIndex())
                && stopRepository.existsByTripIdAndOrderIndex(stop.getTrip().getId(), orderIndex)) {
            throw new IllegalArgumentException(
                    "A stop with order index " + orderIndex + " already exists for this trip");
        }

        stop.setStartDate(request.getStartDate());
        stop.setEndDate(request.getEndDate());
        if (orderIndex != null) {
            stop.setOrderIndex(orderIndex);
        }
        stop.setTransportCost(request.getTransportCost());
        stop.setStayCost(request.getStayCost());
        stop.setMealCost(request.getMealCost());

        stop = stopRepository.save(stop);

        return toStopResponse(stop);
    }

    @Transactional
    public void deleteStop(Long userId, Long stopId) {
        Stop stop = getOwnedStop(userId, stopId);
        stopActivityRepository.deleteByStopId(stop.getId());
        stopRepository.delete(stop);
    }

    private Trip getOwnedTrip(Long userId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        if (!trip.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Trip", tripId);
        }

        return trip;
    }

    private Stop getOwnedStop(Long userId, Long stopId) {
        Stop stop = stopRepository.findById(stopId)
                .orElseThrow(() -> new ResourceNotFoundException("Stop", stopId));

        if (!stop.getTrip().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Stop", stopId);
        }

        return stop;
    }

    private int nextOrderIndex(Long tripId) {
        return stopRepository.findByTripIdOrderByOrderIndexAsc(tripId).stream()
                .mapToInt(s -> s.getOrderIndex() == null ? 0 : s.getOrderIndex())
                .max()
                .orElse(-1) + 1;
    }

    private StopResponse toStopResponse(Stop stop) {
        List<StopActivityResponse> activities = stopActivityRepository
                .findByStopIdOrderByDayDateAscScheduledTimeAsc(stop.getId()).stream()
                .map(this::toStopActivityResponse)
                .toList();

        return StopResponse.builder()
                .id(stop.getId())
                .tripId(stop.getTrip().getId())
                .city(toCityResponse(stop.getCity()))
                .startDate(stop.getStartDate())
                .endDate(stop.getEndDate())
                .orderIndex(stop.getOrderIndex())
                .transportCost(stop.getTransportCost())
                .stayCost(stop.getStayCost())
                .mealCost(stop.getMealCost())
                .activities(activities)
                .build();
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
