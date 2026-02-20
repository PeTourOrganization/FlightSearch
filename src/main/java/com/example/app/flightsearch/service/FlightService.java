package com.example.app.flightsearch.service;

import com.example.app.flightsearch.dataaccess.flightlog.FlightLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.time.LocalDateTime;

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

    public SearchResult getFlightsAvailable(String origin, String destination, LocalDateTime departureDate) {
        var searchRequest = new SearchRequest(origin, destination, departureDate);
        var providerAResults = marshallSendReceive(PROV_A_URI, searchRequest);
        var providerBResults = marshallSendReceive(PROV_B_URI, searchRequest);
        return new SearchResult();
    }


    private SearchResult marshallSendReceive(String uri, SearchRequest request) {
        return (SearchResult)webServiceTemplate.marshalSendAndReceive(uri, request);
    }

//    public List<Flight> getFlightsAvailable(String origin, String destination, LocalDateTime departureDate) {
//
//        var respA = restClient
//                .get()
//                .uri("http://localhost:8081/providerA/flightsAvailable?origin=" + origin + "&destination=" + destination + "&departureDate=" + departureDate)
//                .retrieve()
//                .onStatus(response -> {
//                    if(!response.getStatusCode().is2xxSuccessful()){
//                        ObjectMapper mapper = new ObjectMapper();
//                        return mapper.readValue(response.getBody(), ResponseEntity.class).hasBody();
//                    }
//                    return true;
//                })
//                .body(new ParameterizedTypeReference<List<Flight>>() {});
//
//        var respB = restClient.get()
//                .uri("http://localhost:8082/providerB/flightsAvailable")
//                .retrieve()
//                .onStatus(response -> {
//                    if(!response.getStatusCode().is2xxSuccessful()){
//                        ObjectMapper mapper = new ObjectMapper();
//                        return mapper.readValue(response.getBody(), ResponseEntity.class).hasBody();
//                    }
//                    return true;
//                })
//                .body(new ParameterizedTypeReference<List<Flight>>() {});
//
//        return new ArrayList<>();
//    }

}
