package com.votingapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Referendum;

/**
 * Created by giivanov on 5.05.19.
 */

public class CreateReferendumFragment extends Fragment {

    public CreateReferendumFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_referendum, container, false);
        getActivity().setTitle("Създаване на референдум");

        final TextView referendumTitle = (TextView) view.findViewById(R.id.create_referendum_title);
        final TextView referendumQuestion = (TextView) view.findViewById(R.id.create_referendum_question);
        Button createReferendumButton = (Button) view.findViewById(R.id.create_new_referendum_button);

        createReferendumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(referendumTitle.getText().toString()) || "".equals(referendumQuestion.getText().toString())){
                    CharSequence text = "Моля попълнете всички полета!";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Referendum newReferendum = new Referendum(
                            referendumTitle.getText().toString(), referendumQuestion.getText().toString());

                    String newReferendumId = AppController.firebaseHelper.createReferendum(newReferendum);
                    newReferendum.setId(newReferendumId);

                    CharSequence text = "Референдумът е успешно създаден";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                    toast.show();

                    getActivity().finish();
                }
            }
        });

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
