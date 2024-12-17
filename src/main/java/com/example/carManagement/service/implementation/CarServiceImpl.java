package com.example.carManagement.service.implementation;

import com.example.carManagement.model.Car;
import com.example.carManagement.model.Garage;
import com.example.carManagement.model.dto.request.CreateCarDTO;
import com.example.carManagement.model.dto.request.UpdateCarDTO;
import com.example.carManagement.model.dto.response.ResponseCarDTO;
import com.example.carManagement.repository.CarRepository;
import com.example.carManagement.repository.GarageRepository;
import com.example.carManagement.service.CarService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private GarageRepository garageRepository;
    @Override
    @Transactional
    public ResponseCarDTO createCar(CreateCarDTO createCarDTO) {
        if(createCarDTO.getMake().isBlank() || createCarDTO.getModel().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data");
        }
        Car car = mapCreateDTOtoCar(createCarDTO);
        Car savedCar = carRepository.save(car);
        return mapCarToResponseCarDTO(savedCar);
    }

    @Override
    @Transactional
    public ResponseCarDTO updateCar(Long id, UpdateCarDTO updateCarDTO) {
        Optional<Car> existingCar = carRepository.findById(id);
        if (existingCar.isPresent()) {
            Car updatedCar = mapUpdateToCar(updateCarDTO);
            updatedCar.setId(id);
            Car savedCar = carRepository.save(updatedCar);
            return mapCarToResponseCarDTO(savedCar);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
    }

    @Override
    @Transactional
    public boolean deleteCar(Long id) {
        Optional<Car> existingCar = carRepository.findById(id);
        if (existingCar.isPresent()) {
            carRepository.deleteById(id);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
    }

    @Override
    public ResponseCarDTO getCarById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            return mapCarToResponseCarDTO(car.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
    }

    @Override
    public List<ResponseCarDTO> getCars(String carMake, Long garageId, Integer fromYear, Integer toYear) {
        List<Car> cars = carRepository.findCarsByFilters(carMake,garageId,fromYear,toYear);

        if(cars.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found");
        }
        return cars.stream()
                .map(this::mapCarToResponseCarDTO)
                .collect(Collectors.toList());
    }

    private ResponseCarDTO mapCarToResponseCarDTO(Car car) {
        if (car == null) {
            return null;
        }
        ResponseCarDTO dto = new ResponseCarDTO();
        dto.setId(car.getId());
        dto.setMake(car.getCarMake());
        dto.setModel(car.getModel());
        dto.setProductionYear(car.getProductionYear());
        dto.setLicensePlate(car.getLicensePlate());
        List<Garage> garages = car.getGarages();
        dto.setGarages(garages);
        return dto;
    }

    private Car mapCreateDTOtoCar(CreateCarDTO createCarDTO) {
        if (createCarDTO == null) {
            return null;
        }
        Car car = new Car();
        car.setCarMake(createCarDTO.getMake());
        car.setModel(createCarDTO.getModel());
        car.setProductionYear(createCarDTO.getProductionYear());
        car.setLicensePlate(createCarDTO.getLicensePlate());

        List<Garage> garages = garageRepository.findAllById(createCarDTO.getGarageIds());
        car.setGarages(garages);

        return car;
    }

    private Car mapUpdateToCar(UpdateCarDTO updateCarDTO) {
        if (updateCarDTO == null) {
            return null;
        }
        Car car = new Car();
        car.setCarMake(updateCarDTO.getMake());
        car.setModel(updateCarDTO.getModel());
        car.setProductionYear(updateCarDTO.getProductionYear());
        car.setLicensePlate(updateCarDTO.getLicensePlate());

        List<Garage> garages = garageRepository.findAllById(updateCarDTO.getGarageIds());
        car.setGarages(garages);

        return car;
    }
}
