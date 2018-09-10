package com.votingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.utils.Keys;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class PollResultsFragment extends Fragment {
    public PollResultsFragment() {
        // Required empty public constructor
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poll_results, container, false);
        final Poll poll = (Poll) getArguments().getSerializable(Keys.VOTE_OBJECT);
        ((TextView) view.findViewById(R.id.pollResultsТitle)).setText(poll.getTitle());
        LinearLayout takeVotingLinearLayout = (LinearLayout) view.findViewById(R.id.pollResultsLinearLayout);
        takeVotingLinearLayout.addView(new ScrollView(getActivity()));
        for (Map.Entry pair : poll.getPollContent().entrySet()) {
            Question question = (Question) pair.getKey();
            ArrayList<Option> options = (ArrayList) pair.getValue();
            TextView questionTitle = new TextView(getActivity());
            questionTitle.setText(question.getQuestionText());
            questionTitle.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            questionTitle.setTextAppearance(getActivity(), R.style.text_vote_title);
            takeVotingLinearLayout.addView(questionTitle);
            int totalNumberOfVotes = AppController.getTotalNumberOfVotes(options);
            for (int i = 0; i < options.size(); i++) {
                TextView option = new TextView(getActivity());
                option.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                String optionTextPercentage = new DecimalFormat("##.##").format(((options.get(i).getTimesSelected() / (double) totalNumberOfVotes) * 100)) + "%";
                option.setText((options.get(i)).getOptionText() + " - " + optionTextPercentage + " ("  +(options.get(i)).getTimesSelected() + ")");
                option.setTextAppearance(getActivity(), R.style.text_vote_title);
                takeVotingLinearLayout.addView(option);
            }
        }
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
