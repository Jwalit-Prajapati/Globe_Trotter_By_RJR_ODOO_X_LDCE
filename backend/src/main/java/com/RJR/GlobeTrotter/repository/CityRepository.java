package com.RJR.GlobeTrotter.repository;

import com.RJR.GlobeTrotter.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByNameContainingIgnoreCase(String name);

    List<City> findByCountryIgnoreCase(String country);

    List<City> findByNameContainingIgnoreCaseAndCountryIgnoreCase(
            String name,
            String country
    );

    List<City> findTop10ByOrderByPopularityDesc();
}