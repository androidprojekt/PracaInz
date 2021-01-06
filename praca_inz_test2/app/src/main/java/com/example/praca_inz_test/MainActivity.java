package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView castleImage;
    ImageButton goToBtn;
    BluetoothAdapter mBlueToothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen - no bar
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        castleImage = findViewById(R.id.logoMainImageId);
        castleImage.setAnimation(topAnim);

        goToBtn=findViewById(R.id.goToMenuBtnId);
        goToBtn.setAnimation(bottomAnim);

        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBlueToothAdapter==null)
        {
            Toast.makeText(getApplicationContext(),"Bluetooth not suported",Toast.LENGTH_SHORT).show();
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