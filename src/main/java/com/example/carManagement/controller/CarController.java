package com.example.carManagement.controller;

import com.example.carManagement.model.dto.request.CreateCarDTO;
import com.example.carManagement.model.dto.request.UpdateCarDTO;
import com.example.carManagement.model.dto.response.ResponseCarDTO;
import com.example.carManagement.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<ResponseCarDTO> createCar(@RequestBody CreateCarDTO createCarDTO) {
        ResponseCarDTO resp = carService.createCar(createCarDTO);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCarDTO> updateCar(@PathVariable("id") Long id, @RequestBody UpdateCarDTO updateCarDTO) {
        ResponseCarDTO resp = carService.updateCar(id, updateCarDTO);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCar(@PathVariable("id") Long id) {
        boolean resp = carService.deleteCar(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<ResponseCarDTO>> getCarsByFilters(@RequestParam(value = "carMake", required = false) String carMake,
                                                                 @RequestParam(value = "garageId", required = false) Long garageId,
                                                                 @RequestParam(value = "fromYear", required = false) Integer fromYear,
                                                                 @RequestParam(value = "toYear", required = false) Integer toYear){
        List<ResponseCarDTO> resp = carService.getCars(carMake,garageId,fromYear,toYear);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCarDTO> getCarById(@PathVariable Long id){
        ResponseCarDTO resp = carService.getCarById(id);
        return ResponseEntity.ok(resp);
    }
}
