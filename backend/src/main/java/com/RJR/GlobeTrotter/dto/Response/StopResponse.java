package com.RJR.GlobeTrotter.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopResponse {

    private Long id;
    private Long tripId;
    private CityResponse city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer orderIndex;
    private BigDecimal transportCost;
    private BigDecimal stayCost;
    private BigDecimal mealCost;
    private List<StopActivityResponse> activities;
}
