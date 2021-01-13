package com.example.praca_inz_test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//----------------------activity related to information about the museum----------------------------
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // move screen animation
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out); // move screen animation
    }
}