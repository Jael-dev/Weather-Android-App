package com.kalvin.weather.Domains;

public class Hourly {

    private  String hour;
    private  int temp;
    private  String icon;

    // Constructor
    public Hourly(String hour, int temp, String icon) {
        this.hour = hour;
        this.temp = temp;
        this.icon = icon;
    }

    // Getters and Setters
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }



}
