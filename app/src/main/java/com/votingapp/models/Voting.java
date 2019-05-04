package com.votingapp.models;

import android.annotation.SuppressLint;
import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Voting extends Vote{

    private Question question;
    private ArrayList<Option> options;

    public Voting(String title, Question question, ArrayList<Option> options) {
        super(title);
        this.question = question;
        this.options = options;
    }

    public Voting(){}

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }
}
