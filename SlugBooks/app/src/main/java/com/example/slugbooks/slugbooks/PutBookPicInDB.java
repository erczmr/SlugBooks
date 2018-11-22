package com.example.slugbooks.slugbooks;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PutBookPicInDB extends AsyncTask<String,Void,Void> {
    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    DataModel dataModel;

    @Override
    protected Void doInBackground(String... strings) {

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        System.out.println("the first string is: " + strings[0] + "\n and the second one is: " + strings[1]);
        databaseReference.child("users").child(firebaseAuth.getUid())
                .child("bookObject").child(strings[0]).child("imges").child(strings[1]).setValue(strings[2]);
        return null;
    }
}
