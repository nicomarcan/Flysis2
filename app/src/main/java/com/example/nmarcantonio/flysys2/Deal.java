package com.example.nmarcantonio.flysys2;

import android.text.Html;

import java.io.Serializable;

public class Deal implements Serializable {

    private City city;
    private Double price;

    public Deal(City city,Double price) {
        this.city = city;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

   public String getName() {
        return Html.fromHtml(city.getName()).toString();
   }

    public String getId(){return city.getId();}

    public Double getLatitude(){
        return city.getLatitude();
    }

    public Double getLongitude(){
        return city.getLongitude();
    }


    @Override
    public String toString() {
        return String.format("%s - %d", this.getName(), this.getPrice());
    }
}
