package com.example.nmarcantonio.flysys2;

/**
 * Created by saques on 20/11/2016.
 */

public class CityShort {
    private String id;
    private CountryShort country;

    public CityShort(String id,CountryShort country){
        this.id = id;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getCountry(){
        return country.getId();
    }
}
