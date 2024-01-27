package com.gardener.gardener.service;

import com.gardener.gardener.dto.GardenDto;
import com.gardener.gardener.entity.Garden;
import com.gardener.gardener.repository.GardenRepository;
import com.gardener.gardener.repository.RegionRepository;
import com.gardener.gardener.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GardenService {
    @Autowired
    private GardenRepository gardenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    public GardenDto getGardenById(Long id) {
        Garden garden = gardenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Garden not found with id: " + id));
        return mapToDto(garden);
    }

    public GardenDto createGarden(GardenDto gardenDTO) {
        Garden garden = mapToEntity(gardenDTO);
        garden = gardenRepository.save(garden);
        return mapToDto(garden);
    }

    public GardenDto updateGarden(Long id, GardenDto gardenDTO) {
        if (!gardenRepository.existsById(id)) {
            throw new EntityNotFoundException("Garden not found with id: " + id);
        }
        Garden updatedGarden = mapToEntity(gardenDTO);
        updatedGarden.setId(id);
        updatedGarden = gardenRepository.save(updatedGarden);
        return mapToDto(updatedGarden);
    }

    public void deleteGarden(Long id) {
        if (!gardenRepository.existsById(id)) {
            throw new EntityNotFoundException("Garden not found with id: " + id);
        }
        gardenRepository.deleteById(id);
    }

    private GardenDto mapToDto(Garden garden) {
        GardenDto gardenDTO = new GardenDto();
        gardenDTO.setId(garden.getId());
        gardenDTO.setName(garden.getName());
        gardenDTO.setUserId(garden.getUser().getId());
        gardenDTO.setRegionId(garden.getRegion().getId());
        return gardenDTO;
    }

    private Garden mapToEntity(GardenDto gardenDTO) {
        Garden garden = new Garden();
        garden.setName(gardenDTO.getName());
        garden.setUser(userRepository.findById(gardenDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + gardenDTO.getUserId())));
        garden.setRegion(regionRepository.findById(gardenDTO.getRegionId())
                .orElseThrow(() -> new EntityNotFoundException("Region not found with id: " + gardenDTO.getRegionId())));
        return garden;
    }

}
