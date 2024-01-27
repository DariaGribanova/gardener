package com.gardener.gardener.controller;
import com.gardener.gardener.dto.GardenDto;
import com.gardener.gardener.service.GardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gardens")
public class GardenController {
    @Autowired
    private GardenService gardenService;

    @GetMapping("/{id}")
    public ResponseEntity<GardenDto> getGarden(@PathVariable Long id) {
        GardenDto gardenDTO = gardenService.getGardenById(id);
        return ResponseEntity.ok(gardenDTO);
    }

    @PostMapping
    public ResponseEntity<GardenDto> createGarden(@RequestBody GardenDto gardenDTO) {
        GardenDto createdGarden = gardenService.createGarden(gardenDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGarden);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GardenDto> updateGarden(@PathVariable Long id, @RequestBody GardenDto gardenDTO) {
        GardenDto updatedGarden = gardenService.updateGarden(id, gardenDTO);
        return ResponseEntity.ok(updatedGarden);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGarden(@PathVariable Long id) {
        gardenService.deleteGarden(id);
        return ResponseEntity.noContent().build();
    }
}
