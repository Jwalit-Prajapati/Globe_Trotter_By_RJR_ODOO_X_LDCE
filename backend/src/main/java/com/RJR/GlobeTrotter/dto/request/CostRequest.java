package com.RJR.GlobeTrotter.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostRequest {

    @PositiveOrZero(message = "Transport cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Transport cost may have at most 2 decimal places")
    private BigDecimal transportCost;

    @PositiveOrZero(message = "Stay cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Stay cost may have at most 2 decimal places")
    private BigDecimal stayCost;

    @PositiveOrZero(message = "Meal cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Meal cost may have at most 2 decimal places")
    private BigDecimal mealCost;
}
