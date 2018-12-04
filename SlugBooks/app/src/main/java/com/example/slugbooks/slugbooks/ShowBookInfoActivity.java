package com.example.slugbooks.slugbooks;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
        layoutParams = new LinearLayout.LayoutParams(400, 400);
        textPrams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 600, 40, 50);
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

            Typeface romand = ResourcesCompat.getFont(ShowBookInfoActivity.this, R.font.romand);
            Typeface cour = ResourcesCompat.getFont(ShowBookInfoActivity.this, R.font.cour);

            String beg = "";

            String title = "Book Title\n";
            String title1 = bj.getBookname();

            String author = "\n\nAuthor\n";
            String author1 = bj.getAuthor();

            String description = "\n\nDescription\n";
            String description1 = bj.getDescriptionStr();

            String getClass = "\n\nClass\n";
            String getClass1 = bj.getClassStr();

            String edition = "\n\nEdition\n";
            String edition1 = bj.getEdition();

            String condition = "\n\nCondition\n";
            String condition1 = bj.getCondition();

            String price = "\n\nPrice\n";
            String price1 = bj.getPrice();

            String end = "";

            int titleStart = beg.length();
            int titleEnd = titleStart + title.length();

            int authorStart = titleEnd + title1.length();
            int authorEnd = authorStart + author.length();

            int descriptionStart = authorEnd + author1.length();
            int descriptionEnd = descriptionStart + description.length();

            int getClassStart = descriptionEnd + description1.length();
            int getClassEnd = getClassStart + getClass.length();

            int editionStart = getClassEnd + getClass1.length();
            int editionEnd = editionStart + edition.length();

            int conditionStart = editionEnd + edition1.length();
            int conditionEnd = conditionStart + condition.length();

            int priceStart = conditionEnd + condition1.length();
            int priceEnd = priceStart + price.length();

            int endp = priceEnd + price1.length();


            //tx.setText("Book Title\n" + title, TextView.BufferType.SPANNABLE);
            tx.setText(title + title1 + author + author1 + description + description1 + getClass + getClass1 + edition + edition1 + condition +
                    condition1 + price + price1, TextView.BufferType.SPANNABLE);

            Spannable sp = (Spannable)tx.getText();

            sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), titleStart, titleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new CustomTypefaceSpan("", romand), titleStart, titleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.5f), titleStart, titleEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new CustomTypefaceSpan("", romand), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.5f), authorStart, authorEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), descriptionStart, descriptionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new CustomTypefaceSpan("", romand), descriptionStart, descriptionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.5f), descriptionStart, descriptionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)),getClassStart, getClassEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new CustomTypefaceSpan("", romand), getClassStart, getClassEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.5f), getClassStart, getClassEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



            sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), editionStart, editionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new CustomTypefaceSpan("", romand), editionStart, editionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.5f), editionStart, editionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), conditionStart, conditionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new CustomTypefaceSpan("", romand), conditionStart, conditionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.5f), conditionStart, conditionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), priceStart, priceEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new CustomTypefaceSpan("", romand), priceStart, priceEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new RelativeSizeSpan(1.5f), priceStart, priceEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            sp.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), titleStart, endp, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tx.setTypeface(cour);
            tx.setAllCaps(true);
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
