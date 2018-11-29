package com.example.slugbooks.slugbooks;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    private Uri chosenImage;
    private Button addImageButton;
    private EditText bookNameEditText;
    private EditText authorEditText;
    private EditText descripEditText;
    private EditText classEditText;
    private EditText editionEditText;
    private EditText conditionEditText;
    private EditText priceEditText;

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams layoutParams;

    private BookObject bookObject;
    private DataModel dataModel;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private  List<BookObject> bo;
    private List<String> imgStrings;
    private List<Uri> imgUris;
    private Uri selectedImage;
    private String ali;

    private int id;
    private static final int CHOOSE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_book);

        addImageButton = (Button) findViewById(R.id.addImageButtonID);
        bookNameEditText = (EditText) findViewById(R.id.bookNameEditID);
        authorEditText = (EditText) findViewById(R.id.authorEditID);
        descripEditText = (EditText) findViewById(R.id.descriptionEditID);
        classEditText = (EditText) findViewById(R.id.classEditID);
        editionEditText = (EditText) findViewById(R.id.edititionEditID);
        conditionEditText = (EditText) findViewById(R.id.conditionEditID);
        priceEditText = (EditText) findViewById(R.id.priceEditID);

        linearLayout = (LinearLayout) findViewById(R.id.addImageLayoutID);

        layoutParams = new LinearLayout.LayoutParams(200, 200);
        layoutParams.setMargins(20, 0, 40, 0);

        firebaseAuth = FirebaseAuth.getInstance();
        System.out.println("id is: " + firebaseAuth.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference(firebaseAuth.getUid()).child("BookPics");

        imgStrings = new ArrayList<String>();
        imgUris = new ArrayList<Uri>();
        System.out.println("the refrence at first in addBookpage is: " + storageReference.toString());
        System.out.println("the refrence at first in addBookpage is222: " + storageReference.getPath());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        Intent intent1 = new Intent(AddBookActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_sell:
                        break;

                    case R.id.action_messages:
                        Intent intent3 = new Intent(AddBookActivity.this, InboxActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_profile:
                        Intent intent4 = new Intent(AddBookActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
    }

    //get the image from gllery

    public void openP(View view) {
        openGallery();
    }
    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,CHOOSE_IMAGE);
    }


    //get the image url and save it, also as a test post it on the page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes

        if(resultCode==RESULT_OK && requestCode == CHOOSE_IMAGE) {
            selectedImage = data.getData();

            ali = selectedImage.toString();
            ImageView newBookImg = new ImageView(this);

            newBookImg.setImageURI(selectedImage);
            newBookImg.setLayoutParams(layoutParams);

            linearLayout.addView(newBookImg);
            imgStrings.add(ali);
            imgUris.add(selectedImage);
            //pushToCloud(selectedImage,ali);
        }
    }

    public void post(View view) {

        //get the current Datamodel

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    System.out.println("yoyoyoy ++++++++++++++++++++++++++++++++++++++ other stuff : " + dataSnapshot.getRef() );
                    System.out.println("yoyoyoy ++++++++++++++++++++++++++++++++++++++ other key : " + dataSnapshot.getKey() );
                    System.out.println("yoyoyoy ++++++++++++++++++++++++++++++++++++++ other key : " + dataSnapshot.getValue() );

                    if(dataSnapshot.getKey().equals("users")) {
                        List<String> imgUrls = new ArrayList<String>();
                        bookObject = new BookObject(bookNameEditText.getText().toString(),authorEditText.getText().toString(),
                                descripEditText.getText().toString(),classEditText.getText().toString(),editionEditText.getText().toString(),
                                conditionEditText.getText().toString(),priceEditText.getText().toString(),imgUrls);


                        System.out.println("yoyoyoy ++++++++++++++++++++++++++++++++++++++ database childre : " + dataSnapshot.getChildren().iterator().next());
                        dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);

                        System.out.println(" +++++++++++ " + dataModel.getFirstName() + " +++++++++++++ "
                                + dataModel.getImageUrl());

                        if(dataModel.getBookObject()!=null) {
                            bo = dataModel.getBookObject();
                            System.out.println("the bo object is: " + bo.get(0));
                        }
                        else bo = new ArrayList<BookObject>();


                        bo.add(bookObject);
                        dataModel.setBookObject(bo);
                        databaseReference.child("users").child(firebaseAuth.getUid()).setValue(dataModel);
                        String getIndex = String.valueOf(bo.size()-1);
                        pushToCloud(imgUris,imgStrings,getIndex);
                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

       startActivity(new Intent(AddBookActivity.this,HomeActivity.class));

    }

    private void pushToCloud(List<Uri> igg, final List<String> imgID, final String id1) {
        if(!igg.isEmpty()) {
            for (int i = 0; i < igg.size(); i++) {
                //+ getFileExtension(selectedImage)
                String substr = imgID.get(i).substring(imgID.get(i).length() - 10);
                final StorageReference fileRef = storageReference.child("bookPic#" + i + ".png");
                System.out.println("the imgID is: " + imgID.get(i));

                final int finalI = i;
                fileRef.putFile(igg.get(i)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        System.out.println("the downloaded uri is22222: " + task.getResult().toString());
                        // setImageURLstr(task.getResult().toString());
                        return fileRef.getDownloadUrl();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("didnt put it in the storage");
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            // img = downloadUri.toString();
                            String index = String.valueOf(finalI);
                            new PutBookPicInDB().execute(id1,index , downloadUri.toString());
                            System.out.println("the id1 is: " + id1 + "\nthe index is: " + index );
                            System.out.println("the downloaded uri is: " + downloadUri.toString());
                            //setImageURLstr(downloadUri.toString());

                        } else {
                            Toast.makeText(AddBookActivity.this, "upload failed: "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else{
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }

    }



}

