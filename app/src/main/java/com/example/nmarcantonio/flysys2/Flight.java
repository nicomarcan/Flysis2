package com.example.nmarcantonio.flysys2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Usuario on 19/11/2016.
 */
public class Flight {


    private OutBoundRoute outbound_routes[];


    public Flight(OutBoundRoute outbound_routes[]) {
        this.outbound_routes = outbound_routes;
    }

    public String getNumber() {
        return outbound_routes[0].getSegments()[0].getNumber();

    }

    public String getId(){
        return outbound_routes[0].getSegments()[0].getAirline().getId();
    }


}
