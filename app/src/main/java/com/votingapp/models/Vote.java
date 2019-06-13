package com.votingapp.models;

import java.io.Serializable;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public abstract class Vote implements Serializable {

    private int id;
    private String title;

    public Vote(){}

    public Vote(String title) {
        this.title = title;
    }

    public Vote(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
