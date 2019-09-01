package com.votingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.votingapp.AndroidDatabaseManager;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.ViewPagerAdapter;
import com.votingapp.fragments.ListFragment;
import com.votingapp.models.Vote;
import com.votingapp.utils.Keys;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListFragment.SelectionListener,
        NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawer;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Voting Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Списък с вотове");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!AppController.loggedUser.getIsAdmin()){
            navigationView.getMenu().findItem(R.id.nav_create_vote).setEnabled(false);
            navigationView.getMenu().findItem(R.id.nav_view_database).setEnabled(false);

        }
    }

    private void loadFromDB(){
        AppController.votes.clear();
//        AppController.votes.addAll(AppController.databaseHelper.selectAllReferendums());
//        AppController.votes.addAll(AppController.databaseHelper.selectAllVotings());
//        AppController.votes.addAll(AppController.databaseHelper.selectAllPolls());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadFromDB();
    }

    @Override
    public void onItemSelected(Vote vote) {
        ArrayList<Vote> alreadyVotedByUser = new ArrayList<>();
        alreadyVotedByUser.addAll(AppController.loggedUser.getAllVotes());

        boolean alreadyVotedFlag = false;
        for(Vote alreadyVoted : alreadyVotedByUser) {
            if (alreadyVoted.getTitle().equals(vote.getTitle())) {
                alreadyVotedFlag = true;
                break;
            }
        }

        Intent myIntent = new Intent(MainActivity.this, VotingActivity.class);
        myIntent.putExtra(Keys.ALREADY_VOTED, alreadyVotedFlag);
        AppController.setCurrentVote(vote);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_votes_list){
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_create_vote) {
            Intent myIntent = new Intent(MainActivity.this, CreateVoteActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_view_database) {
            Intent dbmanager = new Intent(MainActivity.this, AndroidDatabaseManager.class);
            startActivity(dbmanager);
        }else if(id == R.id.nav_exit){
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
