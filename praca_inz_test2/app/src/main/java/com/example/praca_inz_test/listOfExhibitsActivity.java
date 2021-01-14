package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
//---------------------------Activity displaying exhibits in the hall-------------------------------

public class listOfExhibitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_exhibits);

        List<Exhibit> lstExhibit = new ArrayList<>();
        lstExhibit.add(new Exhibit("eksponat63",getString(R.string.exhibExampleDesc),R.drawable.pic1));
        lstExhibit.add(new Exhibit("eksponat41",getString(R.string.exhibExampleDesc),R.drawable.pic2));
        lstExhibit.add(new Exhibit("eksponat03",getString(R.string.exhibExampleDesc),R.drawable.pic3));
        lstExhibit.add(new Exhibit("Dywan",getString(R.string.exhibExampleDesc),R.drawable.pic4));

        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        RecyclerViewExhibitsAdapter myAdapter = new RecyclerViewExhibitsAdapter(this, lstExhibit);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);

    }
}