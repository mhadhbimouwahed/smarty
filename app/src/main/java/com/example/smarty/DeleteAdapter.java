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

import java.util.ArrayList;

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.DeleteViewHolder> {


    Context context;
    ArrayList<Product> list_delete;

    DeleteAdapter(Context context,ArrayList<Product> list_delete){
        this.context=context;
        this.list_delete=list_delete;
    }

    @NonNull
    @Override
    public DeleteViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.items_to_delete,parent,false);

        return new DeleteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  DeleteAdapter.DeleteViewHolder holder, int position) {
        Product product=list_delete.get(position);
        holder.nomDuProduit_delete.setText(product.getProductName());
        holder.prixDuProduit_delete.setText(product.getProductPrise());
        holder.descriptionDuProduit_delete.setText(product.getProductDescription());
        Glide.with(context).load(product.getProductImage()).into(holder.imageDuProduit_delete);
        boolean isExpanded=list_delete.get(position).isExpanded();
        holder.expandable_layout_delete.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return list_delete.size();
    }

    public class DeleteViewHolder extends RecyclerView.ViewHolder{
        TextView nomDuProduit_delete;
        ImageView imageDuProduit_delete;
        TextView prixDuProduit_delete;
        TextView descriptionDuProduit_delete;
        LinearLayout expandable_layout_delete;
        LinearLayout expandItem_delete;

        public DeleteViewHolder(@NonNull View itemView) {
            super(itemView);

            nomDuProduit_delete=itemView.findViewById(R.id.nomDuProduit_delete);
            imageDuProduit_delete=itemView.findViewById(R.id.imageDuProduit_delete);
            prixDuProduit_delete=itemView.findViewById(R.id.prixDuProduit_delete);
            descriptionDuProduit_delete=itemView.findViewById(R.id.descriptionDuProduit_delete);
            expandable_layout_delete=itemView.findViewById(R.id.expandable_layout_delete);
            expandItem_delete=itemView.findViewById(R.id.expandItem_delete);

            expandItem_delete.setOnClickListener(x->{
                Product product=list_delete.get(getAdapterPosition());
                product.setIsExpanded(!product.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });



        }
    }

}
