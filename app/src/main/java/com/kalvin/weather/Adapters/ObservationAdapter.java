package com.kalvin.weather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kalvin.weather.Domains.Observation;
import com.kalvin.weather.R;

import java.util.List;

public class ObservationAdapter extends ArrayAdapter<Observation> {
    public ObservationAdapter(Context context, int resource, List<Observation> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView iconImageView = convertView.findViewById(R.id.iconImageView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);

        Observation observation = getItem(position);

        if (observation != null) {
            iconImageView.setImageResource(getIconResourceId(observation.getIcon()));
            nameTextView.setText(observation.getName());
        }

        return convertView;
    }

    private int getIconResourceId(String icon) {
        return getContext().getResources().getIdentifier(icon, "drawable", getContext().getPackageName());
    }

}


