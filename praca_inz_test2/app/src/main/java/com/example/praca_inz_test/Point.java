package com.example.praca_inz_test;

public class Point {

    int x,y;
    double actualX, actualY;
    double euclideanDistance;
    String lastUpdate;

    public Point(int x, int y, double euclideanDistance) {
        this.x = x;
        this.y = y;
        this.euclideanDistance = euclideanDistance;
    }


    public Point(double x, double y, String lastUpdate){
        actualX = x;
        actualY = y;
        this.lastUpdate=lastUpdate;
        euclideanDistance=0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getEuclideanDistance() {
        return euclideanDistance;
    }

    public void setEuclideanDistance(double euclideanDistance) {
        this.euclideanDistance = euclideanDistance;
    }
    public double getActualX() {
        return actualX;
    }

    public double getActualY() {
        return actualY;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

}
