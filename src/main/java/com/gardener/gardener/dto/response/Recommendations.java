package com.gardener.gardener.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommendations {
    List<WorkProgressResponseDto> today;
    List<WorkProgressResponseDto> immediate;
    List<WorkProgressResponseDto> forecast;
}
