package com.example.nmarcantonio.flysys2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Usuario on 19/11/2016.
 */
public class Flight {


    private OutBoundRoute outbound_routes[];
    private Price price;

    public Flight(OutBoundRoute[] outbound_routes, Price price) {
        this.outbound_routes = outbound_routes;
        this.price = price;
    }


    public String getNumber() {
        return outbound_routes[0].getSegments()[0].getNumber();

    }

    public String getdstAir(){
        return outbound_routes[0].getSegments()[0].getArrival().getAirport().getId();
    }

    public String getsrcAir(){
        return outbound_routes[0].getSegments()[0].getDeparture().getAirport().getId();
    }

    public String getPrice(){
        return price.getTotal().getTotal();
    }

    public String getId(){
        return outbound_routes[0].getSegments()[0].getAirline().getId();
    }


    public OutBoundRoute[] getOutbound_routes() {
        return outbound_routes;
    }


}
