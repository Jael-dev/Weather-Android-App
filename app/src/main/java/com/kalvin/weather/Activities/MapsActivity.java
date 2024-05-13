package com.kalvin.weather.Activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kalvin.weather.Dtos.WeatherData;
import com.kalvin.weather.R;
import com.kalvin.weather.Repositories.WeatherRepository;
import java.lang.reflect.Type;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SharedPreferences sharedPreferences;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private ArrayList<WeatherData> sharedWeatherDataList;
    private WeatherRepository weatherRepository;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("WeatherData", MODE_PRIVATE);
        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Initialize WeatherRepository
        weatherRepository = new WeatherRepository();
        // Initialize views and variables
        initViews();
        loadData();
        getLastLocation();
    }

    private void initViews() {
        TextInputLayout searchLayout = findViewById(R.id.search);
        TextInputEditText radiusEditText = findViewById(R.id.radius);

        searchLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String radiusText = radiusEditText.getText().toString();
                if (radiusText.isEmpty() || Integer.parseInt(radiusText)>100) {
                    Toast.makeText(MapsActivity.this, "Please enter a valid radius before " +
                            "searching", Toast.LENGTH_SHORT).show();
                } else {
                    int radius = Integer.parseInt(radiusText);
                    searchByRadius(radius);
                }
            }
        });
    }

    private void searchByRadius(double radius) {
        getReports(currentLocation.getLatitude(), currentLocation.getLongitude(), radius);
    }

    private void getReports(Double latitude, Double longitude, Double radius) {
        System.out.println(latitude + " " + longitude + " " + radius + " " );
        weatherRepository.getReports(latitude, longitude, radius,  new Callback<ArrayList<WeatherData>>() {
            @Override
            public void onResponse(Call<ArrayList<WeatherData>> call, Response<ArrayList<WeatherData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("lol");
                    sharedWeatherDataList = response.body();
                    System.out.println(sharedWeatherDataList.toString() +"this is the list");
                    updateMapMarkers();
                }else {
                    Toast.makeText(MapsActivity.this, "Failed to get reports", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WeatherData>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Failed to get reports", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMapMarkers() {
        mMap.clear();
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));

        if (sharedWeatherDataList != null) {
            for (WeatherData weatherData : sharedWeatherDataList) {
                System.out.println(weatherData.getWeather());
                LatLng weatherLatLng = new LatLng(weatherData.getCoordinate().getLatitude(), weatherData.getCoordinate().getLongitude());
                mMap.addMarker(new MarkerOptions().position(weatherLatLng).title("Temperature: " + weatherData.getTemperature() + "°C, Weather: " + weatherData.getWeather()).icon(getBitmapDescriptor(weatherData.getWeather())));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 25.0f));
    }

    private BitmapDescriptor getBitmapDescriptor(String weather) {
        int resourceId = getResources().getIdentifier(weather, "drawable", getPackageName());
        return BitmapDescriptorFactory.fromResource(resourceId);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        updateMapMarkers();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied. Please allow the location permission to use the feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadData() {
        String json = sharedPreferences.getString("weatherDataList", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<WeatherData>>() {}.getType();
            sharedWeatherDataList = gson.fromJson(json, type);
        }
    }
}
