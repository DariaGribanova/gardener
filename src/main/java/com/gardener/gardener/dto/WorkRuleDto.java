package com.gardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkRuleDto {
    private Long id;
    private Long regionId;
    private Long plantCultureId;
    private Long workId;
    private LocalDate dateStart;
    private LocalDate dateEnd;
}
