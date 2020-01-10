package com.votingapp.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;

import java.util.ArrayList;

public class TakeVotingFragment extends Fragment {

    public TakeVotingFragment() {
        // Required empty public constructor
    }

    public static TakeVotingFragment newInstance(String param1, String param2) {
        TakeVotingFragment fragment = new TakeVotingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_voting, container, false);
        final Voting voting = (Voting) getArguments().getSerializable(Keys.VOTE_OBJECT);
        ((TextView) view.findViewById(R.id.votingТitle)).setText(voting.getQuestion());
        getActivity().setTitle(voting.getTitle());

        LinearLayout takeVotingLinearLayout = (LinearLayout) view.findViewById(R.id.takeVotingLinearLayout);
        RadioGroup radioGroup = new RadioGroup(getActivity());
        radioGroup.setId(new Integer(1234));
        final RadioButton[] options = new RadioButton[voting.getOptions().size()];
        for (int i = 0; i < voting.getOptions().size(); i++) {
            ContextThemeWrapper radioButtonContext = new ContextThemeWrapper(getActivity(), R.style.take_voting_radio_button);
            options[i] = new RadioButton(radioButtonContext);
            options[i].setText((voting.getOptions().get(i)).getOptionText());
            radioGroup.addView(options[i]);
        }

        takeVotingLinearLayout.addView(radioGroup);
        ContextThemeWrapper saveButtonContext = new ContextThemeWrapper(getActivity(), R.style.activity_login_button);
        Button saveButton = new Button(saveButtonContext);
        saveButton.setBackgroundColor(R.color.dodgerBlue);
        saveButton.setText("Гласуване");
        takeVotingLinearLayout.addView(saveButton);

        final RadioGroup radioGroupFinal = radioGroup;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = (View) view.getParent();

                int selectedId = radioGroupFinal.getCheckedRadioButtonId();

                if(selectedId == -1) {
                    CharSequence text = "Моля отговорете на гласуването!";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    RadioButton radioButton = (RadioButton) parentView.findViewById(selectedId);

                    ArrayList<String> selectedByCurrentUserOptionsId = new ArrayList<>();

                    for (Option option : voting.getOptions()) {
                        if (option.getOptionText().equals(radioButton.getText())) {
                            option.increaseTimesSelected();
                            option.setSelectedByCurrentUser(true);
                            selectedByCurrentUserOptionsId.add(option.getId());

                            // Update record in DB
                            DatabaseReference selectedOptionDatabaseReference =
                                    FirebaseDatabase.getInstance().getReference().child("votings").child(voting.getId()).
                                    child("options").child(option.getId()).child("timesSelected");

                            AppController.firebaseHelper.castVote(selectedOptionDatabaseReference);
                        }
                    }

                    AppController.loggedUser.addVote(voting);
                    AppController.loggedUser.addUserVote(voting, selectedByCurrentUserOptionsId);

                    VotingResultsFragment votingResultsFragment = new VotingResultsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Keys.VOTE_OBJECT, voting);
                    votingResultsFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.list_content_fragment, votingResultsFragment);
                    transaction.commit();
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
