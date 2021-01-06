package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ExhibitActivity extends AppCompatActivity {
    private TextView tvName, tvDesc;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit);
        tvName = findViewById(R.id.txtExhibitNameId);
        tvDesc = findViewById(R.id.txtExhibitDescId);
        img=findViewById(R.id.imgExhibThumbnailId);

        Intent receivedIntent = getIntent();
        String name = receivedIntent.getExtras().getString("name");
        String desc = receivedIntent.getExtras().getString("description");
        int thumbnail = receivedIntent.getExtras().getInt("thumbnail");

        tvDesc.setText(desc);
        tvName.setText(name);
        img.setImageResource(thumbnail);

    }
}