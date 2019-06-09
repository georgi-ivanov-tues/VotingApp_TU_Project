package com.votingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.votingapp.AppController;
import com.votingapp.DatabaseHelper;
import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.models.Referendum;
import com.votingapp.models.User;
import com.votingapp.models.Voting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.google.android.gms.drive.Drive.getDriveResourceClient;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Гласувания, анкети, референдуми");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://localhost/phpmyadmin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Fail");
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        Button loadDataButton = (Button) findViewById(R.id.begin_button);
        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFromDB();

                Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void loadFromDB(){
//        AppController.databaseHelper.createAllTables();
        AppController.votes.clear();
        AppController.votes.addAll(AppController.databaseHelper.selectAllReferendums());
        AppController.votes.addAll(AppController.databaseHelper.selectAllVotings());
        AppController.votes.addAll(AppController.databaseHelper.selectAllPolls());
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
            super.onBackPressed();
//        }
    }
}
