package com.example.praca_inz_test;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        ImageView castleImage = findViewById(R.id.logoMainImageId);
        castleImage.setAnimation(topAnim);

        ImageButton goToBtn = findViewById(R.id.goToMenuBtnId);
        goToBtn.setAnimation(bottomAnim);

        BluetoothAdapter mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBlueToothAdapter == null)
        {
            Toast.makeText(getApplicationContext(),"Bluetooth not supported",Toast.LENGTH_SHORT).show();
        }
        else {
            if (!mBlueToothAdapter.isEnabled()) {
                mBlueToothAdapter.enable();
            }
        }
    }

    public void goToMenuMethod(View view) {
        Intent goToMenuIntent;
        goToMenuIntent = new Intent(getApplicationContext(),MainMenuActivity.class);
        startActivity(goToMenuIntent);
    }

}