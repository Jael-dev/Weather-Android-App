package com.kalvin.weather.Services;

import android.location.Location;

public interface LocationUpdatesListener {
    void onLocationChanged(Location location);
}
