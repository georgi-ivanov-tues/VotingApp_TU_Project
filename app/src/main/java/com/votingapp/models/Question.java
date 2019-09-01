package com.votingapp.models;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Question {

    private String id;
    private String questionText;

    public static final String TABLE_NAME = "questions";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUESTION_TEXT = "questionText";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_QUESTION_TEXT + " TEXT NOT NULL"
                    + ");";

    public Question(){}

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
