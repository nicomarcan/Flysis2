package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/23/2016.
 */

public class Currency {
    private String id;
    private Double ratio;
    public Currency(String id, Double ratio) {
        this.id = id;
        this.ratio = ratio;
    }

    public String getId() {
        return id;
    }

    public Double getRatio() {
        return ratio;
    }
}
