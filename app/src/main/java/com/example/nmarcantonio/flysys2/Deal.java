package com.example.nmarcantonio.flysys2;

import android.text.Html;

public class Deal {

    private City city;
    private String price;

    public Deal(City city,String price) {
        this.city = city;
        this.price = price;
    }

    public String getPrice() {
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
