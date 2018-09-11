package com.votingapp.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Poll extends Vote {

    private HashMap<Question, ArrayList<Option>> pollContent;

    public Poll(){}

    public Poll(String title, HashMap<Question, ArrayList<Option>> pollContent) {
        super(title);
        this.pollContent = pollContent;
    }

    public HashMap<Question, ArrayList<Option>> getPollContent() {
        return pollContent;
    }

    public void setPollContent(HashMap<Question, ArrayList<Option>> pollContent) {
        this.pollContent = pollContent;
    }
}
