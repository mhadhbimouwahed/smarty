package com.example.smarty;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainPage extends AppCompatActivity {
    private TextView logout;
    private RecyclerView items;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    DocumentReference documentReference;
    CollectionReference collectionReference;
    ArrayList<Product> list;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        collectionReference=firestore.collection("Products/");
        documentReference=collectionReference.document();
        storage=FirebaseStorage.getInstance();



        logout=findViewById(R.id.logout);
        items=findViewById(R.id.items);




        logout.setOnClickListener(x->{

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });

        items.setHasFixedSize(true);
        items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        list=new ArrayList<>();
        myAdapter=new MyAdapter(getApplicationContext(),list);
        items.setAdapter(myAdapter);





    }

    @Override
    protected void onStart(){
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals("adminpage@gmail.com")){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            FirebaseAuth.getInstance().signOut();
        }else{
            items.setHasFixedSize(true);
            items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            list=new ArrayList<>();
            myAdapter=new MyAdapter(getApplicationContext(),list);
            items.setAdapter(myAdapter);

            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot dataSnapshot:task.getResult()){
                            Map<String, Object> data = dataSnapshot.getData();
                            Product product=new Product(data.get("ProductName"),
                                    data.get("ProductPrise"),
                                    data.get("ProductDescription"),
                                    data.get("ProductCategory"),
                                    data.get("ProductImage"),
                                    data.get("ProductManufacturer"),
                                    data.get("InStock"));

                            list.add(product);
                            myAdapter.notifyDataSetChanged();

                        }

                    }else{
                        Toast.makeText(MainPage.this, "failed to load products", Toast.LENGTH_SHORT).show();
                    }
                }
            });





        }


    }


}