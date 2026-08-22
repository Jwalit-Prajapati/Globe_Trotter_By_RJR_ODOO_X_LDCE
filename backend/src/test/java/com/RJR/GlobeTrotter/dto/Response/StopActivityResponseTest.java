package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class StopActivityResponseTest {

    @Test
    void builderStoresStopActivityDetails() {
        ActivityResponse activity = ActivityResponse.builder()
                .id(1L)
                .name("Louvre Museum")
                .build();

        StopActivityResponse response = StopActivityResponse.builder()
                .id(10L)
                .stopId(2L)
                .activity(activity)
                .dayDate(LocalDate.of(2026, 9, 2))
                .scheduledTime(LocalTime.of(14, 30))
                .cost(new BigDecimal("15.00"))
                .build();

        assertEquals(10L, response.getId());
        assertEquals(2L, response.getStopId());
        assertNotNull(response.getActivity());
        assertEquals("Louvre Museum", response.getActivity().getName());
        assertEquals(LocalDate.of(2026, 9, 2), response.getDayDate());
        assertEquals(LocalTime.of(14, 30), response.getScheduledTime());
        assertEquals(new BigDecimal("15.00"), response.getCost());
    }
}
