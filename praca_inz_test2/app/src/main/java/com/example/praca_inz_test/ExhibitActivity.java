package com.example.praca_inz_test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class ExhibitActivity extends AppCompatActivity {

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

    }
}