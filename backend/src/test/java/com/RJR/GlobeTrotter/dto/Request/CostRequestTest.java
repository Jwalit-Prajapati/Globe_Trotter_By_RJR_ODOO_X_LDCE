package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CostRequestTest {

    @Test
    void builderStoresCostData() {
        CostRequest request = CostRequest.builder()
                .transportCost(new BigDecimal("100.00"))
                .stayCost(new BigDecimal("200.00"))
                .mealCost(new BigDecimal("75.00"))
                .build();

        assertEquals(new BigDecimal("100.00"), request.getTransportCost());
        assertEquals(new BigDecimal("200.00"), request.getStayCost());
        assertEquals(new BigDecimal("75.00"), request.getMealCost());
    }
}
