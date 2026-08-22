package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CityResponseTest {

    @Test
    void builderStoresCityDetails() {
        CityResponse response = CityResponse.builder().id(1L).name("Paris").country("France")
                .region("Ile-de-France").costIndex(75).popularity(95).imageUrl("paris.jpg").build();

        assertEquals(1L, response.getId());
        assertEquals("Paris", response.getName());
        assertEquals("France", response.getCountry());
        assertEquals(75, response.getCostIndex());
        assertEquals(95, response.getPopularity());
        assertEquals("paris.jpg", response.getImageUrl());
    }
}
