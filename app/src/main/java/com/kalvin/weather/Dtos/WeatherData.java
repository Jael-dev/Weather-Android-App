package com.kalvin.weather.Dtos;

import com.kalvin.weather.Domains.Hourly;

import java.text.SimpleDateFormat;

public class WeatherData {
    private double temperature;
    private Coordinate coordinate;
    private long date;
    private String weather;

    public WeatherData(double temperature, Coordinate coordinate, long date, String weather) {
        this.temperature = temperature;
        this.coordinate = coordinate;
        this.date = date;
        this.weather = weather;
    }

    public double getTemperature() {
        return temperature;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public long getDate() {
        return date;
    }

    public String getWeather() {
        return weather;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String toString() {
        return "Temperature: " + temperature + ", Coordinate: " + coordinate + ", Date: " + date + ", Weather: " + weather;
    }

    public Hourly toHourly(WeatherData weatherData) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(weatherData.getDate());
        return new Hourly(time, (int) weatherData.getTemperature(), weatherData.getWeather());
    }

    public ReportDTO toReportDTO(WeatherData weatherData) {
        return new ReportDTO(weatherData.getCoordinate().getLatitude(), weatherData.getCoordinate().getLongitude(), weatherData.getWeather(), (int) weatherData.getTemperature());
    }
}
