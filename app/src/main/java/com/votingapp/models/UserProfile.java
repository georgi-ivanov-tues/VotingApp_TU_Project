package com.votingapp.models;

import java.util.ArrayList;

public class UserProfile {
    private String userName;

    private ArrayList<Voting> votings = new ArrayList<>();
    private ArrayList<Poll> polls = new ArrayList<>();
    private ArrayList<Referendum> referendums = new ArrayList<>();

    private void addVoting(Voting voting){
        votings.add(voting);
    }
    private void addPoll(Poll poll){
        polls.add(poll);
    }
    private void addReferendum(Referendum referendum){
        referendums.add(referendum);
    }
}
