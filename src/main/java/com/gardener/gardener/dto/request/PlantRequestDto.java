package com.gardener.gardener.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantRequestDto {
    private String name;
    private Long gardenId;
    private Long plantCultureId;
}