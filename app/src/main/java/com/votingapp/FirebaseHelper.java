package com.votingapp;

import android.graphics.Path;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.models.Referendum;
import com.votingapp.models.User;
import com.votingapp.models.Voting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giivanov on 31.08.19.
 */

public class FirebaseHelper {

    public FirebaseHelper(){}

    public void loadDatabase(){
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("LOAD DATABASE!!!");
                getUsers(dataSnapshot.child("users"));

                getReferendums(dataSnapshot.child("referendums"));
                getVotings(dataSnapshot.child("votings"));
                getPolls(dataSnapshot.child("polls"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getUsers(DataSnapshot dataSnapshot){
        for(DataSnapshot activitySnapShot: dataSnapshot.getChildren()){
            User userFromDB = activitySnapShot.getValue(User.class);
            AppController.allUsers.add(userFromDB);
        }
    }

    public void getReferendums(DataSnapshot dataSnapshot){
        for(DataSnapshot activitySnapShot: dataSnapshot.getChildren()){
            Referendum referendumFromDB = new Referendum();
            referendumFromDB.setId(activitySnapShot.getKey());
            referendumFromDB.setTitle(activitySnapShot.child("title").getValue(String.class));
            referendumFromDB.getOptionNo().setTimesSelected(activitySnapShot.child("noSelectedTimes").getValue(Integer.class));
            referendumFromDB.getOptionYes().setTimesSelected(activitySnapShot.child("yesSelectedTimes").getValue(Integer.class));
            referendumFromDB.setQuestion(new Question(activitySnapShot.child("question").child("questionText").getValue(String.class)));

            AppController.votes.add(referendumFromDB);
        }
    }

    public void getVotings(DataSnapshot dataSnapshot){
        for(DataSnapshot activitySnapShot: dataSnapshot.getChildren()){
            Voting votingFromDB = new Voting();
            votingFromDB.setId(activitySnapShot.getKey());
            votingFromDB.setTitle(activitySnapShot.child("title").getValue(String.class));
            votingFromDB.setQuestion(new Question(activitySnapShot.child("question").child("questionText").getValue(String.class)));

            ArrayList<Option> options = new ArrayList<>();
            for(DataSnapshot optionSnapshot : activitySnapShot.child("options").getChildren()){
                Option newOption = new Option();
                newOption.setId(optionSnapshot.getKey());
                newOption.setOptionText(optionSnapshot.child("optionText").getValue(String.class));
                newOption.setTimesSelected(optionSnapshot.child("timesSelected").getValue(Integer.class));

                options.add(newOption);
            }

            votingFromDB.setOptions(options);
            AppController.votes.add(votingFromDB);
        }
    }

    public void getPolls(DataSnapshot dataSnapshot){
        for(DataSnapshot activitySnapShot : dataSnapshot.getChildren()){
            Poll pollFromDB = new Poll();
            pollFromDB.setId(activitySnapShot.getKey());
            pollFromDB.setTitle(activitySnapShot.child("title").getValue(String.class));

            HashMap<Question, ArrayList<Option>> pollContent = new HashMap<>();
            for(DataSnapshot contentSnapshot : activitySnapShot.child("content").getChildren()){
                Question question = new Question();
                question.setId(contentSnapshot.getKey());
                question.setQuestionText(contentSnapshot.child("questionText").getValue(String.class));

                ArrayList<Option> options = new ArrayList<>();
                for(DataSnapshot optionsSnapshot : contentSnapshot.child("options").getChildren()){
                    Option option = new Option();
                    option.setId(optionsSnapshot.getKey());
                    option.setOptionText(optionsSnapshot.child("optionText").getValue(String.class));
                    option.setTimesSelected(optionsSnapshot.child("timesSelected").getValue(Integer.class));

                    options.add(option);
                }

                pollContent.put(question, options);
            }

            pollFromDB.setPollContent(pollContent);

            AppController.votes.add(pollFromDB);
        }
    }

    public void updateReferendum(Referendum referendum){
        FirebaseDatabase.getInstance().getReference().
                child("referendums").child(referendum.getId()).child("yesSelectedTimes").
                setValue(referendum.getOptionYes().getTimesSelected());

        FirebaseDatabase.getInstance().getReference().
                child("referendums").child(referendum.getId()).child("noSelectedTimes").
                setValue(referendum.getOptionNo().getTimesSelected());
    }

    public void updateVoting(Voting voting, Option option){
        FirebaseDatabase.getInstance().getReference().
                child("votings").child(voting.getId()).child("options").child(option.getId()).child("timesSelected").
                setValue(option.getTimesSelected());
    }

    public void updatePoll(Poll poll, Question question, Option option){
        FirebaseDatabase.getInstance().getReference().
                child("polls").child(poll.getId()).child("content").child(question.getId()).
                child("options").child(option.getId()).child("timesSelected").
                setValue(option.getTimesSelected());
    }

   public void createReferendum(Referendum newReferendum){
       DatabaseReference newFirebaseReferendum =
               FirebaseDatabase.getInstance().getReference().child("referendums").push();

       newFirebaseReferendum.child("title").setValue(newReferendum.getTitle());
       newFirebaseReferendum.child("noSelectedTimes").setValue(0);
       newFirebaseReferendum.child("yesSelectedTimes").setValue(0);
       newFirebaseReferendum.child("question").child("questionText").setValue(newReferendum.getQuestion().getQuestionText());
   }

    public void createVoting(Voting newVoting){
        DatabaseReference newFirebaseVoting =
                FirebaseDatabase.getInstance().getReference().child("votings").push();

        newFirebaseVoting.child("title").setValue(newVoting.getTitle());
        newFirebaseVoting.child("question").child("questionText").setValue(newVoting.getQuestion().getQuestionText());

        for(Option option : newVoting.getOptions()) {
            DatabaseReference newFirebaseOption = newFirebaseVoting.child("options").push();
            newFirebaseOption.child("optionText").setValue(option.getOptionText());
            newFirebaseOption.child("timesSelected").setValue(option.getTimesSelected());
        }
    }

    public void createPoll(Poll newPoll){
        DatabaseReference newFirebasePoll =
                FirebaseDatabase.getInstance().getReference().child("polls").push();

        newFirebasePoll.child("title").setValue(newPoll.getTitle());
        DatabaseReference newFirebaseContent = newFirebasePoll.child("content");

        for (Map.Entry<Question, ArrayList<Option>> entry : newPoll.getPollContent().entrySet()) {
            DatabaseReference newFirebaseQuestion = newFirebaseContent.push();
            newFirebaseQuestion.child("questionText").setValue(entry.getKey().getQuestionText());

            DatabaseReference newFirebaseOptions = newFirebaseQuestion.child("options");

            for(Option option : entry.getValue()) {
                DatabaseReference newFirebaseOption = newFirebaseOptions.push();

                newFirebaseOption.child("optionText").setValue(option.getOptionText());
                newFirebaseOption.child("timesSelected").setValue(option.getTimesSelected());
            }
        }
    }
}
