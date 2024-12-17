package com.example.carManagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String carMake;
    @NotNull
    private String model;
    private int productionYear;
    private String licensePlate;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "cars_garages",
            joinColumns = @JoinColumn(name ="car_id"),
            inverseJoinColumns = @JoinColumn(name ="garage_id")
    )
    private List<Garage> garages;

    public Car(Long id, String carMake, String model, int productionYear, String licensePlate, List<Garage> garages) {
        this.id = id;
        this.carMake = carMake;
        this.model = model;
        this.productionYear = productionYear;
        this.licensePlate = licensePlate;
        this.garages = garages;
    }

    public Car() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public List<Garage> getGarages() {
        return garages;
    }

    public void setGarages(List<Garage> garages) {
        this.garages = garages;
    }
}
