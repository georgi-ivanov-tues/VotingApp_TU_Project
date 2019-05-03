package com.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.votingapp.models.Vote;

import java.util.List;

public class VotesAdapter extends ArrayAdapter<Vote> {
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
