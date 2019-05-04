package com.votingapp.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.ViewPagerAdapter;
import com.votingapp.fragments.ListFragment;
import com.votingapp.models.Vote;
import com.votingapp.utils.Keys;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListFragment.SelectionListener {

    FragmentTransaction transaction;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Voting Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onItemSelected(Vote vote) {
        ArrayList<Vote> alreadyVotedByUser = new ArrayList<>();
        alreadyVotedByUser.addAll(AppController.userProfile.getAllVotes());

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
        super.onBackPressed();
    }
}
