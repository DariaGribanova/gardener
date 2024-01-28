package com.gardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantDto {
    private Long id;
    private String name;
    private Long gardenId;
    private Long plantCultureId;
    private Long year;
}

