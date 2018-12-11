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

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private List<BookObject> dataModelList;
    private List<BookObject> dataModelListFull;

    private List<String> userIdList;
    private List<String> userIdListFull;

    private List<String> userNameList;
    private List<String> userNameListFull;
    //String userId;
    //String username;
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

    ExampleAdapter(Context context,List<BookObject> dataModelList,List<String> userIdList,List<String> userNameList,int finalI) {
        this.context = context;
        this.dataModelList = dataModelList;
        dataModelListFull = new ArrayList<>(dataModelList);

        this.userIdList = userIdList;
        userIdListFull = new ArrayList<>(userIdList);

        this.userNameList = userNameList;
        userNameListFull = new ArrayList<>(userNameList);

        //this.userId = userId;
       // this.username = username;
        this.finalI = finalI;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,
                parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        final BookObject currentItem = dataModelList.get(position);

        final String currentId = userIdList.get(position);

        final String currentName = userNameList.get(position);

        //holder.bookImg.setImageResource(currentItem.getImges().get(0));
        if(currentItem.getImges() != null) {
            int j = 0;
            while (currentItem.getImges().get(j) == null) {
                j++;
            }

            Picasso.get().load(currentItem.getImges().get(j)).into(holder.bookImg);

            holder.bookName.setText(currentItem.getBookname());
            holder.bookAuthor.setText(currentItem.getAuthor());
            holder.bookClass.setText(currentItem.getClassStr());

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent;
                    intent = new Intent(context, ShowBookInfoActivity.class);
                    //intent.putExtra("book", (Parcelable) bookObject);
                    intent.putExtra("book", (Serializable) currentItem);
                    //System.out.println("final i is: " + finalI);
                    intent.putExtra("index", finalI);
                    intent.putExtra("userID", currentId);
                    intent.putExtra("username", currentName);

                    System.out.println("adapter username isss: " + currentName);

                    context.startActivity(intent);
                }
            });

            holder.contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, MessageActivity.class);
                    it.putExtra("userID", currentId);
                    it.putExtra("username", currentName);

                    System.out.println("adapter username isss: " + currentName);
                    context.startActivity(it);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BookObject> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(dataModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BookObject item : dataModelListFull) {
                    if (item.getBookname().toLowerCase().contains(filterPattern)) {
                        filterList.add(item);
                    }

                }

            }
            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataModelList.clear();
            dataModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };




}
