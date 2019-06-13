package com.votingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://localhost/phpmyadmin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Fail");
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

        Button loadDataButton = (Button) findViewById(R.id.begin_button);
        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.databaseHelper.setUpDatabase();

                AppController.votes.clear();
                AppController.votes.addAll(AppController.databaseHelper.selectAllReferendums());
                AppController.votes.addAll(AppController.databaseHelper.selectAllVotings());
                AppController.votes.addAll(AppController.databaseHelper.selectAllPolls());

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
