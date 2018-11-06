package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private TextView forgotPassTextView;

    private String emailstr;
    private String passwordstr;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private Button facebookConnectButton;

    private static final String EMAIL = "email";

    private CallbackManager callbackManager;

    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = (EditText) findViewById(R.id.emailEditTextId);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextId);
        loginButton = (Button)findViewById(R.id.loginButtonId);
        registerButton = (Button) findViewById(R.id.registerButtonId);
        forgotPassTextView = (TextView) findViewById(R.id.forgotpasswordTextViewId);
        callbackManager = CallbackManager.Factory.create();
        //all about facebook button
        facebookConnectButton = (Button) findViewById(R.id.facebookConnectButtonId);


       /* @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            updateUI(currentUser);
        }
*/

        databaseReference = FirebaseDatabase.getInstance().getReference();
        facebookConnectButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //firebaseAuth = FirebaseAuth.getInstance();
            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList(EMAIL, "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                    // App code
                    handleFacebookAccessToken(loginResult.getAccessToken());
                    getUserInfo(loginResult);
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
        }
    });


        //check to see if the user is already loged in then go stright to the homepage
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }

        //after clicking on register button, go to register page
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });

        //if the forgot password text view was clicked, go to forgot password page
        forgotPassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });

        //after clicking the login button, it will check to see if email password is write, then goes in if true else it wont
        //and will give an error toast
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //store the email edit text and the password edit texts in strings
                emailstr = emailEditText.getText().toString();
                passwordstr = passwordEditText.getText().toString();

                //call the function that logs in the user if the email pass is right
                userLogin(emailstr, passwordstr);

            }
        });

    }

    //log in if the user and passwrrd are correct
    private void userLogin(String em, final String pass){

        firebaseAuth.signInWithEmailAndPassword(em, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if login was a success then go to home page
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            //if login was not a success then give an error msg
                            if (!task.isSuccessful()){

                                Toast.makeText(MainActivity.this, "Username Or password was wrong!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }


    private void handleFacebookAccessToken(AccessToken token) {
        System.err.println("handleFacebookAccessToken");

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.err.println("signInWithCredential:success");

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            System.err.println("signInWithCredential:failed");
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    protected void getUserInfo(final LoginResult login_result){

        GraphRequest data_request = GraphRequest.newMeRequest(
                login_result.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {

                            String facebook_id = "N/A";
                            String f_name = "N/A";
                            String l_name = "N/A";
                            String userName = "N/A";
                            String email_id = "N/A";
                            String picUrl = "N/A";

                            if(object.has("id"))
                                facebook_id = object.getString("id");

                            if(object.has("first_name"))
                            {
                                f_name = object.getString("first_name");
                            }
                             else{
                                f_name = object.getString("name");
                            }

                            if(object.has("last_name"))
                             l_name = object.getString("last_name");

                            if(object.has("name"))
                             userName = object.getString("name");

                            if(object.has("email"))
                             email_id = object.getString("email");
                            //String username = object.getString("username");

                            String token = login_result.getAccessToken().getToken();

                            System.out.println("+++_=_+_=_+_=_=_+ " + token );

                             picUrl = "https://graph.facebook.com/me/picture?type=normal&method=GET&access_token="+ token;

                            dataModel = new DataModel(facebook_id,"@" +userName,email_id,f_name, l_name,picUrl);

                            saveFacebookCredentialsInFirebase(login_result.getAccessToken(),dataModel);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
        data_request.executeAsync();
    }

    private void saveFacebookCredentialsInFirebase(AccessToken accessToken, final DataModel dm){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this,"Facebook Data has been saved", Toast.LENGTH_SHORT).show();
                databaseReference.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(dm);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Facebook Data Didnt save", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error logging in", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Login in Successful", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
