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
public class CityResponse {

    private Long id;
    private String name;
    private String country;
    private String region;
    private Integer costIndex;
    private Integer popularity;
    private String imageUrl;
}
