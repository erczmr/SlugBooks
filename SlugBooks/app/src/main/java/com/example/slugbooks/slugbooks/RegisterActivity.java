package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.firebase.auth.FirebaseAuth;


import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private LoginButton loginButton;

    //make strings to store the edit texts values in them
    private String emailStr;
    private String passwordStr;
    private String repeatPasswordStr;
    private FirebaseAuth mAuth;


    private static final String EMAIL = "email";

    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.registerButtonID);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);


        // Callback registration
       loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }


        });


        //facebook login manager
      /*  LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
       */
       // AccessToken accessToken = AccessToken.getCurrentAccessToken();
       // boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        //Handle the sign-in flow with the Firebase SDK

        


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, RegisterEmailActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
