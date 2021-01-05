package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout);
        setContentView(R.layout.activity_main_menu);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void goToAboutMethod(View view) {
        Intent goToAboutIntent;
        goToAboutIntent = new Intent(getApplicationContext(),AboutActivity.class);
        startActivity(goToAboutIntent);
    }

    public void goToTheWebsiteMethod(View view) {
        Intent goToTheWebsiteIntent;
        goToTheWebsiteIntent = new Intent(getApplicationContext(),OpenTheWebsiteActivity.class);
        startActivity(goToTheWebsiteIntent);
    }

    public void go_to_exhibition_method(View view) {
        Intent goToTheExhibitionIntent;
        goToTheExhibitionIntent = new Intent(getApplicationContext(),ExhibitionActivity.class);
        startActivity(goToTheExhibitionIntent);
    }
    public void goToLocalizationMethod(View view) {
        Intent goToLocalizationMethod;
        goToLocalizationMethod = new Intent(getApplicationContext(),LozalizationActivity.class);
        startActivity(goToLocalizationMethod);
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout);
    }


}