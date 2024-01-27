package com.gardener.gardener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkProgressDto {
    private Long id;
    private Long plantId;
    private Long workRuleId;
    private boolean isDone;
}
