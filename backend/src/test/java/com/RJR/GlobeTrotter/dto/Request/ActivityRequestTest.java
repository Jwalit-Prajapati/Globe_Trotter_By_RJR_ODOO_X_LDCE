package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ActivityRequestTest {

    @Test
    void builderStoresActivityData() {
        ActivityRequest request = ActivityRequest.builder()
                .cityId(1L)
                .name("Eiffel Tower")
                .category("Sightseeing")
                .durationMinutes(120)
                .estimatedCost(new BigDecimal("25.50"))
                .description("Visit the iconic Eiffel Tower")
                .imageUrl("eiffel.jpg")
                .build();

        assertEquals(1L, request.getCityId());
        assertEquals("Eiffel Tower", request.getName());
        assertEquals("Sightseeing", request.getCategory());
        assertEquals(120, request.getDurationMinutes());
        assertEquals(new BigDecimal("25.50"), request.getEstimatedCost());
        assertEquals("Visit the iconic Eiffel Tower", request.getDescription());
        assertEquals("eiffel.jpg", request.getImageUrl());
    }
}
