package com.example.slugbooks.slugbooks;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    private Uri chosenImage;
    Button addImageButton;
    EditText bookNameEditText;
    EditText authorEditText;
    EditText descripEditText;
    EditText classEditText;
    EditText editionEditText;
    EditText conditionEditText;
    EditText priceEditText;

    LinearLayout linearLayout;
    LinearLayout.LayoutParams layoutParams;

    BookObject bookObject;
    DataModel dataModel;

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
            Uri selectedImage = data.getData();
            String ali = selectedImage.getPath();

            ImageView newBookImg = new ImageView(this);

            newBookImg.setImageURI(selectedImage);
            newBookImg.setLayoutParams(layoutParams);

            linearLayout.addView(newBookImg);

        }
    }

    public void post(View view) {
        List<String> imgUrls = new ArrayList<String>();
        bookObject = new BookObject(bookNameEditText.getText().toString(),authorEditText.getText().toString(),
                                descripEditText.getText().toString(),classEditText.getText().toString(),editionEditText.getText().toString(),
                                conditionEditText.getText().toString(),priceEditText.getText().toString(),imgUrls);



    }


}

