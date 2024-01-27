package com.gardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {
    private Long id;
    private String fullAddress;
    private String shortAddress;
    private Long parentRegionId;
}
