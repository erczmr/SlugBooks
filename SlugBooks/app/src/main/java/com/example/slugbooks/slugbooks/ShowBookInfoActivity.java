package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowBookInfoActivity extends AppCompatActivity {

    private Button btn;
    private BookObject bj;

    private LinearLayout lv;
    private LinearLayout linearLayoutHorizanta;
    private LinearLayout.LayoutParams textPrams;
    private LinearLayout.LayoutParams layoutParams;


    private int index = 0;
    private String id = "";
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_info);
        btn = (Button)findViewById(R.id.goToMsgButtonid);

        lv = (LinearLayout) findViewById(R.id.showLayoutId) ;
        layoutParams = new LinearLayout.LayoutParams(250, 250);
        textPrams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 50, 0, 50);
        textPrams.setMargins(40, 0, 0, 0);

        linearLayoutHorizanta = (LinearLayout)findViewById(R.id.linearLayoutHID);
        displayData(bj);

    }


    public void offerChat(View view) {


        Bundle bundle = getIntent().getExtras();

        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());

        index = bundle.getInt("index");
        System.out.println("the index isss: " + index);

        id = bundle.getString("userID");
        System.out.println("id isss: " + id);

        username = bundle.getString("username");
        System.out.println("username isss: " + username);
        moveToMSG();

    }

    private void displayData(BookObject bj) {

        Bundle bundle = getIntent().getExtras();

        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());

        if(bj!=null)
        {
            TextView tx = new TextView(ShowBookInfoActivity.this);
            tx.setText("Book Title: " + bj.getBookname() + "\n\nAuthor: " + bj.getAuthor()
                   +"\n\nDescription: " + bj.getDescriptionStr() + "\n\nClass: " + bj.getClassStr()+"\n\nEdition: "
            + bj.getEdition()+ "\n\nCondition: " + bj.getCondition() + "\n\nPrice: " + bj.getPrice());

            tx.setTextSize(15);
            tx.setLayoutParams(textPrams);

            //System.out.println("the bitmap in view function is: " + bt.toString());
            LinearLayout lh = new LinearLayout(ShowBookInfoActivity.this);
            lh.setOrientation(LinearLayout.HORIZONTAL);

            // img.setImageDrawable(getResources().getDrawable(findViewById(R.drawable.com_facebook_button_icon_white)));
            if (bj.getImges() != null) {
                for(int k = 0 ; k < bj.getImges().size(); k++) {
                    ImageView img = new ImageView(ShowBookInfoActivity.this);
                    List<String> imgStrings = bj.getImges();

                    int index = 0;


                    while (imgStrings.get(index) == null) {
                        index++;

                    }
                    Picasso.get().load(imgStrings.get(index)).into(img);
                    //new DownloadImageTask(img).execute(imgStrings.get(0));
                    //new DownloadImageTask(img).execute(urlsr);
                    img.setLayoutParams(layoutParams);
                    linearLayoutHorizanta.addView(img);
                }
            }

            lh.addView(tx);

            lv.addView(lh);
        }
        else{
            Toast.makeText(this, "Book dosent exist anymore", Toast.LENGTH_SHORT).show();
        }

    }

    private void moveToMSG() {
        Intent it = new Intent(ShowBookInfoActivity.this, MessageActivity.class);
        it.putExtra("userID", id);
        it.putExtra("username", username);

        startActivity(it);
    }
}
