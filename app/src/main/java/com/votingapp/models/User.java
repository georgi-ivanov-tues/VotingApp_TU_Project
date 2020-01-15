package com.votingapp.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String id;
    private String userName;
    private String password;
    private boolean isAdmin;
    private boolean isLoggedIn;

    private ArrayList<Vote> votes = new ArrayList<>();

    private HashMap<String,String> referendumsVotedByUser = new HashMap<>();
    private HashMap<String,String> votingsVotedByUser = new HashMap<>();
    @JsonSerialize
    private HashMap<String, HashMap<String,String>> pollsVotedByUser = new HashMap<String, HashMap<String, String>>();

    public User(){}

    public User(String id, String userName, String password, boolean isAdmin, boolean isLoggedIn){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isLoggedIn = isLoggedIn;
    }

    public User(String userName, String password, boolean isAdmin){
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public void addVote(Vote vote) { this.votes.add(vote); }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public HashMap<String, String> getReferendumsVotedByUser() {
        return referendumsVotedByUser;
    }

    public void setReferendumsVotedByUser(HashMap<String, String> referendumsVotedByUser) {
        this.referendumsVotedByUser = referendumsVotedByUser;
    }

    public HashMap<String, String> getVotingsVotedByUser() {
        return votingsVotedByUser;
    }

    public void setVotingsVotedByUser(HashMap<String, String> votingsVotedByUser) {
        this.votingsVotedByUser = votingsVotedByUser;
    }

    public HashMap<String, HashMap<String, String>> getPollsVotedByUser() {
        return pollsVotedByUser;
    }

    public void setPollsVotedByUser(HashMap<String, HashMap<String, String>> pollsVotedByUser) {
        this.pollsVotedByUser = pollsVotedByUser;
    }
}