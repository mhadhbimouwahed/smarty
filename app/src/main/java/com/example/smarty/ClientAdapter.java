package com.example.smarty;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

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
        TextView addToCart;
        TextView id_produit;
        LinearLayout expandable_layout;
        LinearLayout expandItem;
        FirebaseAuth firebaseAuth;
        CollectionReference cartReference;
        DocumentReference documentReference;



        public ClientViewHolder(@NonNull  View itemView) {
            super(itemView);

            nomDuProduit=itemView.findViewById(R.id.nomDuProduit);
            imageDuProduit=itemView.findViewById(R.id.imageDuProduit);
            prixDuProduit=itemView.findViewById(R.id.prixDuProduit);
            descriptionDuProduit=itemView.findViewById(R.id.descriptionDuProduit);
            addToCart=itemView.findViewById(R.id.addToCartButton);
            id_produit=itemView.findViewById(R.id.idDuProduit_cart);
            expandable_layout=itemView.findViewById(R.id.expandable_layout);
            expandItem=itemView.findViewById(R.id.expandItem);

            firebaseAuth=FirebaseAuth.getInstance();
            cartReference= FirebaseFirestore.getInstance().collection("ClientCart");
            documentReference=cartReference.document(id_produit.getText().toString());




            expandItem.setOnClickListener(x->{
                Product product=list.get(getAdapterPosition());
                product.setIsExpanded(!product.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });

            addToCart.setOnClickListener(x->{

                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    if(!user.getEmail().equals("adminpage@gmail.com")){
                        HashMap<String,Object> cart_items=new HashMap<>();
                        cart_items.put("PID",id_produit.getText().toString());
                        cart_items.put("Email",user.getEmail());
                        documentReference.set(cart_items).addOnCompleteListener(task->{
                            if(task.isSuccessful()){
                                Toast.makeText(context, "product added successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "failed to add product to cart", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(failure->{
                            Toast.makeText(context.getApplicationContext(), "check you internet connection", Toast.LENGTH_SHORT).show();
                        });
                    }else{
                        firebaseAuth.signOut();
                    }
                }else{
                    Toast.makeText(context.getApplicationContext(), "you are not connected, how did you get in here?", Toast.LENGTH_SHORT).show();
                }

            });

        }
    }
}
