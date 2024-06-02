package com.gardener.gardener.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Works {
    Recommendations recs;
    List<WorkProgressResponseDto> completed;
    List<WorkProgressResponseDto> unFulfilled;
}
