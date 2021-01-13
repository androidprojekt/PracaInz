package com.example.praca_inz_test;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static java.lang.String.valueOf;

public class LozalizationActivity extends AppCompatActivity implements SensorEventListener {

    NavigationView navigationView;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String EXHIBIT1 = "text";
    Float loadRateExhibit1 = 0f;
    Button showUserLocation;
    ImageView circleUserAnim;
    ImageView circleAnim;
    ImageView circleAnim2;
    Animation scaleUp, scaleDown;
    Dialog exhibitDialog;
    ImageButton exhibit1Btn;

    //------------------------------variables needed to compass-------------------------------------
    static public SensorManager mSensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];
    private int azimuth;
    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    //----------------------------------------------------------------------------------------------

    //-------------creating variables and objects needed to BLE and WIFI scan-----------------------
    private BluetoothManager mBluetoothManager;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothAdapter mBlueToothAdapter;
    private WifiManager wifiManager;
    private ArrayList<Transmitter> beaconList, wifiList;
    private ArrayList<Point> referencePointList;
    private WifiInfo wifiInfo;
    private List<ScanFilter> filters;
    private ScanSettings scanSettings;
    private Handler mHandler = new Handler();
    private Boolean startScanBeaconFlag = false;// flag specifying to start collecting measurements (from beacon) after clicking the button
    private Boolean startScanWifiFlag = false; // flag specifying to start collecting measurements (from ap) after clicking the button
    //----------------------------------------------------------------------------------------------

    //----------------------------------------layout------------------------------------------------
    private Button startLocalization;
    private RelativeLayout mainLayout;
    RelativeLayout.LayoutParams layoutParams, tempParams;
    private ImageView image; //marker on the map
    private TextView directionCompassTv;
    TextView estimateXTv, estimateYTv, nrOfNeighbours;
    Context context;
    //----------------------------------------------------------------------------------------------

    //-----------------------------configuration variables------------------------------------------
    int numberOfSamples = 5; // number of needed samples to receive in online phase
    int numberOfBeacons = 3; // beacons in system
    int numberOfWifi = 1;    // number of AP's
    int finishedBeaconsIterator = 0; //variable that determines whether the measurements have been collected from beacons
    int finishedWifiIterator = 0; //variable that determines whether the measurements have been collected from wifi
    int xPoints = 4; // number of X coordinates
    int yPoints = 4; // number of Y coordinates
    double percentRangeOfEuclideanDist = 0.2; //percentage of the Euclidean distance range
    //----------------------------------------------------------------------------------------------

    //-------------variables needed to determine the direction intervals in compass-----------------
    String direction = "UP"; //default value

    /* zuzia pokoj
    int upperLimitUp = 120;
    int upperLimitRight = 210;
    int upperLimitDown = 340;
    int upperLimitLeft = 50;*/
    int upperLimitUp = 327;
    int upperLimitRight = 50;
    int upperLimitDown = 142;
    int upperLimitLeft = 240;
    //----------------------------------------------------------------------------------------------
    //-------------------------variables needed to constrained search-space-------------------------
    double estimateX; //previous value of estimate x coordination
    double estimateY; //previous value of estimate y coordination
    int minX;         //left range of x coordinate
    int minY;         //down range of y coordinate
    int maxX;         //right range of x coordinate
    int maxY;         //up range of y coordinate
    private Boolean firstRun; //flag needed to first run (full search area)
    //--------------------------------------JSON objects--------------------------------------------
    JSONObject objectDatabase; // main file database (with all directions)
    JSONObject objectUpDatabase; // all measurments from UP directions
    JSONObject objectDownDatabase;
    JSONObject objectLeftDatabase;
    JSONObject objectRightDatabase;
    //----------------------------------------------------------------------------------------------
    //---------------------needed to previous Cordinates Bufor--------------------------------------
    ArrayList<Point> listOfPreviousCoordinates; //include 3 previous localization
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    double radiusOfCircleArea = 1.5;
    Point firstExhibitPoint;
    Point secondExhibitPoint;
    Point thirdExhibitPoint;
    ArrayList<Point> listOfExhibits;

    //----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lozalization);
        overridePendingTransition(R.anim.fade_in, R.anim.fadeout); //moving activity animation

        //--------------------functionality to sliding option menu----------------------------------
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageNavigationMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                selectFromOptionMenu(id);
                return true;
            }
        });
        //------------------------------------------------------------------------------------------

        //------------------------------------------COPIED----------------------------------------------
        showUserLocation = findViewById(R.id.showUserLocationBtnId);
        circleUserAnim = findViewById(R.id.imgUserAnimation);

        circleAnim = findViewById(R.id.imgAmnimation1);
        circleAnim2 = findViewById(R.id.imgAmnimation2);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        exhibitDialog = new Dialog(this);
        exhibit1Btn = findViewById(R.id.exhibit1BtnId);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        listOfPreviousCoordinates = new ArrayList<>();

        firstExhibitPoint = new Point(6, 3, 0);
        secondExhibitPoint = new Point(4, 1, 0);
        thirdExhibitPoint = new Point(2, 2, 0);

        listOfExhibits = new ArrayList<>();
        listOfExhibits.add(firstExhibitPoint);
        listOfExhibits.add(secondExhibitPoint);
        listOfExhibits.add(thirdExhibitPoint);


        //-----------------------------------------VIEWS--------------------------------------------
        context = getApplicationContext();
        startLocalization = findViewById(R.id.startLocalizationBtnId);
        estimateXTv = findViewById(R.id.estimateOfXId);
        estimateYTv = findViewById(R.id.estimateOfYId);
        nrOfNeighbours = findViewById(R.id.numberOfNeighbours);
        mainLayout = findViewById(R.id.main);
        image = findViewById(R.id.imageView);
        layoutParams = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
        directionCompassTv = findViewById(R.id.directionCompassId);
        //------------------------------------------------------------------------------------------

        //------------------------------------------Sensor------------------------------------------
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //------------------------------------------------------------------------------------------

        //--------------------------------initializing BLE and WIFI---------------------------------
        mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBlueToothAdapter.getBluetoothLeScanner(); // new solution for scanning
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        beaconList = new ArrayList<>();
        wifiList = new ArrayList<>();
        referencePointList = new ArrayList<>();
        wifiInfo = wifiManager.getConnectionInfo(); //actual connected AP

        Transmitter transmitterWifi = new Transmitter(wifiInfo.getMacAddress(),
                wifiInfo.getRssi(), "Wifi", wifiInfo.getSSID()); //create a wifi (acces point) object currently connected

        wifiList.add(transmitterWifi);
        //------------------------------------------------------------------------------------------

        //-------------------------PERMISSIONS-------------------------------
        ActivityCompat.requestPermissions(LozalizationActivity.this,
                new String[]{Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        READ_PHONE_STATE,
                        ACCESS_FINE_LOCATION},
                1);
        //--------------------------------------------------------------------

        //--------------------Settings and filters for scanning bluetooth devices-------------------
        String[] peripheralAddresses = new String[]{"E8:D4:18:0D:DB:37", "D6:2E:C2:40:FD:03", "EF:F7:2A:DC:14:03",
                "DD:BC:33:F9:EE:56", "F7:8B:72:B7:42:C4", "C1:90:8E:4B:16:E5", "C6:40:D6:9C:59:7E", "DB:A8:FF:3E:95:79",
                "FC:02:5B:0D:05:60"};
        //Beacon F7:8B:72:B7:42:C4 probably is defect
        filters = null;
        if (peripheralAddresses != null) {
            filters = new ArrayList<>();
            for (String address : peripheralAddresses) {
                ScanFilter filter = new ScanFilter.Builder()
                        .setDeviceAddress(address)
                        .build();
                filters.add(filter);
            }
        }
        scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
                .setReportDelay(0)
                .build();
        //------------------------------------------------------------------------------------------

        //---------------------------------new functionality----------------------------------------
        // when the app start's, their receiving rssi from wifi and beacons
        // later we should add a functionality in onPause, onResume, onStop etc.
        // also we should make a version in real time without button
        wifiScanner.run();
        BLEstartScan.run();
        //------------------------------------------------------------------------------------------

        showUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tempParams != null) {
                    circleUserAnim.setLayoutParams(tempParams);
                    circleUserAnim.startAnimation(scaleDown);
                }
            }
        });

        startLocalization.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                numberOfSamples = 5;
                startScanBeaconFlag = true;
                startScanWifiFlag = true;
            }
        });

        try {
            // get JSONObject from JSON file
            objectDatabase = new JSONObject(loadJSONFromAsset());
            objectUpDatabase = objectDatabase.getJSONObject("UP");
            objectDownDatabase = objectDatabase.getJSONObject("DOWN");
            objectLeftDatabase = objectDatabase.getJSONObject("LEFT");
            objectRightDatabase = objectDatabase.getJSONObject("RIGHT");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //---------------initializing variables to constrained search-space-------------------------
        estimateX = 0.0;
        estimateY = 0.0;
        minX = 0;
        minY = 0;
        maxX = xPoints;
        maxY = yPoints;
        firstRun = true;
        //--------------------------------------END OF ONCREATE--------------------------------------------

    }

    public void goToOptionMenuItem(Integer id) {
        Intent intent;
        intent = new Intent(getApplicationContext(), OptionsFromMenuActivity.class);
        intent.putExtra("OPTION_ID", id);
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

    public void selectFromOptionMenu(int id) {
        switch (id) {
            case R.id.menuExhibition:
                goToOptionMenuItem(1);
                break;
            case R.id.menuInformation:
                goToOptionMenuItem(2);
                break;
            case R.id.menuOpinions:
                goToOptionMenuItem(3);
                break;
            case R.id.menuAbout:
                goToOptionMenuItem(4);
                break;
            case R.id.menuLogout:
                //goToOptionMenuItem(5);
                break;
            default:
                //default
        }

    }

    public String loadJSONFromAsset() {
        //reading the main database file
        String json = null;
        try {
            InputStream is = getAssets().open("zuzia_pokoj_final.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //-----------------------------------------Threads-----------------------------new version------
    private Runnable BLEstopScan = new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            mBluetoothLeScanner.stopScan(scanCallback);
            mHandler.postDelayed(BLEstartScan, 1);
        }
    };

    private Runnable BLEstartScan = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            mBluetoothLeScanner.startScan(filters, scanSettings, scanCallback);
            mHandler.postDelayed(BLEstopScan, 6000);
        }
    };
    //----------------------------------------------------------------------------------------------

    private Runnable wifiScanner = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            if (startScanWifiFlag) {
                if (wifiList.get(0).isSavingSamples()) {    //the condition that all samples have been collected
                    if (wifiList.get(0).getSamplesIterator() == numberOfSamples) {
                        finishedWifiIterator++;
                        wifiList.get(0).setSavingSamples(false);
                        double average = averageOfList(wifiList.get(0).getSamplesTab());
                        wifiList.get(0).setAverage(average);
                    } else {
                        checkWifi();
                    }
                }
            }
            mHandler.postDelayed(wifiScanner, 200); //nie wiadomo co z czasem, jaka wartosc przyjac?
        }
    };

    private void checkWifi() {
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiList.get(0).getSamplesIterator() != numberOfSamples) {
            wifiList.get(0).addToTheSamplesTab(wifiInfo.getRssi());
            wifiList.get(0).setSamplesIterator();
            wifiList.get(0).setDirectionIterators(determineDirection(azimuth));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d("onScanResult start TIME", "Start test");
            final BluetoothDevice device = result.getDevice();
            final int rssi = result.getRssi();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (startScanBeaconFlag) {
                        boolean newBeacon = true;
                        if (beaconList.size() != 0) { //if there is any beacon on the list
                            for (Transmitter transmitter : beaconList) {
                                if (transmitter.getMacAdress().contains(result.getDevice().getAddress())) {
                                    //If beacon from scan result exist on the beacon list
                                    newBeacon = false;
                                    if (transmitter.isSavingSamples()) {
                                        //the condition that all samples have been collected
                                        if (transmitter.getSamplesIterator() < numberOfSamples) {
                                            transmitter.addToTheSamplesTab(rssi);
                                            transmitter.setSamplesIterator();
                                            transmitter.setDirectionIterators(determineDirection(azimuth));
                                        }
                                        if (transmitter.getSamplesIterator() == numberOfSamples) {
                                            double average = averageOfList(transmitter.getSamplesTab());
                                            transmitter.setAverage(average);
                                            transmitter.setSavingSamples(false);
                                            finishedBeaconsIterator++;
                                        }
                                    }
                                }
                            }
                        }
                        if (newBeacon) {
                            Transmitter transmitter = new Transmitter(device.getAddress(), rssi, "Beacon");
                            beaconList.add(transmitter);
                            transmitter.addToTheSamplesTab(rssi);
                            transmitter.setSamplesIterator();
                            transmitter.setDirectionIterators(determineDirection(azimuth));
                        }
                    }
                    if (finishedBeaconsIterator == numberOfBeacons) {
                        startScanBeaconFlag = false;
                        if (finishedWifiIterator == numberOfWifi) {
                            startScanWifiFlag = false;
                            beaconList.sort(new beaconSorter()); // list of sorted euclidean distances with x,y cordinates

                            for (int i = beaconList.size(); i > numberOfBeacons; i--)
                            //removing beacons from the list above the set value
                            {
                                beaconList.remove(i - 1);
                            }

                            finishedBeaconsIterator = 0;
                            finishedWifiIterator = 0;

                            try {
                                //Toast.makeText(getApplicationContext(), "succes ", Toast.LENGTH_SHORT).show();
                                estimatePositions();
                            } catch (JSONException e) {
                                //Toast.makeText(getApplicationContext(), "exc", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    };

    public double averageOfList(ArrayList<Integer> list) {
        //method needed to calculate average of samplesTab
        double average = 0.0;
        int sum = 0;
        for (int element : list) {
            sum += element;
        }
        average = (double) sum / (list.size());
        return average;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void estimatePositions() throws JSONException {
        ArrayList<Double> tempTab = new ArrayList<>(); // (x_a - x_b)^2
        double tempCalculation = 0.0;
        Log.d("Coordinate ", "x: " + estimateX + ", y: " + estimateY);

        rangeOfSearchSpace();  //determining x and y coordinates needed to search-space

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                String str = "" + x + "," + y;
                Log.d("test123", ": " + str);

                JSONObject tempPoint;

                switch (sumOfDirectionIterators()) {
                    case "UP":
                        tempPoint = objectUpDatabase.getJSONObject(str);
                        //Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT).show();
                        break;
                    case "DOWN":
                        tempPoint = objectDownDatabase.getJSONObject(str);
                        //Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
                        break;
                    case "LEFT":
                        tempPoint = objectLeftDatabase.getJSONObject(str);
                        //Toast.makeText(getApplicationContext(), "LEFT", Toast.LENGTH_SHORT).show();
                        break;
                    case "RIGHT":
                        tempPoint = objectRightDatabase.getJSONObject(str);
                        //Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        tempPoint = objectUpDatabase.getJSONObject(str); //get default object (UP)
                        //Toast.makeText(getApplicationContext(), "Default - UP", Toast.LENGTH_SHORT).show();
                }

                tempTab.clear(); // tempTab -->  (x_a - x_b)^2

                String wifiRssiTemp = tempPoint.getString("WIFI");
                double wifiRssi = Double.parseDouble(wifiRssiTemp);

                tempCalculation = Math.pow((wifiList.get(0).getAverage() - wifiRssi), 2);
                tempTab.add(tempCalculation);

                for (Transmitter beacon : beaconList) {
                    String databaseBeaconRssiTemp = tempPoint.getString(beacon.getMacAdress()); // from database
                    double databaseBeaconRssi = Double.parseDouble(databaseBeaconRssiTemp); //from database
                    double actualBeaconRssi = beacon.getAverage(); //actual
                    tempCalculation = Math.pow((databaseBeaconRssi - actualBeaconRssi), 2);
                    tempTab.add(tempCalculation);
                }

                double sum = 0.0; //sum of elements in tempTab -->(x_a - x_b)^2
                for (Double value : tempTab) {
                    sum += value;
                }

                double euclideanDistance = Math.sqrt(sum);
                Point pt = new Point(x, y, euclideanDistance); //every point (x,y) from loop  has a euclidean distance in Point class
                referencePointList.add(pt);
            }
        }
        referencePointList.sort(new euclideanSorter()); // list of sorted euclidean distances with x,y coordinates
        double maxEuclideanDistance = referencePointList.get(0).getEuclideanDistance() * (1 + percentRangeOfEuclideanDist); //max euclidean distance

        double x = 0.0;
        double y = 0.0;
        double sumOfWeights = 0.0;

        //------------------------------------adaptive method of KNN--------------------------------
        int numberOfNeighbours = 0;
        for (Point pt : referencePointList) {
            if (pt.getEuclideanDistance() <= maxEuclideanDistance) {
                x += pt.getX() * (1 / pt.getEuclideanDistance());
                y += pt.getY() * (1 / pt.getEuclideanDistance());
                sumOfWeights += 1 / pt.getEuclideanDistance();
                numberOfNeighbours++;
            }
        }
        nrOfNeighbours.setText("Neighbours: " + String.valueOf(numberOfNeighbours));
        //------------------------------------------------------------------------------------------

        //-------------------------------old version - standard KNN---------------------------------
        /*
        for (int i = 0; i < kNeighbours; i++) {

            Log.d("Nearest Neigbours", "x: " +referencePointList.get(i).getX()+"  y: "+ referencePointList.get(i).getY()
            +" euclidean distance: " + referencePointList.get(i).getEuclideanDistance());
            x += referencePointList.get(i).getX() * (1 / referencePointList.get(i).getEuclideanDistance());
            y += referencePointList.get(i).getY() * (1 / referencePointList.get(i).getEuclideanDistance());
            sumOfWeights += 1 / referencePointList.get(i).getEuclideanDistance();
        }
     //---------------------------------------------------------------------------------------------
         */
        estimateX = x / sumOfWeights;
        estimateY = y / sumOfWeights;
        estimateX = Math.round(estimateX * 100.0) / 100.0; //rounded to 2 decimal places
        estimateY = Math.round(estimateY * 100.0) / 100.0; //rounded to 2 decimal places
        estimateXTv.setText("x = " + estimateX);
        estimateYTv.setText("y = " + estimateY);

        //-------------------moving the arrow across the room map-----------------------------------
        layoutParams = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
        Log.d("Params", "width " + layoutParams.width + ", height " + layoutParams.height);

        int leftMarginToAdd = (int) (estimateY * 100 * 1.2);
        int bottomMarginToAdd = (int) (estimateX * 100 * 1.2);

        tempParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
        tempParams.leftMargin = 540;
        tempParams.topMargin = mainLayout.getLayoutParams().height - 140;

        image.setLayoutParams(tempParams);
        tempParams.leftMargin -= leftMarginToAdd;
        tempParams.topMargin -= bottomMarginToAdd;

        image.setLayoutParams(tempParams);
        //-----------------------------------------------------------------------------------------

        //adding to the previous coordinates list:
        //transmitter.setLastUpdate(simpleDateFormat.format(calendar.getTime()));
        calendar = Calendar.getInstance();
        Point actualPoint = new Point(estimateX, estimateY, simpleDateFormat.format(calendar.getTime()));
        // call method which adding to the list
        //----------------------------------------
        addToThePreviousCoordinates(actualPoint);
       // if (exhibitZone() == 1) circleAnim.startAnimation(scaleDown);
        prepareToNewScan();
        int tempExhibit = exhibitZone();
        Toast.makeText(getApplicationContext(),"exhibit: "+tempExhibit,Toast.LENGTH_SHORT).show();
        startExhibitAnimation(tempExhibit);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(sensorEvent.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }

        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        // "rotationMatrix" now has up-to-date information.
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        // "orientationAngles" now has up-to-date information.
        azimuth = (int) Math.toDegrees(orientationAngles[0]);
        azimuth = (azimuth + 360) % 360;
        image.setRotation(azimuth - 280); // trzeba edytowac
        directionCompassTv.setText("Direction value: " + azimuth + "°");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        Sensor magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            mSensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    public static class euclideanSorter implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            return Double.compare(p1.getEuclideanDistance(), p2.getEuclideanDistance());
        }
    }

    public static class beaconSorter implements Comparator<Transmitter> {
        @Override
        public int compare(Transmitter t1, Transmitter t2) {
            if (t1.getSamplesIterator() < t2.getSamplesIterator()) {
                return 1;
            } else return -1;
        }
    }


    public void prepareToNewScan() {
        wifiList.get(0).clearSamplesIterator();
        wifiList.get(0).setAverage(0.0);
        wifiList.get(0).setSavingSamples(true);
        wifiList.get(0).clearDirectionIterators();
        wifiList.get(0).clearTheSamplesTab();
        beaconList.clear();
        referencePointList.clear();

        //--------------------------------scanning all time-----------------------------------------
        startScanBeaconFlag = true;
        startScanWifiFlag = true;
        //------------------------------------------------------------------------------------------
    }

    public void rangeOfSearchSpace() {
        int tempEstimateX = (int) Math.round(estimateX);
        int tempEstimateY = (int) Math.round(estimateY);
        if (firstRun) {
            firstRun = false;
        } else {
            minX = (int) Math.ceil(tempEstimateX - 2);
            if (minX < 0)
                minX = 0;
            maxX = (int) Math.floor(tempEstimateX + 2) + 1;
            if (maxX > xPoints)
                maxX = xPoints;
            minY = (int) Math.ceil(tempEstimateY - 2);
            if (minY < 0)
                minY = 0;
            maxY = (int) Math.floor(tempEstimateY + 2) + 1;
            if (maxY > yPoints)
                maxY = yPoints;

            Log.d("Cordinate ", "min x: " + minX + ", max x: " + maxX);
            Log.d("Cordinate ", "min y: " + minY + ", max y: " + maxY);
        }
    }

    public String determineDirection(int value) {

        Log.d("AZIMUTH", "CHECK determinate: " + value);
        Log.d("AZIMUTH", "up, right, down, left: " + upperLimitUp + ", " + upperLimitRight + ", " + upperLimitDown + ", " + upperLimitLeft);
        if (value >= upperLimitLeft && value < upperLimitUp)
            direction = "UP"; //UP
        if ((value >= upperLimitUp && value < 360) || (value >= 0 && value < upperLimitRight))
            direction = "RIGHT"; // RIGHT
        if (value >= upperLimitRight && value < upperLimitDown)
            direction = "DOWN"; // DOWN
        if ((value >= upperLimitDown && value < upperLimitLeft))
            direction = "LEFT"; //LEFT
        Log.d("AZIMUTH", "CHECK determinate retrb: " + direction);
        return direction;
    }

    public String sumOfDirectionIterators() {
        int upIterator = 0;
        int downIterator = 0;
        int leftIterator = 0;
        int rightIterator = 0;

        for (Transmitter transmitter : beaconList) {
            upIterator += transmitter.getDirectionIteratorUp();
            downIterator += transmitter.getDirectionIteratorDown();
            leftIterator += transmitter.getDirectionIteratorLeft();
            rightIterator += transmitter.getDirectionIteratorRight();
        }

        upIterator += wifiList.get(0).getDirectionIteratorUp();
        downIterator += wifiList.get(0).getDirectionIteratorDown();
        leftIterator += wifiList.get(0).getDirectionIteratorLeft();
        rightIterator += wifiList.get(0).getDirectionIteratorRight();

        int max = upIterator;
        String tempDirection = "UP";

        if (max < downIterator) {
            max = downIterator;
            tempDirection = "DOWN";
        }
        if (max < leftIterator) {
            max = leftIterator;
            tempDirection = "LEFT";
        }
        if (max < rightIterator) {
            max = rightIterator;
            tempDirection = "RIGHT";
        }
        return tempDirection;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addToThePreviousCoordinates(Point pt) {
        if (listOfPreviousCoordinates.size() == 2) {
            int lastIndex = listOfPreviousCoordinates.size() - 1;
            listOfPreviousCoordinates.remove(lastIndex);
        }
        listOfPreviousCoordinates.add(0, pt); //adding point in first index at list and shifting other points to the end? Is it okay?
        //listOfPreviousCoordinates.sort(new timeSorter());
        Log.d("listcheck", "-------------------------------------");
        for (Point p : listOfPreviousCoordinates) {
            Log.d("listcheck", "sort list. x: " + p.getActualX() + " y: " + p.getActualY() + " time: " + p.getLastUpdate());
        }
    }

    public int exhibitZone() {
        int exhibit = 0; //none exhibit
        double error = 0.0;
        boolean checkFlag = true; // żeby wszystkie punkty w buforze miały mniejszą odległość niż zadana
        int exhibitIterator=1;

        for (Point exhibitPoint : listOfExhibits) {
            checkFlag = true;
            for (Point pt : listOfPreviousCoordinates) {
                error = 0.0;
                error = Math.sqrt(Math.pow(Math.abs(pt.getActualX() - exhibitPoint.getX()), 2)
                        + Math.pow(Math.abs(pt.getActualY() - exhibitPoint.getY()), 2));
                if (error > radiusOfCircleArea) checkFlag = false;
            }
            if (checkFlag) exhibit = exhibitIterator;

            exhibitIterator++;
        }
        return exhibit;
    }


    public void startExhibitAnimation(int exhibit)
    {
        switch (exhibit) {
            case 0:
                //nothing
                break;
            case 1:
                circleAnim.startAnimation(scaleDown);
                break;
            case 2:
                //circleAnim2.startAnimation(scaleDown);
                break;
            case 3:
                circleAnim.startAnimation(scaleDown);
                circleAnim2.startAnimation(scaleDown);
                break;
            default:
                //nothing
        }

    }
    public void showExhibitPopUp(View v) {
        TextView txtClose;
        exhibitDialog.setContentView(R.layout.exhibit_popup);
        txtClose = exhibitDialog.findViewById(R.id.txtCloseId);
        Button buttonInDialog = exhibitDialog.findViewById(R.id.buttonFromDialog1Id);

        RatingBar ratingBar;
        ratingBar = exhibitDialog.findViewById(R.id.ratingBar);
        loadDataFromSharedPrefecences(); //load user rate
        ratingBar.setRating(loadRateExhibit1); //set user rate
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float myRating, boolean fromUser) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                float myRate = ratingBar.getRating();
                editor.putFloat(EXHIBIT1, myRate);
                editor.apply();
            }
        });

        //comment
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exhibitDialog.dismiss();
            }
        });
        exhibitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        exhibitDialog.show();

    }

    public void loadDataFromSharedPrefecences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loadRateExhibit1 = sharedPreferences.getFloat(EXHIBIT1, 0);
    }

}