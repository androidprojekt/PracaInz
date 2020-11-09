package com.example.praca_inz_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.content.BroadcastReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SavingActivity extends AppCompatActivity {

    DatabaseReference reference;
    Button btnSaveFingerprint; // Button to save Fingerprint to the database
    Fingerprint fingerprint;  //object of fingerprint store in database
    long maxid=0;             // variable used to autoincrement in database
    BroadcastReceiver wifiReceiver;
    WifiManager wifiManager;
    List mywifiList;
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private List<ScanResult> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        final Activity activity = this;
        btnSaveFingerprint = findViewById(R.id.saveFingerprintBtnId2);
        reference = FirebaseDatabase.getInstance().getReference().child("Fingerprint");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid= (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        wifiReceiver = new WifiReceiver();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }

        btnSaveFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    scanWifiList();
            }
        });

    }
    class WifiReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    private void scanWifiList() {
        wifiManager.startScan();
        mywifiList = wifiManager.getScanResults();
        //TextView counterTv = findViewById(R.id.counterId);
        results=mywifiList;
        int counter=0;

        for (ScanResult scanResult : results) {

            if(scanResult.SSID.contains("asia"))
            {
                arrayList.add(scanResult.level);
                fingerprint = new Fingerprint();
                fingerprint.setWifiRssi(scanResult.level);
                fingerprint.setId(counter);
                reference.child(scanResult.SSID).setValue(fingerprint);
                counter++;
            }

            if(scanResult.SSID.contains("FunBox2-5DE4"))
            {
                arrayList.add(scanResult.level);
                fingerprint = new Fingerprint();
                fingerprint.setWifiRssi(scanResult.level);
                fingerprint.setId(counter);
                reference.child(scanResult.SSID).setValue(fingerprint);
                counter++;
            }

            if(scanResult.SSID.contains("gola"))
            {
                arrayList.add(scanResult.level);
                fingerprint = new Fingerprint();
                fingerprint.setWifiRssi(scanResult.level);
                fingerprint.setId(counter);
                reference.child(scanResult.SSID).setValue(fingerprint);
                counter++;
            }




        }

       // String counterTemp = String.valueOf(counter);
        //counterTv.setText(counterTemp);

    }
}
