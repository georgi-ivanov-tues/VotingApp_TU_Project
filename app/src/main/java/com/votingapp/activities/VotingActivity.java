package com.votingapp.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.Exclude;
import com.votingapp.AppController;
import com.votingapp.R;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VotingActivity extends AppCompatActivity {

    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Vote vote = AppController.getCurrentVote();
        boolean alreadyVoted = false;

        if(vote instanceof Voting){
            String optionVotedByUser = null;
            for (Map.Entry<String, String> pair : AppController.loggedUser.getVotingsVotedByUser().entrySet()) {
                if(pair.getKey().equals(vote.getId())) {
                    alreadyVoted = true;
                    optionVotedByUser = pair.getValue();
                    break;
                }
            }

            if(alreadyVoted) loadFragment(new VotingResultsFragment(), (Voting) vote, optionVotedByUser);
            else loadFragment(new TakeVotingFragment(), vote, null);
        }else if(vote instanceof Poll){
            HashMap<String, String> optionsVotedByUser = null;
            for(Map.Entry<String, HashMap<String, String>> poll : AppController.loggedUser.getPollsVotedByUser().entrySet()) {
                for (Map.Entry<String, String> pair : poll.getValue().entrySet()) {
                    if (poll.getKey().equals(vote.getId())) {
                        alreadyVoted = true;
                        optionsVotedByUser = poll.getValue();
                        break;
                    }
                }
            }

            if(alreadyVoted) loadFragment(new PollResultsFragment(), (Poll) vote, optionsVotedByUser);
            else loadFragment(new TakePollFragment(), vote, null);
        }else if(vote instanceof Referendum){
            String optionVotedByUser = null;
            for (Map.Entry<String, String> pair : AppController.loggedUser.getReferendumsVotedByUser().entrySet()) {
                if(pair.getKey().equals(vote.getId())) {
                    alreadyVoted = true;
                    optionVotedByUser = pair.getValue();
                    break;
                }
            }

            if(alreadyVoted) loadFragment(new ReferendumResultsFragment(), (Referendum) vote, optionVotedByUser);
            else loadFragment(new TakeReferendumFragment(), vote, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadFragment(Fragment fragmentToLoad, Vote vote, Serializable optionVotedByUser){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Keys.VOTE_OBJECT, vote);
        bundle.putSerializable(Keys.VOTED_OPTION, optionVotedByUser);
        fragmentToLoad.setArguments(bundle);
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_content_fragment, fragmentToLoad);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
