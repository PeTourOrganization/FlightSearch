package com.example.app.flightsearch.service;

import com.example.app.flightsearch.dataaccess.flightlog.FlightLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class FlightService {

    private final RestClient restClient;
    private final FlightLogRepository flightLogRepository;

    public FlightService(RestClient restClient, FlightLogRepository flightLogRepository) {
        this.restClient = restClient;
        this.flightLogRepository = flightLogRepository;
    }

    public ResponseEntity<String> getFlightsAvailable() {
        var respA = restClient.get()
                .uri("http://localhost:8080/providerA/flightsAvailable")
                .retrieve()
                .onStatus(response -> {
                    if(!response.getStatusCode().is2xxSuccessful()){
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(response.getBody(), ResponseEntity.class).hasBody();
                    }
                    return true;
                })
                .body(new ParameterizedTypeReference<>() {});

        var respB = restClient.get()
                .uri("http://localhost:8081/providerB/flightsAvailable")
                .retrieve()
                .onStatus(response -> {
                    if(!response.getStatusCode().is2xxSuccessful()){
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(response.getBody(), ResponseEntity.class).hasBody();
                    }
                    return true;
                })
                .body(new ParameterizedTypeReference<>() {});


    }

}
