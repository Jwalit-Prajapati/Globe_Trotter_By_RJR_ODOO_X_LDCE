package com.RJR.GlobeTrotter.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {

    @NotNull(message = "City ID is required")
    private Long cityId;

    @NotBlank(message = "Activity name is required")
    @Size(max = 255, message = "Activity name must be less than 255 characters")
    private String name;

    @Size(max = 100, message = "Category must be less than 100 characters")
    private String category;

    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    @PositiveOrZero(message = "Estimated cost must be zero or positive")
    @Digits(integer = 10, fraction = 2, message = "Estimated cost may have at most 2 decimal places")
    private BigDecimal estimatedCost;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    private String imageUrl;
}
