package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageEditActivity extends AppCompatActivity {

    private BookObject jj;
    private List<String> images;
    private static int index;

    private Uri uri;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DataModel dataModel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    StorageReference storageReference;

    private int lastIndex = 0;
    TextView name;
    TextView username;
    ImageView profilePic;
    private LinearLayout lv;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams buttonPram;

    private List<BookObject> bookObjects;
    private List<String> bookImgStr;

    private List<Uri> urlList;
    private List<String> imgList;
    private static final int CHOOSE_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_edit);


        firebaseAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.profilePicImageViewId);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        name = (TextView) findViewById(R.id.nameID);
        username = (TextView) findViewById(R.id.usernameID);

        System.out.println("id:issssssssssssssssssssssssss:   " + firebaseAuth.getUid() + " +============ " + MainActivity.getUID());


        lv = (LinearLayout) findViewById(R.id.editImagePageLinearlayoutid);
        layoutParams = new LinearLayout.LayoutParams(250, 250);
        layoutParams.setMargins(40, 50, 0, 50);
        buttonPram = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonPram.setMargins(40, 50, 0, 50);

        Bundle bundle = getIntent().getExtras();
        jj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + jj.getAuthor());

        index = bundle.getInt("index");
        System.out.println("the index is: " + index);
         displayImages();
    }

    private void displayImages() {

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals("users")) {
                    System.out.println("the frikin id is: " + firebaseAuth.getUid());
                    dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);
                    for (int j = 0; j < dataModel.bookObject.size(); j++) {
                        if (j == index) {
                            BookObject bj = dataModel.bookObject.get(index);
                            if (bj.getImges() != null) {
                                List<String> img = bj.getImges();
                                for (int i = 0; i < img.size(); i++) {
                                    if (img.get(i) != null) {
                                        final String bookImg = img.get(i);

                                        Button bt = new Button(ImageEditActivity.this);
                                        bt.setLayoutParams(buttonPram);
                                        bt.setText("Delete");

                                        //System.out.println("the bitmap in view function is: " + bt.toString());
                                        final LinearLayout lh = new LinearLayout(ImageEditActivity.this);
                                        lh.setOrientation(LinearLayout.HORIZONTAL);

                                        // img.setImageDrawable(getResources().getDrawable(findViewById(R.drawable.com_facebook_button_icon_white)));

                                        ImageView image = new ImageView(ImageEditActivity.this);

                                        Picasso.get().load(img.get(i)).into(image);
                                        //new DownloadImageTask(img).execute(imgStrings.get(0));
                                        //new DownloadImageTask(img).execute(urlsr);
                                        image.setLayoutParams(layoutParams);
                                        lh.addView(image);


                                        lh.addView(bt);
                                        lv.addView(lh);

                                        lastIndex = bj.getImges().size() - 1;
                                        final int finalI = i;
                                        bt.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (firebaseAuth.getUid() != null) {

                                                    dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);

                                                    if (dataModel.getBookObject() != null) {
                                                        bookObjects = dataModel.getBookObject();

                                                        if (bookObjects.get(index).getImges() != null) {
                                                            bookImgStr = bookObjects.get(index).getImges();
                                                            bookImgStr.set(finalI, null);
                                                            lv.removeView(lh);

                                                            bookObjects.get(index).setImges(bookImgStr);

                                                            dataModel.setBookObject(bookObjects);

                                                            databaseReference.child("users").child(firebaseAuth.getUid()).setValue(dataModel);
                                                        }

                                                        dataModel.setBookObject(bookObjects);

                                                    }
                                                } else {
                                                    System.out.println("firebase auth is null : " + firebaseAuth.getUid());
                                                }

                                            }
                                        });

                                    }
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                lv.removeAllViews();
            onChildAdded(dataSnapshot,s);
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



    }

    public void addImg(View view) {
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

        System.out.println("Test 100000000");

        if(resultCode==RESULT_OK && requestCode == CHOOSE_IMAGE) {


            if (data != null ){


            Button bt = new Button(ImageEditActivity.this);
            bt.setLayoutParams(buttonPram);
            bt.setText("Delete");
            LinearLayout lh = new LinearLayout(ImageEditActivity.this);
            lh.setOrientation(LinearLayout.HORIZONTAL);
            System.out.println("Test 123420");
            System.out.println("the Data isss: " + data.getData().toString());

                uri = data.getData();

                final String ali = uri.toString();
                final ImageView newBookImg = new ImageView(this);

                newBookImg.setImageURI(uri);
                newBookImg.setLayoutParams(layoutParams);

                lh.addView(newBookImg);
                lh.addView(bt);
                lv.addView(lh);

                pushToCloud(uri, ali);


                //urlList.add(uri);
                //imgList.add(ali);

                System.out.println("The string isss: " + ali);
                updatenewData(bt, ali);
            }
            else{
                Toast.makeText(this, "Didn't work", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatenewData(Button bt, final String ali) {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals("users"))
                {
                    if(firebaseAuth.getUid() != null) {
                        dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);

                        if(dataModel.getBookObject()!=null) {
                            bookObjects = dataModel.getBookObject();

                            if(bookObjects.get(index).getImges()!= null)
                            {
                                System.out.println("the index isss: " + index);
                                bookImgStr =   bookObjects.get(index).getImges();
                                System.out.println("the imges size isss: " + bookImgStr.size());
                                bookImgStr.add(ali);
                                bookObjects.get(index).setImges(bookImgStr);
                                dataModel.setBookObject(bookObjects);
                                databaseReference.child("users").child(firebaseAuth.getUid()).setValue(dataModel);
                            }

                            dataModel.setBookObject(bookObjects);

                        }
                    }
                    else{
                        System.out.println("firebase auth is null : " + firebaseAuth.getUid());
                    }
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

        //imgStrings.add(ali);
        //imgUris.add(selectedImage);
        //pushToCloud(selectedImage,ali);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.getKey().equals("users"))
                        {
                            if(firebaseAuth.getUid() != null) {

                                dataModel = dataSnapshot.child(firebaseAuth.getUid()).getValue(DataModel.class);
                                if(dataModel.getBookObject()!=null) {
                                    bookObjects = dataModel.getBookObject();
                                    if(bookObjects.get(index).getImges()!= null)
                                    {

                                        System.out.println("the index isss: " + index);
                                        bookImgStr = bookObjects.get(index).getImges();

                                        bookImgStr.set(bookImgStr.size(),null);

                                        bookObjects.get(index).setImges(bookImgStr);

                                        dataModel.setBookObject(bookObjects);

                                        databaseReference.child("users").child(firebaseAuth.getUid()).setValue(dataModel);

                                    }

                                    dataModel.setBookObject(bookObjects);

                                }
                            }
                            else{
                                System.out.println("firebase auth is null : " + firebaseAuth.getUid());
                            }
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

            }
        });
    }


    private void pushToCloud(Uri igg, final String imgID) {
        System.out.println("test 1");
         final String id1 = String.valueOf(lastIndex+1);
        if(igg != null) {
            System.out.println("test 2");

                //+ getFileExtension(selectedImage)
                String substr = imgID.substring(imgID.length() - 10);
                final StorageReference fileRef = storageReference.child(firebaseAuth.getUid()).child("BookPics").child("bookPic#" + id1 + ".png");
                System.out.println("the imgID is: " + imgID);
                System.out.println("test 3");
                final int finalI = lastIndex;
                fileRef.putFile(igg).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                            System.out.println("test 4");
                            String ii = String.valueOf(index);
                            System.out.println("index is: " + index + "\nid1 is : " + id1);
                            new PutBookPicInDB().execute(ii,id1 , downloadUri.toString());
                            System.out.println("the id1 is: " + id1 + "\nthe index is: " + index );
                            System.out.println("the downloaded uri is: " + downloadUri.toString());
                            //setImageURLstr(downloadUri.toString());

                        } else {
                            Toast.makeText(ImageEditActivity.this, "upload failed: "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

}


