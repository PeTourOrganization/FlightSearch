package com.example.app.flightsearch.dbmodel.flightlog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "requests_logs", schema = "public")
public class RequestLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "req_id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestId;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "departure_date", columnDefinition = "date")
    private LocalDateTime departureDate;

    @OneToMany(mappedBy = "requestLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResponseLog> responseLogs = new ArrayList<>();


}

