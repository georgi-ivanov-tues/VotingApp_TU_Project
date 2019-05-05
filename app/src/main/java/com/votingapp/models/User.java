package com.votingapp.models;

import com.votingapp.AppController;

import java.util.ArrayList;

public class User {
    private String userName;
    private String password;
    private boolean isAdmin;

    private ArrayList<Voting> votings = new ArrayList<>();
    private ArrayList<Poll> polls = new ArrayList<>();
    private ArrayList<Referendum> referendums = new ArrayList<>();

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
