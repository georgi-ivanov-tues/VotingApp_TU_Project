package com.votingapp.models;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class Option {

    private int id;
    private String optionText;
    private int timesSelected;
    private boolean selectedByCurrentUser = false;

    public static final String TABLE_NAME = "options";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_OPTION_TEXT = "optionText";
    public static final String COLUMN_TIMES_SELECTED = "timesSelected";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_OPTION_TEXT + " TEXT NOT NULL,"
                    + COLUMN_TIMES_SELECTED + " INTEGER NOT NULL"
                    + ");";


    public Option(){}

    public Option(String optionText) {
        this.optionText = optionText;
        this.timesSelected = 0;
    }

    public Option(int id, String optionText) {
        this.id = id;
        this.optionText = optionText;
        this.timesSelected = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isSelectedByCurrentUser() {
        return selectedByCurrentUser;
    }

    public void setSelectedByCurrentUser(boolean selectedByCurrentUser) {
        this.selectedByCurrentUser = selectedByCurrentUser;
    }
}
