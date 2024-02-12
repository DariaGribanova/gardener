package com.gardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkDto {

    private Long id;

    private String name;

    private Long tempStart;

    private Long tempEnd;

    private String precipitation;

    private Long ageStart;

    private Long ageEnd;
}
