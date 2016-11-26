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

    public FlightShort(String id, int number, AirlineInfo airlineInfo, AirportInfo departure, AirportInfo arrival){
        this.id = id;
        this.number = number;
        this.airlineInfo = airlineInfo;
        this.departure = departure;
        this.arrival = arrival;
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
}
