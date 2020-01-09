package com.votingapp.models;

import java.util.ArrayList;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Voting extends Vote{

    private String question;
    private ArrayList<Option> options;

    public Voting(String title, String question, ArrayList<Option> options) {
        super(title);
        this.question = question;
        this.options = options;
    }

    public Voting(){}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public String getTitle(){ return super.getTitle(); }
}
