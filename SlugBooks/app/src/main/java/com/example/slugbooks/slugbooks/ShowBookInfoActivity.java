package com.example.slugbooks.slugbooks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowBookInfoActivity extends AppCompatActivity {

    private Button btn;
    private BookObject bj;

    private int index = 0;
    private String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book_info);
        btn = (Button)findViewById(R.id.goToMsgButtonid);


    }

    public void offerChat(View view) {
        Bundle bundle = getIntent().getExtras();

        bj = (BookObject) bundle.getSerializable("book");
        System.out.println("author name is: " + bj.getAuthor());

        index = bundle.getInt("index");
        System.out.println("the index isss: " + index);

        id = bundle.getString("userId");
        System.out.println("id isss: " + id);

    }
}
