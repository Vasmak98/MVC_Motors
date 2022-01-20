package com.example.mvc_motors;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.mvc_motors.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    FirebaseAuth Auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        Auth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Select a category to see our vehicles",Toast.LENGTH_LONG).show();
            }
        });

        Intent in = new Intent(this, MyService.class);
        stopService(in);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment, new HomeFragment());
            fragmentTransaction.commit();
        }
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
            super.onOptionsItemSelected(item);
            userlogout();
            return true;
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
    private void userlogout() {
        FirebaseAuth.getInstance().signOut();
        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
        finish();
        Toast.makeText(getApplicationContext(), "You are signed out!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = Auth.getCurrentUser();
        if (user == null)
        {
            userlogout();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.nav_home){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment, new HomeFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_book){
            Intent in = new Intent(this, BookYourRideActivityFirst.class);
            startActivity(in);
        } else if (id == R.id.nav_placesToVisit){
            Intent in = new Intent(this, PlacesToVisitActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_aboutUs){
            Intent in = new Intent(this, AboutContactUs.class);
            startActivity(in);
        } else if (id == R.id.nav_requirements){
            Intent in = new Intent(this, RequirementsActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_rateUs){
            Intent in = new Intent(this, RateUsActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_profile) {
            Intent in = new Intent(this, ProfileActivity.class);
            startActivity(in);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}