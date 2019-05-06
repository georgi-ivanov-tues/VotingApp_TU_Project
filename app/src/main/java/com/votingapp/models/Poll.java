package com.votingapp.models;

import android.annotation.SuppressLint;
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
