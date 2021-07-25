package com.example.smarty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {


    Context context;
    static ArrayList<Product> list;

    public ClientAdapter(Context context,ArrayList<Product> list){

        this.context=context;
        this.list=list;
    }

    @NonNull

    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ClientViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull  ClientAdapter.ClientViewHolder holder, int position) {

        Product product=list.get(position);
        holder.nomDuProduit.setText(product.getProductName());
        holder.prixDuProduit.setText(product.getProductPrise());
        holder.descriptionDuProduit.setText(product.getProductDescription());
        Glide.with(context)
                .load(product.getProductImage())
                .into(holder.imageDuProduit);
        boolean isExpanded=list.get(position).isExpanded();
        holder.expandable_layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder {

        TextView nomDuProduit;
        ImageView imageDuProduit;
        TextView prixDuProduit;
        TextView descriptionDuProduit;
        LinearLayout expandable_layout;
        LinearLayout expandItem;

        public ClientViewHolder(@NonNull  View itemView) {
            super(itemView);

            nomDuProduit=itemView.findViewById(R.id.nomDuProduit);
            imageDuProduit=itemView.findViewById(R.id.imageDuProduit);
            prixDuProduit=itemView.findViewById(R.id.prixDuProduit);
            descriptionDuProduit=itemView.findViewById(R.id.descriptionDuProduit);
            expandable_layout=itemView.findViewById(R.id.expandable_layout);
            expandItem=itemView.findViewById(R.id.expandItem);


            expandItem.setOnClickListener(x->{
                Product product=list.get(getAdapterPosition());
                product.setIsExpanded(!product.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });

        }
    }
}
