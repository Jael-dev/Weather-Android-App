package com.kalvin.weather.Dtos;

public class ReportDTO {

    private double latitude;
    private double longitude;
    private String weather;
    private int temperature;

    // Constructor

    public ReportDTO(double latitude, double longitude, String weather, int temperature) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.weather = weather;
        this.temperature = temperature;
    }
}
