package com.example.smarty;

import android.content.Context;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder> {


    Context context;
    static ArrayList<Product> list;

    public CartItemsAdapter(Context context,ArrayList<Product> list){
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.items_cart,parent,false);
        return new CartItemsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {

        Product product=list.get(position);
        holder.nomDuProduit_cart.setText(product.getProductName());
        holder.prixDuProduit_cart.setText(product.getProductPrise());
        holder.descriptionDuProduit_cart.setText(product.getProductDescription());
        holder.id_produit_cart.setText(product.getProductID());
        holder.constructeurDuProduit_cart.setText(product.getProductManufacturer());
        holder.promotionDuProduit_cart.setText(product.getDiscount());
        Glide.with(context)
                .load(product.getProductImage())
                .into(holder.imageDuProduit_cart);
        boolean isExpanded=list.get(position).isExpanded();
        holder.expandable_layout_cart.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }





    public class CartItemsViewHolder extends RecyclerView.ViewHolder{


        TextView nomDuProduit_cart;
        ImageView imageDuProduit_cart;
        TextView prixDuProduit_cart;
        TextView descriptionDuProduit_cart;
        TextView removeFromCart;
        TextView id_produit_cart;
        TextView constructeurDuProduit_cart;
        TextView promotionDuProduit_cart;
        LinearLayout expandable_layout_cart;
        LinearLayout expandItem_cart;


        FirebaseFirestore firestore;
        CollectionReference productCollection;
        CollectionReference cartCollection;
        FirebaseAuth firebaseAuth;
        FirebaseUser user;

        public CartItemsViewHolder(@NonNull View itemView) {
            super(itemView);


            nomDuProduit_cart=itemView.findViewById(R.id.nomDuProduit_cart);
            imageDuProduit_cart=itemView.findViewById(R.id.imageDuProduit_cart);
            prixDuProduit_cart=itemView.findViewById(R.id.prixDuProduit_cart);
            descriptionDuProduit_cart=itemView.findViewById(R.id.descriptionDuProduit_cart);
            removeFromCart=itemView.findViewById(R.id.removeFromCartButton);
            id_produit_cart=itemView.findViewById(R.id.idDuProduit_cart_cart);
            promotionDuProduit_cart=itemView.findViewById(R.id.promotionDuProduit_cart);
            constructeurDuProduit_cart=itemView.findViewById(R.id.constructeurDuProduit_cart);
            expandable_layout_cart=itemView.findViewById(R.id.expandable_layout_cart);
            expandItem_cart=itemView.findViewById(R.id.expandItem_cart);

            firestore=FirebaseFirestore.getInstance();
            productCollection=firestore.collection("Products/");
            cartCollection=firestore.collection("Cart/");
            firebaseAuth=FirebaseAuth.getInstance();


            expandItem_cart.setOnClickListener(x->{
                Product product=list.get(getAdapterPosition());
                product.setIsExpanded(!product.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });
            
            removeFromCart.setOnClickListener(x->{
                user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    cartCollection.document(user.getEmail()).collection("currentUserCart").document(id_produit_cart.getText().toString()).delete().addOnCompleteListener(task->{
                        Toast.makeText(context.getApplicationContext(), "product deleted successfully from cart", Toast.LENGTH_SHORT).show();
                        list.remove(getAdapterPosition());
                        notifyDataSetChanged();
                    }).addOnFailureListener(failure->{
                        Toast.makeText(context.getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
                    });
                }else{
                    Toast.makeText(context.getApplicationContext(), "you need to be connected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
