package com.example.nmarcantonio.flysys2;

/**
 * Created by traie_000 on 17/11/2016.
 */

public class CityInfo {
    String id;
    String name;
    String latitude;
    String longitude;
    CountryInfo country;

    public CityInfo(String id, String name, String latitude, String longitude, CountryInfo country) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }
}
