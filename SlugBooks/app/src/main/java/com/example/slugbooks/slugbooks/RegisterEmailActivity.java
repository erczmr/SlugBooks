package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterEmailActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;


    private Button registerButton;
    private LoginButton loginButton;

    //make strings to store the edit texts values in them
    private String emailStr;
    private String passwordStr;
    private String repeatPasswordStr;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);


        //initialize the edit texts and the button
        emailEditText = (EditText)findViewById(R.id.emailEditTextID);
        passwordEditText = (EditText)findViewById(R.id.passwordEditTextID);
        repeatPasswordEditText = (EditText)findViewById(R.id.repeatPasswordEditTextId);

        registerButton = (Button) findViewById(R.id.registerButtonID);

        //initialize the firebase auth
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //First store the values of the edit text in the strings
                emailStr = emailEditText.getText().toString();
                passwordStr = passwordEditText.getText().toString();
                repeatPasswordStr = repeatPasswordEditText.getText().toString();

                //test to see if we got the right email
                System.out.println("email: " + emailStr + "=========== password: " + passwordStr + " =============== repeat Password: " + repeatPasswordStr);

                registerUser(emailStr, passwordStr, repeatPasswordStr);
            }
        });
    }
    private void registerUser(String emailStr, String passwordStr, String repeatPasswordStr) {

        if (TextUtils.isEmpty(emailStr)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            // stopping the function execution further
        }
        else if (TextUtils.isEmpty(passwordStr)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            // stopping the function execution further
        }
        else if (TextUtils.isEmpty(repeatPasswordStr)) {
            Toast.makeText(this, "Please repeat the password", Toast.LENGTH_SHORT).show();
        }

        //progressDialog.setMessage("Register User...");
        //progressDialog.show();

        else if (repeatPasswordStr.equals(passwordStr)) {


            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //user is successfully registered and logged in
                                // we will start profile activity here
                                Toast.makeText(RegisterEmailActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(RegisterEmailActivity.this, PreProfileActivity.class));
                            } else {
                                Toast.makeText(RegisterEmailActivity.this, "Could not Register...  Please try again", Toast.LENGTH_SHORT).show();


                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                switch (errorCode) {

                                    case "ERROR_INVALID_CUSTOM_TOKEN":
                                        Toast.makeText(RegisterEmailActivity.this, "The custom token format is incorrect.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                        Toast.makeText(RegisterEmailActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_CREDENTIAL":
                                        Toast.makeText(RegisterEmailActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_EMAIL":
                                        Toast.makeText(RegisterEmailActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                        emailEditText.setError("The email address is badly formatted.");
                                        emailEditText.requestFocus();
                                        break;

                                    case "ERROR_WRONG_PASSWORD":
                                        Toast.makeText(RegisterEmailActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                        passwordEditText.setError("password is incorrect ");
                                        passwordEditText.requestFocus();
                                        passwordEditText.setText("");
                                        break;

                                    case "ERROR_USER_MISMATCH":
                                        Toast.makeText(RegisterEmailActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_REQUIRES_RECENT_LOGIN":
                                        Toast.makeText(RegisterEmailActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        Toast.makeText(RegisterEmailActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        Toast.makeText(RegisterEmailActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                        emailEditText.setError("The email address is already in use by another account.");
                                        emailEditText.requestFocus();
                                        break;

                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        Toast.makeText(RegisterEmailActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_DISABLED":
                                        Toast.makeText(RegisterEmailActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_TOKEN_EXPIRED":
                                        Toast.makeText(RegisterEmailActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_NOT_FOUND":
                                        Toast.makeText(RegisterEmailActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_USER_TOKEN":
                                        Toast.makeText(RegisterEmailActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        Toast.makeText(RegisterEmailActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_WEAK_PASSWORD":
                                        Toast.makeText(RegisterEmailActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                        passwordEditText.setError("The password is invalid it must 6 characters at least");
                                        passwordEditText.requestFocus();
                                        break;
                                }
                            }
                            // ...
                        }
                    });

        }
        else{
            Toast.makeText(this, "The Passwords Were Not A Mach! Try Again!", Toast.LENGTH_SHORT).show();

        }}
}
