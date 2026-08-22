package com.RJR.GlobeTrotter.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopActivityRequest {

    @NotNull(message = "Stop ID is required")
    private Long stopId;

    @NotNull(message = "Activity ID is required")
    private Long activityId;

    @NotNull(message = "Day date is required")
    private LocalDate dayDate;

    private LocalTime scheduledTime;

    @NotNull(message = "Cost is required")
    @PositiveOrZero(message = "Cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Cost may have at most 2 decimal places")
    private BigDecimal cost;
}
