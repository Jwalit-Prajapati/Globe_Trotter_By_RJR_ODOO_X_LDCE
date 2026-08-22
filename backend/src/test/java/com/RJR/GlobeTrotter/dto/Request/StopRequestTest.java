package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class StopRequestTest {

    @Test
    void builderStoresStopData() {
        StopRequest request = StopRequest.builder()
                .tripId(1L)
                .cityId(2L)
                .startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 9, 3))
                .orderIndex(0)
                .transportCost(new BigDecimal("100.00"))
                .stayCost(new BigDecimal("200.00"))
                .mealCost(new BigDecimal("75.00"))
                .build();

        assertEquals(1L, request.getTripId());
        assertEquals(2L, request.getCityId());
        assertEquals(LocalDate.of(2026, 9, 1), request.getStartDate());
        assertEquals(LocalDate.of(2026, 9, 3), request.getEndDate());
        assertEquals(0, request.getOrderIndex());
        assertEquals(new BigDecimal("100.00"), request.getTransportCost());
        assertEquals(new BigDecimal("200.00"), request.getStayCost());
        assertEquals(new BigDecimal("75.00"), request.getMealCost());
    }

    @Test
    void dateRangeValidationRejectsEndDateBeforeStartDate() throws Exception {
        StopRequest request = StopRequest.builder()
                .startDate(LocalDate.of(2026, 9, 3))
                .endDate(LocalDate.of(2026, 9, 1))
                .build();

        Method validator = StopRequest.class.getDeclaredMethod("isDateRangeValid");
        validator.setAccessible(true);
        assertEquals(false, validator.invoke(request));
    }
}
