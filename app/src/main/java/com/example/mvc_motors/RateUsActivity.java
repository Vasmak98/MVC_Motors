package com.example.mvc_motors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvc_motors.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateUsActivity extends AppCompatActivity {

    public static final String INFO = "";

    FirebaseUser user;
    DatabaseReference reference;
    private String userID;
    String _NAME, _SURNAME;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        Auth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Name & Surname
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);
                TextView tvName = findViewById(R.id.tvName);
                TextView tvSurname = findViewById(R.id.tvSurname);


                if (userProfile != null){
                    _NAME = userProfile.Name;
                    _SURNAME = userProfile.Surname;

                    tvName.setText(_NAME);
                    tvSurname.setText(_SURNAME);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("rate").child(userID);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                RatingBar rb1 = findViewById(R.id.rbQuestion1);
                RatingBar rb2 = findViewById(R.id.rbQuestion2);
                RatingBar rb3 = findViewById(R.id.rbQuestion3);
                RatingBar rb4 = findViewById(R.id.rbQuestion4);
                Switch sw = findViewById(R.id.sw1);
                TextView more = findViewById(R.id.etWriteMore);
                CheckBox cb1 = findViewById(R.id.cb1);
                CheckBox cb2 = findViewById(R.id.cb2);
                CheckBox cb3 = findViewById(R.id.cb3);
                CheckBox cb4 = findViewById(R.id.cb4);

                String rt1 = "0.0";
                String rt2 = "0.0";
                String rt3 = "0.0";
                String rt4 = "0.0";

                if( snapshot.getValue() != null) {

                    if(snapshot.child("rate1").getValue().toString() !=null) {
                        rt1 = snapshot.child("rate1").getValue().toString();
                    }
                    if(snapshot.child("rate2").getValue().toString() !=null) {
                        rt2 = snapshot.child("rate2").getValue().toString();
                    }
                    if(snapshot.child("rate3").getValue().toString() !=null) {
                        rt3 = snapshot.child("rate3").getValue().toString();
                    }
                    if(snapshot.child("rate4").getValue().toString() !=null) {
                        rt4 = snapshot.child("rate4").getValue().toString();
                    }
                    String recomend = snapshot.child("recomendation").getValue().toString();
                    String app1 = snapshot.child("app1").getValue().toString();
                    String app2 = snapshot.child("app2").getValue().toString();
                    String app3 = snapshot.child("app3").getValue().toString();
                    String app4 = snapshot.child("app4").getValue().toString();
                    String opinion = snapshot.child("opinion").getValue().toString();

                    if (!rt1.isEmpty()) {
                        float rate1 = Float.parseFloat(rt1);
                        rb1.setRating(rate1);
                    }
                    if (!rt2.isEmpty()) {
                        float rate2 = Float.parseFloat(rt2);
                        rb2.setRating(rate2);
                    }
                    if (!rt3.isEmpty()) {
                        float rate3 = Float.parseFloat(rt3);
                        rb3.setRating(rate3);
                    }
                    if (!rt4.isEmpty()) {
                        float rate4 = Float.parseFloat(rt4);
                        rb4.setRating(rate4);
                    }
                    if (recomend.equals("You would recommend us!")) {
                        sw.setChecked(true);
                        sw.setText("YES");
                    } else {
                        sw.setChecked(false);
                        sw.setText("NO");
                    }

                    more.setText(opinion);

                    if (app1.equals("1")) {
                        cb1.setChecked(true);
                    } else {
                        cb1.setChecked(false);
                    }

                    if (app2.equals("1")) {
                        cb2.setChecked(true);
                    } else {
                        cb2.setChecked(false);
                    }


                    if (app3.equals("1")) {
                        cb3.setChecked(true);
                    } else {
                        cb3.setChecked(false);
                    }


                    if (app4.equals("1")) {
                        cb4.setChecked(true);
                    } else {
                        cb4.setChecked(false);
                    }
                }


          }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
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


    public void recommend(View v){
        Switch sw = findViewById(R.id.sw1);

        if(sw.isChecked()){
            sw.setText("YES");
        } else {
            sw.setText("NO");
        }
    }

    public void submitRating(View v){
        RatingBar rb1 = findViewById(R.id.rbQuestion1);
        RatingBar rb2 = findViewById(R.id.rbQuestion2);
        RatingBar rb3 = findViewById(R.id.rbQuestion3);
        RatingBar rb4 = findViewById(R.id.rbQuestion4);

        Switch sw = findViewById(R.id.sw1);

        CheckBox cb1 = findViewById(R.id.cb1);
        CheckBox cb2 = findViewById(R.id.cb2);
        CheckBox cb3 = findViewById(R.id.cb3);
        CheckBox cb4 = findViewById(R.id.cb4);

        EditText et = findViewById(R.id.etWriteMore);

        double rate1 = 0.0 ;
        double rate2 = 0.0;
        double rate3 =0.0;
        double rate4 = 0.0;

        if(rb1.getRating() > 0) {
            rate1 = rb1.getRating();
        }

        if(rb2.getRating()> 0) {
            rate2 = rb2.getRating();
        }

        if(rb3.getRating()> 0) {
            rate3 = rb3.getRating();
        }

        if(rb4.getRating()> 0) {
            rate4 = rb4.getRating();
        }

        String recommendation = "";
        if (sw.isChecked()){
            recommendation = "You would recommend us!";
        } else {
            recommendation = "You would not recommend us!";
        }

        int app1,app2,app3,app4;
        app1=0;
        app2=0;
        app3=0;
        app4=0;

        String application = "";
        if (cb1.isChecked()){
            application += "\nEasy to use";
            app1=1;

        }
        if (cb2.isChecked()){
            application += "\nNice Design";
            app2=1;
        }
        if (cb3.isChecked()){
            application += "\nUseful";
            app3=1;
        }
        if (cb4.isChecked()){
            application += "\nUser Friendly";
            app4=1;
        }


        String generalOpinion = et.getText().toString();

        String message = "Hello " + _NAME + " " + _SURNAME
                + "\nThank you for rating our application!\n\nYou rated us with:"
                + "\nEase of use: " + rate1 + "/5"
                + "\nCustomer Service: " + rate2 + "/5"
                + "\nCustomer Service: " + rate3 + "/5"
                + "\nValue for Money: " + rate4 + "/5"
                + "\n\n" + recommendation
                + "\n\nOur application according to you is: " + application
                + "\n\nYour general opinion is:\n" + generalOpinion;


        Bundle rate  = new Bundle();
        rate.putInt("app1",app1);
        rate.putInt("app2",app2);
        rate.putInt("app3",app3);
        rate.putInt("app4",app4);
        rate.putDouble("rate1",rate1);
        rate.putDouble("rate2",rate2);
        rate.putDouble("rate3",rate3);
        rate.putDouble("rate4",rate4);
        rate.putString("recomendation",recommendation);
        rate.putString("opinion",generalOpinion);

        Intent in = new Intent(this, ThankYouActivity.class);
        in.putExtras(rate);
        in.putExtra(INFO,message);
        startActivity(in);
    }
}