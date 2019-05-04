package com.votingapp.models;

import com.votingapp.AppController;

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

    public ArrayList<Vote> getAllVotes(){
        ArrayList<Vote> allVotes = new ArrayList<>();
        allVotes.addAll(getVotings());
        allVotes.addAll(getPolls());
        allVotes.addAll(getReferendums());
        return allVotes;
    }
}
