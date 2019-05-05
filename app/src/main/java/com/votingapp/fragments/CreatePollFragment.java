package com.votingapp.fragments;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;

import java.util.ArrayList;

/**
 * Created by giivanov on 5.05.19.
 */

public class CreatePollFragment extends Fragment {

    private Poll newPoll;

    public CreatePollFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_poll, container, false);
        getActivity().setTitle("Създаване на анкета");

        final TextView pollTitle = (TextView) view.findViewById(R.id.create_poll_title);
        final TextView pollQuestion = (TextView) view.findViewById(R.id.create_poll_question);
        Button createPollButton = (Button) view.findViewById(R.id.create_new_poll_button);
        Button addOptionButton = (Button) view.findViewById(R.id.poll_add_option);
        Button addQuestionButton = (Button) view.findViewById(R.id.poll_add_question);

        final LinearLayout createPollLinearLayout = (LinearLayout) view.findViewById(R.id.create_poll_linear_layout);
        final LinearLayout createPollQuestionLinearLayout = (LinearLayout) view.findViewById(R.id.question_linear_layout);
        final LinearLayout createPollQuestionOptionsLinearLayout = (LinearLayout) view.findViewById(R.id.question_options_linear_layout);

        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOptionToQuestion(createPollQuestionOptionsLinearLayout);
            }
        });

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add question
                TextInputLayout newTextInputLayout = new TextInputLayout(getActivity());

                LinearLayout.LayoutParams paramsTextInput = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                paramsTextInput.setMargins(0, 8, 0, 8 );

                newTextInputLayout.setLayoutParams(paramsTextInput);

                newTextInputLayout.setHint("Въпрос");

                EditText newEditText = new EditText(getActivity());

                LinearLayout.LayoutParams paramsEditText = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                newEditText.setLayoutParams(paramsEditText);

                newTextInputLayout.addView(newEditText);

                createPollQuestionLinearLayout.addView(newTextInputLayout);

                // Add options
                final LinearLayout optionsLinearLayout = new LinearLayout(getActivity());
                LinearLayout.LayoutParams paramsOptionsLinearLayout = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                optionsLinearLayout.setLayoutParams(paramsOptionsLinearLayout);
                optionsLinearLayout.setOrientation(LinearLayout.VERTICAL);
                optionsLinearLayout.setPadding(20, 0, 0,0);

                addOptionToQuestion(optionsLinearLayout);
                addOptionToQuestion(optionsLinearLayout);

                Button buttonAddOption = new Button(getActivity());
                LinearLayout.LayoutParams paramsButtonAddOption = new LinearLayout.LayoutParams(
                        300,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                paramsButtonAddOption.gravity = Gravity.RIGHT;
                buttonAddOption.setLayoutParams(paramsButtonAddOption);
                buttonAddOption.setText("Добавяне на отговор");
                buttonAddOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addOptionToQuestion(optionsLinearLayout);
                    }
                });

                createPollQuestionLinearLayout.addView(optionsLinearLayout);
                createPollQuestionLinearLayout.addView(buttonAddOption);
            }
        });

        createPollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfFieldsEmpty(createPollLinearLayout);
            }
        });

        return view;

    }

    private void addOptionToQuestion(LinearLayout questionLinearLayout){
        TextInputLayout newTextInputLayout = new TextInputLayout(getActivity());

        LinearLayout.LayoutParams paramsTextInput = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsTextInput.setMargins(0, 8, 0, 8 );

        newTextInputLayout.setLayoutParams(paramsTextInput);

        int optionsCount = questionLinearLayout.getChildCount();
        newTextInputLayout.setHint("Отговор " + (optionsCount + 1));

        EditText newEditText = new EditText(getActivity());

        LinearLayout.LayoutParams paramsEditText = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newEditText.setLayoutParams(paramsEditText);

        newTextInputLayout.addView(newEditText);

        questionLinearLayout.addView(newTextInputLayout);
    }

    private boolean checkIfFieldsEmpty(LinearLayout linearLayout){
        boolean areAllFieldsFilled = true;
        ArrayList<Option> options = new ArrayList<>();

        int childCount = linearLayout.getChildCount();
        for(int i = 0; i < childCount; i++) {
            View view = linearLayout.getChildAt(i);
            if(view instanceof TextInputLayout){
                TextInputLayout textInputLayout = (TextInputLayout) view;
                EditText editText = textInputLayout.getEditText();

                if ("".equals(editText.getText().toString())) {
                    CharSequence text = "Моля попълнете всички полета!";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();
                    areAllFieldsFilled = false;
                    break;
                }

                if(i > 1) { // 0 is title field, 1 is question field
                    options.add(new Option(editText.getText().toString()));
                }
            }else if(view instanceof LinearLayout){
                if(!checkIfFieldsEmpty((LinearLayout) view)){
                    break;
                }
            }
        }

        return areAllFieldsFilled;
    }
}
