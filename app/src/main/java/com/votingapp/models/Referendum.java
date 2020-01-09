package com.votingapp.models;

import com.google.firebase.database.Exclude;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Referendum extends Vote {

    private String question;
    private Option optionYes = new Option("Да");
    private Option optionNo = new Option("Не");

    public Referendum(){}

    public Referendum(String id, String title, String question) {
        super(id, title);
        this.question = question;
    }

    public Referendum(String title, String question) {
        super(title);
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Exclude
    public Option getOptionYes() {
        return optionYes;
    }

    public int getYesSelectedTimes() {
        return optionYes.getTimesSelected();
    }

    public void setOptionYes(Option optionYes) {
        this.optionYes = optionYes;
    }

    @Exclude
    public Option getOptionNo() {
        return optionNo;
    }

    public int getNoSelectedTimes() {
        return optionNo.getTimesSelected();
    }

    public void setOptionNo(Option optionNo) {
        this.optionNo = optionNo;
    }

    public String getTitle(){ return super.getTitle(); }
}
