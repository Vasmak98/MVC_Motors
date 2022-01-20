package com.example.mvc_motors;

import android.content.Intent;
import android.os.Bundle;

import com.example.mvc_motors.ui.login.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MotorsActivity extends AppCompatActivity {

    public static final String MESSAGE ="";

    FirebaseDatabase dbase;
    FirebaseAuth Auth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motors);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recycle = findViewById(R.id.recyclerview);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));

        dbase = FirebaseDatabase.getInstance();
        Auth = FirebaseAuth.getInstance();
        reference = dbase.getReference("data");

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MotorsActivity.this,"Press more information to visit manufacturer's site",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Motors> options = new FirebaseRecyclerOptions.Builder<Motors>()
                .setQuery(reference,Motors.class).build();

        FirebaseRecyclerAdapter<Motors,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Motors, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Motors model) {

                holder.setDetails(MotorsActivity.this,model.getImage(),model.getTitle(),model.getType(),model.url);

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image,parent,false);

                return new ViewHolder(view);
            }
        };

        
        firebaseRecyclerAdapter.startListening();
        RecyclerView recycle = findViewById(R.id.recyclerview);
        recycle.setAdapter(firebaseRecyclerAdapter);


    }


    public void goToWeb (View v)
    {
        TextView url = findViewById(R.id.rUrl);
        String gotoUrl = url.getText().toString();

        Intent in = new Intent(this,UrlActivity.class);
        in.putExtra(MESSAGE,gotoUrl);
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
        } else if (id == R.id.action_profile) {
            Intent in = new Intent(this, ProfileActivity.class);
            startActivity(in);
            return super.onOptionsItemSelected(item);
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}