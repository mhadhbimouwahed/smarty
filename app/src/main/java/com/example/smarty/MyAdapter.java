package com.example.smarty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {




    Context context;
    ArrayList<Product> list;

    public MyAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,parent,false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyAdapter.MyViewHolder holder, int position) {
        Product product=list.get(position);
        
        holder.nomDuProduit.setText(product.getProductName());
        holder.prixDuProduit.setText(product.getProductPrise());
        holder.descriptionDuProduit.setText(product.getProductDescription());
        Glide.with(context)
                .load(product.getProductImage())
                .into(holder.imageDuProduit);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomDuProduit;
        TextView prixDuProduit;
        TextView descriptionDuProduit;
        ImageView imageDuProduit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomDuProduit=itemView.findViewById(R.id.nomDuProduit);
            prixDuProduit=itemView.findViewById(R.id.prixDuProduit);
            descriptionDuProduit=itemView.findViewById(R.id.descriptionDuProduit);
            imageDuProduit=itemView.findViewById(R.id.imageDuProduit);


        }


    }


}
