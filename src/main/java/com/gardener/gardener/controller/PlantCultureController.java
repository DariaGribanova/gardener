package com.gardener.gardener.controller;

import com.gardener.gardener.dto.PlantCultureDto;
import com.gardener.gardener.entity.PlantCulture;
import com.gardener.gardener.service.PlantCultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plant-cultures")
public class PlantCultureController {

    @Autowired
    private PlantCultureService plantCultureService;

    @GetMapping
    public ResponseEntity<List<PlantCultureDto>> getAllPlantCultures() {
        List<PlantCultureDto> plantCultures = plantCultureService.getAllPlantCultures();
        return ResponseEntity.ok(plantCultures);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantCultureDto> getPlantCultureById(@PathVariable Long id) {
        PlantCultureDto plantCulture = plantCultureService.getPlantCultureById(id);
        return plantCulture != null ?
                ResponseEntity.ok(plantCulture) :
                ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PlantCultureDto> createPlantCulture(@RequestBody PlantCulture plantCulture) {
        PlantCultureDto createdPlantCulture = plantCultureService.createPlantCulture(plantCulture);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlantCulture);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantCultureDto> updatePlantCulture(@PathVariable Long id, @RequestBody PlantCulture plantCulture) {
        PlantCultureDto updatedPlantCulture = plantCultureService.updatePlantCulture(id, plantCulture);
        return updatedPlantCulture != null ?
                ResponseEntity.ok(updatedPlantCulture) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlantCulture(@PathVariable Long id) {
        plantCultureService.deletePlantCulture(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/root")
    public ResponseEntity<List<PlantCultureDto>> getRootPlantCultures() {
        List<PlantCultureDto> rootPlantCultures = plantCultureService.getRootPlantCultures();
        return ResponseEntity.ok(rootPlantCultures);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<PlantCultureDto>> getChildPlantCulturesByParentId(@PathVariable Long id) {
        List<PlantCultureDto> childPlantCultures = plantCultureService.getChildPlantCulturesByParentId(id);
        return ResponseEntity.ok(childPlantCultures);
    }
}