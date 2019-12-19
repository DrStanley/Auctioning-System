package com.stanleyj.android.myauction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;

import Admin.AdminActivity;

public class MainActivity extends AppCompatActivity {
    Button bR, bL;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("Exit",false)){
            System.out.println();
            finish();
        }
//        Shared preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        Shared preference to get the extra data stored;
        SharedPreferences.Editor editor = sharedPref.edit();
        boolean check_log = sharedPref.getBoolean("Login", false);
        System.out.println("Login is "+check_log);
//        boolean check_registered = sharedPref.getBoolean("Registered", false);
        editor.apply();
//        if you have logged in before call intent log
        if (check_log) {

            Intent log = new Intent(this, LoginActivity.class);
            startActivity(log);
            finish();
        } else {
            setContentView(R.layout.activity_main);
            bR = (Button) findViewById(R.id.reg);
            bL = (Button) findViewById(R.id.log);
            imageView=(ImageView)findViewById(R.id.img);
            bR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            bL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            fadeIn2(bL);
            fadeIn2(bR);
            fadeIn(imageView);

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void fadeIn2(View view) {
//        creating a fade-in animation
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
//        how long will it take in milliseconds ie 1000msec = 1sec\
        animation.setDuration(3000);
//        start animation
        view.startAnimation(animation);
//        make view visible after animation
        view.setVisibility(View.VISIBLE);
    }
    private void fadeIn(View view) {
//        creating a fade-in animation
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
//        how long will it take in milliseconds ie 1000msec = 1sec\
        animation.setDuration(2000);
//        start animation
        view.startAnimation(animation);
//        make view visible after animation
        view.setVisibility(View.VISIBLE);
    }


}
