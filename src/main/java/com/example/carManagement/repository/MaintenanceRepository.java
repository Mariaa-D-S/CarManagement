package com.example.carManagement.repository;

import com.example.carManagement.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance,Long> {
    @Query("SELECT MIN(m.scheduleDate) FROM Maintenance m")
    LocalDate findEarliestDate();

    @Query("SELECT MAX(m.scheduleDate) FROM Maintenance m")
    LocalDate findLatestDate();

    @Query(value = "SELECT * FROM maintenance m " +
            "WHERE (:carId IS NULL OR m.car_id = :carId) " +
            "AND (:garageId IS NULL OR m.garage_id = :garageId) " +
            "AND m.schedule_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Maintenance> findMaintenanceByFilters(
            @Param("carId") Long carId,
            @Param("garageId") Long garageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query(value = "SELECT m.schedule_date AS date, COUNT(m.id) AS requests " +
            "FROM maintenance m " +
            "WHERE m.garage_id = :garageId " +
            "AND m.schedule_date BETWEEN :startMonth AND :endMonth " +
            "GROUP BY m.schedule_date " +
            "ORDER BY m.schedule_date ASC", nativeQuery = true)
    List<Object[]> getMonthlyRequests(
            @Param("garageId") Long garageId,
            @Param("startMonth") LocalDate startDate,
            @Param("endMonth") LocalDate endDate);
}
