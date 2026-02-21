package com.example.app.flightsearch.dbmodel.flightlog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "responses_logs", schema = "public")
public class ResponseLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "resp_id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer responseId;

    @Column(name = "has_error")
    private Boolean hasError;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "responseLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightLog> flightOptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_id", nullable = false)
    private RequestLog requestLog;
}
