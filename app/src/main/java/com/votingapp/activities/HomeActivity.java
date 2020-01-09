package com.votingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import com.votingapp.AppController;
import com.votingapp.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Гласувания, анкети, референдуми");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button loadDataButton = (Button) findViewById(R.id.begin_button);
        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.votes.clear();

                Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}
