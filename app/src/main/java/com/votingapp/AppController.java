package com.votingapp;

import android.app.Application;
import android.graphics.Path;

import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.models.Referendum;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class AppController extends Application{

    private static AppController mInstance;
    public static ArrayList<Vote> votes = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

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
}
