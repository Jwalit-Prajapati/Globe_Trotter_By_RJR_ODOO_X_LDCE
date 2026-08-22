package com.RJR.GlobeTrotter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityRequest {

    @NotBlank(message = "City name is required")
    @Size(max = 255, message = "City name must be less than 255 characters")
    private String name;

    @Size(max = 255, message = "Country must be less than 255 characters")
    private String country;

    @Size(max = 255, message = "Region must be less than 255 characters")
    private String region;

    @PositiveOrZero(message = "Cost index must be zero or positive")
    private Integer costIndex;

    @PositiveOrZero(message = "Popularity must be zero or positive")
    private Integer popularity;

    private String imageUrl;
}
