package com.example.praca_inz_test;

public class Exhibit {

    private String exhibitName;
    private String description;
    private int Thumbnail;

    public Exhibit() {
    }

    public Exhibit(String exhibitName, String description, int thumbnail) {
        this.exhibitName = exhibitName;
        this.description = description;
        Thumbnail = thumbnail;
    }

    public void setExhibitName(String exhibitName) {
        this.exhibitName = exhibitName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbnail(int thumbnail) {
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
