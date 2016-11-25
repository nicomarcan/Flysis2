package com.example.nmarcantonio.flysys2;

import android.widget.TextView;

/**
 * Created by traie_000 on 23-Nov-16.
 */

public class FlightStatusHolder {
    private String id;
    private int number;
    TextView origin;
    TextView destintation;
    TextView description;
    TextView header;

    public FlightStatusHolder(String id, int number){
        this.id = id;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

}
