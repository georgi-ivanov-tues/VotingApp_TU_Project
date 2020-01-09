package com.votingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votingapp.AppController;
import com.votingapp.R;
import com.votingapp.models.User;

public class LoginActivity extends AppCompatActivity {

    EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = (EditText) findViewById(R.id.input_username);
        final EditText password = (EditText) findViewById(R.id.input_password);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginUser(userName.getText().toString(), password.getText().toString())) {
                    AppController.votes.clear();
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(myIntent);
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("RELOAD USERS!!!");
                AppController.allUsers.clear();
                AppController.firebaseHelper.getUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean loginUser(String userName, String password){
        if("".equals(userName)){
            showMessage("Моля въведете потребителско име!");
            return false;
        }
        if(!"".equals(userName) && "".equals(password)){
            showMessage("Моля въведете парола!");
            return false;
        }

        for(User user : AppController.allUsers){
            if(userName.equals(user.getUserName())){
                if(password.equals(user.getPassword())){
                    if(user.isLoggedIn()){
                        showMessage("Потребителят вече е в системата!");
                        return false;
                    }else {
                        AppController.loggedUser = user;
                        FirebaseDatabase.getInstance().getReference().child("users").child(user.getId()).child("isLoggedIn").setValue(true);
                        return true;
                    }
                }else{
                    showMessage("Грешна парола!");
                    return false;
                }
            }
        }

        showMessage("Няма такъв потребител!");
        return false;
    }

    private void showMessage(CharSequence text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
