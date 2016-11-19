package com.example.nmarcantonio.flysys2;

/**
 * Created by traie_000 on 17/11/2016.
 */

public class AirportInfo {
    String id;
    String description;
    String time_zone;
    String latitiude;
    String longitude;
    City city;
    String terminal;
    String gate;

    public AirportInfo(String id, String description, String time_zone, String latitiude, String longitude, City city, String terminal, String gate) {
        this.id = id;
        this.description = description;
        this.time_zone = time_zone;
        this.latitiude = latitiude;
        this.longitude = longitude;
        this.city = city;
        this.terminal = terminal;
        this.gate = gate;
    }
}
