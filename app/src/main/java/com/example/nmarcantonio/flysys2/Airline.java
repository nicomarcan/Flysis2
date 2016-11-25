package com.example.nmarcantonio.flysys2;

/**
 * Created by Usuario on 19/11/2016.
 */

public class Airline {


    private String id;
    private String name;
    public float rating;

    public Airline(String id,String name,float rating){
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }
}
