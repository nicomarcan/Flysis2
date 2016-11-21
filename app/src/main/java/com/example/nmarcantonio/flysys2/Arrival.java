package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class Arrival {
    private String date;
    private Airport airport;

    public Arrival(String date, Airport airport) {
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
