package com.example.slugbooks.slugbooks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class editBookActivity extends AppCompatActivity  {
    private BookObject bj;
    private DataModel dataModel;
    private List<BookObject> bookObjects;

    private int index;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        System.out.println("Database Refrence is: " + databaseReference.toString());

        System.out.println("the user id is: " + firebaseAuth.getUid());
        getData();
    }

    private void getData() {

        Bundle bundle = getIntent().getExtras();

        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());

        index = bundle.getInt("index");
        System.out.println("the index isss: " + index);


    }

    public void delete(View view) {

        Bundle bundle = getIntent().getExtras();

        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());

        index = bundle.getInt("index");
        System.out.println("the index isss: " + index);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getKey().equals("users"))
                {

                    if(firebaseAuth.getUid() != null) {

                        dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);

                         bookObjects = dataModel.getBookObject();

                         bookObjects.set(index, null);
                         dataModel.setBookObject(bookObjects);
                         databaseReference.child("users").child(firebaseAuth.getUid()).setValue(dataModel);



                    }
                    else{
                        //System.out.println("firebase auth is null : " + firebaseAuth.getUid());
                    }
                }
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

    public void edit(View view) {
    }
}
