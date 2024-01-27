package com.gardener.gardener.controller;

import com.gardener.gardener.dto.RegionDto;
import com.gardener.gardener.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public List<RegionDto> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getRegionById(@PathVariable Long id) {
        RegionDto region = regionService.getRegionById(id);
        if (region != null) {
            return ResponseEntity.ok(region);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<RegionDto> createRegion(@RequestBody RegionDto regionDto) {
        RegionDto createdRegion = regionService.createRegion(regionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> updateRegion(@PathVariable Long id, @RequestBody RegionDto newRegionDto) {
        RegionDto updatedRegion = regionService.updateRegion(id, newRegionDto);
        if (updatedRegion != null) {
            return ResponseEntity.ok(updatedRegion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/parent/{parentId}")
    public List<RegionDto> getRegionsByParentId(@PathVariable Long parentId) {
        return regionService.getRegionsByParentId(parentId);
    }

    @GetMapping("/root")
    public ResponseEntity<List<RegionDto>> getRootRegions() {
        List<RegionDto> rootRegions = regionService.getRootRegions();
        return ResponseEntity.ok(rootRegions);
    }

}
