package com.gardener.gardener.service;

import com.gardener.gardener.dto.WorkDto;
import com.gardener.gardener.dto.WorkRuleDto;
import com.gardener.gardener.entity.WorkProgress;
import com.gardener.gardener.entity.WorkRule;
import com.gardener.gardener.repository.PlantCultureRepository;
import com.gardener.gardener.repository.RegionRepository;
import com.gardener.gardener.repository.WorkRepository;
import com.gardener.gardener.repository.WorkRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkRuleService {

    @Autowired
    private WorkRuleRepository workRuleRepository;
    @Autowired
    private final RegionRepository regionRepository;
    @Autowired
    private final PlantCultureRepository plantCultureRepository;
    @Autowired
    private final WorkRepository workRepository;


    public WorkRuleDto getWorkRuleById(Long id) {
        WorkRule workRule = workRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkRule not found with id: " + id));
        return convertToDto(workRule);
    }

    public WorkRuleDto createWorkRule(WorkRuleDto workRuleDTO) {
        WorkRule workRule = convertToEntity(workRuleDTO);
        workRule = workRuleRepository.save(workRule);
        return convertToDto(workRule);
    }

    public WorkRuleDto updateWorkRule(Long id, WorkRuleDto workRuleDTO) {
        WorkRule existingWorkRule = workRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WorkRule not found with id: " + id));
        WorkRule updatedWorkRule = convertToEntity(workRuleDTO);
        updatedWorkRule.setId(existingWorkRule.getId());
        updatedWorkRule = workRuleRepository.save(updatedWorkRule);
        return convertToDto(updatedWorkRule);
    }

    public void deleteWorkRule(Long id) {
        if (!workRuleRepository.existsById(id)) {
            throw new EntityNotFoundException("WorkRule not found with id: " + id);
        }
        workRuleRepository.deleteById(id);
    }

    private WorkRuleDto convertToDto(WorkRule workRule) {
        WorkRuleDto workRuleDTO = new WorkRuleDto();
        workRuleDTO.setId(workRule.getId());
        workRuleDTO.setRegionId(workRule.getRegion().getId());
        workRuleDTO.setPlantCultureId(workRule.getPlantCulture().getId());
        workRuleDTO.setWorkId(workRule.getWork().getId());
        workRuleDTO.setDateStart(workRule.getDateStart());
        workRuleDTO.setDateEnd(workRule.getDateEnd());
        return workRuleDTO;
    }

    private WorkRule convertToEntity(WorkRuleDto workRuleDTO) {
        WorkRule workRule = new WorkRule();
        workRule.setId(workRuleDTO.getId());
        workRule.setRegion(regionRepository.findById(workRuleDTO.getRegionId()).orElseThrow(EntityNotFoundException::new));
        workRule.setPlantCulture(plantCultureRepository.findById(workRuleDTO.getPlantCultureId()).orElseThrow(EntityNotFoundException::new));
        workRule.setWork(workRepository.findById(workRuleDTO.getWorkId()).orElseThrow(EntityNotFoundException::new));
        workRule.setDateStart(workRuleDTO.getDateStart());
        workRule.setDateEnd(workRuleDTO.getDateEnd());
        return workRule;
    }

}
