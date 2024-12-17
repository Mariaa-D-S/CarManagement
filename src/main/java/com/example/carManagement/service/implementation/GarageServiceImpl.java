package com.example.carManagement.service.implementation;

import com.example.carManagement.model.Garage;
import com.example.carManagement.model.dto.reports.GarageDailyAvailabilityReportDTO;
import com.example.carManagement.model.dto.request.CreateGarageDTO;
import com.example.carManagement.model.dto.request.UpdateGarageDTO;
import com.example.carManagement.model.dto.response.ResponseGarageDTO;
import com.example.carManagement.repository.GarageRepository;
import com.example.carManagement.service.GarageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GarageServiceImpl implements GarageService {
    @Autowired
    private GarageRepository garageRepository;
    @Override
    @Transactional
    public ResponseGarageDTO createGarage(CreateGarageDTO createGarageDTO) {
        if(createGarageDTO.getName().isBlank() || createGarageDTO.getCity().isBlank() ||
            createGarageDTO.getLocation().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
        Garage garage = mapCreateDTOtoGarage(createGarageDTO);
        Garage savedGarage = garageRepository.save(garage);
        return mapGarageToResponseGarageDTO(savedGarage);
    }

    @Override
    @Transactional
    public ResponseGarageDTO updateGarage(Long id, UpdateGarageDTO updateGarageDTO) {
        Optional<Garage> existingGarage = garageRepository.findById(id);
        if (existingGarage.isPresent()) {
            Garage updatedGarage = mapUpdateToGarage(updateGarageDTO);
            updatedGarage.setId(id);
            Garage savedGarage = garageRepository.save(updatedGarage);
            return mapGarageToResponseGarageDTO(savedGarage);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Garage not found");
    }

    @Override
    @Transactional
    public boolean deleteGarage(Long id) {
        Optional<Garage> existingGarage = garageRepository.findById(id);
        if (existingGarage.isPresent()) {
            garageRepository.deleteById(id);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Garage not found");
    }

    @Override
    public ResponseGarageDTO getGarageById(Long id) {
        Optional<Garage> garage = garageRepository.findById(id);
        if (garage.isPresent()) {
            return mapGarageToResponseGarageDTO(garage.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Garage not found");
    }

    @Override
    public List<ResponseGarageDTO> getGaragesByCity(String city) {
        List<Garage> garages = garageRepository.findByCityIgnoreCase(city);
        if(garages.isEmpty()){
            garages = garageRepository.findAll();
        }
        return garages.stream()
                .map(this::mapGarageToResponseGarageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GarageDailyAvailabilityReportDTO> getGarageDailyReport(Long id, LocalDate startDate, LocalDate endDate) {
        List<Object[]> rawResults = garageRepository.getGarageDailyAvailability(id, startDate, endDate);
        return rawResults.stream()
                .map(row -> {
                        try {
                            Date sqlDate = (Date) row[0];
                            LocalDate date = LocalDate.parse(sqlDate.toString());

                            Integer requests = ((Number) row[1]).intValue();
                            Integer availableCapacity = ((Number) row[2]).intValue();

                            return new GarageDailyAvailabilityReportDTO(date, requests, availableCapacity);
                        } catch (Exception e) {
                            System.err.println("Error mapping row: " + Arrays.toString(row));
                            e.printStackTrace();
                            return null;
                        }

                })
                .collect(Collectors.toList());
    }

    public ResponseGarageDTO mapGarageToResponseGarageDTO(Garage garage) {
        if (garage == null) {
            return null;
        }
        ResponseGarageDTO dto = new ResponseGarageDTO();
        dto.setId(garage.getId());
        dto.setName(garage.getName());
        dto.setLocation(garage.getLocation());
        dto.setCity(garage.getCity());
        dto.setCapacity(garage.getCapacity());
        return dto;
    }

    public Garage mapCreateDTOtoGarage(CreateGarageDTO createGarageDTO) {
        if (createGarageDTO == null) {
            return null;
        }
        Garage garage = new Garage();
        garage.setName(createGarageDTO.getName());
        garage.setLocation(createGarageDTO.getLocation());
        garage.setCity(createGarageDTO.getCity());
        garage.setCapacity(createGarageDTO.getCapacity());
        return garage;
    }

    public Garage mapUpdateToGarage(UpdateGarageDTO updateGarageDTO) {
        if (updateGarageDTO == null) {
            return null;
        }
        Garage garage = new Garage();
        garage.setName(updateGarageDTO.getName());
        garage.setLocation(updateGarageDTO.getLocation());
        garage.setCity(updateGarageDTO.getCity());
        garage.setCapacity(updateGarageDTO.getCapacity());
        return garage;
    }
}
