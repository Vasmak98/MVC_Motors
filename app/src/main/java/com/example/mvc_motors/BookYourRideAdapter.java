package com.example.mvc_motors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;

public class BookYourRideAdapter extends ArrayAdapter {

    private Activity mContext;
    List<Rides> ridesList;


    public BookYourRideAdapter(Activity mContext, List<Rides> ridesList) {
        super(mContext, R.layout.book_your_ride_item_layout, ridesList);
        this.mContext = mContext;
        this.ridesList = ridesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.book_your_ride_item_layout, null, true);

        TextView tvItemRide = listItemView.findViewById(R.id.tvItemRide);
        TextView tvItemNORides = listItemView.findViewById(R.id.tvItemNORides);
        TextView tvItemPrice = listItemView.findViewById(R.id.tvItemPrice);

        Rides rides = ridesList.get(position);
        tvItemRide.setText(rides.getRide());
        tvItemNORides.setText(rides.getNumberOfVehicles());
        tvItemPrice.setText(rides.getPrice());

        return listItemView;
    }
}