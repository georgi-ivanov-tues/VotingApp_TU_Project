package com.votingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;

public class VotingResultsFragment extends Fragment {
    public VotingResultsFragment() {
    }

    public static VotingResultsFragment newInstance(String param1, String param2) {
        VotingResultsFragment fragment = new VotingResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_voting_results, container, false);
        final Voting voting = (Voting) getArguments().getSerializable(Keys.VOTE_OBJECT);
        final String votedOption = (String) getArguments().getSerializable(Keys.VOTED_OPTION);
        ((TextView) view.findViewById(R.id.votingResultsТitle)).setText(voting.getQuestion());
        final LinearLayout votingResultsLinearLayout = (LinearLayout) view.findViewById(R.id.votingResultsOptionsLinearLayout);
        getActivity().setTitle(voting.getTitle());

        FirebaseDatabase.getInstance().getReference().child("votings").child(voting.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int totalNumberOfVotes = AppController.getTotalNumberOfVotes(voting.getOptions());
                    votingResultsLinearLayout.removeAllViews();

                    for (Option option : voting.getOptions()) {
                        option.setTimesSelected(((Long) dataSnapshot.child("options").child(option.getId()).child("timesSelected").getValue()).intValue());

                        if (getActivity() != null) {
                            TextView optionTextView = new TextView(getActivity());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10, 20, 0, 20);
                            optionTextView.setLayoutParams(params);

                            double optionPercentage = AppController.calculateOptionPercentage(option, totalNumberOfVotes);
                            optionTextView.setText(AppController.formatOptionPercentage(option, optionPercentage));
                            optionTextView.setTextAppearance(getActivity(), R.style.text_vote_result_option);
                            if (option.getId().equals(votedOption))
                                optionTextView.setTypeface(null, Typeface.BOLD);

                            votingResultsLinearLayout.addView(optionTextView);

                            votingResultsLinearLayout.addView(AppController.createPercentangeBars(getActivity(), optionPercentage));
                        }
                    }
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
