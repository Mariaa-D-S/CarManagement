package com.example.carManagement.service.implementation;

import com.example.carManagement.model.Car;
import com.example.carManagement.model.Garage;
import com.example.carManagement.model.Maintenance;
import com.example.carManagement.model.dto.reports.GarageDailyAvailabilityReportDTO;
import com.example.carManagement.model.dto.reports.MonthlyRequestsReportDTO;
import com.example.carManagement.model.dto.request.CreateMaintenanceDTO;
import com.example.carManagement.model.dto.request.UpdateMaintenanceDTO;
import com.example.carManagement.model.dto.response.ResponseMaintenanceDTO;
import com.example.carManagement.repository.CarRepository;
import com.example.carManagement.repository.GarageRepository;
import com.example.carManagement.repository.MaintenanceRepository;
import com.example.carManagement.service.MaintenanceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private GarageRepository garageRepository;
    @Override
    @Transactional
    public ResponseMaintenanceDTO createMaintenance(CreateMaintenanceDTO createMaintenanceDTO) {
        Car car = carRepository.findById(createMaintenanceDTO.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found!"));
        Garage garage = garageRepository.findById(createMaintenanceDTO.getGarageId())
                .orElseThrow(() -> new IllegalArgumentException("Garage not found!"));

        Maintenance maintenance = mapCreateDTOToMaintenance(createMaintenanceDTO, car, garage);
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);

        return mapMaintenanceToResponseDTO(savedMaintenance);
    }

    @Override
    @Transactional
    public ResponseMaintenanceDTO updateMaintenance(Long id, UpdateMaintenanceDTO updateMaintenanceDTO) {
        Maintenance existingMaintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance record not found!"));

        Car car = updateMaintenanceDTO.getCarId() != null ? carRepository.findById(updateMaintenanceDTO.getCarId()).orElse(null) : null;
        Garage garage = updateMaintenanceDTO.getGarageId() != null ? garageRepository.findById(updateMaintenanceDTO.getGarageId()).orElse(null) : null;

        Maintenance updatedMaintenance = mapUpdateDTOToMaintenance(existingMaintenance, updateMaintenanceDTO, car, garage);
        Maintenance savedMaintenance = maintenanceRepository.save(updatedMaintenance);

        return mapMaintenanceToResponseDTO(savedMaintenance);
    }

    @Override
    @Transactional
    public boolean deleteMaintenance(Long id) {
        Optional<Maintenance> maintenance = maintenanceRepository.findById(id);
        if (maintenance.isPresent()) {
            maintenanceRepository.deleteById(id);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance not found");
        //return false;
    }

    @Override
    public ResponseMaintenanceDTO getMaintenanceById(Long id) {
        Optional<Maintenance> maintenance = maintenanceRepository.findById(id);
        if (maintenance.isPresent()) {
            return mapMaintenanceToResponseDTO(maintenance.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance not found");
    }

    @Override
    public List<ResponseMaintenanceDTO> getMaintenance(Long carId, Long garageId, LocalDate startDate, LocalDate endDate) {

            if (startDate == null) {
                startDate = maintenanceRepository.findEarliestDate();
            }

            if (endDate == null) {
                endDate = maintenanceRepository.findLatestDate();
            }
            List<Maintenance> maintenances = maintenanceRepository.findMaintenanceByFilters(carId, garageId, startDate, endDate);
            return maintenances.stream()
                    .map(this::mapMaintenanceToResponseDTO)
                    .collect(Collectors.toList());

    }

    @Override
    public List<MonthlyRequestsReportDTO> getMonthlyMaintenance(Long garageId, String startDate, String endDate) {
        YearMonth startYearMonth = YearMonth.parse(startDate);
        YearMonth endYearMonth = YearMonth.parse(endDate);

        LocalDate startMonth = startYearMonth.atDay(1);
        LocalDate endMonth = endYearMonth.atEndOfMonth();

        List<Object[]> result = maintenanceRepository.getMonthlyRequests(garageId, startMonth, endMonth);

        Map<YearMonth, Integer> requestsMap = result.stream()
                .collect(Collectors.toMap(
                        row -> {
                            Date sqlDate = (Date) row[0];
                            LocalDate date = LocalDate.parse(sqlDate.toString());
                            return YearMonth.of(date.getYear(), date.getMonthValue());
                        },
                        row -> ((Number) row[1]).intValue(),
                        Integer::sum
                ));

        List<YearMonth> allMonths = generateYearMonthRange(startYearMonth, endYearMonth);

        return allMonths.stream()
                .map(month -> {
                    Integer requests = requestsMap.getOrDefault(month, 0);
                    return new MonthlyRequestsReportDTO(month, requests);
                })
                .collect(Collectors.toList());
    }

    private List<YearMonth> generateYearMonthRange(YearMonth start, YearMonth end) {
        List<YearMonth> months = new ArrayList<>();
        YearMonth current = start;
        while (!current.isAfter(end)) {
            months.add(current);
            current = current.plusMonths(1);
        }
        return months;
    }

    private ResponseMaintenanceDTO mapMaintenanceToResponseDTO(Maintenance maintenance) {
        if (maintenance == null) {
            return null;
        }

        ResponseMaintenanceDTO dto = new ResponseMaintenanceDTO();
        dto.setId(maintenance.getId());
        dto.setServiceType(maintenance.getServiceType());
        dto.setScheduledDate(maintenance.getScheduleDate());
        dto.setCarId(maintenance.getCar().getId());
        dto.setCarName(maintenance.getCar().getCarMake() + " " + maintenance.getCar().getModel());
        dto.setGarageId(maintenance.getGarage().getId());
        dto.setGarageName(maintenance.getGarage().getName());
        return dto;
    }

    private Maintenance mapCreateDTOToMaintenance(CreateMaintenanceDTO createMaintenanceDTO, Car car, Garage garage) {
        if (createMaintenanceDTO == null || car == null || garage == null) {
            throw new IllegalArgumentException("Car, Garage, and CreateMaintenanceDTO cannot be null");
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setServiceType(createMaintenanceDTO.getServiceType());
        maintenance.setScheduleDate(createMaintenanceDTO.getScheduledDate());
        maintenance.setCar(car);
        maintenance.setGarage(garage);
        return maintenance;
    }

    private Maintenance mapUpdateDTOToMaintenance(Maintenance existingMaintenance, UpdateMaintenanceDTO updateMaintenanceDTO, Car car, Garage garage) {
        if (existingMaintenance == null || updateMaintenanceDTO == null) {
            throw new IllegalArgumentException("Maintenance and UpdateMaintenanceDTO cannot be null");
        }

        existingMaintenance.setServiceType(updateMaintenanceDTO.getServiceType());
        existingMaintenance.setScheduleDate(updateMaintenanceDTO.getScheduledDate());
        if (car != null) {
            existingMaintenance.setCar(car);
        }
        if (garage != null) {
            existingMaintenance.setGarage(garage);
        }
        return existingMaintenance;
    }
}
