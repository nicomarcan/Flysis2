package com.example.nmarcantonio.flysys2;

import java.io.Serializable;

/**
 * Created by traie_000 on 17/11/2016.
 */

public class CityInfo implements Serializable{
    String id;
    String name;
    Double latitude;
    Double longitude;
    CountryInfo country;

    public CityInfo(String id, String name, Double latitude, Double longitude, CountryInfo country) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }
}
