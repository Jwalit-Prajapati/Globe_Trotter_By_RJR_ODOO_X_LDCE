package com.RJR.GlobeTrotter.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareResponse {

    private Long tripId;
    private String publicSlug;
    private String shareUrl;
    private Boolean isPublic;
}
