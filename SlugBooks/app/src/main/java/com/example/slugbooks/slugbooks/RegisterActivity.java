package com.example.slugbooks.slugbooks;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;


public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText usernameEditText;
    private EditText fNameEditText;
    private EditText lNameEditText;


    private Button registerButton;
    private LoginButton loginButton;

    //make strings to store the edit texts values in them
    private String emailStr;
    private String passwordStr;
    private String repeatPasswordStr;
    private String usernameStr;
    private String fNameStr;
    private String lNameStr;
    public String imageURLstr = "N/A";

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressBar progressBar;


    private Uri selectedImage;
    ImageView bookImage;
    TextView uploadPicTextView;


    private static final int PICK_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //initialize the edit texts and the button
        emailEditText = (EditText)findViewById(R.id.emailEditTextID);
        usernameEditText = (EditText)findViewById(R.id.userNameEditTextID);
        passwordEditText = (EditText)findViewById(R.id.passwordEditTextID);
        repeatPasswordEditText = (EditText)findViewById(R.id.repeatPasswordEditTextId);
        fNameEditText = (EditText)findViewById(R.id.firstNameID);
        lNameEditText = (EditText)findViewById(R.id.lastNameID);

        registerButton = (Button) findViewById(R.id.registerButtonID);

        uploadPicTextView = (TextView) findViewById(R.id.uploadPicTextViewID);
        bookImage = (ImageView) findViewById(R.id.uploadIPicImageViewID);

        //initialize the firebase auth
        mAuth = FirebaseAuth.getInstance();


        //upload pics from the phone
        uploadPicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextToString(fNameEditText, lNameEditText, usernameEditText, emailEditText, passwordEditText, repeatPasswordEditText);
                //test to see if we got the right email
                System.out.println("email: " + emailStr + "=========== password: " + passwordStr + " =============== repeat Password: " + repeatPasswordStr);


                if (selectedImage != null){
                    registerUser(emailStr, passwordStr, repeatPasswordStr, usernameStr, fNameStr,lNameStr, imageURLstr);

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please select a picture", Toast.LENGTH_SHORT ).show();
                }

            }
        });

    }




    private void registerUser(final String emailString, String passwordString, String repeatPasswordString, final String usernameString, final String fNameString, final String lNameString, final String imageString) {

        if (TextUtils.isEmpty(fNameString)){Toast.makeText(this, "Please enter First Name", Toast.LENGTH_SHORT).show();}
        else if (TextUtils.isEmpty(lNameString)){Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_SHORT).show();}
        else if (TextUtils.isEmpty(usernameString)){Toast.makeText(this, "Please enter User Name", Toast.LENGTH_SHORT).show();}
        else if (TextUtils.isEmpty(imageString)){Toast.makeText(this, "Please Select an image", Toast.LENGTH_SHORT).show();}
        else if (TextUtils.isEmpty(emailString)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            // stopping the function execution further
        }
        else if (TextUtils.isEmpty(passwordString)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            // stopping the function execution further
        }
        else if (TextUtils.isEmpty(repeatPasswordString)) {
            Toast.makeText(this, "Please repeat the password", Toast.LENGTH_SHORT).show();
        }

        else if (repeatPasswordString.equals(passwordString)) {

            mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //user is successfully registered and logged in
                                // we will start profile activity here
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                DataModel dataModel = new DataModel(mAuth.getUid().toString(), usernameString,emailString,fNameString,lNameString,imageString);
                                writeNewUser(dataModel);

                                storageReference = FirebaseStorage.getInstance().getReference(mAuth.getUid()).child("Profile_pic");
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));

                                pushToCloud(selectedImage);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Could not Register...  Please try again", Toast.LENGTH_SHORT).show();

                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                switch (errorCode) {

                                    case "ERROR_INVALID_CUSTOM_TOKEN":
                                        Toast.makeText(RegisterActivity.this, "The custom token format is incorrect.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                        Toast.makeText(RegisterActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_CREDENTIAL":
                                        Toast.makeText(RegisterActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_EMAIL":
                                        Toast.makeText(RegisterActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                        emailEditText.setError("The email address is badly formatted.");
                                        emailEditText.requestFocus();
                                        break;

                                    case "ERROR_WRONG_PASSWORD":
                                        Toast.makeText(RegisterActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                        passwordEditText.setError("password is incorrect ");
                                        passwordEditText.requestFocus();
                                        passwordEditText.setText("");
                                        break;

                                    case "ERROR_USER_MISMATCH":
                                        Toast.makeText(RegisterActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_REQUIRES_RECENT_LOGIN":
                                        Toast.makeText(RegisterActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        Toast.makeText(RegisterActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        Toast.makeText(RegisterActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                        emailEditText.setError("The email address is already in use by another account.");
                                        emailEditText.requestFocus();
                                        break;

                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        Toast.makeText(RegisterActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_DISABLED":
                                        Toast.makeText(RegisterActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_TOKEN_EXPIRED":
                                        Toast.makeText(RegisterActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_NOT_FOUND":
                                        Toast.makeText(RegisterActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_USER_TOKEN":
                                        Toast.makeText(RegisterActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        Toast.makeText(RegisterActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_WEAK_PASSWORD":
                                        Toast.makeText(RegisterActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                        passwordEditText.setError("The password is invalid it must 6 characters at least");
                                        passwordEditText.requestFocus();
                                        break;
                                }
                            }
                        }
                    });

        }
        else{
            Toast.makeText(this, "The Passwords Were Not A Mach! Try Again!", Toast.LENGTH_SHORT).show();
        }}

    //get the image from gllery
    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }


    //get the image url and save it, also as a test post it on the page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(resultCode==RESULT_OK && requestCode == PICK_IMAGE) {
            selectedImage = data.getData();
            String ali = selectedImage.getPath();

            System.out.println("=-=-=-=-=---=-=-=-=-=-= " + ali );

            imageURLstr = selectedImage.getPath();
            bookImage.setImageURI(selectedImage);
            System.out.println("URL is : " + imageURLstr);

            // bookImage.setImageURI(selectedImage);
        }
    }

    private void writeNewUser(DataModel dataM) {
        databaseReference.child("users").child(dataM.getUserID()).setValue(dataM);
    }

    //this function turns edittexts to strings
    private void EditTextToString(EditText firstName, EditText lastName, EditText userName, EditText email, EditText pass, EditText repass)
    {
        //First store the values of the edit text in the strings
        emailStr = email.getText().toString();
        passwordStr = pass.getText().toString();
        repeatPasswordStr = repass.getText().toString();
        usernameStr = userName.getText().toString();
        fNameStr = firstName.getText().toString();
        lNameStr = lastName.getText().toString();
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void pushToCloud(Uri img) {
        if(img != null)
        {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(selectedImage));

            fileRef.putFile(img).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                    } else {
                        Toast.makeText(RegisterActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}