package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;

    private Button registerButton;

    //make strings to store the edit texts values in them
    private String emailStr;
    private String passwordStr;
    private String repeatPasswordStr;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                                // right now lets display a toast only
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Could not Register...  Please try again", Toast.LENGTH_SHORT).show();

                            }
                            // ...
                        }
                    });

        }
        else{
            Toast.makeText(this, "The Passwords Were Not A Mach! Try Again!", Toast.LENGTH_SHORT).show();
        }
    }
}
