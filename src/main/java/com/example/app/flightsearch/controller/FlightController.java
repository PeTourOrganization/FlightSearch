package com.example.app.flightsearch.controller;

import com.example.app.flightsearch.dbmodel.flightlog.RequestLog;
import com.example.app.flightsearch.providers.info.Flight;
import com.example.app.flightsearch.service.FlightService;
import com.example.app.flightsearch.providers.requests.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Tag(name = "Flight Search", description = "Operations pertaining to flight search")
    @Operation(summary = "Search flights", description = "Find flights by origin, destination and departure date")
    @GetMapping("/flightsAvailable")
    public List<Flight> getFlightsAvailable(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam LocalDateTime departureDate
    ) {
        var request = new SearchRequest(origin, destination, departureDate);
        return flightService.getFlightsAvailable(request.getOrigin(), request.getDestination(), request.getDepartureDate());
    }


    @Tag(name = "Flight Search", description = "Operations pertaining to flight search")
    @Operation(summary = "Get Cheapest Flights", description = "Find flights by origin, destination and departure date. Gets the cheapest flights")
    @GetMapping("/flightsAvailableCheapest")
    public List<Flight> getCheapestFlightsAvailable(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam LocalDateTime departureDate
    ) {
        var request = new SearchRequest(origin, destination, departureDate);
        return flightService.getCheapestFlightsAvailable(request.getOrigin(), request.getDestination(), request.getDepartureDate());
    }

    @Tag(name = "Flight Search", description = "Operations pertaining to flight search")
    @Operation(summary = "Search flights", description = "Find flights by origin, destination and departure date from single service")
    @GetMapping("/flightsAvailableSingleServiceSingle")
    public List<Flight> getFlightsAvailableSingle(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam LocalDateTime departureDate
    ) {
        var request = new SearchRequest(origin, destination, departureDate);
        return flightService.getFlightsAvailableSingle(request.getOrigin(), request.getDestination(), request.getDepartureDate());
    }


    @Tag(name = "Flight Search", description = "Operations pertaining to flight search")
    @Operation(summary = "Get Cheapest Flights", description = "Find flights by origin, destination and departure date. Gets the cheapest flights from single service")
    @GetMapping("/flightsAvailableCheapestSingle")
    public List<Flight> getCheapestFlightsAvailableSingle(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam LocalDateTime departureDate
    ) {
        var request = new SearchRequest(origin, destination, departureDate);
        return flightService.getCheapestFlightsAvailableSingle(request.getOrigin(), request.getDestination(), request.getDepartureDate());
    }

    @Tag(name = "Flight Search", description = "Operations pertaining to flight search")
    @Operation(summary = "Find Flights By Id", description = "Find flights by request id, ")
    @GetMapping("/requestLogById")
    public List<List<Flight>> getRequestLogById(@RequestParam Integer id){
        return flightService.getRequestLog(id);
    }

}
