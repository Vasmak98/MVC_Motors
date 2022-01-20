package com.example.mvc_motors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThankYouActivity extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("rate");
        userID = user.getUid();

        Bundle bundle = getIntent().getExtras();

        Intent in = getIntent();
        String message = in.getStringExtra(RateUsActivity.INFO);

        Double rate1  = bundle.getDouble("rate1");
        Double rate2  = bundle.getDouble("rate2");
        Double rate3  = bundle.getDouble("rate3");
        Double rate4  = bundle.getDouble("rate4");

        int app1 = bundle.getInt("app1");
        int app2 = bundle.getInt("app2");
        int app3 = bundle.getInt("app3");
        int app4 = bundle.getInt("app4");

        String app = bundle.getString("application");
        String recomend = bundle.getString("recomendation");
        String opinion = bundle.getString("opinion");

        TextView tv = findViewById(R.id.tvMessage);
        tv.setText(message);

        Button submit = findViewById(R.id.rate);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child(userID).child("rate1").setValue(rate1.toString());
                reference.child(userID).child("rate2").setValue(rate2.toString());
                reference.child(userID).child("rate3").setValue(rate3.toString());
                reference.child(userID).child("rate4").setValue(rate4.toString());
                reference.child(userID).child("app1").setValue(app1);
                reference.child(userID).child("app2").setValue(app2);
                reference.child(userID).child("app3").setValue(app3);
                reference.child(userID).child("app4").setValue(app4);
                reference.child(userID).child("recomendation").setValue(recomend);
                reference.child(userID).child("opinion").setValue(opinion);

                Intent in = new Intent(getApplicationContext(), RateUsActivity.class);
                startActivity(in);

                Toast.makeText(getApplicationContext(),"Thank you very much! We hope to become your best choice",
                        Toast.LENGTH_LONG).show();

            }

        });

    }



}