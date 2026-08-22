package com.RJR.GlobeTrotter.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CityRequestTest {

    @Test
    void builderStoresCityData() {
        CityRequest request = CityRequest.builder()
                .name("Paris")
                .country("France")
                .region("Ile-de-France")
                .costIndex(75)
                .popularity(95)
                .imageUrl("paris.jpg")
                .build();

        assertEquals("Paris", request.getName());
        assertEquals("France", request.getCountry());
        assertEquals("Ile-de-France", request.getRegion());
        assertEquals(75, request.getCostIndex());
        assertEquals(95, request.getPopularity());
        assertEquals("paris.jpg", request.getImageUrl());
    }
}
