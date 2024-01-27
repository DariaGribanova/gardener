package com.gardener.gardener.controller;

import com.gardener.gardener.dto.WorkProgressDto;
import com.gardener.gardener.service.WorkProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
