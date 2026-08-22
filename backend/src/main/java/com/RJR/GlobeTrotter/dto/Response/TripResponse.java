package com.RJR.GlobeTrotter.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class TripResponse {

    private Long id;
    private Long userId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budgetLimit;
    private Boolean isPublic;
    private String publicSlug;
    private LocalDateTime createdAt;
    private List<StopResponse> stops;
    private BudgetResponse budget;
}
