package com.example.app.flightsearch.dbmodel.flightlog;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "flightlog_requests", schema = "public")
public class FlightLog {
    @Id
    @Column(name = "req_id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_date", columnDefinition = "date")
    private LocalDateTime startDate;

    @Column(name = "end_date", columnDefinition = "date")
    private LocalDateTime endDate;

    @Column(name = "context")
    private String context;
}
