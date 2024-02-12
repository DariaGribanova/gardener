package com.gardener.gardener.service;

import com.gardener.gardener.dto.PlantDto;
import com.gardener.gardener.dto.request.PlantRequestDto;
import com.gardener.gardener.entity.Plant;
import com.gardener.gardener.repository.PlantCultureRepository;
import com.gardener.gardener.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gardener.gardener.repository.GardenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class PlantService {
    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private PlantCultureRepository plantCultureRepository;

    @Autowired
    private WorkProgressService workProgressService;

    public PlantDto getPlantById(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plant not found with id: " + id));
        return mapToDto(plant);
    }

    public PlantDto createPlant(PlantRequestDto plantDTO) {
        Plant plant = mapToEntityFromRequest(plantDTO);
        plant.setYear((long) LocalDate.now().getYear());
        plant = plantRepository.save(plant);
        Plant newPlant = plantRepository.findById(plant.getId()).orElseThrow();
        workProgressService.addWorkProgressInPlant(newPlant);
        return mapToDto(plant);
    }

    public PlantDto updatePlant(Long id, PlantDto plantDTO) {
        if (!plantRepository.existsById(id)) {
            throw new EntityNotFoundException("Plant not found with id: " + id);
        }
        Plant updatedPlant = mapToEntity(plantDTO);
        updatedPlant.setId(id);
        updatedPlant = plantRepository.save(updatedPlant);
        return mapToDto(updatedPlant);
    }

    public void deletePlant(Long id) {
        if (!plantRepository.existsById(id)) {
            throw new EntityNotFoundException("Plant not found with id: " + id);
        }
        plantRepository.deleteById(id);
    }

    private PlantDto mapToDto(Plant plant) {
        PlantDto plantDTO = new PlantDto();
        plantDTO.setId(plant.getId());
        plantDTO.setName(plant.getName());
        plantDTO.setGardenId(plant.getGarden().getId());
        plantDTO.setPlantCultureId(plant.getPlantCulture().getId());
        plantDTO.setYear(plant.getYear());
        return plantDTO;
    }

    private Plant mapToEntity(PlantDto plantDTO) {
        Plant plant = new Plant();
        plant.setName(plantDTO.getName());
        plant.setGarden(gardenRepository.findById(plantDTO.getGardenId())
                .orElseThrow(() -> new EntityNotFoundException("Garden not found with id: " + plantDTO.getGardenId())));
        plant.setPlantCulture(plantCultureRepository.findById(plantDTO.getPlantCultureId())
                .orElseThrow(() -> new EntityNotFoundException("Plant culture not found with id: " + plantDTO.getPlantCultureId())));
        plant.setYear(plantDTO.getYear());
        return plant;
    }

    private Plant mapToEntityFromRequest(PlantRequestDto plantDTO) {
        Plant plant = new Plant();
        plant.setName(plantDTO.getName());
        plant.setGarden(gardenRepository.findById(plantDTO.getGardenId())
                .orElseThrow(() -> new EntityNotFoundException("Garden not found with id: " + plantDTO.getGardenId())));
        plant.setPlantCulture(plantCultureRepository.findById(plantDTO.getPlantCultureId())
                .orElseThrow(() -> new EntityNotFoundException("Plant culture not found with id: " + plantDTO.getPlantCultureId())));
        return plant;
    }
}
