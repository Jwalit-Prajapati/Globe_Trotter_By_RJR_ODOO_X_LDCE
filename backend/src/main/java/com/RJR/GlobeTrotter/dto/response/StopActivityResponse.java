package com.RJR.GlobeTrotter.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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
public class StopActivityResponse {

    private Long id;
    private Long stopId;
    private ActivityResponse activity;
    private LocalDate dayDate;
    private LocalTime scheduledTime;
    private BigDecimal cost;
}
