package com.gardener.gardener.controller;
import com.gardener.gardener.dto.GardenDto;
import com.gardener.gardener.dto.UserDto;
import com.gardener.gardener.dto.request.GardenRequestDto;
import com.gardener.gardener.dto.response.GardenResponseDto;
import com.gardener.gardener.entity.Garden;
import com.gardener.gardener.service.GardenService;
import com.gardener.gardener.service.UserService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gardens")
public class GardenController {
    @Autowired
    private GardenService gardenService;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<GardenDto> getGarden(@PathVariable Long id, @ApiParam(hidden = true) Authentication authentication) {
        GardenDto gardenDTO = gardenService.getGardenById(id);
        return ResponseEntity.ok(gardenDTO);
    }

    @GetMapping("/get_gardens")
    public ResponseEntity<List<GardenResponseDto>> getGardens(@ApiParam(hidden = true) Authentication authentication) {
        List<GardenResponseDto> gardenDto = userService.getGardensByUsername(authentication.getName());
        return ResponseEntity.ok(gardenDto);
    }

    @PostMapping
    public ResponseEntity<GardenDto> createGarden(@RequestBody GardenRequestDto gardenDTO, @ApiParam(hidden = true) Authentication authentication) {
        GardenDto createdGarden = new GardenDto();
        createdGarden.setName(gardenDTO.getName());
        createdGarden.setRegionId(gardenDTO.getRegionId());
        createdGarden.setUserId(userService.getUserByUsername(authentication.getName()).getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(gardenService.createGarden(createdGarden));
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
