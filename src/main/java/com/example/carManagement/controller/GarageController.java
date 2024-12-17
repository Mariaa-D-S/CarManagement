package com.example.carManagement.controller;

import com.example.carManagement.model.dto.reports.GarageDailyAvailabilityReportDTO;
import com.example.carManagement.model.dto.request.CreateGarageDTO;
import com.example.carManagement.model.dto.request.UpdateGarageDTO;
import com.example.carManagement.model.dto.response.ResponseGarageDTO;
import com.example.carManagement.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/garages")
public class GarageController {
    @Autowired
    private GarageService garageService;

    @PostMapping
    public ResponseEntity<ResponseGarageDTO> createGarage(@RequestBody CreateGarageDTO createGarageDTO) {
        ResponseGarageDTO resp = garageService.createGarage(createGarageDTO);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseGarageDTO> updateGarage(@PathVariable("id") Long id, @RequestBody UpdateGarageDTO updateGarageDTO) {
        ResponseGarageDTO resp = garageService.updateGarage(id, updateGarageDTO);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteGarage(@PathVariable("id") Long id) {
        boolean resp = garageService.deleteGarage(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseGarageDTO> getGarageById(@PathVariable Long id){
        ResponseGarageDTO resp = garageService.getGarageById(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<ResponseGarageDTO>> getGaragesByCity(@RequestParam(value = "city", required = false)String city){
        List<ResponseGarageDTO> resp = garageService.getGaragesByCity(city);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/dailyAvailabilityReport")
    public ResponseEntity<List<GarageDailyAvailabilityReportDTO>> dailyAvailabilityReport(@RequestParam(value = "garageId") Long garageId,
                                                                                          @RequestParam(value = "startDate") LocalDate startDate,
                                                                                          @RequestParam(value = "endDate") LocalDate endDate){
        List<GarageDailyAvailabilityReportDTO> resp = garageService.getGarageDailyReport(garageId,startDate,endDate);
        return ResponseEntity.ok(resp);
    }
}
