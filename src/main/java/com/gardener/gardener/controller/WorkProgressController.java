package com.gardener.gardener.controller;

import com.gardener.gardener.dto.WorkProgressDto;
import com.gardener.gardener.dto.request.GardenRequestDto;
import com.gardener.gardener.dto.response.Works;
import com.gardener.gardener.entity.Garden;
import com.gardener.gardener.service.WorkProgressService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-progresses")
public class WorkProgressController {

    @Autowired
    private WorkProgressService workProgressService;


    @GetMapping("/{id}")
    public ResponseEntity<WorkProgressDto> getWorkProgress(@PathVariable Long id) {
        WorkProgressDto workProgressDTO = workProgressService.getWorkProgressById(id);
        return ResponseEntity.ok(workProgressDTO);
    }

    @PostMapping
    public ResponseEntity<WorkProgressDto> createWorkProgress(@RequestBody WorkProgressDto workProgressDTO) {
        WorkProgressDto createdWorkProgress = workProgressService.createWorkProgress(workProgressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkProgress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkProgressDto> updateWorkProgress(@PathVariable Long id, @RequestBody WorkProgressDto workProgressDTO) {
        WorkProgressDto updatedWorkProgress = workProgressService.updateWorkProgress(id, workProgressDTO);
        return ResponseEntity.ok(updatedWorkProgress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkProgress(@PathVariable Long id) {
        workProgressService.deleteWorkProgress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rec")
    public ResponseEntity<?> getRecWorkProgress(@RequestBody Garden garden, @ApiParam(hidden = true) Authentication authentication) {
        Works createdWorkProgress = workProgressService.getRecWorkProgress(garden.getId());
        return ResponseEntity.ok(createdWorkProgress);
    }
}
