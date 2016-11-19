package com.example.nmarcantonio.flysys2;

import com.google.gson.annotations.Expose;

/**
 * Created by Usuario on 19/11/2016.
 */
//TODO agregarle variables en caso de necesitar
public class Flight {
    private int number;
    private Airline airline;

    public Flight(Airline airline,int number){
        this.airline = airline;
        this.number = number;
    }

    public Airline getAirline() {
        return airline;
    }

    public Integer getNumber() {
        return number;
    }


}
