package com.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.models.Referendum;
import com.votingapp.models.UserProfile;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String votingPathFile_OLD = "1DJiTUEblvSyaGpgFcxi840ZNyjCLT4X7\"";
        String votingsPathFile = "1Ee6cBkbY9ohmnA6ewGdrnWLpWVRdHpdn";
        String referendumsPathFile = "1tUdkU2UJfHY-zM1ZWt-82sT4E3gyoYT7";
        String pollsPathFile = "1xICuvwRi53Dk5CtGtFryfRElnRnzpjd7";

        loadDataFromFiles(votingsPathFile, Voting.class);
        loadDataFromFiles(referendumsPathFile, Referendum.class);
        loadDataFromFiles(pollsPathFile, Poll.class);


//        setUpData();
    }

    private void loadDataFromFiles(final String votesPathFile, final Class voteType){
        new Thread(new Runnable(){
            public void run(){
                String json = "";
                try {
                    // Create a URL for the desired page
                    URL url = new URL("https://drive.google.com/uc?export=download&id="+votesPathFile); //My text file location
                    //First open the connection
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000); // timing out in a minute

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String str;
                    StringBuffer sb = new StringBuffer();
                    while ((str = in.readLine()) != null) {
                        sb.append(str);
                    }
                    json = sb.toString();
                    in.close();
                } catch (Exception e) {
                    Log.d("MyTag",e.toString());
                }

                ObjectMapper mapper = new ObjectMapper();
                try {
                    if(voteType == Voting.class) {
                        Voting[] votings = mapper.readValue(json, Voting[].class);
                        AppController.votes.addAll(Arrays.asList(votings));
                    }else if(voteType == Poll.class) {
                        Poll[] polls = mapper.readValue(json, Poll[].class);
                        AppController.votes.addAll(Arrays.asList(polls));
                    }else if(voteType == Referendum.class) {
                        Referendum[] referendums = mapper.readValue(json, Referendum[].class);
                        AppController.votes.addAll(Arrays.asList(referendums));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_user_profile) {
//            Intent userProfileActivityIntent = new UserProfileActivity(HomeActivity.this, UserProfileActivity);
        } else if (id == R.id.nav_votings) {
            Intent votingActivityIntent = new Intent(HomeActivity.this, VotingActivity.class);
            votingActivityIntent.putExtra(Keys.VOTING_ACTIVITY_FRAGMENT, Keys.LIST_VOTINGS);
            startActivity(votingActivityIntent);
        } else if (id == R.id.nav_polls) {
            Intent votingActivityIntent = new Intent(HomeActivity.this, VotingActivity.class);
            votingActivityIntent.putExtra(Keys.VOTING_ACTIVITY_FRAGMENT, Keys.LIST_POOLS);
            startActivity(votingActivityIntent);
        } else if (id == R.id.nav_referendums) {
            Intent votingActivityIntent = new Intent(HomeActivity.this, VotingActivity.class);
            votingActivityIntent.putExtra(Keys.VOTING_ACTIVITY_FRAGMENT, Keys.LIST_REFERENDUMS);
            startActivity(votingActivityIntent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpData(){
        try {
            String voting1Title = "The best city in Bulgaria is?";
            Question voting1Question = new Question(voting1Title);
            ArrayList<Option> voting1Options = new ArrayList<>();
            voting1Options.add(new Option("Plovdiv"));
            voting1Options.add(new Option("Sofia"));
            voting1Options.add(new Option("Varna"));
            voting1Options.add(new Option("Burgas"));
            Voting voting1 = new Voting(voting1Title, voting1Question, voting1Options);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(voting1);
            System.out.println("voting1_json = " + json);

            String voting2Title = "The new president of the company should be?";
            Question voting2Question = new Question(voting2Title);
            ArrayList<Option> voting2Options = new ArrayList<>();
            voting2Options.add(new Option("George Jackson"));
            voting2Options.add(new Option("John Smith"));
            voting2Options.add(new Option("Elizabeth Howards"));
            Voting voting2 = new Voting(voting2Title, voting2Question, voting2Options);

            json = ow.writeValueAsString(voting2);
            System.out.println("voting2_json = " + json);

            // -----------------------------------------

            HashMap<Question, ArrayList<Option>> pollContent = new HashMap<>();

            String poll1Title = "General questions";
            Question poll1Question1 = new Question("What's your favourite colour?");
            ArrayList<Option> poll1Question1Options = new ArrayList<>();
            poll1Question1Options.add(new Option("Red"));
            poll1Question1Options.add(new Option("Green"));
            poll1Question1Options.add(new Option("Blue"));
            poll1Question1Options.add(new Option("Yellow"));

            Question poll1Question2 = new Question("What's your favourite animal?");
            ArrayList<Option> poll1Question2Options = new ArrayList<>();
            poll1Question2Options.add(new Option("Cat"));
            poll1Question2Options.add(new Option("Dog"));
            poll1Question2Options.add(new Option("Horse"));
            poll1Question2Options.add(new Option("Dolphin"));
            poll1Question2Options.add(new Option("Bear"));

            Question poll1Question3 = new Question("Where are you from?");
            ArrayList<Option> poll1Question3Options = new ArrayList<>();
            poll1Question3Options.add(new Option("Bulgaria"));
            poll1Question3Options.add(new Option("Grece"));
            poll1Question3Options.add(new Option("Germany"));

            pollContent.put(poll1Question1, poll1Question1Options);
            pollContent.put(poll1Question2, poll1Question2Options);
            pollContent.put(poll1Question3, poll1Question3Options);
            Poll poll1 = new Poll(poll1Title, pollContent);

            json = ow.writeValueAsString(poll1);
            System.out.println("poll1_json = " + json);

            // -----------------------------------------

            Referendum referendum1 = new Referendum("Do you support Trump?", new Question("Do you support Trump?"));
            json = ow.writeValueAsString(referendum1);
            System.out.println("referendum1_json = " + json);


            Referendum referendum2 = new Referendum("Build nuclear power plant?e", new Question("Build nuclear power plant?"));
            json = ow.writeValueAsString(referendum2);
            System.out.println("referendum2_json = " + json);


//            AppController.votes.add(voting1);
            AppController.votes.add(voting2);
            AppController.votes.add(poll1);
            AppController.votes.add(referendum1);
            AppController.votes.add(referendum2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
