package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class BudgetResponseTest {

    @Test
    void builderStoresBudgetTotals() {
        BudgetResponse response = BudgetResponse.builder().tripId(1L).budgetLimit(new BigDecimal("1000.00"))
                .transportCost(new BigDecimal("100.00")).stayCost(new BigDecimal("300.00"))
                .mealCost(new BigDecimal("150.00")).activityCost(new BigDecimal("25.00"))
                .totalCost(new BigDecimal("575.00")).remainingBudget(new BigDecimal("425.00")).build();

        assertEquals(1L, response.getTripId());
        assertEquals(new BigDecimal("1000.00"), response.getBudgetLimit());
        assertEquals(new BigDecimal("575.00"), response.getTotalCost());
        assertEquals(new BigDecimal("425.00"), response.getRemainingBudget());
    }
}
