package com.votingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.utils.Keys;

import java.util.ArrayList;
import java.util.Map;

public class TakePollFragment extends Fragment {
    public TakePollFragment() {
        // Required empty public constructor
    }

    public static TakePollFragment newInstance(String param1, String param2) {
        TakePollFragment fragment = new TakePollFragment();
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
        View view = inflater.inflate(R.layout.fragment_take_poll, container, false);
        Poll poll = (Poll) getArguments().getSerializable(Keys.VOTE_OBJECT);
        ((TextView) view.findViewById(R.id.pollTitle)).setText(poll.getTitle());
        LinearLayout takeVotingLinearLayout = (LinearLayout) view.findViewById(R.id.takePollLinearLayout);
        takeVotingLinearLayout.addView(new ScrollView(getActivity()));
        for (Map.Entry pair : poll.getPollContent().entrySet()) {
            Question question = (Question) pair.getKey();
            ArrayList<Option> options = (ArrayList) pair.getValue();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            TextView questionTitle = new TextView(getActivity());
            questionTitle.setText(question.getQuestionText());
            takeVotingLinearLayout.addView(questionTitle);
            RadioGroup radioGroup = new RadioGroup(getActivity());
            RadioButton[] radioButtonOptions = new RadioButton[options.size()];
            for (int i = 0; i < options.size(); i++) {
                radioButtonOptions[i] = new RadioButton(getActivity());
                radioButtonOptions[i].setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
                radioButtonOptions[i].setText(((Option) options.get(i)).getOptionText());
                radioGroup.addView(radioButtonOptions[i]);
            }
            takeVotingLinearLayout.addView(radioGroup);
        }
        Button save = new Button(getActivity());
        save.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        save.setText("Гласуване");
        takeVotingLinearLayout.addView(save);
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
