package com.RJR.GlobeTrotter.repository;

import com.RJR.GlobeTrotter.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {

    List<Stop> findByTripId(Long tripId);

    List<Stop> findByTripIdOrderByOrderIndexAsc(Long tripId);

    List<Stop> findByCityId(Long cityId);

    boolean existsByTripIdAndOrderIndex(
            Long tripId,
            Integer orderIndex
    );
}