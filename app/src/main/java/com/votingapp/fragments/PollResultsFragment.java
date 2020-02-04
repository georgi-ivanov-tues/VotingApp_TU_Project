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
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.utils.Keys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PollResultsFragment extends Fragment {
    public PollResultsFragment() {}

    public static PollResultsFragment newInstance(String param1, String param2) {
        PollResultsFragment fragment = new PollResultsFragment();
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
        View view = inflater.inflate(R.layout.fragment_poll_results, container, false);
        final Poll poll = (Poll) getArguments().getSerializable(Keys.VOTE_OBJECT);
        final HashMap<String, String> votedOption = (HashMap<String, String>) getArguments().getSerializable(Keys.VOTED_OPTION);
        final LinearLayout takeVotingLinearLayout = (LinearLayout) view.findViewById(R.id.pollResultsOptionsLinearLayout);
        takeVotingLinearLayout.addView(new ScrollView(getActivity()));
        getActivity().setTitle(poll.getTitle());

        FirebaseDatabase.getInstance().getReference().child("polls").child(poll.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    takeVotingLinearLayout.removeAllViews();

                    for (Map.Entry pair : poll.getContent().entrySet()) {
                        String question = (String) pair.getKey();
                        if (question.charAt(0) == '"' && question.charAt(question.length() - 1) == '"') {
                            question = question.substring(1, question.length() - 1);
                        }

                        ArrayList<Option> options = (ArrayList) pair.getValue();
                        if (getActivity() != null) {
                            TextView questionTitle = new TextView(getActivity());
                            questionTitle.setText(question);
                            LinearLayout.LayoutParams questionParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            questionParams.setMargins(10, 20, 0, 20);
                            questionTitle.setLayoutParams(questionParams);

                            questionTitle.setTextAppearance(getActivity(), R.style.text_vote_title);
                            takeVotingLinearLayout.addView(questionTitle);
                            int totalNumberOfVotes = AppController.getTotalNumberOfVotes(options);
                            for (int i = 0; i < options.size(); i++) {
                                Option option = options.get(i);

                                option.setTimesSelected(((Long) dataSnapshot.child("content").child((String) pair.getKey()).child(option.getId()).child("timesSelected").getValue()).intValue());

                                TextView optionTextView = new TextView(getActivity());
                                LinearLayout.LayoutParams optionParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                optionParams.setMargins(10, 20, 0, 20);
                                optionTextView.setLayoutParams(optionParams);

                                double optionPercentage = AppController.calculateOptionPercentage(option, totalNumberOfVotes);
                                optionTextView.setText(AppController.formatOptionPercentage(option, optionPercentage));
                                optionTextView.setTextAppearance(getActivity(), R.style.text_vote_result_option);
                                for (Map.Entry<String, String> votedPair : votedOption.entrySet()) {
                                    if (question.equals(votedPair.getKey().replace("\"", "")) &&
                                            option.getId().equals(votedPair.getValue().replace("\"", "")))
                                        optionTextView.setTypeface(null, Typeface.BOLD);
                                }

                                takeVotingLinearLayout.addView(optionTextView);

                                // Add Percentage Bars
                                takeVotingLinearLayout.addView(AppController.createPercentangeBars(getActivity(), optionPercentage));
                            }
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

    // TODO: Rename method, update argument and hook method into UI event
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
