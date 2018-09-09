package com.votingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        ((TextView) view.findViewById(R.id.referendumТitle)).setText(referendum.getTitle());

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = (View) view.getParent();
                RadioGroup radioGroup = (RadioGroup) parentView.findViewById(R.id.referendum_radio_group);
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if(selectedId == -1){
                    CharSequence text = "Моля отговорете на референдума!";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    RadioButton radioButton = (RadioButton) parentView.findViewById(selectedId);
                    if ("Да".equals(radioButton.getText()))
                        referendum.getOptionYes().increaseTimesSelected();
                    else if ("Не".equals(radioButton.getText()))
                        referendum.getOptionNo().increaseTimesSelected();

                    System.out.println("REFERENDUM OPTION YES = " + referendum.getOptionYes().getTimesSelected());
                    System.out.println("REFERENDUM OPTION NO = " + referendum.getOptionNo().getTimesSelected());
                }
            }
        });
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
