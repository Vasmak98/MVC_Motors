package com.example.mvc_motors;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;

public class HomeFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button motors = root.findViewById(R.id.atvButton);
        Button buggies = root.findViewById(R.id.buggiesButton);

        motors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId()==R.id.atvButton) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MotorsActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        buggies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId()==R.id.buggiesButton) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), BuggiesActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });


        return root;
    }









}

