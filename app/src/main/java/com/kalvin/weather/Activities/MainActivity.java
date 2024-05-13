package com.kalvin.weather.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.kalvin.weather.Adapters.HourlyAdapter;
import com.kalvin.weather.Dtos.WeatherData;
import com.kalvin.weather.R;
import com.kalvin.weather.Domains.Hourly;
import com.kalvin.weather.Repositories.WeatherRepository;
import com.kalvin.weather.Services.RetrofitClient;
import com.kalvin.weather.Services.WeatherApiInterface;
import java.util.ArrayList;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private RecyclerView.Adapter adapter;
private RecyclerView recyclerView;
private WeatherApiInterface apiService;
private WeatherRepository weatherRepository;
private SharedPreferences sharedPreferences;
private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiInterface.class);
        weatherRepository = new WeatherRepository();
        sharedPreferences = getSharedPreferences("WeatherData", MODE_PRIVATE);
        // Request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            // If permission is granted, get the current location
            getCurrentLocation();
        }
    }

    private void callApi() {
        if (currentLocation == null) {
            System.out.println("Current location is null");
        }
        weatherRepository.getReports(currentLocation.getLatitude(),
                currentLocation.getLongitude(), 100.0,
                new Callback<ArrayList<WeatherData>>() {
            @Override
            public void onResponse(Call<ArrayList<WeatherData>> call, Response<ArrayList<WeatherData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<WeatherData> myResponse = response.body();
                    // Reverse the list to show the latest data first
                    Collections.reverse(myResponse);
                    Gson gson = new Gson();
                    String json = gson.toJson(myResponse);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("weatherDataList", json);
                    editor.apply();
                    initRecyclerView(myResponse);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WeatherData>> call, Throwable t) {
            }
        });
    }



    private void setVariable() {
        TextView last7days = findViewById(R.id.lastdays);
        last7days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FutureActivity.class));
            }
        });
        ImageButton map = findViewById(R.id.mapcard);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        ImageButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRepport.class));
            }
        });
    }

    private void initRecyclerView(ArrayList<WeatherData> reports) {
        ArrayList<Hourly> hourlyArrayList = new ArrayList<>();
        for (WeatherData report : reports) {
            Hourly rep = report.toHourly(report);
            hourlyArrayList.add(rep);
            }
        Collections.reverse(hourlyArrayList);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new HourlyAdapter(hourlyArrayList);
        recyclerView.setAdapter(adapter);

    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    currentLocation = locationResult.getLastLocation();
                    System.out.println(currentLocation.getLongitude());
                    System.out.println(currentLocation.getLatitude());
                    callApi();
                    setVariable();
                    Log.d("Location", "Latitude: " + currentLocation.getLatitude() + ", Longitude: " + currentLocation.getLongitude());
                } else {
                    Log.e("Location", "Location result is null");
                }
            }
        }, Looper.getMainLooper());

    }


    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }
}