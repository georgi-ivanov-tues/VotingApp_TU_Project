package com.votingapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import com.votingapp.utils.Keys;
import java.util.ArrayList;
import java.util.List;

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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.list_view_item, parent, false);
            TextView vote_title = (TextView) rowView.findViewById(R.id.vote_title);
            Vote currRowVeh = values.get(position);
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
