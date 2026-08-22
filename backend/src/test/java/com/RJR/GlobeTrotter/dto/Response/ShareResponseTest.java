package com.RJR.GlobeTrotter.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ShareResponseTest {

    @Test
    void builderStoresShareDetails() {
        ShareResponse response = ShareResponse.builder()
                .tripId(1L)
                .publicSlug("my-paris-trip")
                .shareUrl("https://example.com/share/my-paris-trip")
                .isPublic(true)
                .build();

        assertEquals(1L, response.getTripId());
        assertEquals("my-paris-trip", response.getPublicSlug());
        assertEquals("https://example.com/share/my-paris-trip", response.getShareUrl());
        assertEquals(true, response.getIsPublic());
    }
}
