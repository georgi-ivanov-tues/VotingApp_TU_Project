package com.votingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.votingapp.R;
import com.votingapp.models.Referendum;
import com.votingapp.utils.Keys;

public class ReferendumResultsFragment extends Fragment {
    public ReferendumResultsFragment() {
        // Required empty public constructor
    }

    public static ReferendumResultsFragment newInstance(String param1, String param2) {
        ReferendumResultsFragment fragment = new ReferendumResultsFragment();
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
        View view = inflater.inflate(R.layout.fragment_referendum_results, container, false);
        final Referendum referendum = (Referendum) getArguments().getSerializable(Keys.VOTE_OBJECT);
        ((TextView) view.findViewById(R.id.referendumResultsТitle)).setText(referendum.getTitle());
        ((TextView) view.findViewById(R.id.referendumResultsYesOption)).setText(
                referendum.getOptionYes().getOptionText() + " - " + referendum.getOptionYes().getTimesSelected());
        ((TextView) view.findViewById(R.id.referendumResultsNoOption)).setText(
                referendum.getOptionNo().getOptionText() + " - " + referendum.getOptionNo().getTimesSelected());

        return view;
    }

    public void onBackPressed() {
        super.onResume();
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
