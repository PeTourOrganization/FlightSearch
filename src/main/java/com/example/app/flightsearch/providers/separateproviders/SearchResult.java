package com.example.app.flightsearch.providers.separateproviders;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "SearchResult", namespace = "http://petour.com/flights")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchResult {
	@XmlElement(required = true)
	private boolean hasError = false;

	@XmlElement(name = "flightOptions")
	private List<Flight> flightOptions = new ArrayList<>();
	private String errorMessage;

	// Constructors
	public SearchResult() {
	}

	public SearchResult(boolean hasError, List<Flight> flightOptions, String errorMessage) {
		this.hasError = hasError;
		this.flightOptions = flightOptions;
		this.errorMessage = errorMessage;
	}

	// Getters and Setters
	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public List<Flight> getFlightOptions() {
		return flightOptions;
	}

	public void setFlightOptions(List<Flight> flightOptions) {
		this.flightOptions = flightOptions;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}