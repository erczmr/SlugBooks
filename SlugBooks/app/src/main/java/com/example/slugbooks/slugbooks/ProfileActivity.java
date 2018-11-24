package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.List;

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
    ImageView profilePic;
    private LinearLayout lv;
    private LinearLayout.LayoutParams textPrams;
    private LinearLayout.LayoutParams layoutParams;

    String userIDnum;
    File localFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.profilePicImageViewId);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        name = (TextView) findViewById(R.id.nameID);
        username = (TextView) findViewById(R.id.usernameID);

        System.out.println("id:issssssssssssssssssssssssss:   " + firebaseAuth.getUid() + " +============ "  + MainActivity.getUID());

        lv = (LinearLayout) findViewById(R.id.profilePageLinearlayout);

        //System.out.println("hommmeeeeeeeeeeeeeee: " + HomeActivity.getHomeId());
        storeDataInObject(ref);
        layoutParams = new LinearLayout.LayoutParams(250, 250);
        textPrams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 50, 0, 50);
        textPrams.setMargins(40, 50, 0, 50);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        Intent intent1 = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_sell:
                        Intent intent2 = new Intent(ProfileActivity.this, AddBookActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_messages:
                        Intent intent3 = new Intent(ProfileActivity.this, InboxActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_profile:
                        break;
                }


                return false;
            }
        });

    }
    // check to see if user is logged in with facebook
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    //this function gets the info from database and store it here
        private void storeDataInObject(final DatabaseReference refrence) {

            System.out.println("====================== +++++++= " +
                    refrence.child("users").child(MainActivity.getUID()));

            refrence.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                   if(dataSnapshot.getKey().equals("users")) {
                       System.out.println("yoyoyoy ++++++++++++++++++++++++++++++++++++++ database childre : " + dataSnapshot.getChildren().iterator().next());
                       dataModel = dataSnapshot.child(MainActivity.getUID()).getValue(DataModel.class);

                       System.out.println("The firebase url:" + dataModel.getImageUrl());

                       if(isLoggedIn()){
                           //if you are on with the facebook button upload the profile picture here
                           name.setText(dataModel.getFirstName());
                           new DownloadImageTask(profilePic).execute(dataModel.getImageUrl());
                           //Picasso.get().load(dataModel.getImageUrl()).into(profilePic);
                           username.setText(dataModel.getUsername());}
                           else{
                       name.setText(dataModel.getFirstName() + " " + dataModel.getLastName());

                       //Picasso.get().load(dataModel.getImageUrl()).into(profilePic);
                       new DownloadImageTask(profilePic).execute(dataModel.getImageUrl());
                       username.setText(dataModel.getUsername());
                       }

                       System.out.println(" +++++++++++ " + dataModel.getFirstName() + " +++++++++++++ "
                            + dataModel.getImageUrl());

                        //get the image info and pics
                       if(dataModel.getBookObject() != null)
                       {
                           List<BookObject> bo = dataModel.getBookObject();
                           for(int i = 0; i < bo.size(); i++)
                           {
                               BookObject bookObject = bo.get(i);

                               if(bookObject.getAuthor()== null) bookObject.setAuthor("N/A");
                               if(bookObject.getBookname()== null) bookObject.setAuthor("N/A");
                               if(bookObject.getClassStr()== null) bookObject.setAuthor("N/A");

                               TextView tx = new TextView(ProfileActivity.this);
                               tx.setText("Book Title: " + bookObject.getBookname() + "\nAuthor: " + bookObject.getAuthor()
                                       + "\nClass: " + bookObject.getClassStr());

                               System.out.println("noooooooooo: " + "Book Title: " + bookObject.getBookname() + "\n\nAuthor: " + bookObject.getAuthor()
                                       + "\n\nClass: " + bookObject.getClassStr());
                               tx.setTextSize(15);
                               tx.setLayoutParams(textPrams);

                               //System.out.println("the bitmap in view function is: " + bt.toString());
                               LinearLayout lh = new LinearLayout(ProfileActivity.this);
                               lh.setOrientation(LinearLayout.HORIZONTAL);

                              // img
                              // img.setImageDrawable(getResources().getDrawable(findViewById(R.drawable.com_facebook_button_icon_white)));
                               if(bookObject.getImges()!= null) {
                                   ImageView img = new ImageView(ProfileActivity.this);
                                   List<String> imgStrings = bookObject.getImges();

                                   System.out.println("theeeeee img url iss: " + imgStrings.get(0));

                                   //Picasso.get().load(imgStrings.get(0)).into(img);
                                   new DownloadImageTask(img).execute(imgStrings.get(0));
                                   //new DownloadImageTask(img).execute(urlsr);
                                   img.setLayoutParams(layoutParams);
                                   lh.addView(img);
                               }

                               lh.addView(tx);
                               lv.addView(lh);


                       }
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


    public void launchHomeActivity(View view){
        Intent intent = new Intent (this, HomeActivity.class);
        startActivity(intent);

    }

    public void addBook(View view) {
        startActivity(new Intent(this, AddBookActivity.class));
    }

    public void messageBut(View view) {
        startActivity(new Intent(this, InboxActivity.class));
    }
}
