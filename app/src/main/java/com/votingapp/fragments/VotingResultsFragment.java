package com.votingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voting_results, container, false);
        final Voting voting = (Voting) getArguments().getSerializable(Keys.VOTE_OBJECT);
        ((TextView) view.findViewById(R.id.votingResultsТitle)).setText(voting.getTitle());
        LinearLayout votingResultsLinearLayout = (LinearLayout) view.findViewById(R.id.votingResultsLinearLayout);

        for(Option option : voting.getOptions()){
            TextView optionTextView = new TextView(getActivity());
            optionTextView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            optionTextView.setText(option.getOptionText() + " - " + option.getTimesSelected());
            optionTextView.settextst
            votingResultsLinearLayout.addView(optionTextView);
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
