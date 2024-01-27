package com.gardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GardenDto {
    private Long id;
    private String name;
    private Long userId;
    private Long regionId;
}
