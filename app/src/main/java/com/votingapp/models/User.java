package com.votingapp.models;

import java.util.ArrayList;

public class User {
    private String id;
    private String userName;
    private String password;
    private boolean isAdmin;
    private boolean isLoggedIn;

    private ArrayList<Vote> votes = new ArrayList<>();

    private ArrayList<UserVote> userVotes = new ArrayList<>();

    public void addUserVote(Vote vote, ArrayList<String> selectedByCurrentUserOptionsId){
        UserVote userVote = new UserVote();
        userVote.setVoteId(vote.getId());
        userVote.setOptionsId(selectedByCurrentUserOptionsId);
        userVotes.add(userVote);
    }

    public ArrayList<UserVote> getUserVotes(){
        return userVotes;
    }

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

    private class UserVote{
        private String voteId;
        private ArrayList<String> optionsId;

        UserVote(){}

        public String getVoteId() {
            return voteId;
        }

        public void setVoteId(String vote_Id) {
            this.voteId = vote_Id;
        }

        public ArrayList<String> getOptionsId() {
            return optionsId;
        }

        public void setOptionsId(ArrayList<String> optionsId) {
            this.optionsId = optionsId;
        }
    }
}
