package com.votingapp.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Paint;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Referendum;
import com.votingapp.utils.Keys;

import java.util.ArrayList;

public class TakeReferendumFragment extends Fragment {

    // TODO: Rename and change types of parameters

    public TakeReferendumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_referendum, container, false);
        final Referendum referendum = (Referendum) getArguments().getSerializable(Keys.VOTE_OBJECT);
        ((TextView) view.findViewById(R.id.referendumТitle)).setText(referendum.getQuestion());
        getActivity().setTitle(referendum.getTitle());

        final RadioButton buttonYes = (RadioButton) view.findViewById(R.id.radio_yes);
        final RadioButton buttonNo = (RadioButton) view.findViewById(R.id.radio_no);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonYes.setPaintFlags(buttonYes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                buttonNo.setPaintFlags(buttonNo.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNo.setPaintFlags(buttonNo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                buttonYes.setPaintFlags(buttonYes.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = (View) view.getParent();
                RadioGroup radioGroup = (RadioGroup) parentView.findViewById(R.id.referendum_radio_group);
                int selectedId = radioGroup.getCheckedRadioButtonId();

                ArrayList<String> selectedByCurrentUserOptionsId = new ArrayList<>();

                if(selectedId == -1){
                    CharSequence text = "Моля отговорете на референдума!";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    RadioButton radioButton = (RadioButton) parentView.findViewById(selectedId);
                    String selectedOption = "";
                    String selectedOptionTimes = "";
                    if ("Да".equals(radioButton.getText())) {
                        referendum.getOptionYes().increaseTimesSelected();
                        selectedByCurrentUserOptionsId.add(referendum.getOptionYes().getId());

                        selectedOption = "Да";
                        selectedOptionTimes = "yesSelectedTimes";
                    }else if ("Не".equals(radioButton.getText())) {
                        referendum.getOptionNo().increaseTimesSelected();
                        selectedByCurrentUserOptionsId.add(referendum.getOptionNo().getId());

                        selectedOption = "Не";
                        selectedOptionTimes = "noSelectedTimes";
                    }

                    AppController.loggedUser.addVote(referendum);

                    // Update record in DB
                    DatabaseReference selectedOptionDatabaseReference =
                            FirebaseDatabase.getInstance().getReference().child("referendums").child(referendum.getId()).child(selectedOptionTimes);

                    AppController.firebaseHelper.castVote(selectedOptionDatabaseReference);

                    AppController.firebaseHelper.addToVotedByUser(referendum, null, selectedOption, "referendums");
                    AppController.loggedUser.getReferendumsVotedByUser().put(referendum.getId(), selectedOption);

                    ReferendumResultsFragment referendumResultsFragment = new ReferendumResultsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Keys.VOTE_OBJECT, referendum);
                    bundle.putSerializable(Keys.VOTED_OPTION, selectedOption);
                    referendumResultsFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.list_content_fragment, referendumResultsFragment);
                    transaction.commit();
                }
            }
        });
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
