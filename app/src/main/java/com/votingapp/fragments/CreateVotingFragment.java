package com.votingapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Question;
import com.votingapp.models.Voting;
import java.util.ArrayList;

/**
 * Created by giivanov on 5.05.19.
 */

public class CreateVotingFragment extends Fragment {

    public CreateVotingFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_voting, container, false);
        getActivity().setTitle("Създаване на гласуване");

        final TextView votingTitle = (TextView) view.findViewById(R.id.create_voting_title);
        final TextView votingQuestion = (TextView) view.findViewById(R.id.create_voting_question);
        Button createVotingButton = (Button) view.findViewById(R.id.create_new_voting_button);
        Button addOptionButton = (Button) view.findViewById(R.id.voting_add_option);

        final LinearLayout createVotingLinearLayout = (LinearLayout) view.findViewById(R.id.create_voting_linear_layout);

        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout newTextInputLayout = new TextInputLayout(getActivity());

                LinearLayout.LayoutParams paramsTextInput = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                paramsTextInput.setMargins(0, 8, 0, 8 );

                newTextInputLayout.setLayoutParams(paramsTextInput);

                int optionsCount = createVotingLinearLayout.getChildCount() - 2; // -2 because of title and question fields
                newTextInputLayout.setHint("Отговор " + (optionsCount + 1)); //

                EditText newEditText = new EditText(getActivity());

                LinearLayout.LayoutParams paramsEditText = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                newEditText.setLayoutParams(paramsEditText);

                newTextInputLayout.addView(newEditText);

                createVotingLinearLayout.addView(newTextInputLayout);
            }
        });

        createVotingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean areAllFieldFilled = true;
                ArrayList<Option> options = new ArrayList<>();

                int childCount = createVotingLinearLayout.getChildCount();
                for(int i = 0; i < childCount; i++) {
                    TextInputLayout textInputLayout = (TextInputLayout) createVotingLinearLayout.getChildAt(i);
                    EditText editText = textInputLayout.getEditText();

                    if ("".equals(editText.getText().toString())) {
                        CharSequence text = "Моля попълнете всички полета!";
                        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                        toast.show();
                        areAllFieldFilled = false;
                        break;
                    }

                    if(i > 1) { // 0 is title field, 1 is question field
                        options.add(new Option(editText.getText().toString()));
                    }
                }

                if(areAllFieldFilled){
                    Voting newVoting = new Voting(
                            votingTitle.getText().toString(), new Question(votingQuestion.getText().toString()), options);

                    AppController.databaseHelper.insertVoting(newVoting);
                    AppController.sendNotificitaion(newVoting);

                    CharSequence text = "Гласуването е успешно създадено";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();

                    getActivity().finish();
                }
            }
        });

        return view;

    }
}
