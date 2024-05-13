package com.kalvin.weather.Repositories;

import com.kalvin.weather.Dtos.WeatherData;
import com.kalvin.weather.Services.RetrofitClient;
import com.kalvin.weather.Services.WeatherApiInterface;

import java.time.LocalDate;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class WeatherRepository {
    private WeatherApiInterface apiService;

    public WeatherRepository() {
        apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiInterface.class);
    }

    public void getWeatherData(Callback<ArrayList<WeatherData>> callback) {
        Call<ArrayList<WeatherData>> call = apiService.getResource();
        call.enqueue(callback);
    }

    public void getReports(Double latitude, Double longitude, Double radius,  Callback<ArrayList<WeatherData>> callback) {
        Call<ArrayList<WeatherData>> call = apiService.getReports(latitude, longitude, radius);
        call.enqueue(callback);
    }

    public void getReportsByTime(Double latitude, Double longitude, Double radius, Callback<ArrayList<WeatherData>> callback) {
        Call<ArrayList<WeatherData>> call = apiService.getReportsByTime(latitude, longitude, radius);
        call.enqueue(callback);
    }
}
