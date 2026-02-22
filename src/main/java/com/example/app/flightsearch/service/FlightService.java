package com.example.app.flightsearch.service;

import com.example.app.flightsearch.dataaccess.flightlog.FlightLogRepository;
import com.example.app.flightsearch.dataaccess.flightlog.RequestLogRepository;
import com.example.app.flightsearch.dataaccess.flightlog.ResponseLogRepository;
import com.example.app.flightsearch.dbmodel.flightlog.FlightLog;
import com.example.app.flightsearch.dbmodel.flightlog.RequestLog;
import com.example.app.flightsearch.dbmodel.flightlog.ResponseLog;
import com.example.app.flightsearch.providers.info.Flight;
import com.example.app.flightsearch.providers.requests.SearchRequestA;
import com.example.app.flightsearch.providers.requests.SearchRequestB;
import com.example.app.flightsearch.providers.requests.SearchRequest;
import com.example.app.flightsearch.providers.response.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightService {
    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    private static final String PROV_A_URI = "http://localhost:8081/ws";
    private static final String PROV_B_URI = "http://localhost:8082/ws";
    private static final String SINGLE_URI = "http://localhost:8083/ws";

    private final WebServiceTemplate webServiceTemplate;
    private final FlightLogRepository flightLogRepository;
    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;

    public FlightService(WebServiceTemplate webServiceTemplate,
                         FlightLogRepository flightLogRepository,
                         RequestLogRepository requestLogRepository,
                         ResponseLogRepository responseLogRepository
    ) {
        this.webServiceTemplate = webServiceTemplate;
        this.flightLogRepository = flightLogRepository;
        this.requestLogRepository = requestLogRepository;
        this.responseLogRepository = responseLogRepository;
    }

    public List<Flight> getFlightsAvailable(String origin, String destination, LocalDateTime departureDate) {
        var searchRequest = new SearchRequest(origin, destination, departureDate);
        SearchResult providerAResults;
        SearchResult providerBResults;
        try {
            providerAResults = marshallSendReceive(PROV_A_URI, searchRequest);
        }catch (Exception e){
            logger.error("Error occurred while calling provider A", e);
            providerAResults = new SearchResult(true, new ArrayList<>(), "An error occurred on Provider A while searching for flights: " + e.getMessage());
        }

        try {
            providerBResults = marshallSendReceive(PROV_B_URI, searchRequest);
        } catch (Exception e) {
            logger.error("Error occurred while calling provider B", e);
            providerBResults = new SearchResult(true, new ArrayList<>(), "An error occurred on Provider B while searching for flights: " + e.getMessage());
        }
        var combined = Stream.concat(providerAResults.getFlightOptions().stream(), providerBResults.getFlightOptions().stream())
                .distinct()
                .toList();

        saveSeparateProvidersLogs(searchRequest, providerAResults, providerBResults);
        return combined.isEmpty() ?  new ArrayList<>() : combined;
    }

    public List<Flight> getCheapestFlightsAvailable(String origin, String destination, LocalDateTime departureDate) {
        return getFlightsAvailable(origin, destination, departureDate).stream()
                .sorted(Comparator.comparing(Flight::getPrice))
                .collect(Collectors.toList());
    }

    public List<Flight> getFlightsAvailableSingle(String origin, String destination, LocalDateTime departureDate) {
        var searchRequestA = new SearchRequestA(origin, destination, departureDate);
        var respA = (SearchResult)webServiceTemplate.marshalSendAndReceive(SINGLE_URI, searchRequestA);

        var searchRequestB = new SearchRequestB(origin, destination, departureDate);
        var respB = (SearchResult)webServiceTemplate.marshalSendAndReceive(SINGLE_URI, searchRequestB);

        var combined = Stream.concat(respA.getFlightOptions().stream(), respB.getFlightOptions().stream())
                .distinct()
                .toList();

        saveSeparateProvidersLogs(searchRequestA, searchRequestB, respA, respB);
        return combined.isEmpty() ?  new ArrayList<>() : combined;
    }


    public List<Flight> getCheapestFlightsAvailableSingle(String origin, String destination, LocalDateTime departureDate) {
        return getFlightsAvailableSingle(origin, destination, departureDate).stream()
                .sorted(Comparator.comparing(Flight::getPrice))
                .collect(Collectors.toList());
    }

    public List<List<Flight>> getRequestLog(Integer id){
        var responseIds = requestLogRepository.findAllByRequestId(id);
        var flightLogs = responseIds.stream()
                .map(flightLogRepository::findFlightsByRespId)
                .toList();

        return flightLogs.stream()
                .map(flights -> flights.stream()
                        .map(flightLog -> {
                            var flight = new Flight();
                            flight.setFlightNo(flightLog.getFlightNo());
                            flight.setOrigin(flightLog.getOrigin());
                            flight.setDestination(flightLog.getDestination());
                            flight.setDepartureDateTime(flightLog.getDepartureDateTime());
                            flight.setArrivalDateTime(flightLog.getArrivalDateTime());
                            flight.setPrice(flightLog.getPrice());
                            return flight;
                        }).toList())
                .toList();
    }


    private SearchResult marshallSendReceive(String uri, SearchRequest request) {
        return (SearchResult)webServiceTemplate.marshalSendAndReceive(uri, request);
    }

    private static List<FlightLog> getListOfFlights(SearchResult searchResult, ResponseLog responseLog){
        return searchResult
                .getFlightOptions()
                .stream()
                .map(flight -> {
                    FlightLog flightLog = new FlightLog();
                    flightLog.setArrivalDateTime(flight.getArrivalDateTime());
                    flightLog.setDepartureDateTime(flight.getDepartureDateTime());
                    flightLog.setFlightNo(flight.getFlightNo());
                    flightLog.setDestination(flight.getDestination());
                    flightLog.setOrigin(flight.getOrigin());
                    flightLog.setPrice(flight.getPrice());
                    flightLog.setResponseLog(responseLog);
                    return flightLog;
                })
                .toList();
    }

    private void saveSeparateProvidersLogs(SearchRequest searchRequest, SearchResult providerAResults, SearchResult providerBResults) {
        if(!providerAResults.isHasError() && !providerBResults.isHasError()) {
            var requestLog = new RequestLog();
            requestLog.setOrigin(searchRequest.getOrigin());
            requestLog.setDestination(searchRequest.getDestination());
            requestLog.setDepartureDate(searchRequest.getDepartureDate());

            var responseLogA = new ResponseLog();

            var flightsA = getListOfFlights(providerAResults, responseLogA);
            responseLogA.setRequestLog(requestLog);
            responseLogA.setFlightOptions(flightsA);

            var responseLogB = new ResponseLog();
            var flightsB = getListOfFlights(providerBResults, responseLogB);
            responseLogB.setRequestLog(requestLog);
            responseLogB.setFlightOptions(flightsB);

            var responseLogs = Arrays.asList(responseLogA, responseLogB);
            requestLog.setResponseLogs(responseLogs);

            requestLogRepository.save(requestLog);
        }
    }


    private void saveSeparateProvidersLogs(SearchRequestA searchRequestA, SearchRequestB searchRequestB, SearchResult providerAResults, SearchResult providerBResults) {
        if(!providerAResults.isHasError() && !providerBResults.isHasError()) {
            var requestLog = new RequestLog();
            requestLog.setOrigin(searchRequestA.getOrigin());
            requestLog.setDestination(searchRequestA.getDestination());
            requestLog.setDepartureDate(searchRequestA.getDepartureDate());

            var mapEntryA = prepareResponseLog(requestLog, providerAResults);

            requestLog.setOrigin(searchRequestB.getOrigin());
            requestLog.setDestination(searchRequestB.getDestination());
            requestLog.setDepartureDate(searchRequestB.getDepartureDate());

            var mapEntryB = prepareResponseLog(requestLog, providerBResults);

            var responseLogs = Arrays.asList(mapEntryA.getValue(), mapEntryB.getValue());
            requestLog.setResponseLogs(responseLogs);

            requestLogRepository.save(requestLog);
        }
    }

    private static Map.Entry<List<FlightLog>, ResponseLog> prepareResponseLog(RequestLog requestLog, SearchResult searchResult) {
        var responseLog = new ResponseLog();
        List<FlightLog> flights = getListOfFlights(searchResult, responseLog);
        responseLog.setRequestLog(requestLog);
        responseLog.setFlightOptions(flights);
        return new AbstractMap.SimpleEntry<>(flights, responseLog);
    }
}
