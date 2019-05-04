package com.votingapp.models;

import android.annotation.SuppressLint;
import android.os.Parcel;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Referendum extends Vote {

    private Question question;
    private Option optionYes = new Option("Да");
    private Option optionNo = new Option("Не");

    public Referendum(){}

    public Referendum(String title, Question question) {
        super(title);
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Option getOptionYes() {
        return optionYes;
    }

    public void setOptionYes(Option optionYes) {
        this.optionYes = optionYes;
    }

    public Option getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(Option optionNo) {
        this.optionNo = optionNo;
    }
}
