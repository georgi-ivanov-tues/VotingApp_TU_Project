package com.votingapp.models;

import android.annotation.SuppressLint;
import android.graphics.Path;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Poll extends Vote {

    private HashMap<Question, ArrayList<Option>> pollContent;

    public static final String TABLE_NAME_POLLS = "polls";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";

    public static final String CREATE_TABLE_POLLS =
            "CREATE TABLE " + TABLE_NAME_POLLS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT NOT NULL"
                    + ");";


    public static final String TABLE_NAME_POLL_QUESTIONS = "poll_questions";

    public static final String COLUMN_POLL_ID = "pollId";
    public static final String COLUMN_QUESTION_ID = "questionId";

    public static final String CREATE_TABLE_POLL_QUESTIONS =
            "CREATE TABLE " + TABLE_NAME_POLL_QUESTIONS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_POLL_ID + " INTEGER NOT NULL,"
                    + COLUMN_QUESTION_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY ("+COLUMN_POLL_ID+") REFERENCES "+TABLE_NAME_POLLS+"("+COLUMN_ID+"),"
                    + "FOREIGN KEY ("+COLUMN_QUESTION_ID+") REFERENCES "+Question.TABLE_NAME+"("+Question.COLUMN_ID+")"
                    + ");";

    public static final String TABLE_NAME_POLL_QUESTION_OPTIONS = "poll_question_options";

    public static final String COLUMN_POLL_QUESTION_ID = "pollQuestionId";
    public static final String COLUMN_QUESTION_OPTION_ID = "questionOptionId";

    public static final String CREATE_TABLE_POLL_QUESTION_OPTIONS =
            "CREATE TABLE " + TABLE_NAME_POLL_QUESTION_OPTIONS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_POLL_QUESTION_ID + " INTEGER NOT NULL,"
                    + COLUMN_QUESTION_OPTION_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY ("+COLUMN_POLL_QUESTION_ID+") REFERENCES "+Question.TABLE_NAME+"("+Question.COLUMN_ID+"),"
                    + "FOREIGN KEY ("+COLUMN_QUESTION_OPTION_ID+") REFERENCES "+ Option.TABLE_NAME+"("+Option.COLUMN_ID+")"
                    + ");";

    public Poll(){
        pollContent = new HashMap<>();
    }

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

    public void addQuestion(Question question, ArrayList<Option> options){
        pollContent.put(question,options);
    }

    public void addQuestions(HashMap<Question, ArrayList<Option>> questions){
        Iterator it = questions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            pollContent.put((Question) pair.getKey(), (ArrayList<Option>) pair.getValue());
        }
    }
}
