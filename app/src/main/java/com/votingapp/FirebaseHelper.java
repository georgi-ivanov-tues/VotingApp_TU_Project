package com.votingapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.User;
import com.votingapp.models.Voting;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by giivanov on 31.08.19.
 */

public class FirebaseHelper {

    public FirebaseHelper(){}

    public void getUsers(DataSnapshot dataSnapshot){
        for(DataSnapshot activitySnapShot: dataSnapshot.getChildren()){
            User userFromDB = activitySnapShot.getValue(User.class);
            userFromDB.setLoggedIn(activitySnapShot.child("isLoggedIn").getValue(Boolean.class));
            AppController.allUsers.add(userFromDB);
        }
    }

    public void getReferendum(DataSnapshot dataSnapshot){
        Referendum referendumFromDB = new Referendum();
        referendumFromDB.setId(dataSnapshot.getKey());
        referendumFromDB.setTitle(dataSnapshot.child("title").getValue(String.class));
        referendumFromDB.getOptionNo().setTimesSelected(dataSnapshot.child("noSelectedTimes").getValue(Integer.class));
        referendumFromDB.getOptionYes().setTimesSelected(dataSnapshot.child("yesSelectedTimes").getValue(Integer.class));
        referendumFromDB.setQuestion(dataSnapshot.child("question").getValue(String.class));

        AppController.votes.add(referendumFromDB);
    }

    public void getVoting(DataSnapshot dataSnapshot){
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
    }

    public void getPoll(DataSnapshot dataSnapshot){
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

    public void updatePoll(Poll poll, String question, Option option){
        FirebaseDatabase.getInstance().getReference().
                child("polls").child(poll.getId()).child("content").child(question).
                child(option.getId()).child("timesSelected").
                setValue(option.getTimesSelected());
    }

   public void createReferendum(Referendum newReferendum){
       DatabaseReference newFirebaseReferendum =
               FirebaseDatabase.getInstance().getReference().child("referendums").push();

       newFirebaseReferendum.setValue(newReferendum);
//
//       newFirebaseReferendum.child("title").setValue(newReferendum.getTitle());
//       newFirebaseReferendum.child("noSelectedTimes").setValue(0);
//       newFirebaseReferendum.child("yesSelectedTimes").setValue(0);
//       newFirebaseReferendum.child("question").child("questionText").setValue(newReferendum.getQuestion().getQuestionText());
   }

    public void createVoting(Voting newVoting){
        DatabaseReference newFirebaseVoting =
                FirebaseDatabase.getInstance().getReference().child("votings").push();

        newFirebaseVoting.setValue(newVoting);


//        newFirebaseVoting.child("title").setValue(newVoting.getTitle());
//        newFirebaseVoting.child("question").child("questionText").setValue(newVoting.getQuestion().getQuestionText());
//
//        for(Option option : newVoting.getOptions()) {
//            DatabaseReference newFirebaseOption = newFirebaseVoting.child("options").push();
//            newFirebaseOption.child("optionText").setValue(option.getOptionText());
//            newFirebaseOption.child("timesSelected").setValue(option.getTimesSelected());
//        }
    }

    public void createPoll(Poll newPoll){
        DatabaseReference newFirebasePoll =
                FirebaseDatabase.getInstance().getReference().child("polls").push();

        newFirebasePoll.setValue(newPoll);

//        newFirebasePoll.child("title").setValue(newPoll.getTitle());
//        DatabaseReference newFirebaseContent = newFirebasePoll.child("content");
//
//        for (Map.Entry<Question, ArrayList<Option>> entry : newPoll.getPollContent().entrySet()) {
//            DatabaseReference newFirebaseQuestion = newFirebaseContent.push();
//            newFirebaseQuestion.child("questionText").setValue(entry.getKey().getQuestionText());
//
//            DatabaseReference newFirebaseOptions = newFirebaseQuestion.child("options");
//
//            for(Option option : entry.getValue()) {
//                DatabaseReference newFirebaseOption = newFirebaseOptions.push();
//
//                newFirebaseOption.child("optionText").setValue(option.getOptionText());
//                newFirebaseOption.child("timesSelected").setValue(option.getTimesSelected());
//            }
//        }
    }
}
