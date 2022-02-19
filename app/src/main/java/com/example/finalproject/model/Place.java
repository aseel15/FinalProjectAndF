package com.example.finalproject.model;

import java.io.Serializable;

public class Place implements Serializable {

    private int placeId;
    private String name;
    private String desc;
    private double price;
    private String image;

    public Place(int placeId, String name, String desc, double price, String image) {
        this.placeId = placeId;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.image = image;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
