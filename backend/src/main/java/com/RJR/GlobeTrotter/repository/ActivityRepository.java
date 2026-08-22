package com.RJR.GlobeTrotter.repository;

import com.RJR.GlobeTrotter.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByCityId(Long cityId);

    List<Activity> findByCategoryIgnoreCase(String category);

    List<Activity> findByCityIdAndCategoryIgnoreCase(
            Long cityId,
            String category
    );

    List<Activity> findByNameContainingIgnoreCase(String name);
}