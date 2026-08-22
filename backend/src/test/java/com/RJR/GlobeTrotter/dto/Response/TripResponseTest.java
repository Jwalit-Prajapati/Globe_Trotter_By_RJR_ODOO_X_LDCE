package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class TripResponseTest {

    @Test
    void builderStoresTripRelationships() {
        StopResponse stop = StopResponse.builder().id(2L).build();
        BudgetResponse budget = BudgetResponse.builder().tripId(1L).build();
        TripResponse response = TripResponse.builder().id(1L).userId(10L).name("Europe")
                .stops(List.of(stop)).budget(budget).build();

        assertEquals(1L, response.getId());
        assertEquals(10L, response.getUserId());
        assertEquals("Europe", response.getName());
        assertNotNull(response.getStops());
        assertEquals(stop, response.getStops().get(0));
        assertEquals(budget, response.getBudget());
    }
}
