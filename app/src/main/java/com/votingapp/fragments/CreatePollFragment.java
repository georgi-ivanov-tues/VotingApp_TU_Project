package com.votingapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.votingapp.R;

/**
 * Created by giivanov on 5.05.19.
 */

public class CreatePollFragment extends Fragment {

    public CreatePollFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_poll, container, false);
        getActivity().setTitle("Създаване на анкета");


        return view;

    }

}
