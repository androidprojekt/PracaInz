package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class OptionsFromMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_from_menu);

        //------------------------------------------------------------------------------------------
        Intent receivedIntent = getIntent();
        Integer optionType=receivedIntent.getIntExtra("OPTION_ID",0);
        //  1--> menuExhibition
        //  2--> menuInformation
        //  3--> menuOpinions
        //  4--> menuAbout
        //  5--> menuLogout
        //------------------------------------------------------------------------------------------

        Fragment fragment=null;
        switch(optionType)
        {
            case 1:
                //fragment = new exhibitionFragment();
                //loadFragment(fragment);
                Intent goToMenuIntent;
                goToMenuIntent = new Intent(getApplicationContext(),listOfExhibitsActivity.class);
                startActivity(goToMenuIntent);
                break;
            case 2:
                fragment = new InformationsFragment();
                loadFragment(fragment);
                break;
            case 3:
                Intent goToOpinionsIntent;
                goToOpinionsIntent = new Intent(getApplicationContext(),OpinionsActivity.class);
                startActivity(goToOpinionsIntent);
                //fragment = new opinionsFragment();
                //loadFragment(fragment);
                break;
            case 4:
                fragment = new aboutUsFragment();
                loadFragment(fragment);
                break;
            case 5:

                //
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