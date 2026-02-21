package com.example.app.flightsearch.providers.info;

import com.example.app.flightsearch.util.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Flight", propOrder = {
		"flightNo", "origin", "destination", "departureDateTime", "arrivalDateTime", "price"
})
public class Flight {

	@XmlElement(namespace = "http://petour.com/flights", required = true)
	private String flightNo;

	@XmlElement(namespace = "http://petour.com/flights", required = true)
	private String origin;

	@XmlElement(namespace = "http://petour.com/flights", required = true)
	private String destination;

	@XmlElement(namespace = "http://petour.com/flights", required = true)
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime departureDateTime;

	@XmlElement(namespace = "http://petour.com/flights", required = true)
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime arrivalDateTime;

	@XmlElement(namespace = "http://petour.com/flights", required = true)
	private BigDecimal price;

	public Flight() {}
	
	public Flight(String flightNo, String origin, String destination, LocalDateTime departureDateTime,
				  LocalDateTime arrivalDateTime, BigDecimal price) {
		super();
		this.flightNo = flightNo;
		this.origin = origin;
		this.destination = destination;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
		this.price = price;
	}
	public String getFlightNo() {
		return flightNo;
	}
	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public LocalDateTime getDepartureDateTime() {
		return departureDateTime;
	}
	public void setDepartureDateTime(LocalDateTime departureDateTime) {
		this.departureDateTime = departureDateTime;
	}
	public LocalDateTime getArrivalDateTime() {
		return arrivalDateTime;
	}
	public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
