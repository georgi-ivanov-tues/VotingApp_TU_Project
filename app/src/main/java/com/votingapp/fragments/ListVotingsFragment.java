package com.votingapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.VotesAdapter;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListVotingsFragment extends Fragment {

    private VotesAdapter votesAdapter;
    private List<Vote> votesData;

    public ListVotingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        this.votesData = new ArrayList();
//        votesAdapter = new VotesAdapter(getActivity(), votesData);
//        setListAdapter(this.votesAdapter);
//        this.votesAdapter.clear();
//
//        this.votesAdapter.addAll(AppController.getVotesByType(Voting.class));
//        getActivity().setTitle("Списък от гласувания");

        View view = inflater.inflate(R.layout.fragment_list_votings, container, false);

        LinearLayout viewById = (LinearLayout) view.findViewById(R.id.ListVotingsFragmentLinearLayout);

        ArrayList<Vote> votesByType = AppController.getVotesByType(Voting.class);

        for(Vote vote : votesByType){
            ContextThemeWrapper rowViewContext = new ContextThemeWrapper(getActivity(), R.style.activity_login_button);
            Button rowView = new Button(rowViewContext);
            rowView.setText(vote.getTitle());
            viewById.addView(rowView);
        }

        return view;
    }

}
