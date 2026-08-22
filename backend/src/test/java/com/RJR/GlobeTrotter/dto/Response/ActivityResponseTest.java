package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ActivityResponseTest {

    @Test
    void builderStoresActivityDetails() {
        ActivityResponse response = ActivityResponse.builder()
                .id(1L)
                .cityId(2L)
                .name("Eiffel Tower")
                .category("Sightseeing")
                .durationMinutes(120)
                .estimatedCost(new BigDecimal("25.50"))
                .description("Iconic tower in Paris")
                .imageUrl("eiffel.jpg")
                .build();

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getCityId());
        assertEquals("Eiffel Tower", response.getName());
        assertEquals("Sightseeing", response.getCategory());
        assertEquals(120, response.getDurationMinutes());
        assertEquals(new BigDecimal("25.50"), response.getEstimatedCost());
        assertEquals("Iconic tower in Paris", response.getDescription());
        assertEquals("eiffel.jpg", response.getImageUrl());
    }
}
