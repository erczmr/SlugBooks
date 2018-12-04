package com.example.slugbooks.slugbooks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private List<BookObject> dataModelList;
    private List<BookObject> dataModelListFull;

    class ExampleViewHolder extends RecyclerView.ViewHolder{

        ImageView bookImg;
        TextView bookName;
        TextView bookAuthor;
        TextView bookClass;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            bookImg = itemView.findViewById(R.id.bookPictureId);
            bookName = itemView.findViewById(R.id.bookNameId);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookClass = itemView.findViewById(R.id.bookClass);

        }
    }

    ExampleAdapter(List<BookObject> dataModelList) {
        this.dataModelList = dataModelList;
        dataModelListFull = new ArrayList<>(dataModelList);
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

        BookObject currentItem = dataModelList.get(position);

        //holder.bookImg.setImageResource(currentItem.getImges().get(0));
        Picasso.get().load(currentItem.getImges().get(0)).into(holder.bookImg);

        holder.bookName.setText(currentItem.getBookname());
        holder.bookAuthor.setText(currentItem.getAuthor());
        holder.bookClass.setText(currentItem.getClassStr());

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
