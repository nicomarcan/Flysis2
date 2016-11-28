package com.example.nmarcantonio.flysys2;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class FlightStatusHolder {
    private String id;
    private int number;
    private AirlineInfo airlineInfo;
    private AirportInfo departure;
    private AirportInfo arrival;
    private FlightStatus status;
    TextView origin;
    TextView destintation;
    TextView description;
    TextView header;
    TextView state;
    ImageView logo;

    public FlightStatusHolder(String id, int number, AirlineInfo airlineInfo, AirportInfo departure, AirportInfo arrival, FlightStatus status){
        this.id = id;
        this.number = number;
        this.airlineInfo = airlineInfo;
        this.departure = departure;
        this.arrival = arrival;
        this.status = status;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public AirlineInfo getAirlineInfo() {
        return airlineInfo;
    }

    public AirportInfo getArrival() {
        return arrival;
    }

    public AirportInfo getDeparture() {
        return departure;
    }
}
