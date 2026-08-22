package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class TripRequestTest {

    @Test
    void builderDefaultsTripToPrivate() {
        TripRequest request = TripRequest.builder()
                .name("European Adventure")
                .description("A city break")
                .budgetLimit(new BigDecimal("1000.00"))
                .build();

        assertEquals("European Adventure", request.getName());
        assertEquals("A city break", request.getDescription());
        assertEquals(new BigDecimal("1000.00"), request.getBudgetLimit());
        assertFalse(request.getIsPublic());
    }

    @Test
    void dateRangeValidationAcceptsValidDatesAndRejectsInvalidDates() throws Exception {
        TripRequest validRequest = TripRequest.builder()
                .startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 9, 10))
                .build();
        TripRequest invalidRequest = TripRequest.builder()
                .startDate(LocalDate.of(2026, 9, 10))
                .endDate(LocalDate.of(2026, 9, 1))
                .build();

        Method validator = TripRequest.class.getDeclaredMethod("isDateRangeValid");
        validator.setAccessible(true);
        assertTrue((Boolean) validator.invoke(validRequest));
        assertFalse((Boolean) validator.invoke(invalidRequest));
    }
}
