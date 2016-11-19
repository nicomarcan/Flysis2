package com.example.nmarcantonio.flysys2;

/**
 * Created by Usuario on 19/11/2016.
 */

public class Airline {


    private String id;
    private String name;

    public Airline(String id,String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
