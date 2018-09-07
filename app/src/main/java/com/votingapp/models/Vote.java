package com.votingapp.models;

import java.io.Serializable;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public abstract class Vote implements Serializable {

    private String title;

    public Vote(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
