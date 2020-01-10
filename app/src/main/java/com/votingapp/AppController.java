package com.votingapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.votingapp.activities.VotingActivity;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Referendum;
import com.votingapp.models.User;
import com.votingapp.models.Vote;
import com.votingapp.models.Voting;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by giivanov on 4.5.2018 г..
 */

public class AppController extends Application{

    private static AppController mInstance;

    public static ArrayList<User> allUsers = new ArrayList<>();
    public static ArrayList<Vote> votes = new ArrayList<>();
    public static User loggedUser;
    public static Vote currentVote;

    public static FirebaseHelper firebaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        firebaseHelper = new FirebaseHelper();

        System.out.println("Application created!!!");
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Vote getCurrentVote() {
        return currentVote;
    }

    public static void setCurrentVote(Vote currentVote) {
        AppController.currentVote = currentVote;
    }

    public static ArrayList<Vote> getVotesByType(Class type) {
        ArrayList<Vote> votesByType = new ArrayList();
        AppController.getInstance();
        Iterator it = AppController.votes.iterator();
        while (it.hasNext()) {
            Vote vote = (Vote) it.next();
            if (vote.getClass().equals(type)) {
                votesByType.add(vote);
            }
        }
        return votesByType;
    }

    public static int getTotalNumberOfVotes(ArrayList<Option> options){
        int totalNumberOfVotes = 0;
        for(Option option : options){
            totalNumberOfVotes += option.getTimesSelected();
        }
        return totalNumberOfVotes;
    }

    public static double calculateOptionPercentage(Option option, int totalNumberOfVotes){
        return (option.getTimesSelected() / (double) totalNumberOfVotes) * 100;
    }

    public static String formatOptionPercentage(Option option, double optionPercentage){
        String optionTextPercentage = new DecimalFormat("##.##").format(optionPercentage) + "%";
        return option.getOptionText() + " - " + optionTextPercentage + " ("  + option.getTimesSelected() + ")";
    }

    @SuppressLint("ResourceAsColor")
    public static LinearLayout createPercentangeBars(Context context, double optionPercentage){
        LinearLayout percentageBarLinearLayout = new LinearLayout(context);
        percentageBarLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        percentageBarLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View optionPercentageBarLeft = new View(context);
        optionPercentageBarLeft.setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) (optionPercentage / 100)));
        optionPercentageBarLeft.setBackgroundColor(R.color.dodgerBlue);
        percentageBarLinearLayout.addView(optionPercentageBarLeft);

        View optionPercentageBarRight = new View(context);
        optionPercentageBarRight.setLayoutParams(new LinearLayout.LayoutParams(0, 15, (float) ((100 - optionPercentage) / 100)));
        percentageBarLinearLayout.addView(optionPercentageBarRight);

        return percentageBarLinearLayout;
    }

    public static void sendNotification(Vote vote){
        Intent intent = new Intent(getInstance().getApplicationContext(), VotingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getInstance().getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = new Notification.Builder(getInstance().getApplicationContext());

        String notificationTitle = "";
        if(vote instanceof Voting) notificationTitle = "Ново гласуване!";
        else if(vote instanceof Poll) notificationTitle = "Нова анкета!";
        else if(vote instanceof Referendum) notificationTitle = "Нов референдум!";

        builder.setAutoCancel(true);
        builder.setTicker(notificationTitle);
        builder.setContentTitle(notificationTitle);
        builder.setContentText(vote.getTitle());
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.build();

        AppController.setCurrentVote(vote);

        Notification myNotication = builder.getNotification();
        NotificationManager manager = (NotificationManager) getInstance().getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.notify(11, myNotication);
    }
}
