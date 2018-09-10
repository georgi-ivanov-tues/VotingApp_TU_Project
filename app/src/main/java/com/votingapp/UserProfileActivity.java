package com.votingapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.Voting;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ArrayList<Voting> votings = AppController.userProfile.getVotings();
        ArrayList<Poll> polls = AppController.userProfile.getPolls();
        ArrayList<Referendum> referendums = AppController.userProfile.getReferendums();

        LinearLayout userProfileLinearLayout = (LinearLayout) findViewById(R.id.userProfileLinearLayout);

        TextView votingsTitleTextView = new TextView(this);
        votingsTitleTextView.setText("Гласувания");
        votingsTitleTextView.setTextAppearance(this, R.style.text_vote_title);
        votingsTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        userProfileLinearLayout.addView(votingsTitleTextView);

        for(Voting voting : votings){
            TextView votingTextView = new TextView(this);
            votingTextView.setText(voting.getTitle());
            votingTextView.setTextAppearance(this, R.style.text_vote_title);
            votingTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            userProfileLinearLayout.addView(votingTextView);
        }

        TextView pollsTitleTextView = new TextView(this);
        pollsTitleTextView.setText("Анкети");
        pollsTitleTextView.setTextAppearance(this, R.style.text_vote_title);
        pollsTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        userProfileLinearLayout.addView(pollsTitleTextView);

        TextView referendumsTitleTextView = new TextView(this);
        referendumsTitleTextView.setText("Гласувания");
        referendumsTitleTextView.setTextAppearance(this, R.style.text_vote_title);
        referendumsTitleTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        userProfileLinearLayout.addView(referendumsTitleTextView);
    }
}
