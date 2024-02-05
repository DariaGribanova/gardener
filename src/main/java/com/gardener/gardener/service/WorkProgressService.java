package com.gardener.gardener.service;

import com.gardener.gardener.dto.WorkProgressDto;
import com.gardener.gardener.entity.*;
import com.gardener.gardener.repository.PlantRepository;
import com.gardener.gardener.repository.WorkProgressRepository;
import com.gardener.gardener.repository.WorkRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class WorkProgressService {
    @Autowired
    private WorkProgressRepository workProgressRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private WorkRuleRepository workRuleRepository;


    public WorkProgressDto getWorkProgressById(Long id) {
        WorkProgress workProgress = workProgressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Work progress not found with id: " + id));
        return mapToDto(workProgress);
    }

    public WorkProgressDto createWorkProgress(WorkProgressDto workProgressDTO) {
        WorkProgress workProgress = mapToEntity(workProgressDTO);
        workProgress = workProgressRepository.save(workProgress);
        return mapToDto(workProgress);
    }

    public WorkProgressDto updateWorkProgress(Long id, WorkProgressDto workProgressDTO) {
        if (!workProgressRepository.existsById(id)) {
            throw new EntityNotFoundException("Work progress not found with id: " + id);
        }
        WorkProgress updatedWorkProgress = mapToEntity(workProgressDTO);
        updatedWorkProgress.setId(id);
        updatedWorkProgress = workProgressRepository.save(updatedWorkProgress);
        return mapToDto(updatedWorkProgress);
    }

    public void deleteWorkProgress(Long id) {
        if (!workProgressRepository.existsById(id)) {
            throw new EntityNotFoundException("Work progress not found with id: " + id);
        }
        workProgressRepository.deleteById(id);
    }

    private WorkProgressDto mapToDto(WorkProgress workProgress) {
        WorkProgressDto workProgressDTO = new WorkProgressDto();
        workProgressDTO.setId(workProgress.getId());
        workProgressDTO.setPlantId(workProgress.getPlant().getId());
        workProgressDTO.setWorkRuleId(workProgress.getWorkRule().getId());
        workProgressDTO.setDone(workProgress.isDone());
        workProgressDTO.setYear(workProgress.getYear());
        return workProgressDTO;
    }

    private WorkProgress mapToEntity(WorkProgressDto workProgressDTO) {
        WorkProgress workProgress = new WorkProgress();
        workProgress.setPlant(plantRepository.findById(workProgressDTO.getPlantId()).orElseThrow(() -> new EntityNotFoundException("Plant not found with id: " + workProgressDTO.getPlantId())));
        workProgress.setWorkRule(workRuleRepository.findById(workProgressDTO.getWorkRuleId()).orElseThrow(() -> new EntityNotFoundException("Work rule not found with id: " + workProgressDTO.getWorkRuleId())));
        workProgress.setDone(workProgressDTO.isDone());
        workProgress.setYear(workProgressDTO.getYear());
        return workProgress;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void updateWorkProgressList() {
        List<WorkProgress> workProgressList = new ArrayList<>();

        MonthDay currentDate = MonthDay.now();
        MonthDay endDate = MonthDay.of(currentDate.getMonth().getValue() + 2, currentDate.getDayOfMonth());

        Map<PlantCulture, Set<Work>> workMap = new HashMap<>();

        for (Plant plant : plantRepository.findAll()) {
            PlantCulture plantCulture = plant.getPlantCulture();

            // Получаем или создаем множество Work для данной PlantCulture
            Set<Work> works = workMap.computeIfAbsent(plantCulture, k -> new HashSet<>());
            Set<WorkRule> workRules = new HashSet<>();
            // Добавляем все работы в множество
            for (WorkRule workRule : plantCulture.getWorkRules()) {
                if (isWorkRuleInDateRange(workRule, currentDate, endDate) && isWorkProgressExistsForWorkRule(plant, workRule)
                        && plant.getGarden().getRegion() == workRule.getRegion() && isWorkRuleInAgeRange(plant, workRule)) {
                    WorkProgress workProgress = createWorkProgressForPlantAndWorkRule(plant, workRule);
                    workProgressList.add(workProgress);
                }
            }
            // Проходим по цепочке родителей PlantCulture
            PlantCulture parentCulture = plantCulture.getParentCulture();
            while (parentCulture != null) {
                for (WorkRule workRule : plantCulture.getWorkRules()) {
                    if (isWorkRuleInDateRange(workRule, currentDate, endDate) && isWorkProgressExistsForWorkRule(plant, workRule)
                            && plant.getGarden().getRegion() == workRule.getRegion() && isWorkRuleInAgeRange(plant, workRule)) {
                        if (!works.contains(workRule.getWork())) {
                            workRules.add(workRule);
                        }
                        works.add(workRule.getWork());
                    }
                }
                parentCulture = parentCulture.getParentCulture();
            }

            for (WorkRule workRule : workRules) {
                WorkProgress workProgress = createWorkProgressForPlantAndWorkRule(plant, workRule);
                workProgressList.add(workProgress);
            }

        }
        workProgressRepository.saveAll(workProgressList);
    }

    public void addWorkProgressInPlant(Plant plant) {
        List<WorkProgress> workProgressList = new ArrayList<>();
        MonthDay currentDate = MonthDay.now();
        MonthDay endDate = MonthDay.of(currentDate.getMonth().getValue() + 2, currentDate.getDayOfMonth());

        Map<PlantCulture, Set<Work>> workMap = new HashMap<>();

        PlantCulture plantCulture = plant.getPlantCulture();

        // Получаем или создаем множество Work для данной PlantCulture
        Set<Work> works = workMap.computeIfAbsent(plantCulture, k -> new HashSet<>());
        Set<WorkRule> workRules = new HashSet<>();
        // Добавляем все работы в множество
        for (WorkRule workRule : plantCulture.getWorkRules()) {
            if (isWorkRuleInDateRange(workRule, currentDate, endDate) && isWorkProgressExistsForWorkRule(plant, workRule)
                    && plant.getGarden().getRegion() == workRule.getRegion() && isWorkRuleInAgeRange(plant, workRule)) {
                WorkProgress workProgress = createWorkProgressForPlantAndWorkRule(plant, workRule);
                workProgressList.add(workProgress);
            }
        }
        // Проходим по цепочке родителей PlantCulture
        PlantCulture parentCulture = plantCulture.getParentCulture();
        while (parentCulture != null) {
            for (WorkRule workRule : plantCulture.getWorkRules()) {
                if (isWorkRuleInDateRange(workRule, currentDate, endDate) && isWorkProgressExistsForWorkRule(plant, workRule)
                        && plant.getGarden().getRegion() == workRule.getRegion() && isWorkRuleInAgeRange(plant, workRule)) {
                    if (!works.contains(workRule.getWork())) {
                        workRules.add(workRule);
                    }
                    works.add(workRule.getWork());
                }
            }
            parentCulture = parentCulture.getParentCulture();
        }

        for (WorkRule workRule : workRules) {
            WorkProgress workProgress = createWorkProgressForPlantAndWorkRule(plant, workRule);
            workProgressList.add(workProgress);
        }
        workProgressRepository.saveAll(workProgressList);
    }


    private boolean isWorkRuleInDateRange(WorkRule workRule, MonthDay startDate, MonthDay endDate) {
        // Преобразование MonthDay в LocalDate с использованием текущей даты
        LocalDate currentDate = LocalDate.now();
        LocalDate ruleStartDate = startDate.atYear(currentDate.getYear());
        LocalDate ruleEndDate = endDate.atYear(currentDate.getYear());

        // Получение дат начала и окончания работы из объекта WorkRule
        LocalDate workRuleStartDate = workRule.getDateStart().atYear(currentDate.getYear());
        LocalDate workRuleEndDate = workRule.getDateEnd().atYear(currentDate.getYear());

        // Проверка нахождения даты начала и окончания работы в указанном диапазоне
        return !(workRuleEndDate.isBefore(ruleStartDate) || workRuleStartDate.isAfter(ruleEndDate));
    }

    private boolean isWorkRuleInAgeRange(Plant plant, WorkRule workRule) {
        Long year = plant.getYear();
        Long dateStart = workRule.getWork().getAgeStart();
        Long dateEnd = workRule.getWork().getAgeEnd();
        return (year >= dateStart && year <= dateEnd);
    }

    private boolean isWorkProgressExistsForWorkRule(Plant plant, WorkRule workRule) {
        // Логика проверки существования WorkProgress для данного Plant и WorkRule

        // Получение списка всех WorkProgress для данного растения
        List<WorkProgress> workProgresses = plant.getWorkProgresses();

        // Поиск существующего WorkProgress для данного правила работы
        for (WorkProgress progress : workProgresses) {
            if (progress.getWorkRule().equals(workRule) && (progress.getYear() != LocalDate.now().getYear())) {
                return false; // WorkProgress уже существует для данного Plant и WorkRule
            }
        }

        return true; // WorkProgress не найден для данного Plant и WorkRule
    }


    // Метод для создания WorkProgress для заданной PlantCulture и Work
    private WorkProgress createWorkProgressForPlantAndWorkRule(Plant plant, WorkRule workRule) {
        WorkProgress workProgress = new WorkProgress();
        workProgress.setPlant(plant);
        workProgress.setWorkRule(workRule);
        workProgress.setDone(false);
        workProgress.setYear((long) LocalDate.now().getYear());
        return workProgress;
    }
}
