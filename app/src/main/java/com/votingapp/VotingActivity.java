package com.votingapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.votingapp.fragments.ListFragment;
import com.votingapp.fragments.PollResultsFragment;
import com.votingapp.fragments.ReferendumResultsFragment;
import com.votingapp.fragments.TakePollFragment;
import com.votingapp.fragments.TakeReferendumFragment;
import com.votingapp.fragments.TakeVotingFragment;
import com.votingapp.fragments.VotingResultsFragment;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;

public class VotingActivity extends AppCompatActivity implements ListFragment.SelectionListener {

    FragmentTransaction transaction;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Voting Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);
//        loadListFragment();
    }

    private void loadListFragment(){
        String defaultFragment = (String) getIntent().getSerializableExtra(Keys.VOTING_ACTIVITY_FRAGMENT);
        Bundle bundle = new Bundle();
        if (Keys.LIST_VOTINGS.equals(defaultFragment)) {
            bundle.putSerializable(Keys.VOTING_ACTIVITY_FRAGMENT, Keys.LIST_VOTINGS);
        } else if (Keys.LIST_POOLS.equals(defaultFragment)) {
            bundle.putSerializable(Keys.VOTING_ACTIVITY_FRAGMENT, Keys.LIST_POOLS);
        } else if (Keys.LIST_REFERENDUMS.equals(defaultFragment)) {
            bundle.putSerializable(Keys.VOTING_ACTIVITY_FRAGMENT, Keys.LIST_REFERENDUMS);
        }
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_content_fragment, listFragment, "listFragment");
        transaction.commit();
    }

    @Override
    public void onItemSelected(Vote vote) {
        System.out.println("Selected vote is = " + vote.getTitle());
        boolean alreadyVoted = false;
        if (vote.getClass().equals(Voting.class)) {
            for(Voting currentVoting : AppController.userProfile.getVotings()) {
                if (currentVoting.getTitle().equals(vote.getTitle())) {
                    alreadyVoted = true;
                    loadFragment(new VotingResultsFragment(), currentVoting);
                }
            }
            if(!alreadyVoted) {
                loadFragment(new TakeVotingFragment(), vote);
            }
        } else if (vote.getClass().equals(Poll.class)) {
            for(Poll currentPoll : AppController.userProfile.getPolls()) {
                if (currentPoll.getTitle().equals(vote.getTitle())) {
                    alreadyVoted = true;
                    loadFragment(new PollResultsFragment(), currentPoll);
                }
            }
            if(!alreadyVoted) {
                loadFragment(new TakePollFragment(), vote);
            }
        } else if (vote.getClass().equals(Referendum.class)) {
            for(Referendum currentReferendum : AppController.userProfile.getReferendums()){
                if(currentReferendum.getTitle().equals(vote.getTitle())) {
                    alreadyVoted = true;
                    loadFragment(new ReferendumResultsFragment(), currentReferendum);
                }
            }
            if(!alreadyVoted) {
                loadFragment(new TakeReferendumFragment(), vote);
            }
        }
    }

    private void loadFragment(Fragment fragmentToLoad, Vote vote){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Keys.VOTE_OBJECT, vote);
        fragmentToLoad.setArguments(bundle);
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_content_fragment, fragmentToLoad);
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        Fragment currentFragment = getFragmentManager().findFragmentByTag("listFragment");
        if (currentFragment != null && currentFragment.isVisible()) {
            finish();
        }else {
            loadListFragment();
        }
    }
}
