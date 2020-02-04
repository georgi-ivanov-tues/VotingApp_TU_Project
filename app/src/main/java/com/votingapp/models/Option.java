package com.votingapp.models;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Option {

    private String id;
    private String optionText;
    private int timesSelected;

    public Option(){}

    public Option(String optionText) {
        this.optionText = optionText;
        this.timesSelected = 0;
    }

    public Option(String id, String optionText) {
        this.id = id;
        this.optionText = optionText;
        this.timesSelected = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public int getTimesSelected() {
        return timesSelected;
    }

    public void setTimesSelected(int timesSelected) {
        this.timesSelected = timesSelected;
    }

    public void increaseTimesSelected(){
        timesSelected++;
    }
}
