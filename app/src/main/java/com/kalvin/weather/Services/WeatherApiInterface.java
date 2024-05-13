package com.kalvin.weather.Services;
import com.kalvin.weather.Dtos.ReportDTO;
import com.kalvin.weather.Dtos.WeatherData;

import java.time.LocalDate;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WeatherApiInterface {

    // Get all the reports
    @GET("/springmvc/weather/report")
    Call<ArrayList<WeatherData>> getResource();

    @POST("/springmvc/weather/report")
    Call<ReportDTO> createReport(@Body ReportDTO report);


    @GET("/springmvc/weather/reports")
    Call<ArrayList<WeatherData>> getReports(@Query("latitude") Double latitude,
                                          @Query("longitude")  Double longitude,
                                            @Query("radius") Double radius);

    @GET("/springmvc/weather/reportstime")
    Call<ArrayList<WeatherData>> getReportsByTime(@Query("latitude") Double latitude,
                                                  @Query("longitude")  Double longitude,
                                                  @Query("radius") Double radius);
}
