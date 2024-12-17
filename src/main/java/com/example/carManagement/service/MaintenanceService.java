package com.example.carManagement.service;

import com.example.carManagement.model.dto.reports.MonthlyRequestsReportDTO;
import com.example.carManagement.model.dto.request.CreateMaintenanceDTO;
import com.example.carManagement.model.dto.request.UpdateMaintenanceDTO;
import com.example.carManagement.model.dto.response.ResponseMaintenanceDTO;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceService {
    ResponseMaintenanceDTO createMaintenance(CreateMaintenanceDTO createMaintenanceDTO);
    ResponseMaintenanceDTO updateMaintenance(Long id, UpdateMaintenanceDTO updateMaintenanceDTO);
    boolean deleteMaintenance(Long id);
    ResponseMaintenanceDTO getMaintenanceById(Long id);
    List<ResponseMaintenanceDTO> getMaintenance(Long carId, Long garageId, LocalDate startDate, LocalDate endDate);
    List<MonthlyRequestsReportDTO> getMonthlyMaintenance(Long garageId, String startMonth, String endMonth);

}
