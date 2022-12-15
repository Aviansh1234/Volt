package com.example.voltmotors.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voltmotors.HomeActivity;
import com.example.voltmotors.ProductViewer;
import com.example.voltmotors.R;
import com.example.voltmotors.custom.Product;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    HomeActivity context;
    ArrayList<Product>data;

    public SearchAdapter(HomeActivity context, ArrayList<Product> data) {
        this.context = context;
        this.data = (ArrayList<Product>) data.clone();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        SearchViewHolder viewHolder = new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Product curr = data.get(position);
        Picasso.with(context)
                .load(curr.getUrl())
                .into(holder.image);
        holder.price.setText("$"+curr.getPrice());
        holder.name.setText(curr.getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductViewer.class);
                intent.putExtra("from", "search");
                intent.putExtra("id", holder.getAdapterPosition()+1);
                context.startActivity(intent);
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price;
        ConstraintLayout card;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.searchImage);
            name = itemView.findViewById(R.id.searchName);
            price = itemView.findViewById(R.id.searchPrice);
            card = itemView.findViewById(R.id.card);
        }
    }
    public void updateReceiptsList(ArrayList<Product> newList) {
        data.clear();
        data.addAll(newList);
        this.notifyDataSetChanged();
    }
}
