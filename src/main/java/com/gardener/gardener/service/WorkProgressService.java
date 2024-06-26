package com.gardener.gardener.service;

import com.gardener.gardener.dto.UserDto;
import com.gardener.gardener.dto.WorkProgressDto;
import com.gardener.gardener.dto.response.Recommendations;
import com.gardener.gardener.dto.response.WorkProgressResponseDto;
import com.gardener.gardener.dto.response.Works;
import com.gardener.gardener.entity.*;
import com.gardener.gardener.repository.GardenRepository;
import com.gardener.gardener.repository.PlantRepository;
import com.gardener.gardener.repository.WorkProgressRepository;
import com.gardener.gardener.repository.WorkRuleRepository;
import com.gardener.gardener.weather.WeatherData;
import com.gardener.gardener.weather.WeatherForecastData;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
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
    @Autowired
    private GardenRepository gardenRepository;


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
        return (LocalDate.now().getYear() - year >= dateStart && LocalDate.now().getYear() - year <= dateEnd);
    }

    private boolean isWorkProgressExistsForWorkRule(Plant plant, WorkRule workRule) {
        // Логика проверки существования WorkProgress для данного Plant и WorkRule

        // Получение списка всех WorkProgress для данного растения
        List<WorkProgress> workProgresses = plant.getWorkProgresses();
        // Поиск существующего WorkProgress для данного правила работы
        if (workProgresses != null) {
            for (WorkProgress progress : workProgresses) {
                if (progress.getWorkRule().equals(workRule) && (progress.getYear() != LocalDate.now().getYear())) {
                    return false; // WorkProgress уже существует для данного Plant и WorkRule
                }
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

    public Works getRecWorkProgress(Long gardenId) {
        Garden garden = gardenRepository.findById(gardenId).orElseThrow(() -> new EntityNotFoundException("Garden not found with id: " + gardenId));
        Region region = garden.getRegion();
        List<WorkProgress> workProgresses = new ArrayList<>();
        for (Plant plant: garden.getPlants()){
            workProgresses.addAll(plant.getWorkProgresses());
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/forecast?lat=" + region.getLat() + "&lon=" + region.getLon() + "&appid=" + "8f4f47f0de36155cbb79dd619a280eb3")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                WeatherForecastData weatherData = WeatherForecastData.fromJson(jsonResponse);
                return getWorks(weatherData.getList(), workProgresses);
                //System.out.println(weatherData.getList()[0].getMain().getTemp());
            } else {
                System.out.println("Ошибка: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Works getWorks(WeatherData[] weatherData, List<WorkProgress> workProgresses) {
        Works works = new Works();
        List<WorkProgressResponseDto> completed = new ArrayList<>();
        List<WorkProgressResponseDto> unFulfilled = new ArrayList<>();
        for (WorkProgress workProgress: workProgresses){
            if (workProgress.isDone()){
                completed.add(mapToWorkProgressResponseDto(workProgress));
            } else {
                unFulfilled.add(mapToWorkProgressResponseDto(workProgress));
            }
        }
        works.setCompleted(completed);
        works.setUnFulfilled(unFulfilled);
        Recommendations recommendations = getRecs(weatherData, workProgresses);
        works.setRecs(recommendations);
        return works;
    }


    public Recommendations getRecs(WeatherData[] weatherData, List<WorkProgress> workProgresses) {
        Recommendations recommendations = new Recommendations();
        List<WorkProgressResponseDto> today = new ArrayList<>();
        List<WorkProgressResponseDto> immediate = new ArrayList<>();
        List<WorkProgressResponseDto> forecast = new ArrayList<>();
        for (WorkProgress workProgress: workProgresses){
            if (!workProgress.isDone() && workProgress.getYear() == LocalDateTime.now().getYear()) {
                for (WeatherData wData : weatherData) {
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(wData.getDt()), ZoneOffset.UTC);
                    if (isSuitableWork(workProgress, wData) && localDateTime.getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
                            today.add(mapToWorkProgressResponseDto(workProgress));
                            break;
                    } else if (isSuitableWork(workProgress, wData)){
                            immediate.add(mapToWorkProgressResponseDto(workProgress));
                            break;
                    } else if (isDateInMonth(workProgress.getWorkRule())){
                        forecast.add(mapToWorkProgressResponseDto(workProgress));
                        break;
                    }
                }
            }
        }
        recommendations.setToday(today);
        recommendations.setImmediate(immediate);
        recommendations.setForecast(forecast);
        return recommendations;
    }

    private WorkProgressResponseDto mapToWorkProgressResponseDto(WorkProgress workProgress) {
        WorkProgressResponseDto workProgressResponseDto = new WorkProgressResponseDto();
        workProgressResponseDto.setId(workProgress.getId());
        workProgressResponseDto.setWorkName(workProgress.getWorkRule().getWork().getName());
        workProgressResponseDto.setPlantName(workProgress.getPlant().getName());
        workProgressResponseDto.setDone(workProgress.isDone());
        workProgressResponseDto.setYear(workProgress.getYear());
        return workProgressResponseDto;
    }


    private boolean isSuitableWork(WorkProgress workProgress, WeatherData weatherData) {
        // Проверить, подходит ли работа для выполнения сегодня, учитывая погоду
        return isTemperatureInRange(workProgress.getWorkRule().getWork(), weatherData) &&
                isPrecipitationSuitable(workProgress.getWorkRule().getWork(), weatherData) &&
                isDateInRange(workProgress.getWorkRule(), weatherData);
    }

    private boolean isDateInRange(WorkRule workRule, WeatherData weatherData) {
        // Проверить, попадает ли дата работы в диапазон дат прогноза погоды
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), workRule.getDateStart().getMonth(), workRule.getDateStart().getDayOfMonth());
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), workRule.getDateEnd().getMonth(), workRule.getDateEnd().getDayOfMonth());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(weatherData.getDt()), ZoneOffset.UTC);
        LocalDate forecastDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth());
        return (forecastDate.isEqual(startDate) || forecastDate.isAfter(startDate)) &&
                (forecastDate.isEqual(endDate) || forecastDate.isBefore(endDate));
    }

    private boolean isDateInMonth(WorkRule workRule) {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), workRule.getDateStart().getMonth(), workRule.getDateStart().getDayOfMonth());
        LocalDate forecastDate = LocalDate.now();
        return (forecastDate.isAfter(startDate));
    }

    private boolean isTemperatureInRange(Work work, WeatherData weatherData) {
        // Проверить, находится ли температура в допустимом диапазоне для работы
        double tempInCelsius = weatherData.getMain().getTemp() - 273.15; // Convert temperature to Celsius
        return tempInCelsius >= work.getTempStart() && tempInCelsius <= work.getTempEnd();
    }

    private boolean isPrecipitationSuitable(Work work, WeatherData weatherData) {
        // Проверить, соответствует ли осадкам работа
        String precipitationType = weatherData.getWeather()[0].getMain(); // Assuming only one weather condition for simplicity
        switch (work.getPrecipitation()) {
            case NOPRECIPITATION:
                return !precipitationType.contains("rain") && !precipitationType.contains("snow");
            case WITHPRECIPITATION:
                return precipitationType.contains("rain") || precipitationType.contains("snow");
            case BEFORETHERAIN:
                // Предполагаем, что работа возможна перед любыми осадками
                return true;
            case DOESNTMATTER:
                return true; // Не важно, какие осадки
            default:
                return false;
        }
    }


}
