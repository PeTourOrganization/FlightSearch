package com.example.app.flightsearch.service;

import com.example.app.flightsearch.dataaccess.flightlog.FlightLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightService {

    private static final String PROV_A_URI = "http://localhost:8081/ws";
    private static final String PROV_B_URI = "http://localhost:8082/ws";

    private final WebServiceTemplate webServiceTemplate;
    private final FlightLogRepository flightLogRepository;

    public FlightService(WebServiceTemplate webServiceTemplate, FlightLogRepository flightLogRepository) {
        this.webServiceTemplate = webServiceTemplate;
        this.flightLogRepository = flightLogRepository;
    }

    public List<Flight> getFlightsAvailable(String origin, String destination, LocalDateTime departureDate) {
        var searchRequest = new SearchRequest(origin, destination, departureDate);
        var providerAResults = marshallSendReceive(PROV_A_URI, searchRequest);
        var providerBResults = marshallSendReceive(PROV_B_URI, searchRequest);
        var combined = Stream.concat(providerAResults.getFlightOptions().stream(), providerBResults.getFlightOptions().stream())
                .distinct()
                .toList();
        return combined.isEmpty() ?  new ArrayList<>() : combined;
    }

    public List<Flight> getCheapestFlightsAvailable(String origin, String destination, LocalDateTime departureDate) {
        return getFlightsAvailable(origin, destination, departureDate).stream()
                .sorted(Comparator.comparing(Flight::getPrice))
                .collect(Collectors.toList());
    }


    private SearchResult marshallSendReceive(String uri, SearchRequest request) {
        return (SearchResult)webServiceTemplate.marshalSendAndReceive(uri, request);
    }

}
