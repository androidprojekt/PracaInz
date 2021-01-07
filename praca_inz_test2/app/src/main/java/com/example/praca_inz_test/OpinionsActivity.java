package com.example.praca_inz_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class OpinionsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button sendOpinionBtn;
    EditText opinionEditText,nickEditText;
    private usersOpinionListAdapter adapter;
    private ArrayList<userOpinion> opinionsList;
    ListView opinionsLV;
    long maxid=0;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_opinions);

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Opinions");
            sendOpinionBtn = findViewById(R.id.sendOpinionBtnId);
            opinionEditText = findViewById(R.id.opinionEditTextId);
            nickEditText= findViewById(R.id.nickEditTextId);
            opinionsList = new ArrayList<>();
            adapter = new usersOpinionListAdapter(getApplicationContext(), R.layout.opinions_adapter_view, opinionsList);
            opinionsLV = findViewById(R.id.opinionsListView);

            sendOpinionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userOpinion opinion = new userOpinion();
                    opinion.setNickname(nickEditText.getText().toString());
                    opinion.setOpinion(opinionEditText.getText().toString());

                    myRef.child(String.valueOf(maxid)).setValue(opinion);

                    Toast.makeText(getApplicationContext(),"Send opinion succefully!",Toast.LENGTH_SHORT).show();
                    opinionEditText.setText("");
                    nickEditText.setText("");
                    hideKeyboard(activity);
                }
            });
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                        maxid= (dataSnapshot.getChildrenCount());
                    opinionsList.clear();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        userOpinion temp = dataSnapshot1.getValue(userOpinion.class);
                        opinionsList.add(temp);
                    }
                    opinionsLV.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}