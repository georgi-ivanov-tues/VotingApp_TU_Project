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
        View view = inflater.inflate(R.layout.fragment_voting_results, container, false);
        final Voting voting = (Voting) getArguments().getSerializable(Keys.VOTE_OBJECT);
        ((TextView) view.findViewById(R.id.votingResultsТitle)).setText(voting.getQuestion());
        LinearLayout votingResultsLinearLayout = (LinearLayout) view.findViewById(R.id.votingResultsLinearLayout);

        int totalNumberOfVotes = AppController.getTotalNumberOfVotes(voting.getOptions());
        for(Option option : voting.getOptions()){
            TextView optionTextView = new TextView(getActivity());
            optionTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            double optionPercentage = AppController.calculateOptionPercentage(option, totalNumberOfVotes);
            optionTextView.setText(AppController.formatOptionPercentage(option, optionPercentage));
            optionTextView.setTextAppearance(getActivity(), R.style.text_vote_result_option);
            if(option.isSelectedByCurrentUser())
                optionTextView.setTypeface(null, Typeface.BOLD);

            votingResultsLinearLayout.addView(optionTextView);

            votingResultsLinearLayout.addView(AppController.createPercentangeBars(getActivity(), optionPercentage));
        }

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
