package com.example.smarty;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ModifyAdapter extends RecyclerView.Adapter<ModifyAdapter.ModifyViewHolder> {


    Context context;
    ArrayList<Product> list;
    ModifyAdapter(Context context,ArrayList<Product> list){
        this.context=context;
        this.list=list;
    }

    @NonNull

    @Override
    public ModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.items_to_modify,parent,false);
        return new ModifyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  ModifyAdapter.ModifyViewHolder holder, int position) {

        Product product=list.get(position);
        holder.idProduct_modify.setText(product.getProductID());
        holder.nomDuProduit_modify.setText(product.getProductName());
        holder.prixDuProduit_modify.setText(product.getProductPrise());
        holder.descriptionDuProduit_modify.setText(product.getProductDescription());
        holder.constructeurDuProduit_modify.setText(product.getProductManufacturer());
        holder.promotionDuProduit_modify.setText(product.getDiscount());
        Glide.with(context).load(product.getProductImage()).into(holder.imageDuProduit_modify);
        boolean isExpanded=list.get(position).isExpanded();
        holder.expandable_layout_modify.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ModifyViewHolder extends RecyclerView.ViewHolder{

        TextView idProduct_modify;
        TextView nomDuProduit_modify;
        ImageView imageDuProduit_modify;
        TextView prixDuProduit_modify;
        TextView descriptionDuProduit_modify;
        TextView modifyButton;
        TextView promotionDuProduit_modify;
        TextView constructeurDuProduit_modify;
        LinearLayout expandable_layout_modify;
        LinearLayout expandItem_modify;


        public ModifyViewHolder(@NonNull  View itemView) {
            super(itemView);

            idProduct_modify=itemView.findViewById(R.id.idDuProduit_modify);
            nomDuProduit_modify=itemView.findViewById(R.id.nomDuProduit_modify);
            imageDuProduit_modify=itemView.findViewById(R.id.imageDuProduit_modify);
            prixDuProduit_modify=itemView.findViewById(R.id.prixDuProduit_modify);
            modifyButton=itemView.findViewById(R.id.modifyButton);
            constructeurDuProduit_modify=itemView.findViewById(R.id.constructeurDuProduit_modify);
            descriptionDuProduit_modify=itemView.findViewById(R.id.descriptionDuProduit_modify);
            expandable_layout_modify=itemView.findViewById(R.id.expandable_layout_modify);
            expandItem_modify=itemView.findViewById(R.id.expandItem_modify);
            promotionDuProduit_modify=itemView.findViewById(R.id.promotionDuProduit_modify);




            expandItem_modify.setOnClickListener(x->{
                Product product=list.get(getAdapterPosition());
                product.setIsExpanded(!product.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });

            modifyButton.setOnClickListener(x->{
                Toast.makeText(context.getApplicationContext(), "item clicked", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(context.getApplicationContext(),ModifyPageActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID",idProduct_modify.getText().toString());
                context.startActivity(intent);
            });


        }
    }
}
