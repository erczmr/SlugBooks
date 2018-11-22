package com.example.slugbooks.slugbooks;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity {

    SearchView searchBar;
    //TextView bookName;
    //TextView author;
    //TextView className;
    Button MESSAGE;
    Button profile;
    Button Home;
    public static String imgurl;

    private Button logoutButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    public static String getHomeId() {
        return homeId;
    }

    public static void setHomeId(String homeId) {
        HomeActivity.homeId = homeId;
    }

    static String homeId;
    private FirebaseUser user;

    private static String userIdNum = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        System.out.println("not working ooooooooooooooooo");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        storageReference = FirebaseStorage.getInstance().getReference();

        logoutButton = (Button) findViewById(R.id.logoutButtonId);
        searchBar = (SearchView) findViewById(R.id.searchBarID);
        //bookName = (TextView) findViewById(R.id.bookNameID);
        //author = (TextView) findViewById(R.id.bookName2ID);
        //className = (TextView) findViewById(R.id.classID);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:

                        break;

                    case R.id.action_sell:
                        Intent intent2 = new Intent(HomeActivity.this, AddBookActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_messages:
                        Intent intent3 = new Intent(HomeActivity.this, InboxActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_profile:
                        Intent intent4 = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
/*\

        FirebaseUserMetadata metadata = firebaseAuth.getCurrentUser().getMetadata();
        if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
            // The user is new, show them a fancy intro screen!
            //if(!isLoggedIn())
               // addImgUrlToDB();
        } else {
            // This is an existing user, show them a welcome back screen.
        }

 */

    }

   /* private void addImgUrlToDB() {
            System.out.println("storrrrraaaaaaaaaagggggggge:   " +  storageReference + "\ndatabase: " + databaseReference + "\n authid: " + firebaseAuth.getUid());

       // getImageDisplay(storageReference, firebaseAuth);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    System.out.println("The imgURL is : " + dataSnapshot.getValue() + "\nand the userid is: " + dataSnapshot.getKey()+ "\nThe Home Database ref is: " +
                            databaseReference + "\nThe img url is: " + imgurl);

                    if (!isLoggedIn()) {

                        if(dataSnapshot.getKey().equals("users")) {

                            System.out.println("lettssss : " + imgurlStr);
                            DataModel dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);

                            System.out.println("the image url before change: " + dataModel.getImageUrl() + "\nthe id for data model is: "
                                    + dataModel.getUserID());

                            dataModel.setImageUrl(imgurlStr);

                            System.out.println("the image url after change: " + dataModel.getImageUrl());
                            databaseReference.child("users").child(dataModel.getUserID()).setValue(dataModel);
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
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void getImageDisplay(StorageReference st, FirebaseAuth fAuth) {
        System.out.println("====++++++++====================--=-=-= yooooooooooooo : " + fAuth.getUid() );


        Task<Uri> str = st.child(fAuth.getUid() + "/Profile_pic/profilepic.png").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("got the imgurl from url: " + imgurl);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.err.println("Couldn't get file from Cloud");

            }
        });
    }
*/
    public void logoutUser(){

        //if the user loged in with thier facebook or not
        if(isLoggedIn())
        {
            //logout from the facebook
            LoginManager.getInstance().logOut();
        }
        //logout from the firebase
        firebaseAuth.signOut();
        if(firebaseAuth.getCurrentUser() == null);
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }

    // check to see if user is logged in with facebook
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
    //disable back button
    @Override
    public void onBackPressed() {
        if (true) {
            //do nothing
        } else {
            super.onBackPressed();
        }
    }
}