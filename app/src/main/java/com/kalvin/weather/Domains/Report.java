package com.kalvin.weather.Domains;

import com.kalvin.weather.Dtos.ReportDTO;

import java.util.Date;

public class Report {
    private double longitude;
    private double latitude;
    private String icon;
    private String observation;
    private int temperature;
    private Date currentDateTime;


    public Report(double longitude, double latitude, String icon, String observation,
                  int temperature, Date currentDateTime) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.icon = icon;
        this.observation = observation;
        this.temperature = temperature;
        this.currentDateTime = currentDateTime;
    }

    public ReportDTO ReportToReportDTO(Report report) {
        return new ReportDTO(report.getLatitude(), report.getLongitude(), report.getObservation(), report.getTemperature());
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getIcon() {
        return icon;
    }

    public String getObservation() {
        return observation;
    }

    public int getTemperature() {
        return temperature;
    }

    public Date getCurrentDateTime() {
        return currentDateTime;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setCurrentDateTime(Date currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    // Add toString() method here
    @Override
    public String toString() {
        return "Report{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", icon='" + icon + '\'' +
                ", observation='" + observation + '\'' +
                ", temperature=" + temperature +
                ", currentDateTime=" + currentDateTime +
                '}';
    }
}
