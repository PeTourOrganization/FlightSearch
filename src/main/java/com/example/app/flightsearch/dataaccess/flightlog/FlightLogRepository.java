package com.example.app.flightsearch.dataaccess.flightlog;

import com.example.app.flightsearch.dbmodel.flightlog.FlightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightLogRepository extends JpaRepository<FlightLog, Integer> {

    @Query("SELECT f FROM FlightLog f WHERE f.responseLog.responseId = ?1")
    List<FlightLog> findFlightsByRespId(Integer responseId);
}
