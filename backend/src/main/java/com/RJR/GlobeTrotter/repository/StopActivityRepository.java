package com.RJR.GlobeTrotter.repository;

import com.RJR.GlobeTrotter.entity.StopActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StopActivityRepository
        extends JpaRepository<StopActivity, Long> {

    List<StopActivity> findByStopId(Long stopId);

    List<StopActivity> findByStopIdOrderByDayDateAscScheduledTimeAsc(
            Long stopId
    );

    List<StopActivity> findByActivityId(Long activityId);

    List<StopActivity> findByDayDate(LocalDate dayDate);

    void deleteByStopId(Long stopId);

    boolean existsByStopIdAndActivityIdAndDayDate(
            Long stopId,
            Long activityId,
            LocalDate dayDate
    );
}