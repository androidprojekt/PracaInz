package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
//------------------the activity used to move between options in a menu-----------------------------


public class OptionsFromMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_from_menu);

        //------------------------------------------------------------------------------------------
        Intent receivedIntent = getIntent();
        int optionType=receivedIntent.getIntExtra("OPTION_ID",0);
        //  1--> menuExhibition
        //  2--> menuInformation
        //  3--> menuOpinions
        //  4--> menuAbout
        //  5--> menuLogout
        //------------------------------------------------------------------------------------------

        Fragment fragment;
        switch(optionType)
        {
            case 1:
                Intent goToExhibitionIntent;
                goToExhibitionIntent = new Intent(getApplicationContext(), ListOfExhibitsActivity.class);
                startActivity(goToExhibitionIntent);
                break;
            case 2:
                fragment = new InformationsFragment();
                loadFragment(fragment);
                break;
            case 3:
                Intent goToOpinionsIntent;
                goToOpinionsIntent = new Intent(getApplicationContext(),OpinionsActivity.class);
                startActivity(goToOpinionsIntent);
                break;
            case 4:
                fragment = new aboutUsFragment();
                loadFragment(fragment);
                break;
            case 5:
                // Log out from app
                break;
            default:
        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment ,fragment).commit();
        //DrawerLayout drawerLayout = findViewById(R.id.drawerLayout); only works in localization activity
        //drawerLayout.closeDrawer(GravityCompat.START); only works in localization activity
        //fragmentTransaction.addToBackStack(null); :-)
    }

    private void loadFragmentWithStack(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment ,fragment).commit();
        //DrawerLayout drawerLayout = findViewById(R.id.drawerLayout); only works in localization activity
        //drawerLayout.closeDrawer(GravityCompat.START); only works in localization activity
        fragmentTransaction.addToBackStack(null);
    }
}