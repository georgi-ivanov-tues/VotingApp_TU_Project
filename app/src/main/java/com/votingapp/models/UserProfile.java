package com.votingapp.models;

import java.util.ArrayList;

public class UserProfile {
    private String userName;

    private ArrayList<Voting> votings = new ArrayList<>();
    private ArrayList<Poll> polls = new ArrayList<>();
    private ArrayList<Referendum> referendums = new ArrayList<>();

    public void addVoting(Voting voting){
        votings.add(voting);
    }
    public void addPoll(Poll poll) {
        polls.add(poll);
    }
    public void addReferendum(Referendum referendum){
        referendums.add(referendum);
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
}
