package com.example.nmarcantonio.flysys2;

import android.text.Html;

import java.io.Serializable;

public class City implements Serializable {

    private String id;
    private String name;
    private Double latitude;
    private Double longitude;

    public City(String id, String name,Double latitude,Double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Html.fromHtml(name).toString();
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getId(), this.getName());
    }
}
