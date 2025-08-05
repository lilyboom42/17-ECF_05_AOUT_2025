package org.example.environement.repository;

import org.example.environement.entity.TravelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelLogRepository extends JpaRepository<TravelLog, Long> {

    List<TravelLog> findByObservation_Id(long id);

    @Query("SELECT t FROM TravelLog t WHERE t.observation.observerName = :user AND t.observation.observationDate BETWEEN :startDate AND :endDate")
    List<TravelLog> findByObserverNameAndDateBetween(@Param("user") String user,
                                                     @Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);

    List<TravelLog> findTravellogByUserForLastMonth(String user, LocalDate date);
}
