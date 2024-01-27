package com.gardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantCultureDto {
    private Long id;
    private String cultureName;
    private Long parentCultureId;
    private boolean displayRoot;
}