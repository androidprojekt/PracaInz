package com.example.praca_inz_test;


//--------------class specifying individual exhibits in the listOfExhibitsTab-----------------------
public class Exhibit {

    private String exhibitName;
    private String description;
    private int Thumbnail;

    public Exhibit(String exhibitName, String description, int thumbnail) {
        this.exhibitName = exhibitName;
        this.description = description;
        Thumbnail = thumbnail;
    }


    public String getExhibitName() {
        return exhibitName;
    }

    public String getDescription() {
        return description;
    }

    public int getThumbnail() {
        return Thumbnail;
    }
}
