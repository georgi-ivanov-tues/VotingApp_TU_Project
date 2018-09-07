package com.votingapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ListFragment extends android.app.ListFragment {
    private VotesAdapter votesAdapter;
    private List<Vote> votesData;

    public interface SelectionListener {
        void onItemSelected(Vote vote);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        System.out.println("List Fragment on Create");
//        super.onCreate(savedInstanceState);
//        votesData = new ArrayList<>();
//        votesAdapter = new VotesAdapter(getActivity(), votesData);
//        setListAdapter(votesAdapter);
//        votesAdapter.clear();
////        Vote[] votes = (Vote[]) AppController.getInstance().votes.toArray();
//        if(AppController.getInstance().votes != null)
//            votesAdapter.addAll(AppController.getInstance().votes);
//        // ?????????????

        super.onCreate(savedInstanceState);
        String listType = (String) getArguments().getSerializable(Keys.VOTING_ACTIVITY_FRAGMENT);
        this.votesData = new ArrayList();
        votesAdapter = new VotesAdapter(getActivity(), votesData);
        setListAdapter(this.votesAdapter);
        this.votesAdapter.clear();
        if (Keys.LIST_VOTINGS.equals(listType)) {
            this.votesAdapter.addAll(getVotesByType(Voting.class));
        } else if (Keys.LIST_POOLS.equals(listType)) {
            this.votesAdapter.addAll(getVotesByType(Poll.class));
        } else if (Keys.LIST_REFERENDUMS.equals(listType)) {
            this.votesAdapter.addAll(getVotesByType(Referendum.class));
        }
    }


    private SelectionListener mSelectionCallback;

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

    // for versions below 23
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        if ( context !=null && context instanceof SelectionListener) {
            mSelectionCallback = (SelectionListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.list_view, null);
        //v.setBackgroundColor(Color.LTGRAY);
        return v;
    }

    private ArrayList<Vote> getVotesByType(Class type) {
        ArrayList<Vote> votesByType = new ArrayList();
        AppController.getInstance();
        Iterator it = AppController.votes.iterator();
        while (it.hasNext()) {
            Vote vote = (Vote) it.next();
            if (vote.getClass().equals(type)) {
                System.out.println("TRUE TRUE TRUE");
                votesByType.add(vote);
            }
        }
        return votesByType;
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        super.onActivityCreated(bundle);
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
