package com.example.nmarcantonio.flysys2;

import java.io.Serializable;

/**
 * Created by traie_000 on 17/11/2016.
 */

public class CountryInfo implements Serializable{
    String id;
    String name;

    public CountryInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
