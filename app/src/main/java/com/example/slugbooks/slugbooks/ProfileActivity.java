package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity  {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    DataModel dataModel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    StorageReference storageReference;

    TextView name;
    TextView username;
    TextView logout;
    ImageView profilePic;

    private ProfileAdapter adapter;
    private List<BookObject> dataModelArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        profilePic = findViewById(R.id.profilePicImageViewId);
        logout = findViewById(R.id.logOutTextview);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        dataModelArrayList = new ArrayList<BookObject>();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        name = (TextView) findViewById(R.id.nameID);
        username = (TextView) findViewById(R.id.usernameID);

        System.out.println("id:issssssssssssssssssssssssss:   " + firebaseAuth.getUid() + " +============ "  + MainActivity.getUID());

       // lv.setGravity(Gravity.CENTER);

        //System.out.println("hommmeeeeeeeeeeeeeee: " + HomeActivity.getHomeId());
        storeDataInObject(ref,firebaseAuth);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        Intent intent1 = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_sell:
                        Intent intent2 = new Intent(ProfileActivity.this, AddBookActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_messages:
                        Intent intent3 = new Intent(ProfileActivity.this, InboxActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_profile:
                        break;
                }


                return false;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ProfileAdapter(ProfileActivity.this,dataModelArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    // check to see if user is logged in with facebook
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    //this function gets the info from database and store it here
        private void storeDataInObject(final DatabaseReference refrence,final FirebaseAuth mAuth) {

            System.out.println("========mAuthhhhh============== +++++++= " + mAuth.getUid());

            refrence.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.getKey().equals("users")) {
                        dataModel = dataSnapshot.child(mAuth.getUid()).getValue(DataModel.class);

                        System.out.println("The firebase url:" + dataModel.getImageUrl());

                        if(isLoggedIn()){
                            //if you are on with the facebook button upload the profile picture here
                            name.setText(dataModel.getFirstName());
                            new DownloadImageTask(profilePic).execute(dataModel.getImageUrl());
                            // Picasso.get().load(dataModel.getImageUrl()).into(profilePic);
                            username.setText(dataModel.getUsername());}
                        else{
                            name.setText(dataModel.getFirstName() + " " + dataModel.getLastName());

                            //Picasso.get().load(dataModel.getImageUrl()).into(profilePic);
                            new DownloadImageTask(profilePic).execute(dataModel.getImageUrl());
                            username.setText(dataModel.getUsername());
                        }

                        //get the image info and pics
                        if(dataModel.getBookObject() != null)
                        {
                            List<BookObject> bo = dataModel.getBookObject();
                            for(int i = 0; i < bo.size(); i++)
                            {
                                if(bo.get(i)!=null) {
                                    final BookObject bookObject = bo.get(i);

                                    if (bookObject.getAuthor() == null) bookObject.setAuthor("N/A");
                                    if (bookObject.getBookname() == null)
                                        bookObject.setAuthor("N/A");
                                    if (bookObject.getClassStr() == null)
                                        bookObject.setAuthor("N/A");

                                    dataModelArrayList.add(bookObject);

                                }
                            }
                        }
                    }
                System.out.println("object issssssssss: it works" );
                if (!dataModelArrayList.isEmpty()){
                    for (int j = 0; j < dataModelArrayList.size(); j++) {
                        System.out.println("object " + j + " issss: " + dataModelArrayList.get(j).getBookname());
                        System.out.println("the Object String issss: " + dataModelArrayList.get(j).toString());
                    }
                }else
                    System.out.println("noooo objects");


                    setUpRecyclerView();
                }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                dataModelArrayList.clear();
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

    public void logoutUser(){
        //if the user loged in with thier facebook or not
        if(isLoggedIn())
        {
            //logout from the facebook
            LoginManager.getInstance().logOut();
        }
        //logout from the firebase
        firebaseAuth.signOut();
        if(firebaseAuth.getCurrentUser() == null);
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }

    public void addBook(View view) {
        startActivity(new Intent(this, AddBookActivity.class));
    }

    public void messageBut(View view) {
        startActivity(new Intent(this, InboxActivity.class));
    }

    /*

     public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                   if(dataSnapshot.getKey().equals("users")) {
                       dataModel = dataSnapshot.child(mAuth.getUid()).getValue(DataModel.class);

                       System.out.println("The firebase url:" + dataModel.getImageUrl());

                       if(isLoggedIn()){
                           //if you are on with the facebook button upload the profile picture here
                           name.setText(dataModel.getFirstName());
                           new DownloadImageTask(profilePic).execute(dataModel.getImageUrl());
                          // Picasso.get().load(dataModel.getImageUrl()).into(profilePic);
                           username.setText(dataModel.getUsername());}
                           else{
                       name.setText(dataModel.getFirstName() + " " + dataModel.getLastName());

                       //Picasso.get().load(dataModel.getImageUrl()).into(profilePic);
                       new DownloadImageTask(profilePic).execute(dataModel.getImageUrl());
                       username.setText(dataModel.getUsername());
                       }

                       System.out.println(" +++++++++++ " + dataModel.getFirstName() + " +++++++++++++ "
                            + dataModel.getImageUrl());

                        //get the image info and pics
                       if(dataModel.getBookObject() != null)
                       {
                           List<BookObject> bo = dataModel.getBookObject();
                           for(int i = 0; i < bo.size(); i++)
                           {
                               if(bo.get(i)!=null) {
                                   final BookObject bookObject = bo.get(i);

                                   if (bookObject.getAuthor() == null) bookObject.setAuthor("N/A");
                                   if (bookObject.getBookname() == null)
                                       bookObject.setAuthor("N/A");
                                   if (bookObject.getClassStr() == null)
                                       bookObject.setAuthor("N/A");

                                   dataModelArrayList.add(bookObject);

                                   Button bt = new Button(ProfileActivity.this);
                                   bt.setBackgroundResource(R.drawable.ic_edit_yellow_24dp);
                                   bt.setLayoutParams(buttonPram);


                                   TextView tx = new TextView(ProfileActivity.this);
                                   Typeface romand = ResourcesCompat.getFont(ProfileActivity.this, R.font.romand);
                                   Typeface cour = ResourcesCompat.getFont(ProfileActivity.this, R.font.cour);

                                   String beg = "";
                                   String title = bookObject.getBookname();
                                   String author = "\nBy " + bookObject.getAuthor()
                                           + "\n";
                                   String for1 = "For ";
                                   String forclass = bookObject.getClassStr();

                                   tx.setText(beg + title + author + for1 + forclass, TextView.BufferType.SPANNABLE);

                                   int start = beg.length();
                                   int end = start + title.length();
                                   int start1 = end + author.length() + for1.length();
                                   int end1 = start1 + forclass.length();

                                   Spannable sp = (Spannable)tx.getText();
                                   sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                   sp.setSpan(new CustomTypefaceSpan("", romand), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                   sp.setSpan(new RelativeSizeSpan(.95f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                   sp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                   sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                   System.out.println("noooooooooo: " + "Book Title: " + bookObject.getBookname() + "\n\nAuthor: " + bookObject.getAuthor()
                                           + "\n\nClass: " + bookObject.getClassStr());

                                   tx.setTextSize(15);
                                   tx.setTypeface(cour);
                                   tx.setAllCaps(true);
                                   tx.setLayoutParams(textPrams);



                                   //System.out.println("the bitmap in view function is: " + bt.toString());
                                   LinearLayout lh = new LinearLayout(ProfileActivity.this);
                                   lh.setOrientation(LinearLayout.HORIZONTAL);

                                   // img.setImageDrawable(getResources().getDrawable(findViewById(R.drawable.com_facebook_button_icon_white)));
                                   if (bookObject.getImges() != null) {
                                       ImageView img = new ImageView(ProfileActivity.this);
                                       List<String> imgStrings = bookObject.getImges();

                                     int index = 0;


                                               while(imgStrings.get(index) == null )
                                               {
                                                   index++;

                                               }
                                       Picasso.get().load(imgStrings.get(index)).into(img);
                                       //new DownloadImageTask(img).execute(imgStrings.get(0));
                                       //new DownloadImageTask(img).execute(urlsr);
                                       img.setLayoutParams(layoutParams);
                                       lh.addView(img);
                                   }

                                   lh.addView(tx);
                                   lh.addView(bt);
                                   lv.addView(lh);



                                   final int finalI = i;
                                   bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           Intent intent = new Intent(ProfileActivity.this, editBookActivity.class);
                                           //intent.putExtra("book", (Parcelable) bookObject);
                                           intent.putExtra("book", (Serializable) bookObject);
                                           System.out.println("final i is: " + finalI);
                                           intent.putExtra("index", finalI);
                                           startActivity(intent);
                                       }
                                   });
                               }

                       }
                   }
               }

                setUpRecyclerView();
            }
     */
}
