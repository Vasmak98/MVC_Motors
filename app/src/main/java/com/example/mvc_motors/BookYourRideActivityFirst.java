package com.example.mvc_motors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvc_motors.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookYourRideActivityFirst extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout layoutCard;
    FloatingActionButton fabAdd;
    private String userID;
    FirebaseUser user;
    Integer total = 0;

    FirebaseAuth Auth;
    DatabaseReference reference, reference2, reference3, reference4, reference5;
    static Integer cardID = 0;
    Integer finalPriceInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_your_ride_first);

        layoutCard = findViewById(R.id.layoutCard);
        fabAdd = findViewById(R.id.fabAdd);
        addView();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("rides").child(userID);
        reference.setValue(null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void addView(){
        View card = getLayoutInflater().inflate(R.layout.row_add_card, null, false);

        TextView tvPrice = card.findViewById(R.id.tvPrice);
        TextView tvNumber = card.findViewById(R.id.tvNumber);
        ImageButton remove = card.findViewById(R.id.btnRemove);
        ImageButton add = card.findViewById(R.id.btnAdd);

        cardID++;

        TextView tvID = card.findViewById(R.id.tvID);
        tvID.setText(cardID.toString());

        CardView cardView = (CardView)card.findViewById(R.id.cardView);
        ImageView imgRemove = (ImageView)card.findViewById(R.id.imgRemove);

        Spinner spinner1 = (Spinner) card.findViewById(R.id.spinnerType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> myArrayList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookYourRideActivityFirst.this, android.R.layout.simple_spinner_item, myArrayList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvNumber.setText("1");
                String motorOld = spinner1.getSelectedItem().toString();
                String motor = motorOld.replace("ATV - ", "");
                reference2 = FirebaseDatabase.getInstance().getReference("data");
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            if (snapshot1.child("title").getValue().equals(motor)){
                                String price = snapshot1.child("price").getValue().toString();
                                Integer priceInt = Integer.parseInt(price);
                                tvPrice.setText(price);

                                remove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Integer number = Integer.parseInt(tvNumber.getText().toString());

                                        if (number > 0) {
                                            number--;
                                            finalPriceInt = number * priceInt;
                                        }
                                        tvNumber.setText(number.toString());
                                        tvPrice.setText(finalPriceInt.toString());
                                    }
                                });

                                add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Integer number = Integer.parseInt(tvNumber.getText().toString());

                                        if (number < 10) {
                                            number++;
                                            finalPriceInt = number * priceInt;
                                        } else {
                                            finalPriceInt = 10 * priceInt;
                                        }
                                        tvNumber.setText(number.toString());
                                        tvPrice.setText(finalPriceInt.toString());
                                    }
                                });
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                String buggyOld = spinner1.getSelectedItem().toString();
                String buggy = buggyOld.replace("Buggy - ", "");
                reference2 = FirebaseDatabase.getInstance().getReference("buggies");
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            if (snapshot1.child("title").getValue().equals(buggy)){
                                String price = snapshot1.child("price").getValue().toString();
                                Integer priceInt = Integer.parseInt(price);
                                tvPrice.setText(price);

                                remove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Integer number = Integer.parseInt(tvNumber.getText().toString());
                                        Integer finalPriceInt = 0;

                                        if (number > 0) {
                                            number--;
                                            finalPriceInt = number * priceInt;
                                        }
                                        tvNumber.setText(number.toString());
                                        tvPrice.setText(finalPriceInt.toString());
                                    }
                                });

                                add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Integer number = Integer.parseInt(tvNumber.getText().toString());
                                        Integer finalPriceInt = 0;
                                        if (number < 10) {
                                            number++;
                                            finalPriceInt = number * priceInt;
                                        } else {
                                            finalPriceInt = 10 * priceInt;
                                        }
                                        tvNumber.setText(number.toString());
                                        tvPrice.setText(finalPriceInt.toString());
                                    }
                                });

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Get ATVs name
        reference = FirebaseDatabase.getInstance().getReference().child("data");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Motors value = snapshot.getValue(Motors.class);
                String title = value.title;
                myArrayList.add("ATV - " + title);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        //Get Buggies names
        reference = FirebaseDatabase.getInstance().getReference().child("buggies");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Motors value = snapshot.getValue(Motors.class);
                String title = value.title;
                myArrayList.add("Buggy - " + title);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(card, tvID.getText().toString());
            }
        });


        Button confirmCard = card.findViewById(R.id.btnConfirmCard);
        confirmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner1.setEnabled(false);
                remove.setEnabled(false);
                add.setEnabled(false);

                user = FirebaseAuth.getInstance().getCurrentUser();
                userID = user.getUid();

                reference3 = FirebaseDatabase.getInstance().getReference().child("rides").child(userID);

                String selection = spinner1.getSelectedItem().toString();
                String selectionClean;
                if (selection.contains("Buggy - ")){
                    selectionClean = selection.replace("Buggy - ", "");
                } else {
                    selectionClean = selection.replace("ATV - ", "");

                }
                reference3.child(tvID.getText().toString()).child("Ride").setValue(selectionClean);
                reference3.child(tvID.getText().toString()).child("NumberOfVehicles").setValue(tvNumber.getText().toString());
                reference3.child(tvID.getText().toString()).child("price").setValue(tvPrice.getText().toString());

                reference4 = FirebaseDatabase.getInstance().getReference("rides").child(userID);
                reference4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            if(snapshot1.child("price").getValue() != null) {
                                String price = snapshot1.child("price").getValue().toString();
                                Integer price2 = Integer.parseInt(price);
                                total = total + price2;

                                TextView tvTotal = findViewById(R.id.tvTotalPrice);
                                String finalPrice = total.toString();
                                tvTotal.setText(finalPrice);
                            }
                        }
                        total = 0;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                reference5 = FirebaseDatabase.getInstance().getReference("rides");
                reference5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() == null) {
                            TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
                            tvTotalPrice.setText("0");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                confirmCard.setEnabled(false);
            }
        });
        layoutCard.addView(card);
    }


    private void removeView(View v, String id){
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference2 = FirebaseDatabase.getInstance().getReference("rides").child(userID).child(id);
        if (reference2 != null){
            reference2.setValue(null);
        }

        reference4 = FirebaseDatabase.getInstance().getReference("rides").child(userID);
        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView tvTotal = findViewById(R.id.tvTotalPrice);

                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if(snapshot1.child("price").getValue() != null) {
                        String price = snapshot1.child("price").getValue().toString();
                        Integer price2 = Integer.parseInt(price);
                        total = total + price2;

                        String finalPrice = total.toString();
                        tvTotal.setText(finalPrice);
                    }
                }
                total = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference3 = FirebaseDatabase.getInstance().getReference("rides").child(userID);
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
                    tvTotalPrice.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        layoutCard.removeView(v);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void goToMyCart(View v){
        Intent in = new Intent(this, BookYourRideActivitySecond.class);
        startActivity(in);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
            Toast.makeText(getApplicationContext(), "You are signed out!", Toast.LENGTH_LONG).show();
            finish();
            return super.onOptionsItemSelected(item);
        }
        else if (id == R.id.action_profile) {
            Intent in = new Intent(this, ProfileActivity.class);
            startActivity(in);
            return super.onOptionsItemSelected(item);
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }

    }


}