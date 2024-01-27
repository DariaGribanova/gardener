package com.gardener.gardener.service;

import com.gardener.gardener.dto.PlantCultureDto;
import com.gardener.gardener.entity.PlantCulture;
import com.gardener.gardener.repository.PlantCultureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlantCultureService {

    @Autowired
    private PlantCultureRepository plantCultureRepository;


    public PlantCultureDto getPlantCultureById(Long id) {
        PlantCulture plantCulture = plantCultureRepository.findById(id).orElse(null);
        if (plantCulture != null) {
            return convertToDto(plantCulture);
        } else {
            return null;
        }
    }

    public PlantCultureDto createPlantCulture(PlantCulture plantCulture) {
        return convertToDto(plantCultureRepository.save(plantCulture));
    }

    public PlantCultureDto updatePlantCulture(Long id, PlantCulture plantCulture) {
        return convertToDto(plantCultureRepository.save(plantCulture));
    }

    public void deletePlantCulture(Long id) {
        plantCultureRepository.deleteById(id);
    }

    public List<PlantCultureDto> getRootPlantCultures() {
        return convertToDtoList(plantCultureRepository.findByDisplayRootIsTrueAndParentCultureIsNull());
    }

    public List<PlantCultureDto> getChildPlantCulturesByParentId(Long id) {
        return convertToDtoList(plantCultureRepository.findByParentCultureId(id));
    }


    public List<PlantCultureDto> getAllPlantCultures() {
        List<PlantCulture> plantCultures = plantCultureRepository.findAll();
        return convertToDtoList(plantCultures);
    }


    private List<PlantCultureDto> convertToDtoList(List<PlantCulture> plantCultures) {
        return plantCultures.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PlantCultureDto convertToDto(PlantCulture plantCulture) {
        PlantCultureDto dto = new PlantCultureDto();
        dto.setId(plantCulture.getId());
        dto.setCultureName(plantCulture.getCultureName());
        if (plantCulture.getParentCulture() != null) {
            dto.setParentCultureId(plantCulture.getParentCulture().getId());
        }
        dto.setDisplayRoot(plantCulture.isDisplayRoot());
        return dto;
    }
}
