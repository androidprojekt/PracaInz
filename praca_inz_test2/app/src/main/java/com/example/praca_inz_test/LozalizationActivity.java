package com.example.praca_inz_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class LozalizationActivity extends AppCompatActivity {

    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lozalization);
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout); //moving activity animation

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageNavigationMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch(id)
                {
                    case R.id.menuExhibition:
                        goToOptionMenuItem(1);
                    break;
                    case R.id.menuInformation:
                        goToOptionMenuItem(2);
                        break;
                    case R.id.menuFavorites:
                        goToOptionMenuItem(3);
                        break;
                    case R.id.menuSettings:
                        goToOptionMenuItem(4);
                        break;
                    case R.id.menuAbout:
                        goToOptionMenuItem(5);
                        break;
                    case R.id.menuLogout:
                        //goToOptionMenuItem(6);
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });

    }

    public void goToOptionMenuItem(Integer id)
    {
        Intent intent ;
        intent = new Intent(getApplicationContext(),OptionsFromMenuActivity.class);
        intent.putExtra("OPTION_ID",id);
        startActivity(intent);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }



    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}