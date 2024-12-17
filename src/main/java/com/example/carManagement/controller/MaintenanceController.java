package com.example.carManagement.controller;

import com.example.carManagement.model.dto.reports.MonthlyRequestsReportDTO;
import com.example.carManagement.model.dto.request.CreateMaintenanceDTO;
import com.example.carManagement.model.dto.request.UpdateMaintenanceDTO;
import com.example.carManagement.model.dto.response.ResponseMaintenanceDTO;
import com.example.carManagement.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {
    @Autowired
    private MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<ResponseMaintenanceDTO> createMaintenance(@RequestBody CreateMaintenanceDTO createMaintenanceDTO) {
        ResponseMaintenanceDTO resp = maintenanceService.createMaintenance(createMaintenanceDTO);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMaintenanceDTO> updateMaintenance(@PathVariable("id") Long id, @RequestBody UpdateMaintenanceDTO updateMaintenanceDTO) {
        ResponseMaintenanceDTO resp = maintenanceService.updateMaintenance(id, updateMaintenanceDTO);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMaintenance(@PathVariable("id") Long id) {
        boolean resp = maintenanceService.deleteMaintenance(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<ResponseMaintenanceDTO>> getMaintenanceByFilters(@RequestParam(value = "carId", required = false) Long carId,
                                                                 @RequestParam(value = "garageId", required = false) Long garageId,
                                                                 @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                                 @RequestParam(value = "endDate", required = false) LocalDate endDate){
        List<ResponseMaintenanceDTO> resp = maintenanceService.getMaintenance(carId, garageId, startDate, endDate);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMaintenanceDTO> getMaintenanceById(@PathVariable Long id){
        ResponseMaintenanceDTO resp = maintenanceService.getMaintenanceById(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/monthlyRequestsReport")
    public ResponseEntity<List<MonthlyRequestsReportDTO>> monthlyRequestsReport(@RequestParam(value = "garageId") Long garageId,
                                                                                 @RequestParam(value = "startMonth", required = false) String startDate,
                                                                                 @RequestParam(value = "endMonth", required = false) String endDate){
        List<MonthlyRequestsReportDTO> resp = maintenanceService.getMonthlyMaintenance(garageId, startDate, endDate);
        return ResponseEntity.ok(resp);
    }
}
