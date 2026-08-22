package com.RJR.GlobeTrotter.repository;

import com.RJR.GlobeTrotter.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUserId(Long userId);

    List<Trip> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Trip> findByPublicSlug(String publicSlug);

    boolean existsByPublicSlug(String publicSlug);

    List<Trip> findByIsPublicTrue();
}