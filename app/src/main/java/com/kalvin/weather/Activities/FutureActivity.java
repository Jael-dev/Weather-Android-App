package com.kalvin.weather.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kalvin.weather.Adapters.FutureAdapter;
import com.kalvin.weather.Domains.FutureDomain;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;

import com.kalvin.weather.Dtos.WeatherData;
import com.kalvin.weather.R;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class FutureActivity extends AppCompatActivity {
private RecyclerView.Adapter adapterYesterday;
private RecyclerView recyclerViewYesterday;
private boolean isSortedDescending = false;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        sharedPreferences = getSharedPreferences("WeatherData", MODE_PRIVATE);
        setVariable();
    }

    private void setVariable() {
        ConstraintLayout constraintLayout = findViewById(R.id.backbtn);
        constraintLayout.setOnClickListener(v -> finish());
        TextView sort = findViewById(R.id.sort);
        sort.setOnClickListener(v -> {
            isSortedDescending = !isSortedDescending; // Toggle sort order
            updateRecyclerView();
            Toast.makeText(this, "Sorting", Toast.LENGTH_SHORT).show();
        });

        loadData();
    }

    private void loadData() {
        String json = sharedPreferences.getString("weatherDataList", null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<WeatherData>>(){}.getType();
        ArrayList<WeatherData> weatherDataList = gson.fromJson(json, type);
        initRecyclerView(weatherDataList);
    }

    private void initRecyclerView(ArrayList<WeatherData> weatherData) {

        ArrayList<FutureDomain> futureArrayList = new ArrayList<>();

        for (WeatherData weather : weatherData) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String formattedTime = timeFormat.format(weather.getDate());
            System.out.println("Time: " + formattedTime);
            System.out.println(weather.getDate());

            // Format date (DD:MM/YYYY)
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(weather.getDate());
            System.out.println("Date: " + formattedDate);
            futureArrayList.add(new FutureDomain(formattedDate, weather.getWeather(),(int) weather.getTemperature(),(int) weather.getTemperature(), weather.getWeather()));
        }
        recyclerViewYesterday = findViewById(R.id.view2);
        recyclerViewYesterday.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        adapterYesterday = new FutureAdapter(futureArrayList);
        recyclerViewYesterday.setAdapter(adapterYesterday);
    }

    private void updateRecyclerView() {
        if (adapterYesterday instanceof FutureAdapter) {
            FutureAdapter futureAdapter = (FutureAdapter) adapterYesterday;
            futureAdapter.reverseItemList();
            futureAdapter.notifyDataSetChanged();
        }
    }


}