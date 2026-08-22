package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TripSummaryResponseTest {

    @Test
    void builderStoresTripSummaryDetails() {
        TripSummaryResponse response = TripSummaryResponse.builder()
                .id(1L)
                .name("Euro Trip")
                .description("Visiting France and Italy")
                .startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 9, 15))
                .budgetLimit(new BigDecimal("3000.00"))
                .isPublic(true)
                .publicSlug("euro-trip-2026")
                .stopCount(5)
                .build();

        assertEquals(1L, response.getId());
        assertEquals("Euro Trip", response.getName());
        assertEquals("Visiting France and Italy", response.getDescription());
        assertEquals(LocalDate.of(2026, 9, 1), response.getStartDate());
        assertEquals(LocalDate.of(2026, 9, 15), response.getEndDate());
        assertEquals(new BigDecimal("3000.00"), response.getBudgetLimit());
        assertEquals(true, response.getIsPublic());
        assertEquals("euro-trip-2026", response.getPublicSlug());
        assertEquals(5, response.getStopCount());
    }
}
