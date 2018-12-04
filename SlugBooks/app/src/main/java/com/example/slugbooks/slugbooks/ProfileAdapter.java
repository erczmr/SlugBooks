package com.example.slugbooks.slugbooks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ExampleViewHolder> {

    private List<BookObject> dataModelList;
    private List<BookObject> dataModelListFull;

    int finalI;
    private Context context;
    class ExampleViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        ImageView bookImg;
        TextView bookName;
        TextView bookAuthor;
        TextView bookClass;
        Button contactButton;



        public ExampleViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativelayoutId);
            bookImg = itemView.findViewById(R.id.bookPictureId);
            bookName = itemView.findViewById(R.id.bookNameId);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookClass = itemView.findViewById(R.id.bookClass);
            contactButton = itemView.findViewById(R.id.sendMsgButtonId);

        }
    }

    ProfileAdapter(Context context,List<BookObject> dataModelList,int finalI) {
        this.context =context;
        this.dataModelList = dataModelList;
        dataModelListFull = new ArrayList<>(dataModelList);
        this.finalI = finalI;
    }


    @NonNull
    @Override
    public ProfileAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item,
                parent, false);
        return new ProfileAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ExampleViewHolder holder, int position) {

        final BookObject currentItem = dataModelList.get(position);

        //holder.bookImg.setImageResource(currentItem.getImges().get(0));
        int j = 0;
        while(currentItem.getImges().get(j) == null)
        {
            j++;
        }
        Picasso.get().load(currentItem.getImges().get(j)).into(holder.bookImg);

        holder.bookName.setText(currentItem.getBookname());
        holder.bookAuthor.setText(currentItem.getAuthor());
        holder.bookClass.setText(currentItem.getClassStr());


        holder.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, editBookActivity.class);
                //intent.putExtra("book", (Parcelable) bookObject);
                intent.putExtra("book", (Serializable) currentItem);
                System.out.println("final i is: " + finalI);
                intent.putExtra("index", finalI);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }



}
