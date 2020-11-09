package com.example.praca_inz_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    //------------------------------------------TYLKO TESTY - DZIALA NIEPOPRAWNIE--------------------------------------


    DatabaseReference reference;
    TextView placeWhereUAre;    // This TextView describes where you are
    Button btnCheckRssi;        // When you press this button you receive info about RSSI and place where you are
    Fingerprint fingerprint;  //object of fingerprint store in database
    long maxid=0;             // variable used to autoincrement in database
    BroadcastReceiver wifiReceiver;
    WifiManager wifiManager;
    //private ArrayList<Integer> arrayList = new ArrayList<>();
    private List<ScanResult> results;
    boolean flag=false;
    List names;
    String rssiWifiName1;
    String rssiWifiName2;
    String rssiWifiName3;
    String rssiBle1;
    String rssiBle2;

    int actualRssiFunbox;
    int actualRssiAsia_barti;
    int actualRssiGola;

    int DbRssiFunbox;
    int DbRssiAsia_barti;
    int DbRssiGola;
    String placetemp;
    String place = "unknown";
    int min;
    int euclideanSum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lozalization);
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout); //animacja przejscia

        final Activity activity = this;

        btnCheckRssi = (Button) findViewById(R.id.checkRssiBtnId);
        placeWhereUAre= (TextView) findViewById(R.id.placeTextViewId);

        fingerprint = new Fingerprint();
        reference = FirebaseDatabase.getInstance().getReference().child("Fingerprint");
        wifiReceiver = new LozalizationActivity().wifiReceiver;

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid= (dataSnapshot.getChildrenCount()); //przydatne do autoinkrementacji id w bazie
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //procedura w przypadku bledu
            }
        });


        btnCheckRssi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.startScan(); //rozpoczecie skanowania
                results = wifiManager.getScanResults(); //przypisanie wynikow skanowania do listy elementow typu ScanResults
                TextView actualWifi1Tv = findViewById(R.id.actualWifi1RssiId);
                TextView actualWifi2Tv = findViewById(R.id.actualWifi2RssiId);
                TextView actualWifi3Tv = findViewById(R.id.actualWifi3RssiId);

                for (int i=0;i<results.size();i++)  //petla po liscie znalezionych sieci WIFI
                {
                    if(results.get(i).SSID.contains("FunBox2-5DE4"))
                    {
                        actualRssiFunbox= results.get(i).level;
                        actualWifi1Tv.setText(String.valueOf(results.get(i).level));
                    }

                    if(results.get(i).SSID.contains("asia"))
                    {
                        actualRssiAsia_barti=results.get(i).level;

                        actualWifi2Tv.setText(String.valueOf(results.get(i).level));
                    }

                    if(results.get(i).SSID.contains("gola"))
                    {
                        actualRssiGola = results.get(i).level;
                        actualWifi3Tv.setText(String.valueOf(results.get(i).level));
                    }

                }

                reference = FirebaseDatabase.getInstance().getReference().child("Fingerprint");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            //---------------------deklaracja zmiennych okreslajacych RSSI pobrane z bazy danych oraz RSSI aktualnie pobranych------------
                names = new ArrayList<String>();

            //------------------------------------------------------------------------------------------------------------------------------
                    min=1000;
            //---------------------petla po ustalonych lokalizacjach (typu miejsce przy eksponacie pierwszym, drugim itp.-------------------
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                            placetemp=dataSnapshot1.getKey();
                            DbRssiFunbox =Integer.parseInt(dataSnapshot1.child("FunBox2-5DE4").child("wifiRssi").getValue().toString());
                            DbRssiAsia_barti=Integer.parseInt(dataSnapshot1.child("asia_barti").child("wifiRssi").getValue().toString());
                            DbRssiGola = Integer.parseInt(dataSnapshot1.child("gola").child("wifiRssi").getValue().toString());

                            euclideanSum =  Math.abs(actualRssiAsia_barti-DbRssiAsia_barti)+ Math.abs(actualRssiGola-DbRssiGola)+
                                    Math.abs(actualRssiFunbox-DbRssiFunbox);

                            if(min>euclideanSum)
                            {
                                min=euclideanSum;
                                place = placetemp;
                            }
                            //---------------------skanowanie w poszukiwaniu dostepnych sieci WIFI--------------------
                        }


                        TextView test = findViewById(R.id.baseWifi1RssiId);
                        test.setText(String.valueOf(min));
                        TextView placeTv= findViewById(R.id.placeTextViewId);
                        placeTv.setText(place);


                          //--------------------------------------------------------------------------------------------

            //------------------------------------------------------------------------------------------------------------
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    } //koniec oncreate

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout);
    }

    public void goToSavingLayout(View view) {
        Intent goToSaving;
        goToSaving = new Intent(getApplicationContext(),SavingActivity.class);
        startActivity(goToSaving);
    }

}