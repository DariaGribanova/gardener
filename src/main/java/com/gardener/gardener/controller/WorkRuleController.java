package com.gardener.gardener.controller;
import com.gardener.gardener.dto.WorkRuleDto;
import com.gardener.gardener.service.WorkRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workrules")
public class WorkRuleController {
    @Autowired
    private WorkRuleService workRuleService;


    @GetMapping("/{id}")
    public ResponseEntity<WorkRuleDto> getWorkRule(@PathVariable Long id) {
        WorkRuleDto workRuleDTO = workRuleService.getWorkRuleById(id);
        return ResponseEntity.ok(workRuleDTO);
    }

    @PostMapping
    public ResponseEntity<WorkRuleDto> createWorkRule(@RequestBody WorkRuleDto workRuleDTO) {
        WorkRuleDto createdWorkRule = workRuleService.createWorkRule(workRuleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkRule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkRuleDto> updateWorkRule(@PathVariable Long id, @RequestBody WorkRuleDto workRuleDTO) {
        WorkRuleDto updatedWorkRule = workRuleService.updateWorkRule(id, workRuleDTO);
        return ResponseEntity.ok(updatedWorkRule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkRule(@PathVariable Long id) {
        workRuleService.deleteWorkRule(id);
        return ResponseEntity.noContent().build();
    }
}
