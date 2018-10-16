package com.example.slugbooks.slugbooks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;

    private Button registerButton;

    //make strings to store the edit texts values in them
    private String emailStr;
    private String passwordStr;
    private String repeatPasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize the edit texts and the button
        emailEditText = (EditText)findViewById(R.id.emailEditTextID);
        passwordEditText = (EditText)findViewById(R.id.passwordEditTextID);
        repeatPasswordEditText = (EditText)findViewById(R.id.repeatPasswordEditTextId);

        registerButton = (Button) findViewById(R.id.registerButtonID);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //First store the values of the edit text in the strings
                emailStr = emailEditText.getText().toString();
                passwordStr = passwordEditText.getText().toString();
                repeatPasswordStr = repeatPasswordEditText.getText().toString();

                //test to see if we got the right email
                System.out.println("email: " + emailStr + "=========== password: " + passwordStr + " =============== repeat Password: " + repeatPasswordStr);


            }
        });
    }
}
