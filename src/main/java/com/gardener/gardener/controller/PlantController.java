package com.gardener.gardener.controller;

import com.gardener.gardener.dto.PlantDto;
import com.gardener.gardener.dto.request.PlantRequestDto;
import com.gardener.gardener.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plants")
public class PlantController {
    @Autowired
    private PlantService plantService;

    @GetMapping("/{id}")
    public ResponseEntity<PlantDto> getPlant(@PathVariable Long id) {
        PlantDto plantDTO = plantService.getPlantById(id);
        return ResponseEntity.ok(plantDTO);
    }

    @PostMapping
    public ResponseEntity<PlantDto> createPlant(@RequestBody PlantRequestDto plantDTO) {
        PlantDto createdPlant = plantService.createPlant(plantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantDto> updatePlant(@PathVariable Long id, @RequestBody PlantDto plantDTO) {
        PlantDto updatedPlant = plantService.updatePlant(id, plantDTO);
        return ResponseEntity.ok(updatedPlant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }

}
