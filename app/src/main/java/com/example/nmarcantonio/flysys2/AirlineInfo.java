package com.example.nmarcantonio.flysys2;

import java.io.Serializable;

/**
 * Created by traie_000 on 17/11/2016.
 */

public class AirlineInfo implements Serializable{
    String id;
    String name;
    String logo;

    public AirlineInfo(String id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public AirlineInfo() {
    }
}
