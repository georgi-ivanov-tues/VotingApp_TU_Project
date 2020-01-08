package com.votingapp.fragments;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.utils.Keys;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class TakePollFragment extends Fragment {
    public TakePollFragment() {
        // Required empty public constructor
    }

    public static TakePollFragment newInstance(String param1, String param2) {
        TakePollFragment fragment = new TakePollFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_poll, container, false);
        final Poll poll = (Poll) getArguments().getSerializable(Keys.VOTE_OBJECT);
        getActivity().setTitle(poll.getTitle());

        LinearLayout takePollLinearLayout = (LinearLayout) view.findViewById(R.id.takePollLinearLayout);
        final ArrayList<TextView> questions = new ArrayList<>();
        final ArrayList<RadioGroup> radioGroups = new ArrayList<>();
        for (Map.Entry pair : poll.getPollContent().entrySet()) {
            Question question = (Question) pair.getKey();
            ArrayList<Option> options = (ArrayList) pair.getValue();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            TextView questionTitle = new TextView(getActivity());
            questionTitle.setText(question.getQuestionText());
            questionTitle.setTextAppearance(getActivity(), R.style.text_vote_title);
            takePollLinearLayout.addView(questionTitle);
            RadioGroup radioGroup = new RadioGroup(getActivity());
            RadioButton[] radioButtonOptions = new RadioButton[options.size()];
            for (int i = 0; i < options.size(); i++) {
                ContextThemeWrapper radioButtonContext = new ContextThemeWrapper(getActivity(), R.style.take_voting_radio_button);
                radioButtonOptions[i] = new RadioButton(radioButtonContext);
                radioButtonOptions[i].setText((options.get(i)).getOptionText());
                radioGroup.addView(radioButtonOptions[i]);
            }
            Random random = new Random();
            radioGroup.setId(random.nextInt());
            takePollLinearLayout.addView(radioGroup);
            radioGroups.add(radioGroup);
            questions.add(questionTitle);
        }
        ContextThemeWrapper saveButtonContext = new ContextThemeWrapper(getActivity(), R.style.activity_login_button);
        Button saveButton = new Button(saveButtonContext);
        saveButton.setBackgroundColor(R.color.dodgerBlue);
        saveButton.setText("Гласуване");
        takePollLinearLayout.addView(saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parentView = (View) view.getParent();
                int i = 0;

                ArrayList<String> selectedByCurrentUserOptionsId = new ArrayList<>();

                boolean allQuestionsAnswered = true;
                for (Map.Entry pair : poll.getPollContent().entrySet()) {
                    RadioGroup radioGroup = radioGroups.get(i);
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if(selectedId == -1) {
                        CharSequence text = "Моля попълнете цялата анкета!";
                        allQuestionsAnswered = false;
                        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        Question question = (Question) pair.getKey();
                        ArrayList<Option> options = (ArrayList) pair.getValue();

                        RadioButton radioButton = (RadioButton) parentView.findViewById(selectedId);

                        for (Option option : options) {
                            if (option.getOptionText().equals(radioButton.getText())) {
                                option.increaseTimesSelected();
                                option.setSelectedByCurrentUser(true);
                                selectedByCurrentUserOptionsId.add(option.getId());

                                // Update record in DB
                                AppController.firebaseHelper.updatePoll(poll, question, option);
                            }
                        }

                        System.out.println(question.getQuestionText());
                        for (Option option : options) {
                            System.out.println(option.getOptionText() + " = " + option.getTimesSelected());
                        }

                        i++;
                    }
                }

                if(allQuestionsAnswered) {
                    AppController.loggedUser.addVote(poll);
                    AppController.loggedUser.addUserVote(poll, selectedByCurrentUserOptionsId);

                    PollResultsFragment pollResultsFragment = new PollResultsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Keys.VOTE_OBJECT, poll);
                    pollResultsFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.list_content_fragment, pollResultsFragment);
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
