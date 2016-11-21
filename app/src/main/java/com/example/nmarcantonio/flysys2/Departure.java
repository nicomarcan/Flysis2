package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class Departure {
    private String date;
    private Airport airport;

    public Departure(String date, Airport airport) {
        this.date = date;
        this.airport = airport;
    }

    public String getDate() {
        return date;
    }

    public Airport getAirport() {
        return airport;
    }
}
