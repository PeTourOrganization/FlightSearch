package com.example.app.flightsearch.dbmodel.flightlog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "flight_logs", schema = "public")
public class FlightLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Long flightId;

    @Column(name = "flight_no")
    private String flightNo;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "departure_datetime")
    private LocalDateTime departureDateTime;

    @Column(name = "arrival_datetime")
    private LocalDateTime arrivalDateTime;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resp_id", nullable = false)
    private ResponseLog responseLog;
}
