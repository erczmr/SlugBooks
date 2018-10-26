package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button logoutButton;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logoutButton = (Button) findViewById(R.id.logoutButtonId);

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
