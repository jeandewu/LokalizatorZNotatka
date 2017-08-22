package com.example.rent.taskone;


import android.location.Location;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LocationItem extends RealmObject {
    @PrimaryKey
    private int id;

    private double Longlitude;
    private double Latitude;
    private String note;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLonglitude() {
        return Longlitude;
    }

    public void setLonglitude(double longlitude) {
        Longlitude = longlitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
