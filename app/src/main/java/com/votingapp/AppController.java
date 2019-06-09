package com.votingapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.votingapp.models.Option;
import com.votingapp.models.User;
import com.votingapp.models.Vote;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class AppController extends Application{

    private static AppController mInstance;

    public static ArrayList<User> allUsers = new ArrayList<>();
    public static ArrayList<Vote> votes = new ArrayList<>();
    public static User loggedUser;
    public static Vote currentVote;

    public static DatabaseHelper databaseHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        databaseHelper = new DatabaseHelper(this);

        System.out.println("Application created!!!");
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Vote getCurrentVote() {
        return currentVote;
    }

    public static void setCurrentVote(Vote currentVote) {
        AppController.currentVote = currentVote;
    }

    public static ArrayList<Vote> getVotesByType(Class type) {
        ArrayList<Vote> votesByType = new ArrayList();
        AppController.getInstance();
        Iterator it = AppController.votes.iterator();
        while (it.hasNext()) {
            Vote vote = (Vote) it.next();
            if (vote.getClass().equals(type)) {
                votesByType.add(vote);
            }
        }
        return votesByType;
    }

    public static int getTotalNumberOfVotes(ArrayList<Option> options){
        int totalNumberOfVotes = 0;
        for(Option option : options){
            totalNumberOfVotes += option.getTimesSelected();
        }
        return totalNumberOfVotes;
    }

    public static double calculateOptionPercentage(Option option, int totalNumberOfVotes){
        return (option.getTimesSelected() / (double) totalNumberOfVotes) * 100;
    }

    public static String formatOptionPercentage(Option option, double optionPercentage){
        String optionTextPercentage = new DecimalFormat("##.##").format(optionPercentage) + "%";
        return option.getOptionText() + " - " + optionTextPercentage + " ("  + option.getTimesSelected() + ")";
    }

    @SuppressLint("ResourceAsColor")
    public static LinearLayout createPercentangeBars(Context context, double optionPercentage){
        LinearLayout percentageBarLinearLayout = new LinearLayout(context);
        percentageBarLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        percentageBarLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View optionPercentageBarLeft = new View(context);
        optionPercentageBarLeft.setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) (optionPercentage / 100)));
        optionPercentageBarLeft.setBackgroundColor(R.color.dodgerBlue);
        percentageBarLinearLayout.addView(optionPercentageBarLeft);

        View optionPercentageBarRight = new View(context);
        optionPercentageBarRight.setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) ((100 - optionPercentage) / 100)));
        percentageBarLinearLayout.addView(optionPercentageBarRight);

        return percentageBarLinearLayout;
    }
}
