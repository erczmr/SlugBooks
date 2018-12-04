package com.example.slugbooks.slugbooks;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams textPrams;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams buttonPram;

    private int ind;
    private String usern;
    private String iduser;
    public static void setHomeId(String homeId) {
        HomeActivity.homeId = homeId;
    }

    static String homeId;
    private FirebaseUser user;

    private ExampleAdapter adapter;

    private List<BookObject> dataModelArrayList;

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

        //linearLayout = (LinearLayout) findViewById(R.id.homepageLinearLayoutId);
        storageReference = FirebaseStorage.getInstance().getReference();

        layoutParams = new LinearLayout.LayoutParams(250, 250);
        textPrams = new LinearLayout.LayoutParams(950, 250);
        buttonPram = new LinearLayout.LayoutParams(150, 150);

        layoutParams.setMargins(20, 50, 0, 50);
        textPrams.setMargins(20, 30, 0, 50);
        buttonPram.setMargins(20,100,40,0);

        searchBar = (SearchView) findViewById(R.id.searchBarID);

        dataModelArrayList = new ArrayList<BookObject>();
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

        //postBooks();
       getStoreInfo();

        //the new layout with the search
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // System.out.println("the filter issss: " + adapter.getFilter());
                adapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    private void getStoreInfo() {

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final DataModel user = snapshot.getValue(DataModel.class);

                    assert user != null;

                    if (user.getUserID() != null && !user.getUserID().equals(firebaseAuth.getUid())) {
                        mUsers.add(user);

                        //get the image info and pics
                        if (user.getBookObject() != null) {
                            List<BookObject> bo = user.getBookObject();

                            for (int i = 0; i < bo.size(); i++) {
                                if (bo.get(i) != null) {
                                    final BookObject bookObject = bo.get(i);

                                    if (bookObject.getAuthor() == null) bookObject.setAuthor("N/A");
                                    if (bookObject.getBookname() == null)
                                        bookObject.setAuthor("N/A");
                                    if (bookObject.getClassStr() == null)
                                        bookObject.setAuthor("N/A");
                                    dataModelArrayList.add(bookObject);
                                    ind = i;
                                    usern = user.getUsername();
                                    iduser = user.getUserID();


                                }

                            }
                        }
                    }
                }
                System.out.println("object issssssssss: it works" );
                if (!dataModelArrayList.isEmpty()){
                    for (int j = 0; j < dataModelArrayList.size(); j++) {
                        System.out.println("object " + j + " issss: " + dataModelArrayList.get(j).getBookname());
                    }
                }else
                    System.out.println("noooo objects");

                setUpRecyclerView();
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


    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(HomeActivity.this,dataModelArrayList,iduser,usern,ind);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

   /* private void postBooks() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final DataModel user = snapshot.getValue(DataModel.class);

                    assert user != null;

                    if (user.getUserID() != null && !user.getUserID().equals(firebaseAuth.getUid())) {
                        System.out.println("the ++++ uiddd Issss: " + user.getUserID());
                        mUsers.add(user);
                        System.out.println(" +++++++++++ user first name: " + user.getFirstName() + " +++++++++++++ user img url "
                                + user.getImageUrl());

                        //get the image info and pics
                        if (user.getBookObject() != null) {
                            List<BookObject> bo = user.getBookObject();

                            for (int i = 0; i < bo.size(); i++) {
                                if (bo.get(i) != null) {
                                    final BookObject bookObject = bo.get(i);

                                    if (bookObject.getAuthor() == null) bookObject.setAuthor("N/A");
                                    if (bookObject.getBookname() == null)
                                        bookObject.setAuthor("N/A");
                                    if (bookObject.getClassStr() == null)
                                        bookObject.setAuthor("N/A");
                                    dataModelArrayList.add(bookObject);
                                    Button bt = new Button(HomeActivity.this);
                                    bt.setBackgroundResource(R.drawable.ic_message_yellow_24dp);
                                    bt.setLayoutParams(buttonPram);

                                    TextView tx = new TextView(HomeActivity.this);
                                    Typeface bebas = ResourcesCompat.getFont(HomeActivity.this, R.font.romand);
                                    String beg = "";
                                    String title = bookObject.getBookname();
                                    String author = "\nBy " + bookObject.getAuthor()
                                            + "\n";
                                    String for1 = "For ";
                                    String forclass = bookObject.getClassStr();
                                    tx.setText(beg + title + author + for1 + forclass, TextView.BufferType.SPANNABLE);
                                    Spannable sp = (Spannable) tx.getText();
                                    int start = beg.length();
                                    int end = start + title.length();
                                    int start1 = end + author.length() + for1.length();
                                    int end1 = start1 + forclass.length();
                                    sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    sp.setSpan(new CustomTypefaceSpan("", bebas), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    sp.setSpan(new RelativeSizeSpan(.95f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    sp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), start1, end1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    System.out.println("noooooooooo: " + "Book Title: " + bookObject.getBookname() + "\n\nAuthor: " + bookObject.getAuthor()
                                            + "\n\nClass: " + bookObject.getClassStr());
                                    tx.setTextSize(15);
                                    Typeface cour = ResourcesCompat.getFont(HomeActivity.this, R.font.cour);
                                    tx.setTypeface(cour);
                                    tx.setAllCaps(true);
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

                                        int index = 0;
                                        while (imgStrings.get(index) == null) {
                                            index++;

                                        }
                                        Picasso.get().load(imgStrings.get(0)).into(img);
                                        //new DownloadImageTask(img).execute(imgStrings.get(0));
                                        //new DownloadImageTask(img).execute(urlsr);
                                        img.setLayoutParams(layoutParams);
                                        lh.addView(img);
                                    }


                                    lh.addView(tx);
                                    lh.addView(bt);
                                    linearLayout.addView(lh);

*/
// -------------------------------- UNCOMMENT WHEN CAN DIRECT TO MESSAGES WITH SPECIFIC USER
 /*                                   final int finalI = i;
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
 */

 /*
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

                                }
                            }
                        }

                    }
                }
                System.out.println("object issssssssss: it works" );
                if (!dataModelArrayList.isEmpty()){
                    for (int j = 0; j < dataModelArrayList.size(); j++) {
                        System.out.println("object " + j + " issss: " + dataModelArrayList.get(j).getBookname());
                    }
                }else
                System.out.println("noooo objects");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                linearLayout.removeAllViews();
                onChildAdded(dataSnapshot, s);
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
*/
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