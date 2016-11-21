package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class OfferInfo {

    private String id;
    private String number;


    public OfferInfo(String id, String number) {
        this.id = id;
        this.number = number;
    }


    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}

