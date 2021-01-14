package com.example.praca_inz_test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Objects;

public class ExhibitActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    String exhibit;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit);
        TextView tvName = findViewById(R.id.txtExhibitNameId);
        TextView tvDesc = findViewById(R.id.txtExhibitDescId);
        ImageView img = findViewById(R.id.imgExhibThumbnailId);

        Intent receivedIntent = getIntent();
        String name = Objects.requireNonNull(receivedIntent.getExtras()).getString("name");
        String desc = receivedIntent.getExtras().getString("description");
        int thumbnail = receivedIntent.getExtras().getInt("thumbnail");

        tvDesc.setText(desc);
        tvName.setText(name);
        img.setImageResource(thumbnail);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        RatingBar ratingBar;
        ratingBar = findViewById(R.id.ratingbar);


        switch (name) {
            case "Waza":
                ratingBar.setRating(sharedPreferences.getFloat("textExhibit63",0)); //set user rate
                exhibit="textExhibit63";
                break;
            case "Miniatura":
                ratingBar.setRating(sharedPreferences.getFloat("textExhibit41",0)); //set user rate
                exhibit="textExhibit41";
                break;
            case "Puchar":
                ratingBar.setRating(sharedPreferences.getFloat("textExhibit03",0)); //set user rate
                exhibit="textExhibit03";
                break;
            default:
                //default
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float myRating, boolean fromUser) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                float myRate = ratingBar.getRating();
                editor.putFloat(exhibit, myRate);
                editor.apply();
            }
        });


    }
}