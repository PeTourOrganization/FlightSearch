package com.example.app.flightsearch.dataaccess.flightlog;

import com.example.app.flightsearch.dbmodel.flightlog.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Integer> {

    @Query("SELECT r.responseId FROM ResponseLog r WHERE r.requestLog.requestId = ?1")
    List<Integer> findAllByRequestId(Integer requestId);

}
