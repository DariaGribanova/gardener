package com.gardener.gardener.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkProgressResponseDto {
    private Long id;
    private String plantName;
    private String workName;
    private boolean isDone;
    private Long year;
    private ZonedDateTime dateTime;
}
