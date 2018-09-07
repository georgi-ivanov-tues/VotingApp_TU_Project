package com.votingapp;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.votingapp.fragments.ListFragment;
import com.votingapp.fragments.TakePollFragment;
import com.votingapp.fragments.TakeReferendumFragment;
import com.votingapp.fragments.TakeVotingFragment;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;

public class VotingActivity extends AppCompatActivity implements ListFragment.SelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        System.out.println("Voting Activity");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_voting);
//
//        String defaultFragment = (String) getIntent().getSerializableExtra(Keys.VOTING_ACTIVITY_FRAGMENT);
//
//        ListFragment listFragment = new ListFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.list_content_fragment, listFragment);
//        transaction.commit();
//
//        if(Keys.LIST_VOTINGS.equals(defaultFragment)){
//
//        }else if(Keys.LIST_POOLS.equals(defaultFragment)){
//
//        }else if(Keys.LIST_REFERENDUMS.equals(defaultFragment)){
//
//        }


        System.out.println("Voting Activity");
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_voting);
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
        transaction.replace(R.id.list_content_fragment, listFragment);
        transaction.commit();
    }

    @Override
    public void onItemSelected(Vote vote) {
        System.out.println("Selected vote is = " + vote.getTitle());
        Bundle bundle;
        FragmentTransaction transaction;
        if (vote.getClass().equals(Voting.class)) {
            TakeVotingFragment takeVotingFragment = new TakeVotingFragment();
            bundle = new Bundle();
            bundle.putSerializable(Keys.VOTE_OBJECT, vote);
            takeVotingFragment.setArguments(bundle);
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.list_content_fragment, takeVotingFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (vote.getClass().equals(Poll.class)) {
            TakePollFragment takePollFragment = new TakePollFragment();
            bundle = new Bundle();
            bundle.putSerializable(Keys.VOTE_OBJECT, vote);
            takePollFragment.setArguments(bundle);
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.list_content_fragment, takePollFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (vote.getClass().equals(Referendum.class)) {
            TakeReferendumFragment takeReferendumFragment = new TakeReferendumFragment();
            bundle = new Bundle();
            bundle.putSerializable(Keys.VOTE_OBJECT, vote);
            takeReferendumFragment.setArguments(bundle);
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.list_content_fragment, takeReferendumFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
