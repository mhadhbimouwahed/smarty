package com.example.smarty;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        holder.idDuProduit_delete.setText(product.getProductID());
        holder.promotionDuProduit_delete.setText(product.getDiscount());
        holder.constructeurDuProduit_delete.setText(product.getProductManufacturer());
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
        TextView deleteButton;
        TextView idDuProduit_delete;
        TextView promotionDuProduit_delete;
        LinearLayout expandable_layout_delete;
        LinearLayout expandItem_delete;
        TextView constructeurDuProduit_delete;
        FirebaseStorage storage;
        CollectionReference collectionReference;
        FirebaseFirestore firestore;
        StorageReference reference;
        CollectionReference cartCollection;
        CollectionReference currentUserCart;
        DocumentReference documentReference;
        CollectionReference usersCollection;
        public DeleteViewHolder(@NonNull View itemView) {
            super(itemView);

            nomDuProduit_delete=itemView.findViewById(R.id.nomDuProduit_delete);
            imageDuProduit_delete=itemView.findViewById(R.id.imageDuProduit_delete);
            prixDuProduit_delete=itemView.findViewById(R.id.prixDuProduit_delete);
            descriptionDuProduit_delete=itemView.findViewById(R.id.descriptionDuProduit_delete);
            deleteButton=itemView.findViewById(R.id.deleteButton);
            constructeurDuProduit_delete=itemView.findViewById(R.id.constructeurDuProduit_delete);
            expandable_layout_delete=itemView.findViewById(R.id.expandable_layout_delete);
            expandItem_delete=itemView.findViewById(R.id.expandItem_delete);
            promotionDuProduit_delete=itemView.findViewById(R.id.promotionDuProduit_delete);
            idDuProduit_delete=itemView.findViewById(R.id.idDuProduit_delete);

            storage=FirebaseStorage.getInstance();
            firestore=FirebaseFirestore.getInstance();
            collectionReference= firestore.collection("Products/");
            reference=storage.getReference("Images/");
            cartCollection=firestore.collection("Cart/");
            usersCollection=firestore.collection("Users");



            expandItem_delete.setOnClickListener(x->{
                Product product=list_delete.get(getAdapterPosition());
                product.setIsExpanded(!product.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });

            deleteButton.setOnClickListener(x->{

                usersCollection.get().addOnCompleteListener(task->{
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                            HashMap<String,Object> hashMap=new HashMap<>(documentSnapshot.getData());
                            cartCollection.document(hashMap.get("Email").toString()).collection("currentUserCart").document(idDuProduit_delete.getText().toString())
                                    .delete().addOnCompleteListener(secondTask->{
                                        if (task.isSuccessful()){
                                            Toast.makeText(context.getApplicationContext(), "deleted related cart products", Toast.LENGTH_SHORT).show();
                                        }
                            }).addOnFailureListener(secondFailure->{
                               Log.d("ERROR_DELETING_FROM_CART",secondFailure.getMessage());
                            });
                        }
                    }
                }).addOnFailureListener(failure->{
                   Log.d("ERROR_READING_FROM_USERS",failure.getMessage());
                });


                reference.child(idDuProduit_delete.getText().toString()).delete().addOnSuccessListener(sucess->{
                    Toast.makeText(context.getApplicationContext(), "deleted the product image", Toast.LENGTH_SHORT).show();

                }).addOnFailureListener(fail->{
                    Toast.makeText(context, "failed to delete the image, do it manually", Toast.LENGTH_SHORT).show();
                });

                collectionReference.document(idDuProduit_delete.getText().toString()).delete().addOnSuccessListener(success->{

                    Toast.makeText(context.getApplicationContext(), "product deleted successfully", Toast.LENGTH_SHORT).show();
                    list_delete.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }).addOnFailureListener(fail->{
                    Toast.makeText(context.getApplicationContext(), "failed to delete the product", Toast.LENGTH_SHORT).show();
                });
            });





        }
    }

}
