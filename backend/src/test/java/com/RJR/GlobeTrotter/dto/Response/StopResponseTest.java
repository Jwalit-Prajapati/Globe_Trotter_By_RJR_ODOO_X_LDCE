package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class StopResponseTest {

    @Test
    void builderStoresCityAndActivities() {
        CityResponse city = CityResponse.builder().id(2L).name("Paris").build();
        StopActivityResponse activity = StopActivityResponse.builder().id(3L).build();
        StopResponse response = StopResponse.builder().id(1L).tripId(10L).city(city)
                .orderIndex(0).activities(List.of(activity)).build();

        assertEquals(1L, response.getId());
        assertEquals(10L, response.getTripId());
        assertEquals("Paris", response.getCity().getName());
        assertEquals(activity, response.getActivities().get(0));
    }
}
