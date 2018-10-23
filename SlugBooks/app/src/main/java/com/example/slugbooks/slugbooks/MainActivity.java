package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private TextView forgotPassTextView;

    private String emailstr;
    private String passwordstr;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = (EditText) findViewById(R.id.emailEditTextId);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextId);

        loginButton = (Button)findViewById(R.id.loginButtonId);
        registerButton = (Button) findViewById(R.id.registerButtonId);

        forgotPassTextView = (TextView) findViewById(R.id.forgotpasswordTextViewId);


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
}
