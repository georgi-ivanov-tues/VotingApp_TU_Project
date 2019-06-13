package com.votingapp.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.fragments.TakePollFragment;
import com.votingapp.fragments.TakeReferendumFragment;
import com.votingapp.fragments.TakeVotingFragment;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;

public class VotingActivity extends AppCompatActivity {

    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Vote vote = AppController.getCurrentVote();

        if(vote instanceof Voting){
            loadFragment(new TakeVotingFragment(), vote);
        }else if(vote instanceof Poll){
            loadFragment(new TakePollFragment(), vote);
        }else if(vote instanceof Referendum){
            loadFragment(new TakeReferendumFragment(), vote);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadFragment(Fragment fragmentToLoad, Vote vote){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Keys.VOTE_OBJECT, vote);
        fragmentToLoad.setArguments(bundle);
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.list_content_fragment, fragmentToLoad);
        transaction.commit();
    }
}
