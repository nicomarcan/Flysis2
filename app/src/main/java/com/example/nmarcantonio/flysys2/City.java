package com.example.nmarcantonio.flysys2;

import android.text.Html;

public class City {

    private String id;
    private String name;

    public City(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Html.fromHtml(name).toString();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getId(), this.getName());
    }
}
