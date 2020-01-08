package com.votingapp.models;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.firebase.database.Exclude;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Referendum extends Vote {

    private Question question;
    private Option optionYes = new Option("Да");
    private Option optionNo = new Option("Не");

    public static final String TABLE_NAME = "referendums";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_QUESTION_ID = "questionId";
    public static final String COLUMN_YES_SELECTED_TIMES = "yesSelectedTimes";
    public static final String COLUMN_NO_SELECTED_TIMES = "noSelectedTimes";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT NOT NULL,"
                    + COLUMN_QUESTION_ID + " INTEGER NOT NULL,"
                    + COLUMN_YES_SELECTED_TIMES + " INTEGER NOT NULL,"
                    + COLUMN_NO_SELECTED_TIMES + " INTEGER NOT NULL, "
                    + "FOREIGN KEY ("+COLUMN_QUESTION_ID+") REFERENCES "+Question.TABLE_NAME+"("+Question.COLUMN_ID+")"
                    + ");";


    public Referendum(){}

    public Referendum(String id, String title, Question question) {
        super(id, title);
        this.question = question;
    }

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
