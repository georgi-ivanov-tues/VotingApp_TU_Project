package com.votingapp.fragments;

import android.content.Context;
import android.graphics.Typeface;
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
import com.votingapp.models.Referendum;
import com.votingapp.utils.Keys;

import java.util.ArrayList;

public class ReferendumResultsFragment extends Fragment {
    public ReferendumResultsFragment() {}

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
        ((TextView) view.findViewById(R.id.referendumResultsТitle)).setText(referendum.getQuestion().getQuestionText());
        ArrayList<Option> options = new ArrayList<>();
        options.add(referendum.getOptionYes());
        options.add(referendum.getOptionNo());

        int totalNumberOfVotes = AppController.getTotalNumberOfVotes(options);

        double percentageYes = AppController.calculateOptionPercentage(referendum.getOptionYes(),totalNumberOfVotes);
        TextView optionYesTextView = ((TextView) view.findViewById(R.id.referendumResultsYesOption));
        optionYesTextView.setText(AppController.formatOptionPercentage(referendum.getOptionYes(), percentageYes));
        if(referendum.getOptionYes().isSelectedByCurrentUser())
            optionYesTextView.setTypeface(null, Typeface.BOLD);

        double percentageNo = AppController.calculateOptionPercentage(referendum.getOptionNo(), totalNumberOfVotes);
        TextView optionNoTextView = ((TextView) view.findViewById(R.id.referendumResultsNoOption));
        optionNoTextView.setText(AppController.formatOptionPercentage(referendum.getOptionNo(),percentageNo));

        if(referendum.getOptionNo().isSelectedByCurrentUser())
            optionNoTextView.setTypeface(null, Typeface.BOLD);

        view.findViewById(R.id.optionYesBar).setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) (percentageYes / 100)));
        view.findViewById(R.id.optionYesBarHidden).setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) ((100 - percentageYes) / 100)));

        view.findViewById(R.id.optionNoBar).setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) (percentageNo / 100)));
        view.findViewById(R.id.optionNoBarHidden).setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) ((100 - percentageNo) / 100)));

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
