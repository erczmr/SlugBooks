package com.example.slugbooks.slugbooks;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPassword;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.emailForgotPasswordEditTextId);
        resetPassword = (Button) findViewById(R.id.resetPasswordButtonId);

        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailEditText.getText().toString().isEmpty() )
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                }
                else{
                    resetPassword(emailEditText.getText().toString());
                }
            }
        });

    }

    private void resetPassword(final String em) {
        firebaseAuth.sendPasswordResetEmail(em)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "We have sent you a new Password to email:", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to sent a password, There is no such email", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}