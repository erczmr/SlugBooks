package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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


    private EditText bookNameEditText;
    private EditText authorEditText;
    private EditText descripEditText;
    private EditText classEditText;
    private EditText editionEditText;
    private EditText conditionEditText;
    private EditText priceEditText;

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

        bookNameEditText = (EditText) findViewById(R.id.bookNameEditID);
        authorEditText = (EditText) findViewById(R.id.authorEditID);
        descripEditText = (EditText) findViewById(R.id.descriptionEditID);
        classEditText = (EditText) findViewById(R.id.classEditID);
        editionEditText = (EditText) findViewById(R.id.editionEditID);
        conditionEditText = (EditText) findViewById(R.id.conditionEditID);
        priceEditText = (EditText) findViewById(R.id.priceEditID);


        System.out.println("the user id is: " + firebaseAuth.getUid());
        getData();
    }

    private void getData() {

        Bundle bundle = getIntent().getExtras();

        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());

        index = bundle.getInt("index");
        System.out.println("the index isss: " + index);

        bookNameEditText.setText(bj.getBookname());
        authorEditText.setText(bj.getAuthor());
        descripEditText.setText(bj.getDescriptionStr());
        classEditText.setText(bj.getClassStr());
        editionEditText.setText(bj.getEdition());
        conditionEditText.setText(bj.getCondition());
        priceEditText.setText(bj.getPrice());



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

                            startActivity(new Intent(editBookActivity.this,ProfileActivity.class));

                    }
                    else{
                        System.out.println("firebase auth is null : " + firebaseAuth.getUid());
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
        Bundle bundle = getIntent().getExtras();

        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());

        index = bundle.getInt("index");
        System.out.println("the index isss: " + index);

        List<String> imgUrls = bj.getImges();

        BookObject bj2= new BookObject(bookNameEditText.getText().toString(),authorEditText.getText().toString(),
                descripEditText.getText().toString(),classEditText.getText().toString(),editionEditText.getText().toString(),
                conditionEditText.getText().toString(),
                priceEditText.getText().toString(),imgUrls);

        databaseReference.child("users").child(firebaseAuth.getUid()).child("bookObject").
                child(String.valueOf(index)).setValue(bj2);
        System.out.println("the new book name isss: " + bookNameEditText.getText().toString());
        startActivity(new Intent(editBookActivity.this,ProfileActivity.class));
    }

    public void editPics(View view) {
        Bundle bundle = getIntent().getExtras();
        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());
        index = bundle.getInt("index");

        Intent i = new Intent(editBookActivity.this, ImageEditActivity.class);
        //intent.putExtra("book", (Parcelable) bookObject);
        i.putExtra("book", (Serializable) bj);
        System.out.println("final i is: " + index);
        i.putExtra("index", index);
        startActivity(i);

    }
}
