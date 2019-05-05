package com.votingapp.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.votingapp.R;
import com.votingapp.models.Referendum;
import com.votingapp.utils.Keys;

/**
 * Created by giivanov on 5.05.19.
 */

public class CreateVoteMainFragment extends Fragment {

    public CreateVoteMainFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_vote_main, container, false);
        getActivity().setTitle("Създаване на вот");

        Button createVotingButton = (Button) view.findViewById(R.id.button_create_voting);
        Button createPollButton = (Button) view.findViewById(R.id.button_create_poll);
        Button createReferendumButton = (Button) view.findViewById(R.id.button_create_referendum);

        createVotingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateVotingFragment createVotingFragment = new CreateVotingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.create_vote_fragment, createVotingFragment);
                transaction.commit();
            }
        });

        createPollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePollFragment createPollFragment = new CreatePollFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.create_vote_fragment, createPollFragment);
                transaction.commit();
            }
        });

        createReferendumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateReferendumFragment createReferendumFragment = new CreateReferendumFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.create_vote_fragment, createReferendumFragment);
                transaction.commit();
            }
        });


        return view;
    }


}
