package com.example.praca_inz_test;

import java.util.ArrayList;

public class Transmitter {

    String macAdress;
    String lastUpdate;
    String type;
    String name;
    double average;
    int rssi;
    boolean savingSamples;
    ArrayList<Integer> samplesTab;
    int samplesIterator;

    //--------------iterators needed to determine direction from database---------------
    int directionIteratorUp;
    int directionIteratorDown;
    int directionIteratorLeft;
    int directionIteratorRight;
    //-----------------------------------------------------------------------------------


    public Transmitter(String macAdress, int rssi, String type) {
        this.macAdress = macAdress;
        this.rssi = rssi;
        this.type = type;
        this.name = "default";
        this.savingSamples = true;
        this.samplesTab = new ArrayList<>();
        this.samplesIterator = 0;
        this.average = 0.0;
        directionIteratorDown = 0;
        directionIteratorLeft = 0;
        directionIteratorUp = 0;
        directionIteratorRight = 0;
    }


    public Transmitter(String macAdress, int rssi, String type, String name) {
        this.macAdress = macAdress;
        this.lastUpdate = lastUpdate;
        this.rssi = rssi;
        this.type = type;
        this.name = name;
        this.savingSamples = true;
        this.samplesTab = new ArrayList<>();
        this.samplesIterator = 0;
        directionIteratorDown = 0;
        directionIteratorLeft = 0;
        directionIteratorUp = 0;
        directionIteratorRight = 0;
    }

    public void addToTheSamplesTab(int sample) {
        samplesTab.add(sample);
    }


    public ArrayList<Integer> getSamplesTab() {
        return samplesTab;
    }

    public void setSamplesTab(ArrayList<Integer> samplesTab) {
        this.samplesTab = samplesTab;
    }

    public void clearTheSamplesTab() {
        samplesTab.clear();
    }

    public boolean isSavingSamples() {
        return savingSamples;
    }

    public void setSavingSamples(boolean savingSamples) {
        this.savingSamples = savingSamples;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String name) {
        this.lastUpdate = name;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSamplesIterator() {
        return samplesIterator;
    }


    public void setSamplesIterator() {
        this.samplesIterator++;
    }

    public void clearSamplesIterator() {
        this.samplesIterator = 0;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public void setDirectionIterators(String direction) {
        switch (direction) {
            case "UP":
                directionIteratorUp++;
                break;
            case "DOWN":
                directionIteratorDown++;
                break;
            case "LEFT":
                directionIteratorLeft++;
                break;
            case "RIGHT":
                directionIteratorRight++;
                break;
            default:
                //log d
        }
    }

    public int getDirectionIteratorUp() {
        return directionIteratorUp;
    }

    public int getDirectionIteratorDown() {
        return directionIteratorDown;
    }

    public int getDirectionIteratorLeft() {
        return directionIteratorLeft;
    }

    public int getDirectionIteratorRight() {
        return directionIteratorRight;
    }

    public void clearDirectionIterators() {
        directionIteratorDown = 0;
        directionIteratorLeft = 0;
        directionIteratorUp = 0;
        directionIteratorRight = 0;
    }
}
