package com.example.praca_inz_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OpinionsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button sendOpinionBtn;
    EditText opinionEditText,nickEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinions);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Opinions");
        sendOpinionBtn = findViewById(R.id.sendOpinionBtnId);
        opinionEditText = findViewById(R.id.opinionEditTextId);
        nickEditText= findViewById(R.id.nickEditTextId);

        sendOpinionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String opinion= opinionEditText.getText().toString();
                String nick = nickEditText.getText().toString();
                myRef.child(nick).setValue(opinion);
                Toast.makeText(getApplicationContext(),"Send opinion succefully!",Toast.LENGTH_SHORT).show();
                opinionEditText.setText("");
                nickEditText.setText("");
            }
        });
        //myRef.setValue("Hello, World!");
    }
}