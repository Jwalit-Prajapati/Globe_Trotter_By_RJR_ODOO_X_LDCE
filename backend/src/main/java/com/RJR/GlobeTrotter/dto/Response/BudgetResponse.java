package com.RJR.GlobeTrotter.dto.response;

import java.math.BigDecimal;

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
public class BudgetResponse {

    private Long tripId;
    private BigDecimal budgetLimit;
    private BigDecimal transportCost;
    private BigDecimal stayCost;
    private BigDecimal mealCost;
    private BigDecimal activityCost;
    private BigDecimal totalCost;
    private BigDecimal remainingBudget;
}
