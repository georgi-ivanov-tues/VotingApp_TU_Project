package com.votingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ArrayList<Voting> votings = AppController.userProfile.getVotings();
        ArrayList<Poll> polls = AppController.userProfile.getPolls();
        ArrayList<Referendum> referendums = AppController.userProfile.getReferendums();

        // Get the widgets reference from XML layout
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.userProfileLinearLayout);
        TextView votingsTitleTextView = new TextView(getApplicationContext());
        votingsTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        votingsTitleTextView.setTextAppearance(getApplicationContext(), R.style.text_vote_title2);
        votingsTitleTextView.setText("Гласувания");
        linearLayout.addView(votingsTitleTextView);

        for(final Voting voting : votings){
            TextView votingTextView = new TextView(this);
            votingTextView.setText(voting.getTitle());
            votingTextView.setTextAppearance(this, R.style.text_vote_title);
            votingTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            votingsTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent votingActivityIntent = new Intent(UserProfileActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Keys.VOTE_OBJECT, voting);
                    votingActivityIntent.putExtra(Keys.VOTING_ACTIVITY_FRAGMENT, Keys.LIST_VOTINGS);
                    votingActivityIntent.putExtras(bundle);
                    startActivity(votingActivityIntent);
                }
            });
            linearLayout.addView(votingTextView);
        }

        TextView pollsTitleTextView = new TextView(getApplicationContext());
        pollsTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        pollsTitleTextView.setText("Анкети");
        pollsTitleTextView.setTextAppearance(getApplicationContext(), R.style.text_vote_title2);
        linearLayout.addView(pollsTitleTextView);

        TextView referendumsTitleTextView = new TextView(getApplicationContext());
        referendumsTitleTextView.setText("Референдуми");
        referendumsTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        referendumsTitleTextView.setTextAppearance(getApplicationContext(), R.style.text_vote_title2);
        linearLayout.addView(referendumsTitleTextView);
    }
}
