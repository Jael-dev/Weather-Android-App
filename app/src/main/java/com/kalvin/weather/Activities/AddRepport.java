package com.kalvin.weather.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kalvin.weather.Adapters.ObservationAdapter;
import com.kalvin.weather.Domains.Observation;
import com.kalvin.weather.Domains.Report;
import com.kalvin.weather.Dtos.ReportDTO;
import com.kalvin.weather.R;
import com.kalvin.weather.Services.RetrofitClient;
import com.kalvin.weather.Services.WeatherApiInterface;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRepport extends AppCompatActivity {
    List<Observation> observationList = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<Observation> adapter;
    Location currentLocation;
    Observation currentObservation;
    private WeatherApiInterface apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repport);

        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
        Button add = findViewById(R.id.addButton);
        TextInputLayout textInputLayout = findViewById(R.id.observationsInputLayout);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        setVariable();
        getCurrentLocation();

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Observation observation = (Observation) parent.getItemAtPosition(position);
            currentObservation = observation;
                int drawableResourceId = getResources().getIdentifier(observation.getIcon(), "drawable",
                        getPackageName());
                autoCompleteTextView.setText(observation.getName());
                textInputLayout.setStartIconDrawable(drawableResourceId);
                Toast.makeText(AddRepport.this, observation.getName() +" "+observation.getIcon(),
                        Toast.LENGTH_SHORT).show();

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String observation = textInputEditText.getText().toString();
                if (currentObservation == null || observation.isEmpty()) {
                    Toast.makeText(AddRepport.this, "The text is " + currentLocation.getLatitude(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Report report = new Report(currentLocation.getLongitude(),
                        currentLocation.getLatitude(),
                        currentObservation.getIcon(),
                        currentObservation.getName(), Integer.parseInt(observation),
                        new Date());
                System.out.println(report);
                createReport(report);
                Intent intent = new Intent(AddRepport.this, MainActivity.class);
                startActivity(intent);

                finish();


            }
        });
    }

    private void createReport(Report report){
        apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiInterface.class);
        System.out.println("Running");
        Call<ReportDTO> createReportCall = apiService.createReport(report.ReportToReportDTO(report));
        createReportCall.enqueue(new Callback<ReportDTO>() {
            @Override
            public void onResponse(Call<ReportDTO> call, Response<ReportDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReportDTO createdReport = response.body();
                    System.out.println(createdReport);
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<ReportDTO> call, Throwable t) {
                // Handle failure
                System.out.println(t.getMessage());
                System.out.println("error");
            }
        });
    }

    private void setVariable() {
        observationList.add(new Observation("cloudy", "cloudy"));
        observationList.add(new Observation("rainy", "rainy"));
        observationList.add(new Observation("sunny", "sunny"));
        observationList.add(new Observation("storm", "storm"));
        observationList.add(new Observation("wind", "wind"));

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        adapter = new ObservationAdapter(this, R.layout.list_item, observationList);
        autoCompleteTextView.setAdapter(adapter);

        ConstraintLayout constraintLayout = findViewById(R.id.backbtn);
        constraintLayout.setOnClickListener(v -> finish());
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
