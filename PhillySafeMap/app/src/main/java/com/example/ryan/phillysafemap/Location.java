package com.example.ryan.phillysafemap;

/**
 * Created by mahmudaliza on 3/12/16.
 */
public class Location {

    private double latitude;
    private double longitude;

    public Location(double latitude_, double longitude_){
        latitude = latitude_;
        longitude = longitude_;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}

//1
