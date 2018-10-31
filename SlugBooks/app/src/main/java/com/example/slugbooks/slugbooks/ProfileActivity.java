package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.example.slugbooks.slugbooks.HomeActivity;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    DataModel dataModel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView name;
    TextView username;
    TextView bookName2;
    TextView author2;
    TextView className2;
    Button MESSAGE2;
    Button profile2;
    Button Home2;

    String userIDnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userIDnum = HomeActivity.getUserIdNum();

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        name = (TextView) findViewById(R.id.nameID);
        bookName2 = (TextView) findViewById(R.id.bookName2ID);
        author2 = (TextView) findViewById(R.id.authorID2);
        className2 = (TextView) findViewById(R.id.classID2);
        MESSAGE2 = (Button) findViewById(R.id.messageButton2ID);
        profile2 = (Button) findViewById(R.id.profileButton2ID);
        Home2 = (Button) findViewById(R.id.homeButton2ID);
        username = (TextView) findViewById(R.id.usernameID);

        System.out.println("3234123l4kjlkdjsa;klfjsdkfjals;dfja" + firebaseAuth.getUid() + " +============ "  + HomeActivity.getUserIdNum());

        storeDataInObject(ref);



    }

    //this function gets the info from database and store it here
        private void storeDataInObject(DatabaseReference refrence) {

        refrence.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                dataModel = dataSnapshot.child(userIDnum).getValue(DataModel.class);
                name.setText(dataModel.getFirstName() + " " + dataModel.getLastName());
                username.setText(dataModel.getUsername());

                System.out.println(" +++++++++++ " +dataModel.getFirstName() + " +++++++++++++ " + dataModel.getImageUrl());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void launchHomeActivity(View view){
        Intent intent = new Intent (this, HomeActivity.class);
        startActivity(intent);

    }
}
