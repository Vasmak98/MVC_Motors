package com.example.mvc_motors;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View view;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        view=itemView;
    }

    public void setDetails(Context context,String image, String title, String type,String url)
    {
        TextView fireTitle = view.findViewById(R.id.rTitleView);
        ImageView fireImage = view.findViewById(R.id.rImageView);
        TextView fireType = view.findViewById(R.id.rType);
        TextView fireUrl = view.findViewById(R.id.rUrl);

        fireTitle.setText(title);
        fireType.setText(type);
        fireUrl.setText(url);

        Picasso.get().load(image).into(fireImage);
    }
}
