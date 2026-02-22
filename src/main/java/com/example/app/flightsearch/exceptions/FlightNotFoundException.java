package com.example.app.flightsearch.exceptions;

public class FlightNotFoundException extends RTException{

    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
