package com.RJR.GlobeTrotter.service;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RJR.GlobeTrotter.dto.response.ShareResponse;
import com.RJR.GlobeTrotter.dto.response.TripResponse;
import com.RJR.GlobeTrotter.dto.response.TripSummaryResponse;
import com.RJR.GlobeTrotter.dto.request.TripRequest;
import com.RJR.GlobeTrotter.entity.Trip;
import com.RJR.GlobeTrotter.entity.User;
import com.RJR.GlobeTrotter.exception.ResourceNotFoundException;
import com.RJR.GlobeTrotter.repository.StopRepository;
import com.RJR.GlobeTrotter.repository.TripRepository;
import com.RJR.GlobeTrotter.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

    private static final String SLUG_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SLUG_LENGTH = 10;

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final StopRepository stopRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public TripResponse createTrip(Long userId, TripRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Trip trip = Trip.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .budgetLimit(request.getBudgetLimit())
                .isPublic(request.getIsPublic())
                .build();

        trip = tripRepository.save(trip);

        return toTripResponse(trip);
    }

    @Transactional(readOnly = true)
    public List<TripSummaryResponse> getUserTrips(Long userId) {
        return tripRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toTripSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TripResponse getTripById(Long userId, Long tripId) {
        Trip trip = getOwnedTrip(userId, tripId);
        return toTripResponse(trip);
    }

    @Transactional
    public TripResponse updateTrip(Long userId, Long tripId, TripRequest request) {
        Trip trip = getOwnedTrip(userId, tripId);

        trip.setName(request.getName());
        trip.setDescription(request.getDescription());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setBudgetLimit(request.getBudgetLimit());
        trip.setIsPublic(request.getIsPublic());

        trip = tripRepository.save(trip);

        return toTripResponse(trip);
    }

    @Transactional
    public void deleteTrip(Long userId, Long tripId) {
        Trip trip = getOwnedTrip(userId, tripId);
        tripRepository.delete(trip);
    }

    @Transactional
    public ShareResponse shareTrip(Long userId, Long tripId) {
        Trip trip = getOwnedTrip(userId, tripId);

        if (trip.getPublicSlug() == null) {
            trip.setPublicSlug(generateUniqueSlug());
        }
        trip.setIsPublic(true);

        trip = tripRepository.save(trip);

        return ShareResponse.builder()
                .tripId(trip.getId())
                .publicSlug(trip.getPublicSlug())
                .shareUrl("/public/" + trip.getPublicSlug())
                .isPublic(trip.getIsPublic())
                .build();
    }

    @Transactional(readOnly = true)
    public TripResponse getPublicTrip(String publicSlug) {
        Trip trip = tripRepository.findByPublicSlug(publicSlug)
                .filter(Trip::getIsPublic)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", publicSlug));

        return toTripResponse(trip);
    }

    private Trip getOwnedTrip(Long userId, Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        if (!trip.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Trip", tripId);
        }

        return trip;
    }

    private String generateUniqueSlug() {
        String slug;
        do {
            slug = randomSlug();
        } while (tripRepository.existsByPublicSlug(slug));
        return slug;
    }

    private String randomSlug() {
        StringBuilder sb = new StringBuilder(SLUG_LENGTH);
        for (int i = 0; i < SLUG_LENGTH; i++) {
            sb.append(SLUG_CHARS.charAt(secureRandom.nextInt(SLUG_CHARS.length())));
        }
        return sb.toString();
    }

    private TripResponse toTripResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .userId(trip.getUser().getId())
                .name(trip.getName())
                .description(trip.getDescription())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .budgetLimit(trip.getBudgetLimit())
                .isPublic(trip.getIsPublic())
                .publicSlug(trip.getPublicSlug())
                .createdAt(trip.getCreatedAt())
                .build();
    }

    private TripSummaryResponse toTripSummaryResponse(Trip trip) {
        return TripSummaryResponse.builder()
                .id(trip.getId())
                .name(trip.getName())
                .description(trip.getDescription())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .budgetLimit(trip.getBudgetLimit())
                .isPublic(trip.getIsPublic())
                .publicSlug(trip.getPublicSlug())
                .stopCount(stopRepository.findByTripId(trip.getId()).size())
                .build();
    }
}
