package com.stanleyj.android.myauction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class WinnersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winners);
        Toolbar toolbar = (Toolbar) findViewById(R.id.winner_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Winners List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Toast.makeText(WinnersActivity.this, "No winner yet for all auctions ", Toast.LENGTH_SHORT).show();
    }
}
