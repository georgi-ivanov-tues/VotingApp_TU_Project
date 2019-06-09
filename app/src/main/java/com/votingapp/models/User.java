package com.votingapp.models;

import com.votingapp.AppController;

import java.util.ArrayList;

public class User {
    private int id;
    private String userName;
    private String password;
    private boolean isAdmin;

    public static final String TABLE_NAME = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "userName";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ISADMIN = "isAdmin";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT NOT NULL,"
                    + COLUMN_PASSWORD + " TEXT NOT NULL,"
                    + COLUMN_ISADMIN + " INTEGER NOT NULL"
                    + ");";


    private ArrayList<Voting> votings = new ArrayList<>();
    private ArrayList<Poll> polls = new ArrayList<>();
    private ArrayList<Referendum> referendums = new ArrayList<>();

    public User(int id, String userName, String password, boolean isAdmin){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(String userName, String password, boolean isAdmin){
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public void addVoting(Voting voting){
        votings.add(voting);
    }
    public void addPoll(Poll poll) {
        polls.add(poll);
    }
    public void addReferendum(Referendum referendum){
        referendums.add(referendum);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<Voting> getVotings() {
        return votings;
    }

    public ArrayList<Poll> getPolls() {
        return polls;
    }

    public ArrayList<Referendum> getReferendums() {
        return referendums;
    }

    public ArrayList<Vote> getAllVotes(){
        ArrayList<Vote> allVotes = new ArrayList<>();
        allVotes.addAll(getVotings());
        allVotes.addAll(getPolls());
        allVotes.addAll(getReferendums());
        return allVotes;
    }
}
