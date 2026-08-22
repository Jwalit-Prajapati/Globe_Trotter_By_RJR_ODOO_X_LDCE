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
public class ActivityResponse {

    private Long id;
    private Long cityId;
    private String name;
    private String category;
    private Integer durationMinutes;
    private BigDecimal estimatedCost;
    private String description;
    private String imageUrl;
}
