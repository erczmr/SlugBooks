package com.example.slugbooks.slugbooks;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    SearchView searchBar;
    public static String imgurl;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private List<DataModel> mUsers;
    public static String getHomeId() {
        return homeId;
    }

    private LinearLayout.LayoutParams textPrams;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout linearLayout;

    public static void setHomeId(String homeId) {
        HomeActivity.homeId = homeId;
    }

    static String homeId;
    private FirebaseUser user;

    private static String userIdNum = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        System.out.println("not working ooooooooooooooooo");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        linearLayout = (LinearLayout) findViewById(R.id.homepageLinearLayoutId);
        storageReference = FirebaseStorage.getInstance().getReference();

        layoutParams = new LinearLayout.LayoutParams(250, 250);
        textPrams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 50, 0, 50);
        textPrams.setMargins(40, 50, 0, 50);

        searchBar = (SearchView) findViewById(R.id.searchBarID);
        //bookName = (TextView) findViewById(R.id.bookNameID);
        //author = (TextView) findViewById(R.id.bookName2ID);
        //className = (TextView) findViewById(R.id.classID);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:

                        break;

                    case R.id.action_sell:
                        Intent intent2 = new Intent(HomeActivity.this, AddBookActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_messages:
                        Intent intent3 = new Intent(HomeActivity.this, InboxActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_profile:
                        Intent intent4 = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        mUsers = new ArrayList<>();

        postBooks();
    }

    private void postBooks() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final DataModel user = snapshot.getValue(DataModel.class);

                    assert user != null;

                    if(user.getUserID()!= null && !user.getUserID().equals( firebaseAuth.getUid())){
                        System.out.println("the ++++ uiddd Issss: " + user.getUserID());
                        mUsers.add(user);
                        System.out.println(" +++++++++++ user first name: " + user.getFirstName() + " +++++++++++++ user img url "
                                + user.getImageUrl());

                        //get the image info and pics
                        if(user.getBookObject() != null)
                        {
                            List<BookObject> bo = user.getBookObject();
                            for(int i = 0; i < bo.size(); i++)
                            {
                                if(bo.get(i)!=null) {
                                    final BookObject bookObject = bo.get(i);

                                    if (bookObject.getAuthor() == null) bookObject.setAuthor("N/A");
                                    if (bookObject.getBookname() == null)
                                        bookObject.setAuthor("N/A");
                                    if (bookObject.getClassStr() == null)
                                        bookObject.setAuthor("N/A");

                                    TextView tx = new TextView(HomeActivity.this);
                                    tx.setText("Book Title: " + bookObject.getBookname() + "\nAuthor: " + bookObject.getAuthor()
                                            + "\nClass: " + bookObject.getClassStr());

                                    System.out.println("noooooooooo: " + "Book Title: " + bookObject.getBookname() + "\n\nAuthor: " + bookObject.getAuthor()
                                            + "\n\nClass: " + bookObject.getClassStr());
                                    tx.setTextSize(15);
                                    tx.setLayoutParams(textPrams);

                                    //System.out.println("the bitmap in view function is: " + bt.toString());
                                    LinearLayout lh = new LinearLayout(HomeActivity.this);
                                    lh.setOrientation(LinearLayout.HORIZONTAL);

                                    // img
                                    // img.setImageDrawable(getResources().getDrawable(findViewById(R.drawable.com_facebook_button_icon_white)));
                                    if (bookObject.getImges() != null) {
                                        ImageView img = new ImageView(HomeActivity.this);
                                        List<String> imgStrings = bookObject.getImges();

                                        System.out.println("theeeeee img url iss: " + imgStrings.get(0));

                                        Picasso.get().load(imgStrings.get(0)).into(img);
                                        //new DownloadImageTask(img).execute(imgStrings.get(0));
                                        //new DownloadImageTask(img).execute(urlsr);
                                        img.setLayoutParams(layoutParams);
                                        lh.addView(img);
                                    }


                                    lh.addView(tx);
                                    final int finalI = i;
                                    lh.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent intent = new Intent(HomeActivity.this, ShowBookInfoActivity.class);
                                            //intent.putExtra("book", (Parcelable) bookObject);
                                            intent.putExtra("book", (Serializable) bookObject);
                                            System.out.println("final i is: " + finalI);
                                            intent.putExtra("index", finalI);
                                            intent.putExtra("userID", user.getUserID());
                                            intent.putExtra("username", user.getUsername());
                                            startActivity(intent);

                                        }
                                    });
                                    linearLayout.addView(lh);

                                }
                            }
                        }




                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                DataModel user = dataSnapshot.getValue(DataModel.class);
                if(user.getUserID() != null)
                {
                    System.out.println("********************&&&^^%% it worked: " + user.getUserID());

                }
                System.out.println("its null");
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

    // check to see if user is logged in with facebook
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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