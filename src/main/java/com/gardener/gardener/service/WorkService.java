package com.gardener.gardener.service;

import com.gardener.gardener.dto.WorkDto;
import com.gardener.gardener.entity.Precipitation;
import com.gardener.gardener.entity.Work;
import com.gardener.gardener.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkService {
    @Autowired
    private WorkRepository workRepository;

    public List<WorkDto> getAllWorks() {
        List<Work> works = workRepository.findAll();
        return convertToDtoList(works);
    }

    public WorkDto getWorkById(Long id) {
        Work work = workRepository.findById(id).orElse(null);
        if (work != null) {
            return convertToDto(work);
        }
        return null;
    }

    public WorkDto createWork(WorkDto workDto) {
        Work work = convertToEntity(workDto);
        Work savedWork = workRepository.save(work);
        return convertToDto(savedWork);
    }

    public WorkDto updateWork(Long id, WorkDto workDto) {
        Work existingWork = workRepository.findById(id).orElse(null);
        if (existingWork != null) {
            Work updatedWork = convertToEntity(workDto);
            updatedWork.setId(id);
            Work savedWork = workRepository.save(updatedWork);
            return convertToDto(savedWork);
        }
        return null;
    }

    public void deleteWork(Long id) {
        workRepository.deleteById(id);
    }

    private Work convertToEntity(WorkDto workDto) {
        Work work = new Work();
        work.setName(workDto.getName());
        work.setTempStart(workDto.getTempStart());
        work.setTempEnd(workDto.getTempEnd());
        work.setRole(Precipitation.valueOf(workDto.getRole()));
        work.setAgeStart(workDto.getAgeStart());
        work.setAgeEnd(workDto.getAgeEnd());
        return work;
    }

    private List<WorkDto> convertToDtoList(List<Work> works) {
        return works.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private WorkDto convertToDto(Work work) {
        WorkDto dto = new WorkDto();
        dto.setId(work.getId());
        dto.setName(work.getName());
        dto.setTempStart(work.getTempStart());
        dto.setTempEnd(work.getTempEnd());
        dto.setRole(work.getRole().toString());
        dto.setAgeStart(work.getAgeStart());
        dto.setAgeEnd(work.getAgeEnd());
        return dto;
    }
}
