package com.example.app.flightsearch.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class RTException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public RTException(String message) {
        super(message);
    }

    public RTException(String message, Throwable cause){
        super(message, cause);
    }
}
