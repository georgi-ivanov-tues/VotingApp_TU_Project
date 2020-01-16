package com.votingapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.User;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by giivanov on 31.08.19.
 */

public class FirebaseHelper {

    public FirebaseHelper(){}

    public void getUsers(DataSnapshot dataSnapshot){
        for(DataSnapshot activitySnapShot: dataSnapshot.getChildren()){
            User userFromDB = activitySnapShot.getValue(User.class);
            userFromDB.setLoggedIn(activitySnapShot.child("isLoggedIn").getValue(Boolean.class));

            if(activitySnapShot.child("votedByUser").child("referendums").getValue() != null){
                HashMap<String,String> referendumsVotedByUser = (HashMap<String,String>) activitySnapShot.child("votedByUser").child("referendums").getValue();
                for (Map.Entry<String, String> pair : referendumsVotedByUser.entrySet()) {
                    userFromDB.getReferendumsVotedByUser().put(pair.getKey(), pair.getValue());
                }
            }

            if(activitySnapShot.child("votedByUser").child("votings").getValue() != null){
                HashMap<String,String> votingsVotedByUser = (HashMap<String,String>) activitySnapShot.child("votedByUser").child("votings").getValue();
                for (Map.Entry<String, String> pair : votingsVotedByUser.entrySet()) {
                    userFromDB.getVotingsVotedByUser().put(pair.getKey(), pair.getValue());
                }
            }

            if(activitySnapShot.child("votedByUser").child("polls").getValue() != null){
                HashMap<String, HashMap<String, String>> polls = new HashMap<>();
                for(DataSnapshot ds : activitySnapShot.child("votedByUser").child("polls").getChildren()){
                    polls.put(ds.getKey(), (HashMap<String, String>) ds.getValue());
                }
                userFromDB.setPollsVotedByUser(polls);
            }
            AppController.allUsers.add(userFromDB);
        }
    }

    public Referendum getReferendum(DataSnapshot dataSnapshot){
        Referendum referendumFromDB = new Referendum();
        referendumFromDB.setId(dataSnapshot.getKey());
        referendumFromDB.setTitle(dataSnapshot.child("title").getValue(String.class));
        referendumFromDB.getOptionNo().setTimesSelected(dataSnapshot.child("noSelectedTimes").getValue(Integer.class));
        referendumFromDB.getOptionYes().setTimesSelected(dataSnapshot.child("yesSelectedTimes").getValue(Integer.class));
        referendumFromDB.setQuestion(dataSnapshot.child("question").getValue(String.class));

        AppController.votes.add(referendumFromDB);

        return referendumFromDB;
    }

    public Voting getVoting(DataSnapshot dataSnapshot){
        Voting votingFromDB = new Voting();
        votingFromDB.setId(dataSnapshot.getKey());
        votingFromDB.setTitle(dataSnapshot.child("title").getValue(String.class));
        votingFromDB.setQuestion(dataSnapshot.child("question").getValue(String.class));

        ArrayList<Option> options = new ArrayList<>();
        for(DataSnapshot optionSnapshot : dataSnapshot.child("options").getChildren()){
            Option newOption = new Option();
            newOption.setId(optionSnapshot.getKey());
            newOption.setOptionText(optionSnapshot.child("optionText").getValue(String.class));
            newOption.setTimesSelected(optionSnapshot.child("timesSelected").getValue(Integer.class));

            options.add(newOption);
        }

        votingFromDB.setOptions(options);
        AppController.votes.add(votingFromDB);

        return votingFromDB;
    }

    public Poll getPoll(DataSnapshot dataSnapshot){
        Poll pollFromDB = new Poll();
        pollFromDB.setId(dataSnapshot.getKey());
        pollFromDB.setTitle(dataSnapshot.child("title").getValue(String.class));

        HashMap<String, ArrayList<Option>> pollContent = new HashMap<>();
        for(DataSnapshot contentSnapshot : dataSnapshot.child("content").getChildren()){
            String question = contentSnapshot.getKey();

            ArrayList<Option> options = new ArrayList<>();

            for(DataSnapshot optionSnapshot : contentSnapshot.getChildren()){
                Option option = new Option();
                option.setId(optionSnapshot.getKey());
                option.setOptionText(optionSnapshot.child("optionText").getValue(String.class));
                option.setTimesSelected(optionSnapshot.child("timesSelected").getValue(Integer.class));

                options.add(option);
            }

            pollContent.put(question, options);
        }

        pollFromDB.setPollContent(pollContent);

        AppController.votes.add(pollFromDB);

        return pollFromDB;
    }

    public void castVote(DatabaseReference selectedOptionDatabaseReference){
        selectedOptionDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long timesSelected = (long) dataSnapshot.getValue();
                dataSnapshot.getRef().setValue(timesSelected + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

   public String createReferendum(Referendum newReferendum){
       DatabaseReference newFirebaseReferendum =
               FirebaseDatabase.getInstance().getReference().child("referendums").push();

       newFirebaseReferendum.setValue(newReferendum);

       return newFirebaseReferendum.getKey();
   }

    public String createVoting(Voting newVoting){
        DatabaseReference newFirebaseVoting =
                FirebaseDatabase.getInstance().getReference().child("votings").push();

        newFirebaseVoting.setValue(newVoting);

        return newFirebaseVoting.getKey();
    }

    public String createPoll(Poll newPoll){
        DatabaseReference newFirebasePoll =
                FirebaseDatabase.getInstance().getReference().child("polls").push();

        newFirebasePoll.setValue(newPoll);

        return newFirebasePoll.getKey();
    }

    public void addToVotedByUser(Vote vote, String question, String option, String voteType){
        if("votings".equals(voteType)) {
            FirebaseDatabase.getInstance().getReference().child("users").child(AppController.loggedUser.getId()).child("votedByUser").
                    child(voteType).child(vote.getId()).setValue(option);
        }else if("referendums".equals(voteType)){
            FirebaseDatabase.getInstance().getReference().child("users").child(AppController.loggedUser.getId()).child("votedByUser").
                    child(voteType).child(vote.getId()).setValue(option);
        }else if("polls".equals(voteType)){
            FirebaseDatabase.getInstance().getReference().child("users").child(AppController.loggedUser.getId()).child("votedByUser").
                    child(voteType).child(vote.getId()).child(question).setValue(option);
        }
    }

    public void deleteVoteFromDB(Vote vote){
        String voteType = "";
        if(vote instanceof Referendum)
            voteType = "referendums";
        else if(vote instanceof Voting)
            voteType = "votings";
        else if(vote instanceof Poll)
            voteType = "polls";

        FirebaseDatabase.getInstance().getReference().child(voteType).child(vote.getId()).removeValue();
    }
}
