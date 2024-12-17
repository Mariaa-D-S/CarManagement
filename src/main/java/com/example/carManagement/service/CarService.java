package com.example.carManagement.service;

import com.example.carManagement.model.dto.request.CreateCarDTO;
import com.example.carManagement.model.dto.request.UpdateCarDTO;
import com.example.carManagement.model.dto.response.ResponseCarDTO;

import java.util.List;

public interface CarService {
    ResponseCarDTO createCar(CreateCarDTO createCarDTO);
    ResponseCarDTO updateCar(Long id, UpdateCarDTO updateCarDTO);
    boolean deleteCar(Long id);
    ResponseCarDTO getCarById(Long id);
    List<ResponseCarDTO> getCars(String carMake, Long garageId, Integer fromYear, Integer toYear);
}
