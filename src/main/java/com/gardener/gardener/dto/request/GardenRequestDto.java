package com.gardener.gardener.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GardenRequestDto {
    private String name;
    private Long regionId;
}
