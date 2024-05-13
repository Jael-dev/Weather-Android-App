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
import com.kalvin.weather.Domains.FutureDomain;
import com.kalvin.weather.R;

import java.util.ArrayList;
import java.util.Collections;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.viewHolder>{

    public FutureAdapter(ArrayList<FutureDomain> items) {
        this.items = items;
    }

    ArrayList<FutureDomain> items;
    Context context;

    @NonNull
    @Override
    public FutureAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_future,
                parent, false);
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureAdapter.viewHolder holder, int position) {
holder.dayTxt.setText(items.get(position).getDay());
holder.statusTxt.setText(items.get(position).getStatus());
holder.lowTxt.setText(items.get(position).getLow() + "°");
holder.highTxt.setText(items.get(position).getHigh() + "°");

int drawableResourceId = holder.itemView.getResources()
        .getIdentifier(items.get(position).getPic(), "drawable", context.getPackageName());
        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void reverseItemList() {
        Collections.reverse(items);
        notifyDataSetChanged();
    }

    public class viewHolder  extends RecyclerView.ViewHolder {

        TextView dayTxt, statusTxt, lowTxt, highTxt;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            dayTxt = itemView.findViewById(R.id.dayText);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            lowTxt = itemView.findViewById(R.id.lowTxt);
            highTxt = itemView.findViewById(R.id.highTxt);
            pic = itemView.findViewById(R.id.pic);
        }

    }


}
