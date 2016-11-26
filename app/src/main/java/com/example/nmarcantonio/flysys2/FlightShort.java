package com.example.nmarcantonio.flysys2;

/**
 * Created by saques on 25/11/2016.
 */

public class FlightShort {
    private String id;
    private int number;
    private AirlineInfo airlineInfo;
    private AirportInfo departure;
    private AirportInfo arrival;
    private FlightStatus status;

    public FlightShort(String id, int number, AirlineInfo airlineInfo, AirportInfo departure, AirportInfo arrival, FlightStatus status){
        this.id = id;
        this.number = number;
        this.airlineInfo = airlineInfo;
        this.departure = departure;
        this.arrival = arrival;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public AirlineInfo getAirlineInfo() {
        return airlineInfo;
    }

    public AirportInfo getDeparture() {
        return departure;
    }

    public AirportInfo getArrival() {
        return arrival;
    }

    public FlightStatus getStatus() {
        return status;
    }
}
