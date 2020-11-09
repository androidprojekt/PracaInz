package com.example.praca_inz_test;

public class Fingerprint {

    private int wifiRssi;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fingerprint() {
    }

    public int getWifiRssi() {
        return wifiRssi;
    }

    public void setWifiRssi(int wifiRssi) {
        this.wifiRssi = wifiRssi;
    }
}
