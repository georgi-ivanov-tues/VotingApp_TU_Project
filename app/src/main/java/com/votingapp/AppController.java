package com.votingapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.models.Referendum;
import com.votingapp.models.UserProfile;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class AppController extends Application{

    private static AppController mInstance;
    public static UserProfile userProfile;
    public static ArrayList<Vote> votes = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        userProfile = new UserProfile();
        System.out.println("Application created!!!");
    }

    public static synchronized AppController getInstance() {
        return mInstance;
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
