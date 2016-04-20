package com.example.ryan.phillysafemap;

public class Crime  {
    String ucr;
    String location;
    String description;
    String latitude;  // y
    String time;
    String date;
    String longitude; // x


    // A no-args constructor is required for deserialization
    public Crime() {

    }

    public Crime(String date, String time, String location, String description, String longitude, String latitude, String UCR)
    {
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ucr = UCR;
    }

    // accessor methods
    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    public String getLocation()
    {
        return location;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUCR() {
        return ucr;
    }

    public void setUCR(String UCR) {
        this.ucr = UCR;
    }


    // toString
    public String toString()
    {
        return   " Date: " + date + "\n"
                +" Time: " + time + "\n"
                +" Description: " + description + "\n"
                +" Location: " + location + "\n";
    }

    public String toCSVString()
    {
        return date + ","
                + time + ","
                + description + ","
                + location + ","
                + longitude + ","
                + latitude + "\n";
    } // end toString
}
