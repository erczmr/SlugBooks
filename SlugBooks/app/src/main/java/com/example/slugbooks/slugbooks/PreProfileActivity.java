package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PreProfileActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_profile);


        firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText)findViewById(R.id.lastNameEditTextId);

        saveButton = (Button)findViewById(R.id.saveButtonId);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // store the name in the database
                writeNewUser(firebaseAuth.getUid().toString(),"alibabaei@gmail.com","ali","babaei");


                startActivity(new Intent(PreProfileActivity.this, HomeActivity.class));
                finish();
            }
        });


    }

    private void writeNewUser(String userId, String email, String fName, String lName) {
        DataModel dataModel = new DataModel(userId, email,fName,lName);

        databaseReference.child("users").child(userId).setValue(dataModel);
    }

}
