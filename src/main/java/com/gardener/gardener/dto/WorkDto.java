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

    private int tempStart;

    private int tempEnd;

    private String role;

    private int ageStart;

    private int ageEnd;
}
