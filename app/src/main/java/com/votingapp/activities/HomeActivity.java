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

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

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
                setUpVotesAndUsers();

                Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void setUpVotesAndUsers(){
        User admin = new User("admin", "1234", true);
        User test = new User("test", "1234", false);

        AppController.allUsers.add(admin);
        AppController.allUsers.add(test);

        AppController.votes.clear();

        ArrayList<Option> voting1Options = new ArrayList<>();
        voting1Options.add(new Option("Георги Иванов"));
        voting1Options.add(new Option("Иван Георгиев"));
        voting1Options.add(new Option("Петър Димитров"));

        voting1Options.get(0).setTimesSelected(5);
        voting1Options.get(1).setTimesSelected(3);
        voting1Options.get(2).setTimesSelected(4);
        Voting voting1 = new Voting("Гласуване за нов президент на компанията", new Question("Кой искате да е новият президент на компанията?"), voting1Options);
        AppController.votes.add(voting1);

        // -------------------------------------------------

        ArrayList<Poll> polls = new ArrayList<>();
        ArrayList<Question> poll1Questions = new ArrayList<>();
        poll1Questions.add(new Question("Любим цвят"));
        poll1Questions.add(new Question("Любимо животно"));

        ArrayList<Option> poll1Question1Options = new ArrayList<>();
        poll1Question1Options.add(new Option("Зелен"));
        poll1Question1Options.add(new Option("Червен"));
        poll1Question1Options.add(new Option("Син"));
        poll1Question1Options.add(new Option("Жълт"));

        ArrayList<Option> poll1Question2Options = new ArrayList<>();
        poll1Question2Options.add(new Option("Котка"));
        poll1Question2Options.add(new Option("Куче"));
        poll1Question2Options.add(new Option("Жираф"));
        poll1Question2Options.add(new Option("Гущер"));
        poll1Question2Options.add(new Option("Слон"));

        HashMap<Question, ArrayList<Option>> pollContent = new HashMap<>();
        pollContent.put(poll1Questions.get(0), poll1Question1Options);
        pollContent.put(poll1Questions.get(1), poll1Question2Options);

        Poll poll1 = new Poll("Първа анкета", pollContent);
        AppController.votes.add(poll1);

        // -------------------------------------------------

        ArrayList<Referendum> referendums = new ArrayList<>();
        Referendum referendum = new Referendum("Референдум 2019", new Question("Съгласни ли сте с плана за създаване на нова атомна електроцентрала?"));
        referendum.getOptionYes().setTimesSelected(1);
        referendum.getOptionNo().setTimesSelected(1);
        AppController.votes.add(referendum);
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
