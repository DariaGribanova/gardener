package com.gardener.gardener.controller;

import com.gardener.gardener.dto.WorkDto;
import com.gardener.gardener.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/works")
public class WorkController {

    @Autowired
    private WorkService workService;

    @GetMapping
    public ResponseEntity<List<WorkDto>> getAllWorks() {
        List<WorkDto> works = workService.getAllWorks();
        return ResponseEntity.ok(works);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkDto> getWorkById(@PathVariable Long id) {
        WorkDto work = workService.getWorkById(id);
        return work != null ?
                ResponseEntity.ok(work) :
                ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<WorkDto> createWork(@RequestBody WorkDto workDto) {
        WorkDto createdWork = workService.createWork(workDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWork);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkDto> updateWork(@PathVariable Long id, @RequestBody WorkDto workDto) {
        WorkDto updatedWork = workService.updateWork(id, workDto);
        return updatedWork != null ?
                ResponseEntity.ok(updatedWork) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWork(@PathVariable Long id) {
        workService.deleteWork(id);
        return ResponseEntity.noContent().build();
    }

}
