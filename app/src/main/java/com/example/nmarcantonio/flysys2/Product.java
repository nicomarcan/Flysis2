package com.example.nmarcantonio.flysys2;

public class Product {
    private int id;
    private String name;
    private Double price;
    private Double latitude;
    private Double longitude;

    public Product(int id, String name, Double price,Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return this.price;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
