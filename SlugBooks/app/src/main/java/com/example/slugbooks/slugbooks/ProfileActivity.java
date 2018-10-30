package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView Name;
    TextView username;
    TextView bookName2;
    TextView author2;
    TextView className2;
    Button MESSAGE2;
    Button profile2;
    Button Home2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bookName2 = (TextView) findViewById(R.id.bookName2ID);
        author2 = (TextView) findViewById(R.id.authorID2);
        className2 = (TextView) findViewById(R.id.classID2);
        MESSAGE2 = (Button) findViewById(R.id.messageButton2ID);
        profile2 = (Button) findViewById(R.id.profileButton2ID);
        Home2 = (Button) findViewById(R.id.homeButton2ID);
    }

    public void launchHomeActivity(View view){
        Intent intent = new Intent (this, HomeActivity.class);
        startActivity(intent);
    }
}
