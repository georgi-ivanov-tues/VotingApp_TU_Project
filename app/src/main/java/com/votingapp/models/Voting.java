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

    public static final String TABLE_NAME_VOTINGS = "votings";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_QUESTION_ID = "questionId";

    public static final String CREATE_TABLE_VOTINGS =
            "CREATE TABLE " + TABLE_NAME_VOTINGS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT NOT NULL,"
                    + COLUMN_QUESTION_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY ("+COLUMN_QUESTION_ID+") REFERENCES "+Question.TABLE_NAME+"("+Question.COLUMN_ID+")"
                    + ");";

    public static final String TABLE_NAME_VOTING_OPTIONS = "voting_options";

    public static final String COLUMN_VOTING_ID = "votingId";
    public static final String COLUMN_OPTION_ID = "optionId";

    public static final String CREATE_TABLE_VOTING_OPTIONS =
            "CREATE TABLE " + TABLE_NAME_VOTING_OPTIONS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_VOTING_ID + " INTEGER NOT NULL,"
                    + COLUMN_OPTION_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY ("+COLUMN_VOTING_ID+") REFERENCES "+TABLE_NAME_VOTINGS+"("+COLUMN_ID+"),"
                    + "FOREIGN KEY ("+COLUMN_OPTION_ID+") REFERENCES "+Option.TABLE_NAME+"("+Option.COLUMN_ID+")"
                    + ");";

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
