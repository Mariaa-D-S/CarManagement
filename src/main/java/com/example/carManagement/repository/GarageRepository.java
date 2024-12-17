package com.example.carManagement.repository;

import com.example.carManagement.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GarageRepository extends JpaRepository<Garage, Long> {
    List<Garage> findByCityIgnoreCase(String city);

    @Query(value = "SELECT m.schedule_date AS date, " +
            "COUNT(m.id) AS requests, " +
            "(g.capacity - COUNT(m.id)) AS available_capacity " +
            "FROM maintenance m " +
            "JOIN garage g ON m.garage_id = g.id " +
            "WHERE m.garage_id = :garageId " +
            "AND m.schedule_date BETWEEN :startDate AND :endDate " +
            "GROUP BY m.schedule_date, g.capacity " +
            "ORDER BY m.schedule_date ASC", nativeQuery = true)
    List<Object[]> getGarageDailyAvailability(
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
