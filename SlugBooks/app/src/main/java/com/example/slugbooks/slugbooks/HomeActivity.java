package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {

    SearchView searchBar;
    TextView bookName;
    TextView author;
    TextView className;
    Button MESSAGE;
    Button profile;
    Button Home;

    private Button logoutButton;
    private FirebaseAuth firebaseAuth;

    private FirebaseUser user;
    private static String userIdNum = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logoutButton = (Button) findViewById(R.id.logoutButtonId);
        searchBar = (SearchView) findViewById(R.id.searchBarID);
        bookName = (TextView) findViewById(R.id.bookNameID);
        author = (TextView) findViewById(R.id.bookName2ID);
        className = (TextView) findViewById(R.id.classID);
        MESSAGE = (Button) findViewById(R.id.messageButtonID);
        profile = (Button) findViewById(R.id.profileButtonID);
        Home = (Button) findViewById(R.id.homeButtonID);

        firebaseAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

    }


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

    public void launchProfileActivity(View view){
        Intent intent = new Intent (this, ProfileActivity.class);
        startActivity(intent);
    }

}