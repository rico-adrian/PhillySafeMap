package com.example.ryan.phillysafemap;

public class States
{
    String crimeType = null;
    Integer imageID = null;
    boolean isChecked = false;

    public States(String crimeType, Integer imageID, boolean selected)
    {
        super();
        this.crimeType = crimeType;
        this.imageID = imageID;
        this.isChecked = selected;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        this.isChecked = checked;
    }

}