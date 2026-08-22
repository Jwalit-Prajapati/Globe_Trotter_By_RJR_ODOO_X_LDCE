package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class StopActivityRequestTest {

    @Test
    void builderStoresStopActivityData() {
        StopActivityRequest request = StopActivityRequest.builder()
                .stopId(1L)
                .activityId(2L)
                .dayDate(LocalDate.of(2026, 9, 1))
                .scheduledTime(LocalTime.of(10, 0))
                .cost(new BigDecimal("50.00"))
                .build();

        assertEquals(1L, request.getStopId());
        assertEquals(2L, request.getActivityId());
        assertEquals(LocalDate.of(2026, 9, 1), request.getDayDate());
        assertEquals(LocalTime.of(10, 0), request.getScheduledTime());
        assertEquals(new BigDecimal("50.00"), request.getCost());
    }
}
