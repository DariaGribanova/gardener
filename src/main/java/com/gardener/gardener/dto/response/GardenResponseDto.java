package com.gardener.gardener.dto.response;

import com.gardener.gardener.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GardenResponseDto {
    private Long id;
    private String name;
    private Region region;
}
