package com.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.net.ConnectivityManager;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveResourceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.android.gms.tasks.Task;
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

import static com.google.android.gms.drive.Drive.getDriveResourceClient;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Гласувания, анкети, референдуми");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button loadDataButton = (Button) findViewById(R.id.load_data_button);
        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpVotes2();
            }
        });

        setUpVotes();
//        GoogleSignInAccount googleSignInAccount = new GoogleSignInAccount()
//        Task<DriveContents> openFileTask =
//                getDriveResourceClient(this,).openFile(file, DriveFile.MODE_READ_ONLY);
    }

    private void setUpVotes2(){
        ArrayList<Option> voting1Options = new ArrayList<>();
        voting1Options.add(new Option("Option 1"));
        voting1Options.add(new Option("Option 2"));
        voting1Options.add(new Option("Option 3"));
        Voting voting1 = new Voting("Voting 1", new Question("Voting 1"), voting1Options);
        AppController.votes.add(voting1);

        // -------------------------------------------------

        ArrayList<Poll> polls = new ArrayList<>();
        ArrayList<Question> poll1Questions = new ArrayList<>();
        poll1Questions.add(new Question("Question 1"));
        poll1Questions.add(new Question("Question 2"));

        ArrayList<Option> poll1Question1Options = new ArrayList<>();
        poll1Question1Options.add(new Option("Option 1"));
        poll1Question1Options.add(new Option("Option 2"));

        ArrayList<Option> poll1Question2Options = new ArrayList<>();
        poll1Question2Options.add(new Option("Option 1"));
        poll1Question2Options.add(new Option("Option 2"));
        HashMap<Question, ArrayList<Option>> pollContent = new HashMap<>();
        pollContent.put(poll1Questions.get(0), poll1Question1Options);
        pollContent.put(poll1Questions.get(1), poll1Question2Options);

        Poll poll1 = new Poll("Poll 1", pollContent);
        AppController.votes.add(poll1);

        // -------------------------------------------------

        ArrayList<Referendum> referendums = new ArrayList<>();
        Referendum referendum = new Referendum("Референдум 2019", new Question("Съгласни ли сте с плана за създаване на нова атомна електроцентрала?"));
        referendum.getOptionYes().setTimesSelected(1);
        referendum.getOptionNo().setTimesSelected(1);
        AppController.votes.add(referendum);
    }

    private void setUpVotes(){
        if(isNetworkConnected()) {
            AppController.votes.clear();
            String votingsPathFile = "1Ee6cBkbY9ohmnA6ewGdrnWLpWVRdHpdn";
            String referendumsPathFile = "1tUdkU2UJfHY-zM1ZWt-82sT4E3gyoYT7";
            String pollsPathFile = "1xICuvwRi53Dk5CtGtFryfRElnRnzpjd7";

            loadDataFromFiles(votingsPathFile, Voting.class);
            loadDataFromFiles(referendumsPathFile, Referendum.class);
            loadDataFromFiles(pollsPathFile, Poll.class);

            CharSequence text = "Успешно зареждане на нови гласувания, анкети и референдуми";
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.show();
        }else{
            CharSequence text = "Липса на интернет връзка...";
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
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
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



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
            Intent userProfileActivityIntent = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(userProfileActivityIntent);

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
