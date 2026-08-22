package com.RJR.GlobeTrotter.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RJR.GlobeTrotter.dto.response.BudgetResponse;
import com.RJR.GlobeTrotter.entity.Stop;
import com.RJR.GlobeTrotter.entity.StopActivity;
import com.RJR.GlobeTrotter.entity.Trip;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.StopActivityRepository;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final TripRepository tripRepository;
    private final StopRepository stopRepository;
    private final StopActivityRepository stopActivityRepository;

    @Transactional(readOnly = true)
    public BudgetResponse getTripBudget(Long userId, Long tripId) {
        Trip trip = getOwnedTrip(userId, tripId);

        List<Stop> stops = stopRepository.findByTripId(tripId);
        List<Long> stopIds = stops.stream().map(Stop::getId).toList();

        BigDecimal transportCost = sum(stops, Stop::getTransportCost);
        BigDecimal stayCost = sum(stops, Stop::getStayCost);
        BigDecimal mealCost = sum(stops, Stop::getMealCost);

        BigDecimal activityCost = stopActivityRepository.findByStopIdIn(stopIds).stream()
                .map(StopActivity::getCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCost = transportCost.add(stayCost).add(mealCost).add(activityCost);

        BigDecimal budgetLimit = trip.getBudgetLimit();
        BigDecimal remainingBudget = budgetLimit != null ? budgetLimit.subtract(totalCost) : null;

        return BudgetResponse.builder()
                .tripId(trip.getId())
                .budgetLimit(budgetLimit)
                .transportCost(transportCost)
                .stayCost(stayCost)
                .mealCost(mealCost)
                .activityCost(activityCost)
                .totalCost(totalCost)
                .remainingBudget(remainingBudget)
                .build();
    }

    private Trip getOwnedTrip(Long userId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        if (!trip.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Trip", tripId);
        }

        return trip;
    }

    private BigDecimal sum(List<Stop> stops, Function<Stop, BigDecimal> extractor) {
        return stops.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
