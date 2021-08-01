package com.example.smarty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MainPage extends AppCompatActivity  {


    private RecyclerView items;
    private ProgressBar loadingProducts_mainPage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    DocumentReference documentReference;
    CollectionReference collectionReference;


    ArrayList<Product> list;
    ClientAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Products/");
        documentReference=collectionReference.document();
        storage=FirebaseStorage.getInstance();


        TextView logout = findViewById(R.id.logout);
        items=findViewById(R.id.items);
        loadingProducts_mainPage=findViewById(R.id.loadingProducts_mainPage);





        logout.setOnClickListener(x->{

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            if(Objects.requireNonNull(user.getEmail()).equals("adminpage@gmail.com")){
                firebaseAuth.signOut();

            }else{
                loadingProducts_mainPage.setVisibility(View.VISIBLE);
                items.setHasFixedSize(true);
                items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                list=new ArrayList<>();
                myAdapter= new ClientAdapter(getApplicationContext(),list);

                items.setAdapter(myAdapter);

                collectionReference.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot dataSnapshot: Objects.requireNonNull(task.getResult())){
                            Map<String, Object> data = dataSnapshot.getData();
                            Product product=new Product(data.get("PID"),
                                    data.get("ProductName"),
                                    data.get("ProductPrise"),
                                    data.get("ProductDescription"),
                                    data.get("ProductCategory"),
                                    data.get("ProductImage"),
                                    data.get("ProductManufacturer"),
                                    data.get("InStock"),
                                    data.get("ProductDiscount"));
                            loadingProducts_mainPage.setVisibility(View.GONE);
                            list.add(product);
                            myAdapter.notifyDataSetChanged();


                        }

                    }else{
                        Toast.makeText(MainPage.this, "failed to load products", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(fail-> Toast.makeText(this, "failed to load products", Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        loadingProducts_mainPage.setVisibility(View.GONE);
    }
}