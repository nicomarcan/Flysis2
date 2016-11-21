package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class Segment {

    private String number;
    private Airline airline;
    private String duration;

    public Segment(String number,Airline airline,String duration){
        this.airline = airline;
        this.number=number;
        this.duration=duration;
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
}
