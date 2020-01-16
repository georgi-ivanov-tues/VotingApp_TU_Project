package com.votingapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.activities.MainActivity;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends android.support.v4.app.ListFragment {

    private VotesAdapter votesAdapter;
    private List<Vote> votesData;

    public ListFragment() {
        // Required empty public constructor
    }

    public interface SelectionListener {
        void onItemSelected(Vote vote);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, null);

        loadVotes();

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Vote voteToDelete = votesAdapter.getItem(arg2);
                                AppController.firebaseHelper.deleteVoteFromDB(voteToDelete);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Искате ли да изтриете ... ?").setPositiveButton("Да", dialogClickListener).setNegativeButton("Не", dialogClickListener).show();
                return true;
            }
        });
    }

    private SelectionListener mSelectionCallback;

    @Override
    public void onResume() {
        super.onResume();

        loadVotes();
    }

    private void loadVotes(){
        String tabNum = getArguments().getString("currentTab");
        ArrayList<Vote> votesByType = new ArrayList<>();

        votesAdapter = new ListFragment.VotesAdapter(getActivity(), votesByType);
        setListAdapter(this.votesAdapter);
        this.votesAdapter.clear();
        if(Keys.TAB_VOTINGS.equals(tabNum)){
            votesByType.addAll(AppController.getVotesByType(Voting.class));
        }else if(Keys.TAB_POLLS.equals(tabNum)){
            votesByType.addAll(AppController.getVotesByType(Poll.class));
        }else if(Keys.TAB_REFERENDUMS.equals(tabNum)){
            votesByType.addAll(AppController.getVotesByType(Referendum.class));
        }
    }

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        Vote selected = votesAdapter.getItem(position);
        mSelectionCallback.onItemSelected(selected);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if ( context !=null && context instanceof SelectionListener) {
            mSelectionCallback = (SelectionListener) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        super.onActivityCreated(bundle);
    }

    // for versions below 23
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if ( context !=null && context instanceof SelectionListener) {
            mSelectionCallback = (SelectionListener) context;
        }
    }

    class VotesAdapter extends ArrayAdapter<Vote> {

        private final Context context;
        private final List<Vote> values;

        public VotesAdapter(Context context, List<Vote> values) {
            super(context, R.layout.list_view_item, values);
            this.context = context;
            this.values = values;
        }

        public void addItem(Vote aVehicle){
            this.values.add(aVehicle);
            this.notifyDataSetChanged();
        }

        public void addAll(List<Vote> votes){
            System.out.println("List Fragment addAll");
            this.values.addAll(votes);
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            final Vote currRowVeh = values.get(position);

            View rowView = inflater.inflate(R.layout.list_view_item, parent, false);
//            rowView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    System.out.println("LONG CLICK!!!!");
//                    System.out.println(currRowVeh.getTitle());
//                    return false;
//                }
//            });
//
//            rowView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
            TextView vote_title = (TextView) rowView.findViewById(R.id.vote_title);
            vote_title.setText(currRowVeh.getTitle());

            return rowView;
        }

        @Override
        public void clear(){
            if(this.values !=null) {
                this.values.clear();
            }
        }

        @Override
        public Vote getItem(int position){
            return this.values.get(position);
        }
    }

}
