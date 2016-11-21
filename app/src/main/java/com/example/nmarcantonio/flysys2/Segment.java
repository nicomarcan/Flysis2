package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class Segment {

    private String number;
    private Airline airline;
    private String duration;
    private Arrival arrival;
    private Departure departure;

    public Segment(String number, Airline airline, String duration, Arrival arrival, Departure departure) {
        this.number = number;
        this.airline = airline;
        this.duration = duration;
        this.arrival = arrival;
        this.departure = departure;
    }


    public String getNumber() {
        return number;
    }

    public String getDuration() {
        return duration;
    }

    public Airline getAirline() {
        return airline;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public Departure getDeparture() {
        return departure;
    }
}
