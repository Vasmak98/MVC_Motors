package com.example.mvc_motors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;

public class BookYourRideActivitySecond extends AppCompatActivity {

    ListView myListView;
    List<Rides> ridesList;
    private String userID;
    FirebaseUser user;
    DatabaseReference reference;

    Integer total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_your_ride_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myListView = findViewById(R.id.myListView);
        ridesList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("rides").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ridesList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Rides rides = snapshot.getValue(Rides.class);
                    ridesList.add(rides);

                    if(snapshot.child("price").getValue() != null) {
                        String price = snapshot.child("price").getValue().toString();
                        Integer price2 = Integer.parseInt(price);
                        total = total + price2;

                        TextView tvTotal = findViewById(R.id.tvTotalPrice2);
                        String finalPrice = total.toString();
                        tvTotal.setText(finalPrice);
                    }
                }
                total = 0;

                BookYourRideAdapter adapter = new BookYourRideAdapter(BookYourRideActivitySecond.this, ridesList);
                myListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void placeOrder(View v){
        Intent in = new Intent(this, MyService.class);
        startService(in);

        Toast.makeText(getApplicationContext(), "Thank you for your order! You will be notified when your order is accepted", Toast.LENGTH_LONG).show();
        Intent in2 = new Intent(this, MainActivity.class);
        startActivity(in2);
    }
}