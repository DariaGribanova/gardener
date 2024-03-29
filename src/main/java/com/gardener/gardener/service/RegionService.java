package com.gardener.gardener.service;

import com.gardener.gardener.dto.PlantCultureDto;
import com.gardener.gardener.dto.RegionDto;
import com.gardener.gardener.entity.Region;
import com.gardener.gardener.entity.WorkRule;
import com.gardener.gardener.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDto> getAllRegions() {
        List<Region> regions = regionRepository.findAll();
        return regions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RegionDto getRegionById(Long id) {
        Region region = regionRepository.findById(id).orElse(null);
        if (region != null) {
            return convertToDto(region);
        } else {
            return null;
        }
    }

    public RegionDto createRegion(RegionDto regionDto) {
        Region region = convertToEntity(regionDto);
        return convertToDto(regionRepository.save(region));
    }

    public RegionDto updateRegion(Long id, RegionDto newRegionDto) {
        if (regionRepository.existsById(id)) {
            Region updatedRegion = convertToEntity(newRegionDto);
            updatedRegion.setId(id);
            return convertToDto(regionRepository.save(updatedRegion));
        } else {
            return null;
        }
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    public List<RegionDto> getRegionsByParentId(Long parentId) {
        List<Region> regions = regionRepository.findByParentRegionId(parentId);
        return regions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RegionDto> getRootRegions() {
        List<Region> regions = regionRepository.findByParentRegionIdIsNull();
        return regions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Region convertToEntity(RegionDto regionDto) {
        Region region = new Region();
        region.setId(regionDto.getId());
        region.setFullAddress(regionDto.getFullAddress());
        region.setShortAddress(regionDto.getShortAddress());
        if (regionDto.getParentRegionId() != null) {
            Region parentRegion = regionRepository.findById(regionDto.getParentRegionId()).orElse(null);
            region.setParentRegion(parentRegion);
        }
        return region;
    }

    private RegionDto convertToDto(Region region) {
        RegionDto regionDto = new RegionDto();
        regionDto.setId(region.getId());
        regionDto.setFullAddress(region.getFullAddress());
        regionDto.setShortAddress(region.getShortAddress());
        if (region.getParentRegion() != null) {
            regionDto.setParentRegionId(region.getParentRegion().getId());
        }
        return regionDto;
    }


    public List<List<Region>> getRegionHierarchy(Long parentId) {
        List<List<Region>> lists = new ArrayList<>();
        if (parentId == null) {
            lists.add(regionRepository.findByParentRegionIdIsNull());
            return lists;
        } else {
            if (!regionRepository.findByParentRegionId(parentId).isEmpty()) {
                lists.add(regionRepository.findByParentRegionId(parentId));
            }
            Region region = regionRepository.findById(parentId).orElse(null);
            while (region != null && region.getParentRegion() != null) {
                List<Region> list = regionRepository.findByParentRegionId(region.getParentRegion().getId());
                for (Region currentRegion : new ArrayList<>(list)) {
                    if (currentRegion.getId().equals(region.getId())) {
                        list.remove(currentRegion);  // Удаляем элемент с желаемым id
                        list.add(0, currentRegion);  // Добавляем его на первое место
                        break;  // Выходим из цикла после перемещения первого вхождения
                    }
                }
                lists.add(list);
                region = regionRepository.findById(region.getParentRegion().getId()).orElse(null);
            }
            if (region != null && region.getParentRegion() == null) {
                List<Region> list = regionRepository.findByParentRegionIdIsNull();
                for (Region currentRegion : new ArrayList<>(list)) {
                    if (currentRegion.getId().equals(region.getId())) {
                        list.remove(currentRegion);  // Удаляем элемент с желаемым id
                        list.add(0, currentRegion);  // Добавляем его на первое место
                        break;  // Выходим из цикла после перемещения первого вхождения
                    }
                }
                lists.add(list);
            }
        }
        return lists;
    }

}
