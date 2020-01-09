package com.votingapp.models;

import com.google.firebase.database.Exclude;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Poll extends Vote {

    private HashMap<String, ArrayList<Option>> content;

    public Poll(){
        content = new HashMap<>();
    }

    public Poll(String title, HashMap<String, ArrayList<Option>> content) {
        super(title);
        this.content = content;
    }

    public HashMap<String, ArrayList<Option>> getContent() {
        return content;
    }

    public void setPollContent(HashMap<String, ArrayList<Option>> content) {
        this.content = content;
    }

    public void addQuestion(String question, ArrayList<Option> options){
        content.put(question,options);
    }

    public void addQuestions(HashMap<String, ArrayList<Option>> questions){
        Iterator it = questions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            content.put((String) pair.getKey(), (ArrayList<Option>) pair.getValue());
        }
    }

    public String getTitle(){ return super.getTitle(); }
}
