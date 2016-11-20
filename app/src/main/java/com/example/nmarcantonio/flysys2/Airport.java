package com.example.nmarcantonio.flysys2;

/**
 * Created by saques on 20/11/2016.
 */

public class Airport {
    private String id;
    private String description;
    private CityShort city;
    private String time_zone;
    private double longitude;
    private double latitude;

    public Airport(String id, String description, CityShort city, String time_zone, double longitude, double latitude){
        this.id = id;
        this.description = description;
        this.city = city;
        this.time_zone = time_zone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public CityShort getCity() {
        return city;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String toString(){
        return id + " " + description  + " " + city.getCountry()  + " " + city.getId()  + " " + time_zone +
                longitude + latitude;
    }
}
