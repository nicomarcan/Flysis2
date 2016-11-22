package com.example.nmarcantonio.flysys2;

/**
 * Created by saques on 22/11/2016.
 */

public class CityInfo_2 {
    private String id;
    private CountryInfo country;
    private String name;
    private double longitude;
    private double latitude;
    private boolean has_airport;

    public CityInfo_2(String id,CountryInfo country,String name,double longitude,double latitude,boolean has_airport){
        this.id = id;
        this.country = country;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.has_airport = has_airport;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public CountryInfo getCountry() {
        return country;
    }

    public boolean isHas_airport() {
        return has_airport;
    }

    public String getName() {
        return name;
    }
}
