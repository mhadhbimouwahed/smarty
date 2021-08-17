package com.example.smarty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

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
        View v= LayoutInflater.from(context).inflate(R.layout.items,parent,false);
        return new ClientViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull  ClientAdapter.ClientViewHolder holder, int position) {

        Product product=list.get(position);
        holder.nomDuProduit.setText(product.getProductName());
        holder.prixDuProduit.setText(product.getProductPrise());
        holder.descriptionDuProduit.setText(product.getProductDescription());
        holder.id_produit.setText(product.getProductID());
        holder.constructeurDuProduit.setText(product.getProductManufacturer());
        holder.promotionDuProduit.setText(product.getDiscount());
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
        TextView constructeurDuProduit;
        TextView promotionDuProduit;
        LinearLayout expandable_layout;
        LinearLayout expandItem;



        FirebaseFirestore firestore;
        CollectionReference productCollection;
        CollectionReference cartCollection;
        FirebaseAuth firebaseAuth;
        FirebaseUser user;




        public ClientViewHolder(@NonNull  View itemView) {
            super(itemView);

            nomDuProduit=itemView.findViewById(R.id.nomDuProduit);
            imageDuProduit=itemView.findViewById(R.id.imageDuProduit);
            prixDuProduit=itemView.findViewById(R.id.prixDuProduit);
            descriptionDuProduit=itemView.findViewById(R.id.descriptionDuProduit);
            addToCart=itemView.findViewById(R.id.addToCartButton);
            id_produit=itemView.findViewById(R.id.idDuProduit_cart);
            promotionDuProduit=itemView.findViewById(R.id.promotionDuProduit);
            constructeurDuProduit=itemView.findViewById(R.id.constructeurDuProduit);
            expandable_layout=itemView.findViewById(R.id.expandable_layout);
            expandItem=itemView.findViewById(R.id.expandItem);

            firestore=FirebaseFirestore.getInstance();
            productCollection=firestore.collection("Products/");
            cartCollection=firestore.collection("Cart/");
            firebaseAuth=FirebaseAuth.getInstance();




            expandItem.setOnClickListener(x->{
                Product product=list.get(getAdapterPosition());
                product.setIsExpanded(!product.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });

            imageDuProduit.setOnClickListener(x->{
                zoom();
            });


            addToCart.setOnClickListener(x->{
                productCollection.whereEqualTo("PID",id_produit.getText().toString()).get().addOnCompleteListener(task->{
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        HashMap<String,Object> data=new HashMap<>(documentSnapshot.getData());
                        user=firebaseAuth.getCurrentUser();
                        if (user!=null){
                            cartCollection.document(user.getEmail()).collection("currentUserCart").document(id_produit.getText().toString()).set(data).addOnCompleteListener(taskAdd->{
                                if (task.isSuccessful()){
                                    Toast.makeText(context.getApplicationContext(), "produit ajouté au panier avec succès", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context.getApplicationContext(), "échec de l'ajout du produit au panier", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(failureAdd->{
                                Toast.makeText(context.getApplicationContext(), "veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
                            });
                        }else{
                            Toast.makeText(context.getApplicationContext(), "Vous devez être connecté", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(failure->{
                    Toast.makeText(context.getApplicationContext(), "S'il vous plait, vérifiez votre connexion internet", Toast.LENGTH_SHORT).show();
                });
            });

        }

        private void zoom() {
            Toast.makeText(context.getApplicationContext(), "zooming in", Toast.LENGTH_SHORT).show();
        }
    }
}
