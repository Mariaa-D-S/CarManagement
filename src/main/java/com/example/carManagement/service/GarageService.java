package com.example.carManagement.service;

import com.example.carManagement.model.dto.reports.GarageDailyAvailabilityReportDTO;
import com.example.carManagement.model.dto.request.CreateGarageDTO;
import com.example.carManagement.model.dto.request.UpdateGarageDTO;
import com.example.carManagement.model.dto.response.ResponseGarageDTO;

import java.time.LocalDate;
import java.util.List;

public interface GarageService {
    ResponseGarageDTO createGarage(CreateGarageDTO createGarageDTO);
    ResponseGarageDTO updateGarage(Long id, UpdateGarageDTO updateGarageDTO);
    boolean deleteGarage(Long id);
    ResponseGarageDTO getGarageById(Long id);
    List<ResponseGarageDTO> getGaragesByCity(String city);
    List<GarageDailyAvailabilityReportDTO> getGarageDailyReport(Long id, LocalDate startDate, LocalDate endDate);

}
