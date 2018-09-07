package com.votingapp.models;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Option {

    private String optionText;
    private int timesSelected;

    public Option(String optionText) {
        this.optionText = optionText;
        this.timesSelected = 0;
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
}
