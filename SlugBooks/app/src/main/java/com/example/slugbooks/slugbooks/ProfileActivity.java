package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.example.slugbooks.slugbooks.HomeActivity;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ProfileActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    DataModel dataModel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    StorageReference storageReference;

    TextView name;
    TextView username;
    Button MESSAGE2;
    Button profile2;
    Button Home2;
    ImageView profilePic;


    String userIDnum;
    File localFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        profilePic = findViewById(R.id.profilePicImageViewId);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        name = (TextView) findViewById(R.id.nameID);
        MESSAGE2 = (Button) findViewById(R.id.messageButton2ID);
        profile2 = (Button) findViewById(R.id.profileButton2ID);
        Home2 = (Button) findViewById(R.id.homeButton2ID);
        username = (TextView) findViewById(R.id.usernameID);

        System.out.println("3234123l4kjlkdjsa;klfjsdkfjals;dfja   " + firebaseAuth.getUid() + " +============ "  + MainActivity.getUID());

        System.out.println("idddddddddddddddddd : " + RegisterActivity.getMyId());

        //System.out.println("hommmeeeeeeeeeeeeeee: " + HomeActivity.getHomeId());
        storeDataInObject(ref);

        if(isLoggedIn()){}

        else{
        getImageDisplay(storageReference);}

    }
    // check to see if user is logged in with facebook
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
   private void getImageDisplay(StorageReference st) {
       System.out.println("====++++++++====================--=-=-= yooooooooooooo" );


            Task<Uri> str = st.child(RegisterActivity.getMyId() + "/Profile_pic/profilepic.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.get().load(uri).into(profilePic);

                    Toast.makeText(ProfileActivity.this, "Yay worked", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Couldn't get file from Cloud", Toast.LENGTH_SHORT).show();

                }
            });

    }

    //this function gets the info from database and store it here
        private void storeDataInObject(DatabaseReference refrence) {

            System.out.println("====================== +++++++= " + refrence);

            refrence.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


               if(isLoggedIn()){
                   System.out.println("I am loggggggggged in: " + firebaseAuth.getUid() );
                   dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);
                    //if you are on with the facebook button upload the profile picture here
                    name.setText(dataModel.getFirstName());
                   new DownloadImageTask(profilePic).execute(dataModel.getImageUrl());

                }
                else{
                   dataModel = dataSnapshot.child(RegisterActivity.getMyId()).getValue(DataModel.class);
                   name.setText(dataModel.getFirstName() + " " + dataModel.getLastName());
               }

                username.setText(dataModel.getUsername());

               System.out.println(" +++++++++++ " + dataModel.getFirstName() + " +++++++++++++ "
                     + dataModel.getImageUrl());
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

    public void addBook(View view) {
        startActivity(new Intent(this, AddBookActivity.class));
    }
}
