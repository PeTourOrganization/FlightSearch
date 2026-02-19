package com.example.app.flightsearch.controller;

import com.example.app.flightsearch.service.FlightService;
import com.example.app.flightsearch.service.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String getFlightsAvailable(
            @RequestBody SearchRequest request
    ) {
        flightService.getFlightsAvailable(request.getOrigin(), request.getDestination(), request.getDepartureDate());
        return "flights available";
    }

}
