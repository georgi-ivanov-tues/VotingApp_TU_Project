package com.votingapp.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(VehicleActivity.this);
                    builder.setTitle(R.string.vehicle_activity_view_on_back_pressed)
                            .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                    */
                }else {
                    RadioButton radioButton = (RadioButton) parentView.findViewById(selectedId);
                    if ("Да".equals(radioButton.getText()))
                        referendum.getOptionYes().increaseTimesSelected();
                    else if ("Не".equals(radioButton.getText()))
                        referendum.getOptionNo().increaseTimesSelected();


                    System.out.println("REFERENDUM OPTION YES = " + referendum.getOptionYes().getTimesSelected());
                    System.out.println("REFERENDUM OPTION NO = " + referendum.getOptionNo().getTimesSelected());

                    ReferendumResultsFragment takePollFragment = new ReferendumResultsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Keys.VOTE_OBJECT, referendum);
                    takePollFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.list_content_fragment, takePollFragment);
                    transaction.addToBackStack(null);
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
