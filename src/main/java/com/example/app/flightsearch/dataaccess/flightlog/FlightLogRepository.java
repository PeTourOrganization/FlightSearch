package com.example.app.flightsearch.dataaccess.flightlog;

import com.example.app.flightsearch.dbmodel.flightlog.FlightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightLogRepository extends JpaRepository<FlightLog, Integer> {
}
