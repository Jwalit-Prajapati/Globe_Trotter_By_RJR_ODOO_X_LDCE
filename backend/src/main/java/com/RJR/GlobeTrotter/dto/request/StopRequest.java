package com.RJR.GlobeTrotter.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopRequest {

    @NotNull(message = "Trip ID is required")
    private Long tripId;

    @NotNull(message = "City ID is required")
    private Long cityId;

    private LocalDate startDate;

    private LocalDate endDate;

    @Min(value = 0, message = "Order index must be 0 or positive")
    private Integer orderIndex;

    @PositiveOrZero(message = "Transport cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Transport cost may have at most 2 decimal places")
    private BigDecimal transportCost;

    @PositiveOrZero(message = "Stay cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Stay cost may have at most 2 decimal places")
    private BigDecimal stayCost;

    @PositiveOrZero(message = "Meal cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Meal cost may have at most 2 decimal places")
    private BigDecimal mealCost;

    @AssertTrue(message = "End date must not be before start date")
    private boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }
}
