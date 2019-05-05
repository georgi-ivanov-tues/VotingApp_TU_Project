package com.votingapp.models;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Question {

    private String questionText;

    public Question(){}

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
