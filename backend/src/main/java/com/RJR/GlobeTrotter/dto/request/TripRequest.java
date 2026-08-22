package com.RJR.GlobeTrotter.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
public class TripRequest {

    @NotBlank(message = "Trip name is required")
    @Size(max = 255, message = "Trip name must be less than 255 characters")
    private String name;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @PositiveOrZero(message = "Budget limit must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Budget limit may have at most 2 decimal places")
    private BigDecimal budgetLimit;

    @Builder.Default
    private Boolean isPublic = false;

    @AssertTrue(message = "End date must not be before start date")
    private boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }
}
