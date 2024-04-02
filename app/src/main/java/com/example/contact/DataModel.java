package com.example.contact;

import android.graphics.Bitmap;

public class DataModel {

    Integer id;
    String name;
    String number;
    Bitmap imgUrl;

    public DataModel(Integer id, String name, String number, Bitmap imgUrl) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(Bitmap imgUrl) {
        this.imgUrl = imgUrl;
    }
}
