package com.example.carManagement.repository;

import com.example.carManagement.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query(value = "SELECT DISTINCT c.* FROM car c " +
            "JOIN cars_garages cg ON c.id = cg.car_id " +
            "JOIN garage g ON cg.garage_id = g.id " +
            "WHERE (:carMake IS NULL OR LOWER(c.car_make) = LOWER(:carMake)) " +
            "AND (:garageId IS NULL OR g.id = :garageId) " +
            "AND (:fromYear IS NULL OR c.production_year >= :fromYear) " +
            "AND (:toYear IS NULL OR c.production_year <= :toYear)", nativeQuery = true)
    List<Car> findCarsByFilters(
            @Param("carMake") String carMake,
            @Param("garageId") Long garageId,
            @Param("fromYear") Integer fromYear,
            @Param("toYear") Integer toYear);

}
