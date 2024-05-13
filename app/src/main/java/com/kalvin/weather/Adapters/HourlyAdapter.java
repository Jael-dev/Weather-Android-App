package com.kalvin.weather.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kalvin.weather.Domains.Hourly;
import java.util.ArrayList;
import com.kalvin.weather.R;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.viewHolder>{

    ArrayList<Hourly> hourlyArrayList;
    Context context;

    public HourlyAdapter(ArrayList<Hourly> hourlyArrayList) {
        this.hourlyArrayList = hourlyArrayList;
    }

    @NonNull
    @Override
    public HourlyAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_hourly, parent, false);
        context = parent.getContext();
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.hourtxt.setText(hourlyArrayList.get(position).getHour());
        holder.temptxt.setText(hourlyArrayList.get(position).getTemp() + "°");

        int drawableResourceId = holder.itemView.getResources()
                .getIdentifier(hourlyArrayList.get(position).getIcon(), "drawable", context.getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.iconimg);
    }

    @Override
    public int getItemCount() {
        return hourlyArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView hourtxt, temptxt;
        ImageView iconimg;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            hourtxt = itemView.findViewById(R.id.hourtxt);
            temptxt = itemView.findViewById(R.id.temptxt);
            iconimg = itemView.findViewById(R.id.iconimg);
        }
    }
}
